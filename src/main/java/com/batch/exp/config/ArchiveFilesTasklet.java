package com.batch.exp.config;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * {@link ArchiveFilesTasklet} to move processed files into archive directory
 */
@Component
public class ArchiveFilesTasklet implements Tasklet {
    private Resource[] resources;

    @Value("${archive.file.location}")
    private String archiveFileLocation;

    /**
     *
     * @param stepContribution {@link StepContribution}
     * @param chunkContext {@link ChunkContext}
     * @return {@link RepeatStatus}
     * @throws Exception
     */

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        for (Resource resource : resources) {
            File file = resource.getFile();
            Files.move(file.toPath(), Paths.get(archiveFileLocation + file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }
        return RepeatStatus.FINISHED;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }

}
