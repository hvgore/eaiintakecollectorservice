package com.bsci.eaiintakecollectorservice.publisher;

public interface ResultPublisher {

//    public void sendAckJob();

    void sendPositiveAck(String sourceSystem, String sourceSystemID);

    void sendNegativeAck(Throwable ex, String message);

//    void sendAckMessage(String jsonMessage);



}
