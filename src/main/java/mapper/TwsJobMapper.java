package com.bsci.eaiintakecollectorservice.mapper;

import com.bsci.eaiintakecollectorservice.model.TwsJob;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TwsJobMapper {
    @Insert("Insert into xtr_tws_jobs(JOB_ID,JOB_PR_ID,JOB_NAME,JOB_TYPE,JOB_STATUS,JOB_RETRIES," +
            "PARENT_JOB_ID,JOB_CREATED,JOB_UPDATED,JOB_ERROR)" +
            " values(#{job.id}, #{job.prId,jdbcType=VARCHAR},#{job.name,jdbcType=VARCHAR}" +
            ", #{job.data,jdbcType=VARCHAR},#{job.status,jdbcType=VARCHAR}" +
            ", #{job.retries},,#{job.parentJobId},#{job.jobCreated,jdbcType=DATE}" +
            ", #{job.jobUpdated,jdbcType=DATE},#{job.jobError,jdbcType=VARCHAR})")
    void storeJob(@Param("job") TwsJob job);

    @Select("Select" +
            " JOB_ID,JOB_PR_ID,JOB_NAME,JOB_TYPE,JOB_STATUS,JOB_RETRIES,"+
            " PARENT_JOB_ID,JOB_CREATED,JOB_UPDATED,JOB_ERROR" +
            " from xtr_tws_jobs where ROWNUM <= 10")
        //+
        // "where JOB_NAME= #{pendingJob.name} and JOB_STATUS= #{pendingJob.status}")
    List<TwsJob> queryPendingJob();

    @Select("Select" +
            " JOB_ID,JOB_PR_ID,JOB_NAME,JOB_TYPE,JOB_STATUS,JOB_RETRIES,"+
            " PARENT_JOB_ID,JOB_CREATED,JOB_UPDATED,JOB_ERROR" +
            " from xtr_tws_jobs"+
            " where JOB_NAME= '' and JOB_STATUS= 'COMPLETED'")
    List<TwsJob> queryCompletedJob();
}


