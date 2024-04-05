package com.example.demo.akka;

import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;

public class ReducerActor extends UntypedActor {
    private Map<String, Integer> dictionary = null;

    public ReducerActor () {
        super();
        this.dictionary = new HashMap<String, Integer>();
    }

    public void onReceive (Object message) {
        if (message instanceof WordMessage m) {
            if (m.message().length() != 0) {
                addWordDictionary(m.message());
            }
        }
        if (message instanceof OccurencyMessage m) {
            getSender().tell(new OccurencyResponseMessage(getOccurencies(m.message())), getSelf());
        }
        if (message instanceof ResetReducer) {
            reset();
        }
    }

    public void addWordDictionary (String word) {
        if (dictionary.keySet().contains(word)) {
            dictionary.put(word, dictionary.get(word) + 1);
        } else {
            dictionary.put(word, 1);
        }
    }

    public int getOccurencies (String word) {
        if (dictionary.keySet().contains(word)) {
            return dictionary.get(word);
        }
        return 0;
    }

    public void reset () {
        this.dictionary.clear();
    }
}
