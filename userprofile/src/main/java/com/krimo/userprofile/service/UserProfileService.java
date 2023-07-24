package com.krimo.userprofile.service;

import com.krimo.userprofile.domain.UserProfile;
import com.krimo.userprofile.dto.UserProfileDTO;
import com.krimo.userprofile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface UserProfileService {
    Long createUserProfile(UserProfileDTO dto);
    void updateUserProfile(Long id, UserProfileDTO dto);
    UserProfileDTO getUserProfile(Long id);
    List<UserProfileDTO> getUserProfiles();
    String getUserEmail (Long id);
}

@RequiredArgsConstructor
@Service
@Transactional
class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public Long createUserProfile(UserProfileDTO dto) {
        UserProfile userProfile = UserProfile.create(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getBirthDate()
        );

        return userProfileRepository.saveAndFlush(userProfile).getId();
    }

    @Override
    public void updateUserProfile(Long id, UserProfileDTO dto) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow();

        if (dto.getEmail() != null) { userProfile.setEmail(dto.getEmail()); }
        if (dto.getPhone() != null) { userProfile.setPhone(dto.getPhone()); }
        if (dto.getLastName() != null) { userProfile.setLastName(dto.getLastName()); }
        if (dto.getFirstName() != null) { userProfile.setFirstName(dto.getFirstName()); }
        if (dto.getBirthDate() != null) { userProfile.setBirthDate(dto.getBirthDate()); }

        userProfileRepository.save(userProfile);
     }

    @Override
    public UserProfileDTO getUserProfile(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow();
        return mapToDTO(userProfile);
    }

    @Override
    public List<UserProfileDTO> getUserProfiles() {
        return userProfileRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public String getUserEmail (Long id) {
        return userProfileRepository.getReferenceById(id).getEmail();
    }

    private UserProfileDTO mapToDTO(UserProfile userProfile) {
        return UserProfileDTO.builder()
                .id(userProfile.getId())
                .lastName(userProfile.getLastName())
                .firstName(userProfile.getFirstName())
                .email(userProfile.getEmail())
                .phone(userProfile.getPhone())
                .birthDate(userProfile.getBirthDate())
                .registeredAt(userProfile.getRegisteredAt())
                .build();
    }


}