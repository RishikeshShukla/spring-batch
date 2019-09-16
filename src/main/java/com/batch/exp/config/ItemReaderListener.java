package com.batch.exp.config;

import com.batch.exp.model.PayrollDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 * Custom {@link ItemReadListener} which logs reading events.
 */
@Slf4j
public class ItemReaderListener implements ItemReadListener<PayrollDetails> {
    private final static Logger LOGGER = LoggerFactory.getLogger("ItemReaderListener");

    /**
     * Debug message before a read.
     */
    @Override
    public void beforeRead() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Starting read of Payroll record");
        }
    }

    /**
     * Debug message after a read.
     */
    @Override
    public void afterRead(PayrollDetails payrollDetails) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Read: " + payrollDetails);
        }
    }

    /**
     * Error message on read error.
     */
    @Override
    public void onReadError(Exception exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Error while reading payroll details record", exception);
        }
    }
}
