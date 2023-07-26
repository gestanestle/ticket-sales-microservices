package com.krimo.notification.service;

import com.krimo.notification.client.UserProfileClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ClientService {
    String getEmail(Long id);
}

@RequiredArgsConstructor
@Service
class UserProfileClientService implements ClientService {

    private final UserProfileClient userProfileClient;

    @Override
    public String getEmail(Long id){
        ResponseEntity<String> response = userProfileClient.getUserEmail(id);

        return response.getBody();
    }
}
