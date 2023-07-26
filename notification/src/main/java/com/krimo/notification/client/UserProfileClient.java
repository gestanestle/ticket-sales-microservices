package com.krimo.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("userprofile")
public interface UserProfileClient {

    @GetMapping(path = "api/v2/user-profiles/{id}/email")
    ResponseEntity<String> getUserEmail(@PathVariable("id") Long id);
}
