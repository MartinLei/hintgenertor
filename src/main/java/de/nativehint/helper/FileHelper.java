package de.nativehint.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileHelper {

    public File getFile(String pathName) throws FileNotFoundException {
        Path path = Paths.get(pathName);
        File file = path.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File with path '%s' does not exist", pathName));
        }

        return file;
    }
}
