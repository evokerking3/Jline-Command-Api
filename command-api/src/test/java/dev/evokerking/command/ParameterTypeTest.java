package dev.evokerking.command;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTypeTest {

    @Test
    void testStringType() {
        ParameterType type = ParameterType.STRING;
        assertEquals("string", type.getType());
        assertEquals("Text input", type.getDescription());
    }

    @Test
    void testIntegerType() {
        ParameterType type = ParameterType.INTEGER;
        assertEquals("integer", type.getType());
        assertEquals("Numeric input (whole numbers)", type.getDescription());
    }

    @Test
    void testBooleanType() {
        ParameterType type = ParameterType.BOOLEAN;
        assertEquals("boolean", type.getType());
        assertEquals("True or false", type.getDescription());
    }

    @Test
    void testEnumType() {
        ParameterType type = ParameterType.ENUM;
        assertEquals("enum", type.getType());
        assertEquals("One of several predefined values", type.getDescription());
    }

    @Test
    void testFloatType() {
        ParameterType type = ParameterType.FLOAT;
        assertEquals("float", type.getType());
        assertEquals("Numeric input (decimals)", type.getDescription());
    }

    @Test
    void testFileType() {
        ParameterType type = ParameterType.FILE;
        assertEquals("file", type.getType());
        assertEquals("File path", type.getDescription());
    }

    @Test
    void testChoiceType() {
        ParameterType type = ParameterType.CHOICE;
        assertEquals("choice", type.getType());
        assertEquals("Multiple choice options", type.getDescription());
    }

    @Test
    void testAllTypesExist() {
        assertDoesNotThrow(() -> {
            ParameterType.STRING.getType();
            ParameterType.INTEGER.getType();
            ParameterType.BOOLEAN.getType();
            ParameterType.ENUM.getType();
            ParameterType.FLOAT.getType();
            ParameterType.FILE.getType();
            ParameterType.CHOICE.getType();
        });
    }

    @Test
    void testTypeStringFormats() {
        assertEquals("string", ParameterType.STRING.getType());
        assertEquals("integer", ParameterType.INTEGER.getType());
        assertEquals("boolean", ParameterType.BOOLEAN.getType());
        assertEquals("enum", ParameterType.ENUM.getType());
        assertEquals("float", ParameterType.FLOAT.getType());
        assertEquals("file", ParameterType.FILE.getType());
        assertEquals("choice", ParameterType.CHOICE.getType());
    }

    @Test
    void testDescriptionStrings() {
        assertNotNull(ParameterType.STRING.getDescription());
        assertNotNull(ParameterType.INTEGER.getDescription());
        assertNotNull(ParameterType.BOOLEAN.getDescription());
        assertNotNull(ParameterType.ENUM.getDescription());
        assertNotNull(ParameterType.FLOAT.getDescription());
        assertNotNull(ParameterType.FILE.getDescription());
        assertNotNull(ParameterType.CHOICE.getDescription());
    }

    @Test
    void testEnumValues() {
        ParameterType[] types = ParameterType.values();
        assertEquals(7, types.length, "Should have exactly 7 parameter types");
    }

    @Test
    void testEnumValueOf() {
        assertEquals(ParameterType.STRING, ParameterType.valueOf("STRING"));
        assertEquals(ParameterType.INTEGER, ParameterType.valueOf("INTEGER"));
        assertEquals(ParameterType.BOOLEAN, ParameterType.valueOf("BOOLEAN"));
    }
}
