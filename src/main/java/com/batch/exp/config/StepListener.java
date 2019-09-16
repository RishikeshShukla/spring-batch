package com.batch.exp.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

/**
 * Custom {@link StepListener} to log meta data after processing.
 */
@Slf4j
public class StepListener extends StepExecutionListenerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepListener.class);

    /**
     *
     * @param stepExecution {@link StepExecution}
     * @return {@link ExitStatus}
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Records read:    " + stepExecution.getReadCount());
        LOGGER.info("Records written: " + stepExecution.getWriteCount());
        LOGGER.info("Records skipped: "
                + "[read: " + stepExecution.getReadSkipCount()
                + ", written: " + stepExecution.getWriteSkipCount() + "]");

        return ExitStatus.COMPLETED;
    }
}
