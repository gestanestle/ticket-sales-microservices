package com.krimo.userprofile.controller;

import com.krimo.userprofile.dto.UserProfileDTO;
import com.krimo.userprofile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<Long> createUserProfile(@RequestBody UserProfileDTO dto) {
        return new ResponseEntity<>(userProfileService.createUserProfile(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getUserProfiles() {
        return new ResponseEntity<>(userProfileService.getUserProfiles(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userProfileService.getUserProfile(id), HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable("id") Long id,
                                                    @RequestBody UserProfileDTO dto) {
        userProfileService.updateUserProfile(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable("id") Long id) {
        userProfileService.deleteUserProfile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "{id}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userProfileService.getUserEmail(id), HttpStatus.OK);
    }
}
