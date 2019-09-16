package com.batch.exp.config;

import com.batch.exp.model.PayrollDetails;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private static final int CHUNK_SIZE = 100;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ResourceFactory resourceFactory;
    /**
     * Constructor which autowires the necessary factories and datasource.
     *
     * @param jobBuilderFactory  {@link JobBuilderFactory}
     * @param stepBuilderFactory {@link StepBuilderFactory}
     * @param stepBuilderFactory {@link ResourceFactory}
     */

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ResourceFactory resourceFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.resourceFactory = resourceFactory;
    }

    /**
     * Extract job which calls step(extractionStep and archiveFiles) in the job
     * @return {@link Job}
     */
    @Bean
    public Job extractPayrollDetailsData() {
        return jobBuilderFactory
                .get("extract-payroll-data")
                .incrementer(new RunIdIncrementer())
                .start(extractionStep())
                .next(archiveFiles())
                .build();
    }

    /**
     * Starts the extract by reading {@link PayrollDetails}'s and writing {@link PayrollDetails}'s.
     *
     * @return {@link Step}
     */
    @Bean
    public Step extractionStep() {
        return stepBuilderFactory.get("extraction-step").
                <PayrollDetails, PayrollDetails>chunk(CHUNK_SIZE)
                .reader(multiResourceItemReader())
                .writer(writer())
                .listener(new ItemReaderListener())
                .listener(new ItemWriterListener())
                .listener(new StepListener())
                .build();
    }

    /**
     * To move processed file to archive
     * @return {@link Step}
     */

    @Bean
    protected Step archiveFiles() {
        ArchiveFilesTasklet archiveFilesTasklet = new ArchiveFilesTasklet();
        archiveFilesTasklet.setResources(resourceFactory.resources());
        return stepBuilderFactory.get("archiving-step")
                .tasklet(archiveFilesTasklet)
                .build();
    }

    @Bean
    public MultiResourceItemReader<PayrollDetails> multiResourceItemReader() {
        MultiResourceItemReader<PayrollDetails> resourceItemReader = new MultiResourceItemReader<PayrollDetails>();
        resourceItemReader.setResources(resourceFactory.resources());
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<PayrollDetails> reader() {
        return new FlatFileItemReaderBuilder<PayrollDetails>()
                .name("payroll-csv-reader")
                .linesToSkip(1)
                //.resource(new ClassPathResource("payroll.csv"))
                .delimited().delimiter("|")
                .names(new String[]{"numericLookUpCode", "numericAssociateId", "amount", "paymentStartDate", "paymentType", "recurringPaymentDate"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PayrollDetails>() {{
                    setTargetType(PayrollDetails.class);
                }})
                .build();
    }

    @Bean
    public PayrollDetailItemWriterService<PayrollDetails> writer() {
        return new PayrollDetailItemWriterService<>(restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
