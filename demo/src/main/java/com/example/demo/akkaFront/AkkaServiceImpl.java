package com.example.demo.akkaFront;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.demo.akka.MapperActor;
import com.example.demo.akka.ReducerActor;

@Service
public class AkkaServiceImpl implements AkkaService {

    private BufferedReader buffer = null;

    private MapperActor mapper1, mapper2, mapper3;
    private ReducerActor reducer1, reducer2;

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

    public boolean initialize() {
        // initialiser tous les mappers et reducers en mettant en param des mapper les reducers
        // faire l'initialisation comme on fait dans le main
        return true;
    }

}
