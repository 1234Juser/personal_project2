package com.example.project_3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list"; //8080:페이지접속시 나타남
    }
}