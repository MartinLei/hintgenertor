package de.test;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

@SuppressWarnings({"checkstyle:LineLength", "checkstyle:MethodLength"})
public class GeneratedMissingNativeServiceHint implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
            .registerType(TypeReference.of("de.dummy.AClass.java"), MemberCategory.values())
            .registerType(TypeReference.of("de.dummy.BClass.java"), MemberCategory.values());

    }
}
