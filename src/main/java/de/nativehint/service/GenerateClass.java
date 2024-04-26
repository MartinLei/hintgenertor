package de.nativehint.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GenerateClass {


    @Value("${hintsgenerator.generatedClassName}")
    String generatedClassName;

    @Value("${hintsgenerator.generatedClassPackageName}")
    String generatedClassPackageName;


    public String generateClassSource(List<String> entryList) {
        return generateClassSource() +
            generateRegisterTypes(entryList) +
            generateClassSourceCodeEnd();
    }

    public String generateClassSource(Map<String, List<String>> folderMap) {
        return generateClassSource() +
            generateRegisterTypes(folderMap) +
            generateClassSourceCodeEnd();
    }

    private String generateClassSource() {
        return "package " + generatedClassPackageName + ";\n\n" +
            "import org.springframework.aot.hint.MemberCategory;\n" +
            "import org.springframework.aot.hint.RuntimeHints;\n" +
            "import org.springframework.aot.hint.RuntimeHintsRegistrar;\n" +
            "import org.springframework.aot.hint.TypeReference;\n\n"+
            "@SuppressWarnings({\"checkstyle:LineLength\", \"checkstyle:MethodLength\"})\n" +
            "public class " + generatedClassName + " implements RuntimeHintsRegistrar {\n\n" +
            "    @Override\n" +
            "    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {\n";
    }

    private String generateClassSourceCodeEnd() {
        return "" +
            "    }\n" +
            "}\n";
    }

    private String generateRegisterTypes(List<String> entryList) {
        StringBuilder sb = new StringBuilder();

        Iterator<String> iterator = entryList.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i % 30 == 0) {
                sb.append("        hints.reflection()\n");
            }
            String entry = iterator.next();
            sb.append("            .registerType(TypeReference.of(\"");
            sb.append(entry);
            sb.append("\"), MemberCategory.values())");

            if (i % 30 == 29 || !iterator.hasNext()) {
                sb.append(";\n");
            }
            sb.append("\n");

            i++;
        }

        return sb.toString();
    }

    private String generateRegisterTypes(Map<String, List<String>> folderMap) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : folderMap.entrySet()) {
            sb.append("        // " + entry.getKey() + "\n");
            sb.append(generateRegisterTypes(entry.getValue()));
        }
        return sb.toString();
    }
}
