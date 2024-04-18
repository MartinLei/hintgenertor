package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import de.nativehint.valueobject.ReflectionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class HintGenerationService {

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private JsonDiff jsonDiff;

    @Autowired
    private GenerateClass generateClass;



    public void run() throws FileNotFoundException, URISyntaxException {
        String pathSpringReflection = ClassLoader.getSystemResource("exampleJson/spring_reflect-config.json").toURI().getPath();
        String pathAgentReflection = ClassLoader.getSystemResource("exampleJson/agent_reflect-config.json").toURI().getPath();

        File springJson = fileHelper.getFile(pathSpringReflection);
        File agentJson = fileHelper.getFile(pathAgentReflection);
        List<ReflectionEntry> reflectionEntries = jsonDiff.generateDiff(agentJson, springJson);

        generateClass.generateAndSaveClass(reflectionEntries);
    }
}
