package com.example.springrestmultithread.service;

import com.example.springrestmultithread.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class AsyncService {
    private static final Logger log = LoggerFactory.getLogger(AsyncService.class);
    private final RestTemplate restTemplate;

    @Autowired
    public AsyncService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<CompletableFuture<User>>> getUser(List<Integer> ids) {
        log.info("user start");
        List<CompletableFuture<User>> userFutures =
                ids.stream().map(id -> CompletableFuture.supplyAsync(
                        () -> mapToUser(id))).collect(toList());
        return CompletableFuture.completedFuture(userFutures);
    }

    private User mapToUser(int id) {
        return restTemplate.getForObject("https://......" + id, User.class);
    }
}
