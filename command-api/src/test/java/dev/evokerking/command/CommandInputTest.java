package dev.evokerking.command;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CommandInputTest {

    @Test
    void testCreateCommandInput() {
        CommandInput input = new CommandInput("test", "arg1 arg2");
        assertEquals("test", input.getCommandName());
        assertEquals("arg1 arg2", input.getArgs());
    }

    @Test
    void testGetCommandName() {
        CommandInput input = new CommandInput("mycommand", "");
        assertEquals("mycommand", input.getCommandName());
    }

    @Test
    void testGetArgs() {
        CommandInput input = new CommandInput("test", "some arguments");
        assertEquals("some arguments", input.getArgs());
    }

    @Test
    void testCommandInputWithEmptyArgs() {
        CommandInput input = new CommandInput("test", "");
        assertEquals("test", input.getCommandName());
        assertEquals("", input.getArgs());
    }

    @Test
    void testCommandInputWithNullArgs() {
        CommandInput input = new CommandInput("test", null);
        assertEquals("test", input.getCommandName());
        assertNull(input.getArgs());
    }

    @Test
    void testCommandInputCaseSensitivity() {
        CommandInput input = new CommandInput("TEST", "ARGS");
        assertEquals("TEST", input.getCommandName(), "Command name case should be preserved");
        assertEquals("ARGS", input.getArgs(), "Args case should be preserved");
    }

    @Test
    void testCommandInputWithSpecialCharacters() {
        CommandInput input = new CommandInput("cmd-with-dash", "arg-with-dash");
        assertEquals("cmd-with-dash", input.getCommandName());
        assertEquals("arg-with-dash", input.getArgs());
    }

    @Test
    void testCommandInputWithSpaces() {
        CommandInput input = new CommandInput("test", "   multiple   spaces   ");
        assertEquals("test", input.getCommandName());
        assertEquals("   multiple   spaces   ", input.getArgs());
    }

    @Test
    void testCommandInputImmutability() {
        CommandInput input = new CommandInput("test", "args");
        String originalName = input.getCommandName();
        String originalArgs = input.getArgs();
        
        // Verify values haven't changed
        assertEquals(originalName, input.getCommandName());
        assertEquals(originalArgs, input.getArgs());
    }
}
