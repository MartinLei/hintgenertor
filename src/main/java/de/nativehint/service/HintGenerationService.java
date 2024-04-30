package de.nativehint.service;

import de.nativehint.valueobject.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HintGenerationService {


    @Autowired
    private HintGenerationDiffService hintGenerationDiffService;

    @Autowired
    private HintGenerationFolderService hintGenerationFolderService;


    @Value("${hintsgenerator.execute}")
    private JobName execute;


    public void run() throws IOException {

        switch (execute) {
            case diff -> hintGenerationDiffService.run();
            case singleFile -> hintGenerationDiffService.runFromBasePath();
            case folder -> hintGenerationFolderService.run();
            default -> throw new IllegalStateException("No match for execution found: '" + execute + "'");
        }
    }

}
