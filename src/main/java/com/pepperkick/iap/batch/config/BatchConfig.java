package com.pepperkick.iap.batch.config;

import com.pepperkick.iap.batch.processor.LineProcessor;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Date;

@Configuration
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job processJob() {
        return jobBuilderFactory.
            get("batchJob").
            listener(new JobExecutionListener() {
                @Override
                public void beforeJob(JobExecution jobExecution) {
                    // Do nothing
                }

                @Override
                public void afterJob(JobExecution jobExecution) {
                    Date start = jobExecution.getStartTime();
                    Date end = jobExecution.getEndTime();
                    long diff = end.getTime() - start.getTime();
                    logger.info("Job Execution Time: " + diff + " ms");
                }
            }).
            flow(processStep()).
            end().
            build();
    }

    @Bean
    public Step processStep() {
        return stepBuilderFactory.
            get("batchStep").
            allowStartIfComplete(true).
            <String, JSONObject> chunk(5000).
            reader(createFileReader()).
            writer(createFileWriter()).
            processor(new LineProcessor()).
            build();
    }

    private ItemReader<String> createFileReader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();

        reader.setResource(new FileSystemResource("input.txt"));
        reader.setLineMapper(new PassThroughLineMapper());

        return reader;
    }

    private ItemWriter<JSONObject> createFileWriter() {
        FlatFileItemWriter<JSONObject> writer = new FlatFileItemWriter<>();

        writer.setResource(new FileSystemResource("output.txt"));
        writer.setLineAggregator(new PassThroughLineAggregator<>());

        return writer;
    }
}
