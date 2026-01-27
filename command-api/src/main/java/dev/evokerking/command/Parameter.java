package dev.evokerking.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Defines a parameter for a command with type information and optional choices.
 */
public class Parameter {
    private final String name;
    private final ParameterType type;
    private final String description;
    private final boolean required;
    private final List<String> choices; // For ENUM and CHOICE types

    private Parameter(String name, ParameterType type, String description, boolean required, List<String> choices) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.required = required;
        this.choices = choices;
    }

    public static class Builder {
        private final String name;
        private ParameterType type = ParameterType.STRING;
        private String description = "";
        private boolean required = false;
        private List<String> choices = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder type(ParameterType type) {
            this.type = type;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder choices(String... choices) {
            this.choices = Arrays.asList(choices);
            return this;
        }

        public Parameter build() {
            return new Parameter(name, type, description, required, choices);
        }
    }

    public String getName() {
        return name;
    }

    public ParameterType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public List<String> getChoices() {
        return new ArrayList<>(choices);
    }

    public boolean hasChoices() {
        return !choices.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)%s", 
            type.getType(), 
            name, 
            description,
            required ? " - REQUIRED" : "");
    }
}
