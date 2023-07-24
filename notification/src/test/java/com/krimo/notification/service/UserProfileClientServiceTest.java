package com.krimo.notification.service;

import com.krimo.notification.client.UserProfileClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileClientServiceTest {

    @Mock
    private UserProfileClient userProfileClient;
    @InjectMocks
    @Autowired
    private UserProfileClientService userProfileClientService;

    @BeforeEach
    void setUp() {
        userProfileClientService = new UserProfileClientService(userProfileClient);
    }

}
