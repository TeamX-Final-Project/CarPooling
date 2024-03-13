package org.example.carpooling.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeMvcController {
    @GetMapping
    public String showHomePage(Model Model){

        return "index";
    }
}
