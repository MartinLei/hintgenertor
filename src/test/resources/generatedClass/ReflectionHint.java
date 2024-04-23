package de.test;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

@SuppressWarnings("checkstyle:LineLength")
public class GeneratedMissingNativeServiceHint implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
            .registerType(de.dummy.AClass.java.class, MemberCategory.values())
            .registerType(de.dummy.BClass.java.class, MemberCategory.values());

    }
}
