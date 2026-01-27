package dev.evokerking.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {
    private CommandRegistry registry;
    private Command testCommand;

    @BeforeEach
    void setUp() {
        // Get fresh instance for each test (need to reset the registry)
        registry = CommandRegistry.getInstance();
        registry.reset(); // Clear any previous state
        testCommand = new Command() {
            @Override
            public String execute(String args) {
                return "Test output: " + args;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "A test command";
            }

            @Override
            public String getUsage() {
                return "test [args]";
            }
        };
    }

    @Test
    void testGetInstance() {
        CommandRegistry instance1 = CommandRegistry.getInstance();
        CommandRegistry instance2 = CommandRegistry.getInstance();
        assertSame(instance1, instance2, "CommandRegistry should be a singleton");
    }

    @Test
    void testRegisterCommand() {
        registry.register(testCommand);
        assertTrue(registry.exists("test"), "Registered command should exist");
    }

    @Test
    void testRegisterCommandCaseInsensitive() {
        registry.register(testCommand);
        assertTrue(registry.exists("TEST"), "Command lookup should be case-insensitive");
        assertTrue(registry.exists("Test"), "Command lookup should be case-insensitive");
        assertTrue(registry.exists("test"), "Command lookup should be case-insensitive");
    }

    @Test
    void testCommandDoesNotExist() {
        assertFalse(registry.exists("nonexistent"), "Non-existent command should return false");
    }

    @Test
    void testExecuteCommand() {
        registry.register(testCommand);
        String result = registry.execute("test", "hello world");
        assertEquals("Test output: hello world", result, "Command should execute and return output");
    }

    @Test
    void testExecuteCommandCaseInsensitive() {
        registry.register(testCommand);
        String result = registry.execute("TEST", "hello");
        assertEquals("Test output: hello", result, "Command execution should be case-insensitive");
    }

    @Test
    void testExecuteNonexistentCommand() {
        String result = registry.execute("nonexistent", "args");
        assertTrue(result.contains("Command not found"), "Should return error for non-existent command");
        assertTrue(result.contains("nonexistent"), "Error should mention the command name");
    }

    @Test
    void testExecuteCommandWithException() {
        Command errorCommand = new Command() {
            @Override
            public String execute(String args) {
                throw new RuntimeException("Test error");
            }

            @Override
            public String getName() {
                return "error";
            }

            @Override
            public String getDescription() {
                return "Error command";
            }

            @Override
            public String getUsage() {
                return "error";
            }
        };

        registry.register(errorCommand);
        String result = registry.execute("error", "");
        assertTrue(result.contains("Error executing command"), "Should handle command exceptions");
        assertTrue(result.contains("Test error"), "Error message should contain exception details");
    }

    @Test
    void testExecuteCommandWithEmptyArgs() {
        registry.register(testCommand);
        String result = registry.execute("test", "");
        assertEquals("Test output: ", result, "Command should handle empty arguments");
    }

    @Test
    void testGetCommands() {
        registry.register(testCommand);
        var commands = registry.getCommands();
        assertTrue(commands.containsKey("test"), "Should return registered command");
        assertEquals(testCommand, commands.get("test"), "Should return the correct command");
    }

    @Test
    void testGetCommandsReturnsACopy() {
        registry.register(testCommand);
        var commands1 = registry.getCommands();
        var commands2 = registry.getCommands();
        assertNotSame(commands1, commands2, "Should return a copy of the commands map");
    }

    @Test
    void testRegisterMultipleCommands() {
        Command cmd1 = new MockCommand("cmd1");
        Command cmd2 = new MockCommand("cmd2");

        registry.register(cmd1);
        registry.register(cmd2);

        assertTrue(registry.exists("cmd1"), "First command should be registered");
        assertTrue(registry.exists("cmd2"), "Second command should be registered");
        assertEquals(2, registry.getCommands().size(), "Should have 2 commands");
    }

    // Mock command for testing
    private static class MockCommand implements Command {
        private final String name;

        MockCommand(String name) {
            this.name = name;
        }

        @Override
        public String execute(String args) {
            return name + " executed with: " + args;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return "Mock command " + name;
        }

        @Override
        public String getUsage() {
            return name;
        }
    }
}
