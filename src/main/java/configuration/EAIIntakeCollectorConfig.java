package com.bsci.eaiintakecollectorservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("application")
public class EAIIntakeCollectorConfig {
    private String sendTo;
    private String jobExecutionInSeconds;
    private String jmsType;
    private String jmsFactory;
}
