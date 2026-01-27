package dev.evokerking.command;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

/**
 * JLine Completer for command-based autocomplete in interactive mode.
 */
public class CommandCompleter implements Completer {
    private final CommandRegistry registry;

    public CommandCompleter(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String[] parts = line.line().split("\\s+");
        
        if (parts.length == 0 || parts[0].isEmpty()) {
            return;
        }

        if (parts.length == 1) {
            // Autocomplete command names
            String prefix = parts[0].toLowerCase();
            for (String commandName : registry.getCommands().keySet()) {
                if (commandName.startsWith(prefix)) {
                    candidates.add(new Candidate(commandName + " ", commandName, null, null, null, null, false));
                }
            }
        } else {
            // Autocomplete parameters for the command
            String commandName = parts[0];
            Command command = registry.getCommands().get(commandName.toLowerCase());
            
            if (command != null) {
                List<Parameter> parameters = command.getParameters();
                int paramIndex = parts.length - 2; // -1 for command, -1 for current word
                
                if (paramIndex < parameters.size()) {
                    Parameter param = parameters.get(paramIndex);
                    String currentInput = parts[parts.length - 1];
                    List<String> suggestions = AutocompleteProvider.getSuggestions(param, currentInput);
                    
                    for (String suggestion : suggestions) {
                        if (suggestion.startsWith("<") && suggestion.endsWith(">")) {
                            // Placeholder suggestion - don't add as candidate, just informational
                            candidates.add(new Candidate("", "", null, 
                                param.getDescription() + " - " + suggestion, null, null, true));
                        } else {
                            candidates.add(new Candidate(suggestion, suggestion, null, 
                                param.getDescription(), null, null, false));
                        }
                    }
                }
            }
        }
    }
}
