package com.bsci.eaiintakecollectorservice.publisher.impl;

import com.bsci.eaiintakecollectorservice.publisher.ResultPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import com.google.gson.JsonObject;

public class ResultPublisherImpl implements ResultPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ResultPublisherImpl.class);

    private JmsTemplate positiveTemplate;

    private JmsTemplate negativeTemplate;

    @Override
    public void sendPositiveAck(String sourceSystem, String sourceSystemID) {
        JsonObject json = new JsonObject();
        json.addProperty("sourceSystem", sourceSystem);
        json.addProperty("sourceSystemID", sourceSystemID);

        positiveTemplate.convertAndSend(json.toString()); //sendAckMessage();
    }

    @Override
    public void sendNegativeAck(Throwable ex, String message) {
        JsonObject json = new JsonObject();
        // Similar logic for extracting fields from message as in your cis-fasttrack processor code
        json.addProperty("eaiIntakeError", ex.getMessage());
        json.addProperty("message", message);

        negativeTemplate.convertAndSend(json.toString()); //sendAckMessage(json.toString());
    }

    @Autowired
    @Qualifier("positive")
    public void setPositiveTemplate(JmsTemplate positiveTemplate) {
        this.positiveTemplate = positiveTemplate;
    }

    @Autowired
    @Qualifier("negative")
    public void setNegativeTemplate(JmsTemplate negativeTemplate) {
        this.negativeTemplate = negativeTemplate;
    }


}
