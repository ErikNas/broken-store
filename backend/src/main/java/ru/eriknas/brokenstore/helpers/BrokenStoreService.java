package ru.eriknas.brokenstore.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokenStoreService {
    private static final Logger logger = LoggerFactory.getLogger(BrokenStoreService.class);

    @Autowired
    private LogMetrics logMetrics;

    public void doSomething() {
        try {
            logger.info("This is an INFO log");
            logMetrics.incrementInfo();
        } catch (Exception e) {
            logger.error("This is an ERROR log", e);
            logMetrics.incrementError();
        }
    }
}
