package com.example.chatapplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     * Get page manager
     *
     * @return
     */
    @GetMapping(value = {"/manager"})
    public String chatApplication(HttpServletRequest request, HttpServletResponse response) {

        return "chat-light-mode";
    }
}
