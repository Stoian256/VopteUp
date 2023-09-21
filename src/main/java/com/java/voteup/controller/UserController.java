package com.java.voteup.controller;

import com.java.voteup.dto.UserDto;
import com.java.voteup.service.IdentityVerificationService;
import com.java.voteup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IdentityVerificationService identityVerificationService;
    private final UserService userService;

    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin
    public ResponseEntity<String> verifyDocument(@RequestParam("document") MultipartFile mpFile, @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> response = null;
        try {
            byte[] fileBytes = mpFile.getBytes();
            String fileExtension = FilenameUtils.getExtension(mpFile.getOriginalFilename());

            File file = File.createTempFile("temp", '.' + fileExtension);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileBytes);
            fos.close();
            String userId = authorizationHeader.substring("Bearer ".length());
            response = identityVerificationService.extractTextFromImage(file, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @CrossOrigin
    @GetMapping("/users/informations")
    public UserDto getUserInformation(@RequestParam("userId") String userId) {
        return userService.getUserInfoById(userId);
    }

    @CrossOrigin
    @GetMapping("/checkValidated")
    public Boolean checkValidated(@RequestParam("userId") String userId) {
        return userService.checkValidate(userId);
    }

    @CrossOrigin
    @PostMapping("/update")
    public ResponseEntity<String> registerUser(@RequestBody UserDto updatedUser, @RequestHeader("Authorization") String authorizationHeader) {
        String userId = authorizationHeader.substring("Bearer ".length());
        userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok("Informațiile au fost actualizate cu succes!");
    }
}
