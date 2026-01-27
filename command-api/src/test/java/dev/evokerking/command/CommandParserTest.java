package dev.evokerking.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void testParseSimpleCommand() {
        CommandInput input = CommandParser.parse("test");
        assertEquals("test", input.getCommandName());
        assertEquals("", input.getArgs());
    }

    @Test
    void testParseCommandWithSingleArgument() {
        CommandInput input = CommandParser.parse("test arg");
        assertEquals("test", input.getCommandName());
        assertEquals("arg", input.getArgs());
    }

    @Test
    void testParseCommandWithMultipleArguments() {
        CommandInput input = CommandParser.parse("test arg1 arg2 arg3");
        assertEquals("test", input.getCommandName());
        assertEquals("arg1 arg2 arg3", input.getArgs());
    }

    @Test
    void testParseCommandWithMultipleSpaces() {
        CommandInput input = CommandParser.parse("test    multiple   spaces");
        assertEquals("test", input.getCommandName());
        assertEquals("multiple   spaces", input.getArgs());
    }

    @Test
    void testParseCommandWithLeadingSpaces() {
        CommandInput input = CommandParser.parse("  test arg");
        assertEquals("", input.getCommandName(), "Leading spaces should result in empty command name");
    }

    @Test
    void testParseCommandNameCaseSensitivity() {
        CommandInput input = CommandParser.parse("TEST arg");
        assertEquals("TEST", input.getCommandName(), "Parser should preserve case");
    }

    @Test
    void testParseCommandWithQuotedArguments() {
        // Parser doesn't handle quotes specially, it just splits on whitespace
        CommandInput input = CommandParser.parse("test \"quoted arg\"");
        assertEquals("test", input.getCommandName());
        assertEquals("\"quoted arg\"", input.getArgs());
    }

    @Test
    void testParseCommandWithSpecialCharacters() {
        CommandInput input = CommandParser.parse("test arg-with-dash");
        assertEquals("test", input.getCommandName());
        assertEquals("arg-with-dash", input.getArgs());
    }

    @Test
    void testParseCommandWithEqualsSign() {
        CommandInput input = CommandParser.parse("test key=value");
        assertEquals("test", input.getCommandName());
        assertEquals("key=value", input.getArgs());
    }

    @Test
    void testParseEmptyString() {
        CommandInput input = CommandParser.parse("");
        assertEquals("", input.getCommandName());
        assertEquals("", input.getArgs());
    }

    @Test
    void testParseWhitespaceOnly() {
        CommandInput input = CommandParser.parse("   ");
        assertEquals("", input.getCommandName());
        assertEquals("", input.getArgs());
    }

    @Test
    void testParseCommandWithTab() {
        CommandInput input = CommandParser.parse("test\targ");
        assertEquals("test", input.getCommandName());
        assertEquals("arg", input.getArgs());
    }

    @Test
    void testParseCommandWithNewline() {
        CommandInput input = CommandParser.parse("test\narg");
        assertEquals("test", input.getCommandName());
        assertEquals("arg", input.getArgs());
    }

    @Test
    void testParseCommandWithNumberArguments() {
        CommandInput input = CommandParser.parse("calc 123 456");
        assertEquals("calc", input.getCommandName());
        assertEquals("123 456", input.getArgs());
    }

    @Test
    void testParseCommandWithPath() {
        CommandInput input = CommandParser.parse("open /path/to/file.txt");
        assertEquals("open", input.getCommandName());
        assertEquals("/path/to/file.txt", input.getArgs());
    }
}
