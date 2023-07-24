package com.krimo.notification.job;


import com.krimo.notification.repository.CacheService;
import com.krimo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional
public class FailedTransactionsManager {

    private static final Logger logger = LoggerFactory.getLogger(FailedTransactionsManager.class);
    private final CacheService cacheService;
    private final NotificationService notificationService;

//    @Scheduled(cron = "0 0 23 * * *") // 0 seconds, 0 minutes, 22 hours (10 pm)
    public void resendPurchaseConfirmation() {
        final String hashKey = "FAILED";

        Map<String, String> keyVals = cacheService.getHashFromKey(hashKey);

        for (Map.Entry<String, String> entry: keyVals.entrySet()) {
            String msgID = entry.getKey();
            String msgPayload = entry.getValue();

            try {
                notificationService.sendConfirmationMessage(msgID, msgPayload);
            } catch (Exception e) {
                logger.debug("Failed to execute resending of confirmation message.");
                logger.debug(e.getMessage());
                return;
            }

            // delete from hash of failed transactions if successful
            cacheService.deleteHash(hashKey, msgID);
        }

    }
}
