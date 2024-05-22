package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import de.nativehint.valueobject.HintEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
        List<Path> folders = fileNameCollector.getFolders();
        List<HintEntry> folderEntries = fileNameCollector.getFileList(folders);
        String sourceCode = generateClass.generateClassSource(folderEntries);
        fileHelper.saveSourceCodeToFile(sourceCode);
    }

}
