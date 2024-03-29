package com.example.demo.akkaFront;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.akka.MapperActor;
import com.example.demo.akka.ReducerActor;

@Controller
@RequestMapping("/akka")
public class AkkaController {
    
    @Autowired
    private AkkaService akkaService;

    @GetMapping("/home")
    public String home() {
        return ("home");
    }

    @PostMapping("/file")
    public String file (@RequestParam File chosenFile) {
        String path = "/home/m1ipint/maxime.de-sainte-maresville.etu/Documents/s2/car/tps/tp3/files/" + chosenFile.getName();
        if (akkaService.openFile(new File(path))) {
            String line;
            line = akkaService.readLine();
            while (line != "" && line != null) {
                System.out.println(line);
                line = akkaService.readLine();
            }
        }
        return "redirect:/akka/home";
    }

}
