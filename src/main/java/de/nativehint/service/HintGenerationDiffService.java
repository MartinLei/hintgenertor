package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import de.nativehint.valueobject.HintEntry;
import de.nativehint.valueobject.HintType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class HintGenerationDiffService {

    static final String REFLECT_CONFIG_JSON = "/reflect-config.json";

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private JsonDiff jsonDiff;

    @Autowired
    private GenerateClass generateClass;

    @Value("${hintsgenerator.diff.baseMetaInfoPath}")
    private String baseMetInfoPath;

    @Value("${hintsgenerator.diff.extendedMetaInfoPath}")
    private String extendedMetaInfoPath;


    public void run() throws FileNotFoundException {
        File baseMetaInfoJson = fileHelper.getFile(baseMetInfoPath + REFLECT_CONFIG_JSON);
        File extendedMetaInfoJson = fileHelper.getFile(extendedMetaInfoPath + REFLECT_CONFIG_JSON);

        List<String> reflectionEntries = jsonDiff.generateDiff_ReflectionConfig(extendedMetaInfoJson, baseMetaInfoJson);
        List<HintEntry> hintEntries = List.of(new HintEntry(HintType.reflection,null,reflectionEntries));

        String sourceCode = generateClass.generateClassSource(hintEntries);
        fileHelper.saveSourceCodeToFile(sourceCode);
    }

    public void runFromBasePath() throws FileNotFoundException {
        File baseMetaInfoJson = fileHelper.getFile(baseMetInfoPath + REFLECT_CONFIG_JSON);
        List<String> reflectionEntries = jsonDiff.generate_ReflectionConfig(baseMetaInfoJson);
        List<HintEntry> hintEntries = List.of(new HintEntry(HintType.reflection,null,reflectionEntries));

        String sourceCode = generateClass.generateClassSource(hintEntries);
        fileHelper.saveSourceCodeToFile(sourceCode);
    }

}
