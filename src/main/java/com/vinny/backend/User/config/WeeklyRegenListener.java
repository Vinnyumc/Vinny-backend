package com.vinny.backend.User.config;

import com.vinny.backend.User.service.UserShopForYouService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Component
@RequiredArgsConstructor
public class WeeklyRegenListener {

    private final UserShopForYouService userShopForYouService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserPreferenceChangedEvent event) {
        userShopForYouService.regenerateThisWeek(event.userId());
    }
}
