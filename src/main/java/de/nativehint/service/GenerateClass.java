package de.nativehint.service;

import de.nativehint.valueobject.ReflectionEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class GenerateClass {

    @Value("${hintsgenerator.generatedClassPath}")
    private String generatedClassPath;

    @Value("${hintsgenerator.generatedClassName}")
    String generatedClassName;

    @Value("${hintsgenerator.generatedClassPackageName}")
    String generatedClassPackageName;



    public void generateAndSaveClass(List<ReflectionEntry> entryList) {
        String sourceCode = generateClassSourceCode( entryList);
        saveSourceCodeToFile(sourceCode);
    }

    public String generateClassSourceCode(List<ReflectionEntry> entryList) {
        return "package " + generatedClassPackageName + ";\n\n" +
            "import org.springframework.aot.hint.MemberCategory;\n" +
            "import org.springframework.aot.hint.RuntimeHints;\n" +
            "import org.springframework.aot.hint.RuntimeHintsRegistrar;\n\n" +
            "public class " + generatedClassName + " implements RuntimeHintsRegistrar {\n" +
            "    @Override\n" +
            "    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {\n" +
            "        hints.reflection()\n" +
            generateRegisterTypes(entryList) +
            "    }\n" +
            "}\n";
    }

    private String generateRegisterTypes(List<ReflectionEntry> entryList) {
        StringBuilder sb = new StringBuilder();

        Iterator<ReflectionEntry> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            ReflectionEntry entry = iterator.next();

            sb.append("            .registerType(");
            sb.append(entry.getName());
            sb.append(".class, MemberCategory.values())");

            if (!iterator.hasNext()) {
                sb.append(";\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void saveSourceCodeToFile(String sourceCode) {
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
