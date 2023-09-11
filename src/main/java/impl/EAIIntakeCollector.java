package com.bsci.eaiintakecollectorservice.jobs;

public interface EAIIntakeCollector {

    void collectpendingJob();
    void sendAcknowledge();
}
