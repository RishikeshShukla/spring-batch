package com.batch.exp.config;

import com.batch.exp.model.PayrollDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

/**
 * Custom {@link ItemReadListener} which logs writing events.
 *
 */
@Slf4j
public class ItemWriterListener implements ItemWriteListener<PayrollDetails> {
	private final static Logger LOGGER = LoggerFactory.getLogger("ItemWriterListener");
	private static final String RECORDS = " records";

	/**
	 * Debug message before a write.
	 *
	 * @param employees list of {@link PayrollDetails}
	 */
	@Override
	public void beforeWrite(List<? extends PayrollDetails> employees) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Writing: " + employees.size() + RECORDS);
		}
	}

	/**
	 * Debug message after a write.
	 *
	 * @param payrollDetails list of {@link PayrollDetails}
	 */
	@Override
	public void afterWrite(List<? extends PayrollDetails> payrollDetails) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Successfully wrote: " + payrollDetails.size() + RECORDS);
		}
	}

	/**
	 * Error message on write error.
	 *
	 * @param exception {@link Exception} thrown during write
	 * @param payrollDetails list of {@link PayrollDetails}
	 */
	@Override
	public void onWriteError(Exception exception, List<? extends PayrollDetails> payrollDetails) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("An error occurred while writing: " + payrollDetails.size() + RECORDS, exception);
		}
	}
}
