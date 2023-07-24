package com.krimo.notification.repository;


import java.util.Map;
import java.util.Set;


public interface KVService {
    boolean isKeyPresent(String key);
    void saveKey(String key);
    void saveKV(String key, String value);
    Map<String, String> getKeysWithPrefix();
//    void addUserIdToSet(Long userId);
//    void removeUserIdFromSet(Long userId);
//    Set<Long> getAllUserIDsFromSet();
}




