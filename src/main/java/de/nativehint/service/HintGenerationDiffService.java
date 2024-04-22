package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import de.nativehint.valueobject.ReflectionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class HintGenerationDiffService {

    private static final String REFLECT_CONFIG_JSON = "/reflect-config.json";

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private JsonDiff jsonDiff;

    @Autowired
    private GenerateClass generateClass;

    @Value("${hintsgenerator.springPath}")
    private String springPath;

    @Value("${hintsgenerator.agentPath}")
    private String agentPath;



    public void run() throws FileNotFoundException {
        List<ReflectionEntry> reflectionEntries = generateHints_ReflectionConfig(springPath, agentPath);

        //generateClass.generateAndSaveClass(reflectionEntries);
    }

    private List<ReflectionEntry> generateHints_ReflectionConfig(String springPath, String agentPath) throws FileNotFoundException {
        File springJson = fileHelper.getFile(springPath + REFLECT_CONFIG_JSON);
        File agentJson = fileHelper.getFile(agentPath + REFLECT_CONFIG_JSON);

        return jsonDiff.generateDiff_ReflectionConfig(agentJson, springJson);
    }


}
