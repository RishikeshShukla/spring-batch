package com.batch.exp.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Slf4j
@Component
public class ResourceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFactory.class);
    @Value("${input.file.location}")
    private String inputFileLocation;

    @Value("${input.file.pattern}")
    private String inputFilePattern;

    private final ResourceLoader resourceLoader;

    /**
     * @param resourceLoader {@link ResourceLoader}
     */
    public ResourceFactory(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Resource[] resources() {
        LOGGER.info("Started resource loading...");
        Resource[] resources = {};
        try {
            resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("file:" + inputFileLocation + inputFilePattern);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Failed to load resources due to: {0}", e.getMessage(), e));
            System.exit(1);
        }
        LOGGER.info(MessageFormat.format("Resource Loading completed Successfully, with {0}, resources", resources.length));
        return resources;
    }
}
