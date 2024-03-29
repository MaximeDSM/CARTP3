package com.example.demo.akkaFront;

import java.io.File;

public interface AkkaService {
    boolean openFile (File file);
    String readLine ();
    boolean closeFile();
    boolean initialize();
}
