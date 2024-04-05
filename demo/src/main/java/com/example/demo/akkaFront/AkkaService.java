package com.example.demo.akkaFront;

import java.io.File;

public interface AkkaService {
    boolean openFile (File file);
    String readLine ();
    boolean closeFile();
    void splitFile (String fileName);
    void initialize(int nbMapper, int nbReducer);
    Integer getOccurencies (String word);
}
