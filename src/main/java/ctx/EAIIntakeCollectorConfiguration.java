package com.bsci.eaiintakecollectorservice.ctx;

import com.bsci.eaiintakecollectorservice.configuration.EAIIntakeCollectorConfig;
import com.bsci.eaiintakecollectorservice.jobs.EAIIntakeCollector;
import com.bsci.eaiintakecollectorservice.jobs.impl.EAIIntakeCollectorImpl;
import function.SerializationFunctions;
import model.EmailMessage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.function.Function;

@Configuration
@EnableJms
@MapperScan("com.bsci.complaintintakeeaicollectorservice.mapper")
@EnableScheduling
@EnableConfigurationProperties(EAIIntakeCollectorConfig.class)
public class EAIIntakeCollectorConfiguration {



    @Bean("pubSubEAI1")
    public DefaultJmsListenerContainerFactory pubSub1(ConnectionFactory factory) {
        DefaultJmsListenerContainerFactory containerFactory1 = new DefaultJmsListenerContainerFactory();
        containerFactory1.setConnectionFactory(factory);
        containerFactory1.setConcurrency("1");
        containerFactory1.setPubSubDomain(true);
        return containerFactory1;
    }

    @Bean("pubSubEAI2")
    public DefaultJmsListenerContainerFactory pubSub2(ConnectionFactory factory) {
        DefaultJmsListenerContainerFactory containerFactory2 = new DefaultJmsListenerContainerFactory();
        containerFactory2.setConnectionFactory(factory);
        containerFactory2.setConcurrency("1");
        containerFactory2.setPubSubDomain(true);
        return containerFactory2;
    }

//    @Bean("pubSubEAI1")
//    public ActiveMQConnectionFactory pubSub1() throws JMSException {
//        ActiveMQConnectionFactory containerFactory1 = new ActiveMQConnectionFactory();
//        containerFactory1.setBrokerURL("tcp://localhost:8161"); // Replace with your broker URL
//        containerFactory1.setUser("dmane"); // Replace with your username
//        containerFactory1.setPassword("dmane"); // Replace with your password
//        return containerFactory1;
//    }
//
//    @Bean("pubSubEAI2")
//    public ActiveMQConnectionFactory pubSub2() throws JMSException {
//        ActiveMQConnectionFactory containerFactory2 = new ActiveMQConnectionFactory();
//        containerFactory2.setBrokerURL("tcp://localhost:8162"); // Replace with your broker URL
//        containerFactory2.setUser("dmane"); // Replace with your username
//        containerFactory2.setPassword("dmane"); // Replace with your password
//        return containerFactory2;
//    }

    @Bean("eaiintakecollector")
    public EAIIntakeCollector collector() {
        return new EAIIntakeCollectorImpl();
    }

    @Bean
    public Function<EmailMessage,String> serializer() {
        return SerializationFunctions.serializer(EmailMessage.class);
    }


}
