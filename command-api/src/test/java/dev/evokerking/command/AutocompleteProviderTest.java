package dev.evokerking.command;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AutocompleteProviderTest {

    @Test
    void testGetSuggestionsForBoolean() {
        Parameter param = new Parameter.Builder("flag")
                .type(ParameterType.BOOLEAN)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertTrue(suggestions.contains("true"), "Should suggest 'true'");
        assertTrue(suggestions.contains("false"), "Should suggest 'false'");
    }

    @Test
    void testGetSuggestionsForBooleanWithPartialInput() {
        Parameter param = new Parameter.Builder("flag")
                .type(ParameterType.BOOLEAN)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "t");
        assertTrue(suggestions.contains("true"), "Should suggest 'true' for 't'");
        assertFalse(suggestions.contains("false"), "Should not suggest 'false' for 't'");
    }

    @Test
    void testGetSuggestionsForChoice() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("red", "green", "blue")
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.containsAll(List.of("red", "green", "blue")));
    }

    @Test
    void testGetSuggestionsForChoiceWithFilter() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("red", "green", "blue")
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "r");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("red"));
    }

    @Test
    void testGetSuggestionsForEnum() {
        Parameter param = new Parameter.Builder("status")
                .type(ParameterType.ENUM)
                .choices("active", "inactive", "pending")
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.containsAll(List.of("active", "inactive", "pending")));
    }

    @Test
    void testGetSuggestionsForInteger() {
        Parameter param = new Parameter.Builder("age")
                .type(ParameterType.INTEGER)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("<number>"), "Should suggest placeholder for integer");
    }

    @Test
    void testGetSuggestionsForFloat() {
        Parameter param = new Parameter.Builder("price")
                .type(ParameterType.FLOAT)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("<decimal>"), "Should suggest placeholder for float");
    }

    @Test
    void testGetSuggestionsForFile() {
        Parameter param = new Parameter.Builder("path")
                .type(ParameterType.FILE)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("<file-path>"), "Should suggest placeholder for file");
    }

    @Test
    void testGetSuggestionsForString() {
        Parameter param = new Parameter.Builder("name")
                .type(ParameterType.STRING)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("<text>"), "Should suggest placeholder for string");
    }

    @Test
    void testGetSuggestionsForCommandWithParameters() {
        Command cmd = new MockCommand(List.of(
                new Parameter.Builder("name").type(ParameterType.STRING).build(),
                new Parameter.Builder("age").type(ParameterType.INTEGER).build()
        ));
        
        List<String> suggestions1 = AutocompleteProvider.getSuggestionsForCommand(cmd, 0, "");
        assertTrue(suggestions1.contains("<text>"));
        
        List<String> suggestions2 = AutocompleteProvider.getSuggestionsForCommand(cmd, 1, "");
        assertTrue(suggestions2.contains("<number>"));
    }

    @Test
    void testGetSuggestionsForCommandOutOfRange() {
        Command cmd = new MockCommand(List.of(
                new Parameter.Builder("name").type(ParameterType.STRING).build()
        ));
        
        List<String> suggestions = AutocompleteProvider.getSuggestionsForCommand(cmd, 5, "");
        assertTrue(suggestions.isEmpty(), "Should return empty list for out of range parameter");
    }

    @Test
    void testChoiceFilterCaseSensitive() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("Red", "Green", "Blue")
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "r");
        assertTrue(suggestions.contains("Red"), "Should be case-insensitive");
    }

    @Test
    void testBooleanFilterCaseSensitive() {
        Parameter param = new Parameter.Builder("flag")
                .type(ParameterType.BOOLEAN)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "f");
        assertTrue(suggestions.contains("false"), "Should be case-insensitive");
    }

    @Test
    void testNoMatchesReturnsAllChoices() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("red", "green", "blue")
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "xyz");
        assertEquals(3, suggestions.size(), "Should return all choices if none match");
        assertTrue(suggestions.containsAll(List.of("red", "green", "blue")));
    }

    @Test
    void testEnumWithNoChoices() {
        Parameter param = new Parameter.Builder("status")
                .type(ParameterType.ENUM)
                .build();
        
        List<String> suggestions = AutocompleteProvider.getSuggestions(param, "");
        assertTrue(suggestions.isEmpty(), "Should return empty list for enum with no choices");
    }

    // Mock command for testing
    private static class MockCommand implements Command {
        private final List<Parameter> parameters;

        MockCommand(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        @Override
        public String execute(String args) {
            return "Mock executed";
        }

        @Override
        public String getName() {
            return "mock";
        }

        @Override
        public String getDescription() {
            return "Mock command";
        }

        @Override
        public String getUsage() {
            return "mock";
        }

        @Override
        public List<Parameter> getParameters() {
            return parameters;
        }
    }
}
