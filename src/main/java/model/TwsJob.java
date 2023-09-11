package com.bsci.eaiintakecollectorservice.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwsJob {
    private Long JOB_ID;
    private int PARENT_JOB_ID;
    private String JOB_PR_ID;
    private String JOB_NAME;
    private String JOB_TYPE;
    private Integer JOB_RETRIES;
    private String JOB_STATUS;
    private Timestamp JOB_UPDATED;
    private Timestamp JOB_CREATED;
    private String JOB_ERROR;
}
