package com.bsci.eaiintakecollectorservice.jobs.impl;

import com.bsci.eaiintakecollectorservice.configuration.EAIIntakeCollectorConfig;
import com.bsci.eaiintakecollectorservice.jobs.EAIIntakeCollector;
import com.bsci.eaiintakecollectorservice.mapper.TwsJobMapper;
import com.bsci.eaiintakecollectorservice.model.TwsJob;
import model.EmailMessage;
import model.Envelope;
import model.MessageLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class EAIIntakeCollectorImpl implements EAIIntakeCollector {

    private static final Logger log = LoggerFactory.getLogger(EAIIntakeCollectorImpl.class);

    private static final String Message = "Job Id %s Job Name %s Job Type %s Job Status %s";

    private TwsJobMapper twsJobMapper;

    private String emailQueueDestination;

    private JmsTemplate template;

    private Function<EmailMessage,String> serializer;

    private EAIIntakeCollectorConfig configuration;

    @Override
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRateString = "${collecteaintake.jobExecutionInSeconds}")
    public void collectpendingJob() {
        System.out.println(">>>>>>>>>>>>>>> collectpendingJob mapper:: "+twsJobMapper);
//        var twsJobList = this.twsJobMapper.queryPendingJob();//TwsPendingJob.builder().name("Create Event").status("PENDING").build());
//        //var twsJobList = this.twsJobMapper.queryJob(new TwsPendingJob("Create Event","COMPLETED"));
//        System.out.println(">>>>>>>>>>>>>>>twsJobList:: "+twsJobList.size());
//        log.info("Pending Job Query found {} records for processing", twsJobList.toString());
//        if(twsJobList!=null) {
//            twsJobList.stream().forEach(
//                    this::sendEmailForPendingJobs
//            );
//        }
    }

    // schedule a job to check if Status completed than acknowledge here 2nd approach
    @Override
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRateString = "${collecteaintake.jobExecutionInSeconds}")
    public void sendAcknowledge() {
        System.out.println(">>>>>>>>>>>>>>>sendAcknowledge mapper:: "+twsJobMapper);
//        var twsJobList = this.twsJobMapper.queryCompletedJob();//TwsPendingJob.builder().name("Create Event").status("PENDING").build());
//        //var twsJobList = this.twsJobMapper.queryJob(new TwsPendingJob("Create Event","COMPLETED"));
//        System.out.println(">>>>>>>>>>>>>>>twsJobList:: "+twsJobList.size());
//        log.info("Completed Job Query found {} records for processing", twsJobList.toString());
//        if(twsJobList!=null) {
//            twsJobList.stream().forEach(
//                    this::sendEmailForPendingJobs
//            );
//        }
    }
    private void sendEmailForPendingJobs(TwsJob twsJob) {
        this.template.convertAndSend(this.emailQueueDestination,this.serializer.apply(buildEmailMessage(twsJob)));
    }

    private EmailMessage buildEmailMessage(TwsJob twsJob) {
        return EmailMessage.builder()
                .prId(0)
                .envelope(Envelope.builder().messagePOJOName(this.getClass().getCanonicalName())
                        .address(this.emailQueueDestination)
                        .sender("eaiIntakeCollector")
                        .build())
                .messageLifeCycle(MessageLifeCycle.builder().
                        jobFirstSent(Calendar.getInstance())
                        .build())
                .message(String.format(twsJob.getJOB_ID().toString(),twsJob.getJOB_NAME(),twsJob.getJOB_TYPE(),twsJob.getJOB_STATUS()))
                .to(Arrays.asList(configuration.getSendTo()))
                .build();
    }

    @Autowired
    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setEmailQueueDestination(@Value("${queues.email}") String emailQueueDestination) {
        this.emailQueueDestination = emailQueueDestination;
    }

    @Autowired
    public void setSerializer(Function<EmailMessage, String> serializer) {
        this.serializer = serializer;
    }

    @Autowired
    public void setTwsJobMapper(TwsJobMapper twsJobMapper) {
        this.twsJobMapper = twsJobMapper;
    }

    @Autowired
    public void setConfiguration(EAIIntakeCollectorConfig configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    void printconfiguration()
    {
        System.out.println(">>>>>>>>>>> "+this.twsJobMapper);
    }
}
