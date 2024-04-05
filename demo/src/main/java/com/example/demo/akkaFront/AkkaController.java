package com.example.demo.akkaFront;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/akka")
public class AkkaController {
    
    @Autowired
    private AkkaService akkaService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("Occurencies", 0);
        return ("home");
    }

    @PostMapping("/file")
    public String file (@RequestParam File chosenFile) {
        akkaService.splitFile(chosenFile.getName());
        return "redirect:/akka/home";
    }

    @PostMapping("/initialization")
    public String initialize () {
        akkaService.initialize(3, 2);
        return "redirect:/akka/home";
    }

    @PostMapping("/result")
    public String result (@RequestParam String word, Model model) {
        model.addAttribute("Occurencies", akkaService.getOccurencies(word));
        return "home";
    }
}
