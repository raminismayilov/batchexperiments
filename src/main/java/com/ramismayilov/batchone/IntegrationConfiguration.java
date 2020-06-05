package com.ramismayilov.batchone;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

import java.io.File;

@Configuration
public class IntegrationConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job sampleJob;

    protected DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow sampleFlow() {
        return IntegrationFlows
                .from(fileReadingMessageSource(), c -> c.poller(Pollers.fixedDelay(5000)))
                .channel(inputChannel())
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingMessageHandler())
                .handle(jobExecution -> System.out.println(jobExecution.getPayload()))
                .get();
    }

    @Bean
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("watchedfolder"));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        source.setUseWatchService(true);
        source.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE);
        return source;
    }

    @Bean
    FileMessageToJobRequest fileMessageToJobRequest() {
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();
        transformer.setJob(sampleJob);
        transformer.setFileParameterName("file_path");
        return transformer;
    }

    @Bean
    JobLaunchingMessageHandler jobLaunchingMessageHandler() {
        JobLaunchingMessageHandler handler = new JobLaunchingMessageHandler(jobLauncher);
        return handler;
    }
}
