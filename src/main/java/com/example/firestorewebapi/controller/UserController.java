package com.example.firestorewebapi.controller;


import com.example.firestorewebapi.service.PubSubPublisher;
import com.example.firestorewebapi.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  private final PubSubPublisher publisher;

  public UserController(UserService userService, PubSubPublisher publisher) {
    this.userService = userService;
    this.publisher = publisher;
  }

  @GetMapping
  public String addRandomUser() {

    String userName = userService.addRandomUser();


    publisher.publishWithErrorHandlerExample();


    return userName + " was added";
  }
}
