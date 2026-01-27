package dev.evokerking;

import java.io.IOException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.google.gson.Gson;

import dev.evokerking.command.CommandRegistry;
import dev.evokerking.command.CommandCompleter;
import dev.evokerking.command.CommandParser;
import dev.evokerking.command.CommandInput;
import dev.evokerking.impl.CloseFile;
import dev.evokerking.impl.FromJsonCommand;
import dev.evokerking.impl.HelpCommand;
import dev.evokerking.impl.OpenFile;
import dev.evokerking.impl.ToJsonCommand;
import dev.evokerking.impl.WriteToFile;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class App {

    public static Gson gson = new Gson();

    public String getGreeting() {
        return "Hello, this program is meant to be ran with the -i flag.";
    }

    private static void initializeCommands(CommandRegistry registry) {
        registry.reset();
        registry.register(new ToJsonCommand(gson));
        registry.register(new HelpCommand(registry));
        registry.register(new FromJsonCommand(gson));
        registry.register(new OpenFile());
        registry.register(new WriteToFile());
        registry.register(new CloseFile());
    }

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        
        // Add the -i flag (no argument required)
        parser.accepts("i", "Activates interactive mode");
        
        
        OptionSet options = parser.parse(args);
        
        if (options.has("i")) {
            try {
                // Create a terminal
                Terminal terminal = TerminalBuilder.builder().system(true).build();

                // Initialize command registry
                CommandRegistry commandRegistry = CommandRegistry.getInstance();
                initializeCommands(commandRegistry);

                // Create a line reader with command completer
                LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new CommandCompleter(commandRegistry))
                    .build();
                
                terminal.writer().println("Interactive mode enabled. Type 'help' for available commands.");
                terminal.flush();
                
                // Read lines from the user
                while (true) {
                    String line = reader.readLine("> ");

                    // Exit if requested
                    if ("exit".equalsIgnoreCase(line)) {
                        break;
                    }

                    // Parse command and arguments
                    CommandInput input = CommandParser.parse(line);

                    // Execute command
                    String result = commandRegistry.execute(input.getCommandName(), input.getArgs());
                    terminal.writer().println(result);
                    terminal.flush();
                }

                terminal.writer().println("Goodbye!");
                terminal.close();

            } catch (IOException e) {
                System.err.println("Error creating terminal: " + e.getMessage());
            }

        } else {
            System.out.println(new App().getGreeting());
        }
    }
}
