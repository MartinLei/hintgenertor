package de.nativehint.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileHelper {

    @Value("${hintsgenerator.generatedClassPath}")
    private String generatedClassPath;

    @Value("${hintsgenerator.generatedClassName}")
    String generatedClassName;

    public File getFile(String pathName) throws FileNotFoundException {
        Path path = Paths.get(pathName);
        File file = path.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File with path '%s' does not exist", pathName));
        }

        return file;
    }

    public void saveSourceCodeToFile(String sourceCode) {
        try {
            FileWriter writer = new FileWriter(generatedClassPath + "/" + generatedClassName + ".java");
            writer.write(sourceCode);
            writer.close();
            log.info(String.format("Write Java file '%s'.java to '%s'", generatedClassName, generatedClassPath));
        } catch (IOException e) {
            log.error("Error saving Java file to disk:", e);
        }
    }
}
