package com.bsci.eaiintakecollectorservice.function;

import com.bsci.eaiintakecollectorservice.model.TwsJob;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.function.Function;


public interface JobProcessingFunctions {

    public final String JOB_CREATE_EVENT = "Create Event";

    static Validation<Seq<String>, TwsJob> operationResultToTwsJob(Message message) throws JMSException {
        return Validation.combine(
                validateNonNull(getMessageText.apply(message),(v) -> v,"Message is not correct"),
                validateNonNull(message.getStringProperty("Topic"),(v) -> v,"Topic cannot be null!")
        ).ap(operationResultToTwsJobMapper);
    }

    static <T> Validation<String,T> validateNonNull(String result, Function1<String, T> isNotNull, String error ) {
        T value = isNotNull.apply(result);
        return value != null ? Validation.valid(value) : Validation.invalid(error);
    }

    static Function2<String,String,TwsJob>
            operationResultToTwsJobMapper = (message,topic) ->
            TwsJob.builder()
                    .JOB_PR_ID("0")
                    .JOB_NAME(JOB_CREATE_EVENT)
                    .JOB_TYPE(message)
                    .build();

    static Function<Message, String> getMessageText = msg -> {
        return Optional.ofNullable(msg)
                .filter(TextMessage.class::isInstance)
                .map(TextMessage.class::cast)
                .map(textMsg -> {
                    try {
                        return textMsg.getText();
                    } catch (JMSException e) {
                        e.printStackTrace();
                        return null; // or throw an exception if desired
                    }
                })
                .or(() -> Optional.ofNullable(msg)
                        .filter(BytesMessage.class::isInstance)
                        .map(BytesMessage.class::cast)
                        .map(bytesMsg -> {
                            try {
                                int length = (int) bytesMsg.getBodyLength();
                                byte[] b = new byte[length];
                                bytesMsg.readBytes(b, length);
                                try {
                                    return new String(b, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    return null; // or throw an exception if desired
                                }
                            } catch (JMSException e) {
                                e.printStackTrace();
                                return null; // or throw an exception if desired
                            }
                        }))
                .orElse(null); // Return a default value or handle the case when msg doesn't match any condition.
    };
}
