package dev.evokerking.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides autocomplete suggestions based on command parameters and types.
 */
public class AutocompleteProvider {

    /**
     * Generate autocomplete suggestions for a given parameter type.
     *
     * @param parameter The parameter to generate suggestions for
     * @param currentInput The current user input
     * @return A list of autocomplete suggestions
     */
    public static List<String> getSuggestions(Parameter parameter, String currentInput) {
        List<String> suggestions = new ArrayList<>();

        switch (parameter.getType()) {
            case BOOLEAN:
                suggestions.addAll(filterMatches(currentInput, "true", "false"));
                break;

            case ENUM:
            case CHOICE:
                suggestions.addAll(filterMatches(currentInput, parameter.getChoices().toArray(new String[0])));
                break;

            case INTEGER:
                suggestions.add("<number>");
                break;

            case FLOAT:
                suggestions.add("<decimal>");
                break;

            case FILE:
                suggestions.add("<file-path>");
                break;

            case STRING:
            default:
                suggestions.add("<text>");
                break;
        }

        return suggestions;
    }

    /**
     * Generate autocomplete suggestions for a command and its parameters.
     *
     * @param command The command to get suggestions for
     * @param parameterIndex The index of the parameter being autocompleted
     * @param currentInput The current user input
     * @return A list of autocomplete suggestions
     */
    public static List<String> getSuggestionsForCommand(Command command, int parameterIndex, String currentInput) {
        List<Parameter> parameters = command.getParameters();
        
        if (parameterIndex >= parameters.size()) {
            return new ArrayList<>();
        }

        return getSuggestions(parameters.get(parameterIndex), currentInput);
    }

    /**
     * Filter choices that match the current input.
     *
     * @param input The current user input
     * @param choices The available choices
     * @return Filtered list of matching choices
     */
    private static List<String> filterMatches(String input, String... choices) {
        List<String> matches = new ArrayList<>();
        String lowerInput = input.toLowerCase();

        for (String choice : choices) {
            if (choice.toLowerCase().startsWith(lowerInput)) {
                matches.add(choice);
            }
        }

        // If no matches found, return all choices
        if (matches.isEmpty()) {
            for (String choice : choices) {
                matches.add(choice);
            }
        }

        return matches;
    }
}
