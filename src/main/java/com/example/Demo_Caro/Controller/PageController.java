package com.example.Demo_Caro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index"; // Điều hướng tới index.html trong thư mục static
    }

    @GetMapping("/join")
    public String game() {
        return "join"; // Điều hướng tới join.html (nếu có)
    }
}

