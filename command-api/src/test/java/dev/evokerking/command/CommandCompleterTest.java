package dev.evokerking.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandCompleterTest {
    private CommandRegistry registry;
    private CommandCompleter completer;

    @BeforeEach
    void setUp() {
        registry = CommandRegistry.getInstance();
        completer = new CommandCompleter(registry);
    }

    @Test
    void testCompleterCreation() {
        assertNotNull(completer);
        assertNotNull(registry);
    }

    @Test
    void testCompleterWithCommandRegistry() {
        Command cmd = createMockCommand("test");
        registry.register(cmd);
        
        assertNotNull(completer);
        assertTrue(registry.exists("test"));
    }

    @Test
    void testCompleterImplementsCompleter() {
        assertTrue(completer instanceof org.jline.reader.Completer);
    }

    // Helper methods
    private Command createMockCommand(String name) {
        return createMockCommand(name, new ArrayList<>());
    }

    private Command createMockCommand(String name, List<Parameter> params) {
        return new Command() {
            @Override
            public String execute(String args) {
                return "Executed " + name;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return "Command: " + name;
            }

            @Override
            public String getUsage() {
                return name;
            }

            @Override
            public List<Parameter> getParameters() {
                return params;
            }
        };
    }
}

