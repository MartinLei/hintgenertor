package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import de.nativehint.valueobject.HintEntry;
import de.nativehint.valueobject.HintType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class HintGenerationFolderService {


    @Autowired
    private GenerateClass generateClass;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private FileNameCollector fileNameCollector;



    public void run() throws IOException {
        List<Path> reflectionFolders = fileNameCollector.getReflectionFolders();
        List<Path> serializationFolders = fileNameCollector.getSerializationFolders();

        List<HintEntry> folderEntries = Stream.concat(
            fileNameCollector.getFileList(HintType.serialization, serializationFolders).stream(),
            fileNameCollector.getFileList(HintType.reflection, reflectionFolders).stream()
            )
            .toList();

        String sourceCode = generateClass.generateClassSource(folderEntries);
        fileHelper.saveSourceCodeToFile(sourceCode);
    }

}
