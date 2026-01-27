package dev.evokerking.command;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest {

    @Test
    void testParameterBuilder() {
        Parameter param = new Parameter.Builder("name").build();
        assertEquals("name", param.getName());
        assertEquals(ParameterType.STRING, param.getType());
        assertEquals("", param.getDescription());
        assertFalse(param.isRequired());
    }

    @Test
    void testParameterBuilderWithType() {
        Parameter param = new Parameter.Builder("age")
                .type(ParameterType.INTEGER)
                .build();
        assertEquals("age", param.getName());
        assertEquals(ParameterType.INTEGER, param.getType());
    }

    @Test
    void testParameterBuilderWithDescription() {
        Parameter param = new Parameter.Builder("username")
                .description("The username of the user")
                .build();
        assertEquals("The username of the user", param.getDescription());
    }

    @Test
    void testParameterBuilderRequired() {
        Parameter param = new Parameter.Builder("password")
                .required(true)
                .build();
        assertTrue(param.isRequired());
    }

    @Test
    void testParameterBuilderOptional() {
        Parameter param = new Parameter.Builder("nickname")
                .required(false)
                .build();
        assertFalse(param.isRequired());
    }

    @Test
    void testParameterBuilderWithChoices() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("red", "green", "blue")
                .build();
        
        assertTrue(param.hasChoices());
        List<String> choices = param.getChoices();
        assertEquals(3, choices.size());
        assertTrue(choices.contains("red"));
        assertTrue(choices.contains("green"));
        assertTrue(choices.contains("blue"));
    }

    @Test
    void testParameterBuilderNoChoices() {
        Parameter param = new Parameter.Builder("name")
                .type(ParameterType.STRING)
                .build();
        assertFalse(param.hasChoices());
    }

    @Test
    void testGetChoicesReturnsCopy() {
        Parameter param = new Parameter.Builder("color")
                .type(ParameterType.CHOICE)
                .choices("red", "green")
                .build();
        
        List<String> choices1 = param.getChoices();
        List<String> choices2 = param.getChoices();
        assertNotSame(choices1, choices2, "Should return a copy");
        assertEquals(choices1, choices2, "But copies should be equal");
    }

    @Test
    void testParameterCompleteBuilder() {
        Parameter param = new Parameter.Builder("level")
                .type(ParameterType.INTEGER)
                .description("The level (1-10)")
                .required(true)
                .build();
        
        assertEquals("level", param.getName());
        assertEquals(ParameterType.INTEGER, param.getType());
        assertEquals("The level (1-10)", param.getDescription());
        assertTrue(param.isRequired());
        assertFalse(param.hasChoices());
    }

    @Test
    void testParameterWithEnumType() {
        Parameter param = new Parameter.Builder("status")
                .type(ParameterType.ENUM)
                .choices("active", "inactive", "pending")
                .build();
        
        assertEquals(ParameterType.ENUM, param.getType());
        assertEquals(3, param.getChoices().size());
    }

    @Test
    void testParameterToString() {
        Parameter param = new Parameter.Builder("username")
                .type(ParameterType.STRING)
                .description("User login name")
                .required(true)
                .build();
        
        String str = param.toString();
        assertTrue(str.contains("string"), "Should contain type");
        assertTrue(str.contains("username"), "Should contain name");
        assertTrue(str.contains("User login name"), "Should contain description");
        assertTrue(str.contains("REQUIRED"), "Should indicate required");
    }

    @Test
    void testParameterToStringOptional() {
        Parameter param = new Parameter.Builder("nickname")
                .type(ParameterType.STRING)
                .description("Optional display name")
                .required(false)
                .build();
        
        String str = param.toString();
        assertFalse(str.contains("REQUIRED"), "Should not indicate required");
    }

    @Test
    void testParameterAllTypes() {
        testParameterType(ParameterType.STRING, "string");
        testParameterType(ParameterType.INTEGER, "integer");
        testParameterType(ParameterType.BOOLEAN, "boolean");
        testParameterType(ParameterType.ENUM, "enum");
        testParameterType(ParameterType.FLOAT, "float");
        testParameterType(ParameterType.FILE, "file");
        testParameterType(ParameterType.CHOICE, "choice");
    }

    private void testParameterType(ParameterType type, String expectedTypeString) {
        Parameter param = new Parameter.Builder("test")
                .type(type)
                .build();
        assertEquals(type, param.getType());
        assertEquals(expectedTypeString, param.getType().getType());
    }

    @Test
    void testParameterBuilderChaining() {
        Parameter param = new Parameter.Builder("test")
                .type(ParameterType.INTEGER)
                .description("Test param")
                .required(true)
                .choices("1", "2", "3")
                .build();
        
        assertEquals("test", param.getName());
        assertEquals(ParameterType.INTEGER, param.getType());
        assertEquals("Test param", param.getDescription());
        assertTrue(param.isRequired());
        assertEquals(3, param.getChoices().size());
    }

    @Test
    void testParameterWithEmptyChoices() {
        Parameter param = new Parameter.Builder("option")
                .type(ParameterType.CHOICE)
                .build();
        
        assertFalse(param.hasChoices());
        assertTrue(param.getChoices().isEmpty());
    }

    @Test
    void testParameterWithSingleChoice() {
        Parameter param = new Parameter.Builder("flag")
                .type(ParameterType.CHOICE)
                .choices("yes")
                .build();
        
        assertTrue(param.hasChoices());
        assertEquals(1, param.getChoices().size());
        assertEquals("yes", param.getChoices().get(0));
    }
}
