package com.bsci.eaiintakecollectorservice.receiver.impl;

import com.bsci.eaiintakecollectorservice.function.JobProcessingFunctions;
import com.bsci.eaiintakecollectorservice.mapper.TwsJobMapper;
import com.bsci.eaiintakecollectorservice.model.TwsJob;
import com.bsci.eaiintakecollectorservice.receiver.ComplaintIntakeEAIReceiver;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.stream.Collectors;

@Component
public class ComplaintintakeEAIReceiverImplCF1 implements ComplaintIntakeEAIReceiver {

    private TwsJobMapper jobMapper;

    private static final Logger logger = LoggerFactory.getLogger(ComplaintintakeEAIReceiverImplCF1.class);

    @JmsListener(containerFactory = "pubSubEAI1", destination = "${queues.successes1}" )
    public void recordSuccesses(Message message) throws JMSException {
        System.out.println("Received message from pubSubEAI1 - successQueue :: "+message);
        storeOrError(message);
    }

    @JmsListener(containerFactory = "pubSubEAI1", destination = "${queues.failures1}")
    public void recordFailures(Message message) throws JMSException {
        storeOrError(message);
    }

    private void storeOrError(Message message) {
        Validation<Seq<String>, TwsJob> validationResult = null;
        try {
            validationResult = JobProcessingFunctions.operationResultToTwsJob(message);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        if(validationResult.isValid() ) {
            this.jobMapper.storeJob(validationResult.get());
        } else {
            logger.error("Could not process message due to validation errors {}", validationResult.getError().asJava().stream().collect(Collectors.joining(System.lineSeparator())));
        }
    }

    @Autowired
    public void setJobMapper(TwsJobMapper mapper) {
        this.jobMapper = mapper;
    }

}
