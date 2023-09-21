package com.java.voteup.controller;

import com.java.voteup.domain.User;
import com.java.voteup.dto.LoginDto;
import com.java.voteup.service.UserService;
import com.java.voteup.utils.GroupGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @CrossOrigin
    @GetMapping("/publicKey")
    public Integer getPublicKey(@RequestParam("email") String email) {
        return userService.getPublicKeyByEmail(email);
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<Integer> loginUser(@RequestBody LoginDto request) {

        try {
            Integer userId = userService.userLogin(request);
            if (userId != null)

                return ResponseEntity.ok(userId);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User request) {
        userService.userRegistration(request);
        return ResponseEntity.ok("Înregistrare realizată cu succes");
    }

    @CrossOrigin
    @GetMapping("/getG")
    public List<Integer> getG() {
        return GroupGenerator.generateGroup();
    }

}
