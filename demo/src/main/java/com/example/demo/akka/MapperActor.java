package com.example.demo.akka;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class MapperActor extends UntypedActor {
    
    private List<ActorRef> reducers = null;

    public void onReceive (Object message) {
        if (message instanceof ReducerMessage m) {
            addReducer(m.reducer());
        }
        if (message instanceof LineMessage m) {
            readLine(m.message());
        }
    }

    public void addReducer (ActorRef reducer) {
        if (this.reducers == null) {
            this.reducers = new ArrayList<ActorRef>();
        }
        this.reducers.add(reducer);
    }

    public void readLine (String message) {
        String[] words = message.split(" |;|,");
        for (int i = 0; i < words.length; i++) {
            this.reducers.get(words[i].length() % this.reducers.size()).tell(new WordMessage(words[i]), ActorRef.noSender());
        }
    }
}
