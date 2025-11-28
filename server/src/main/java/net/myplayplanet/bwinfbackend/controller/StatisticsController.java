package net.myplayplanet.bwinfbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats/")

public class StatisticsController {

    @GetMapping("dummy")
    public String dummy(){
        return "abcdde";
    }

}
