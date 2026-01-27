package dev.evokerking.command;

/**
 * Enum representing different parameter types for command arguments.
 */
public enum ParameterType {
    STRING("string", "Text input"),
    INTEGER("integer", "Numeric input (whole numbers)"),
    BOOLEAN("boolean", "True or false"),
    ENUM("enum", "One of several predefined values"),
    FLOAT("float", "Numeric input (decimals)"),
    FILE("file", "File path"),
    CHOICE("choice", "Multiple choice options");

    private final String type;
    private final String description;

    ParameterType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
