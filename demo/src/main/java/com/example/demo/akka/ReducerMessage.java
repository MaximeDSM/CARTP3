package com.example.demo.akka;

import akka.actor.ActorRef;

public record ReducerMessage ( ActorRef reducer ) {
    
}
