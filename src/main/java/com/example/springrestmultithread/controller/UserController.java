package com.example.springrestmultithread.controller;

import com.example.springrestmultithread.model.User;
import com.example.springrestmultithread.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final AsyncService asyncService;

    @Autowired
    public UserController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping(value = "/getusers")
    public ResponseEntity<List<User>> getUserInfoByName(@RequestParam List<Integer> ids) {
        List<User> users = new ArrayList<>();
        try {
            users = asyncService.getUser(ids).get().stream().map(CompletableFuture::join).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
