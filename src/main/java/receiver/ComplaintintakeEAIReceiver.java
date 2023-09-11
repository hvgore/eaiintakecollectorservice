package com.bsci.eaiintakecollectorservice.receiver;

import javax.jms.JMSException;
import javax.jms.Message;

public interface ComplaintIntakeEAIReceiver {
    public void recordSuccesses(Message message) throws JMSException;

    public void recordFailures(Message message) throws JMSException;
}
