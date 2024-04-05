package com.example.demo.akkaFront;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;

import com.example.demo.akka.LineMessage;
import com.example.demo.akka.MapperActor;
import com.example.demo.akka.OccurencyMessage;
import com.example.demo.akka.OccurencyResponseMessage;
import com.example.demo.akka.ReducerActor;
import com.example.demo.akka.ReducerMessage;
import com.example.demo.akka.ResetReducer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import scala.concurrent.duration.FiniteDuration;

@Service
public class AkkaServiceImpl implements AkkaService {

    private BufferedReader buffer = null;

    private List<ActorRef> mappers = null;
    private List<ActorRef> reducers = null;

    private Inbox inbox;

    public boolean openFile (File file) {
        try {
            this.buffer = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public String readLine() {
        if (this.buffer != null) {
            try {
                return this.buffer.readLine();
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }

    public boolean closeFile (){
        if (this.buffer != null) {
            try {
                buffer.close();
                this.buffer = null;
                return true;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public void initialize(int nbMapper, int nbReducer) {
        if (this.reducers == null && this.mappers == null) {
            ActorSystem system = ActorSystem.create("MySystem");

            this.mappers = new ArrayList<ActorRef>();
            this.reducers = new ArrayList<ActorRef>();

            for (int i = 0; i < nbReducer; i++) {
                this.reducers.add(system.actorOf(Props.create(ReducerActor.class), "reducer" + i));
            }

            for (int j = 0; j < nbMapper; j++) {
                this.mappers.add(system.actorOf(Props.create(MapperActor.class), "mapper" + j));
                for (int i = 0; i < nbReducer; i++) {
                    this.mappers.get(j).tell(new ReducerMessage(reducers.get(i)), ActorRef.noSender());
                }
            }

            this.inbox = Inbox.create(system);
        } else {
            // Reset des reducers
            for (int i = 0; i < nbReducer; i++) {
                this.reducers.get(i).tell(new ResetReducer(), ActorRef.noSender());;
            }
        }
    }

    public void splitFile (String fileName) {
        if (this.reducers != null && this.mappers != null) {
            String path = "demo/files/" + fileName;
            int i = 0;
            int nbMapper = this.mappers.size();
            if (openFile(new File(path))) {
                String line;
                line = readLine();
                while (line != "" && line != null) {
                    this.mappers.get(i).tell(new LineMessage(line), ActorRef.noSender());
                    i++;
                    if (i % nbMapper == 0) {
                        i = 0;
                    }
                    line = readLine();
                }
            }
        }
    }

    public Integer getOccurencies (String word) {
        inbox.send(this.reducers.get(word.length() % this.reducers.size()), new OccurencyMessage(word));
        Object reply = null;
        try {
            reply = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            // Code si echéance
        }
        if (reply instanceof OccurencyResponseMessage rm) {
            return rm.message();
        }
        return 0;
    }
}
