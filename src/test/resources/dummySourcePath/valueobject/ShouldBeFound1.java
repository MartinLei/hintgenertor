package de.valueobject;

public class ShouldNotBeFound1 {

    public static class InnerClassShouldBeFound1 {
    }

    public static class InnerClassShouldBeFound2 implements Bla {
    }

    public enum InnerEnum {
        test1,
        test2
    }
}
