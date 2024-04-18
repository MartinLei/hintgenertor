package de.nativehint.valueobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReflectionEntry {

    @Setter
    private String name;
    @JsonIgnore
    private Boolean allPublicFields;
    @JsonIgnore
    private Boolean allDeclaredFields;
    @JsonIgnore
    private Boolean queryAllPublicConstructors;
    @JsonIgnore
    private Boolean queryAllDeclaredConstructors;
    @JsonIgnore
    private Boolean allPublicConstructors;
    @JsonIgnore
    private Boolean allDeclaredConstructors;
    @JsonIgnore
    private Boolean queryAllPublicMethods;
    @JsonIgnore
    private Boolean queryAllDeclaredMethods;
    @JsonIgnore
    private Boolean allPublicMethods;
    @JsonIgnore
    private Boolean allDeclaredMethods;
    @JsonIgnore
    private Boolean allPublicClasses;
    @JsonIgnore
    private Boolean allDeclaredClasses;
    @JsonIgnore
    private Object queriedMethods;
    @JsonIgnore
    private Object methods;
    @JsonIgnore
    private Object fields;
    @JsonIgnore
    private Object condition;

    public ReflectionEntry(String name) {
        this(name, false, false, false, false, false, false, false, false, false, false, false, false, null, null, null, null);
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof ReflectionEntry)) {
            return false;
        }
        ReflectionEntry reflectionEntry = (ReflectionEntry) other;
        if (reflectionEntry.name.equals(this.name)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
