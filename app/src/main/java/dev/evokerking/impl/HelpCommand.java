package dev.evokerking.impl;

import java.util.ArrayList;
import java.util.List;

import dev.evokerking.command.Command;
import dev.evokerking.command.CommandRegistry;
import dev.evokerking.command.Parameter;

/**
 * Built-in help command to display available commands.
 */
public class HelpCommand implements Command {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String execute(String args) {
        String commandName = args.trim().toLowerCase();
        
        // If specific command requested, show detailed help
        if (!commandName.isEmpty() && registry.exists(commandName)) {
            return getDetailedHelp(registry.getCommands().get(commandName));
        }
        
        // Otherwise show all commands
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        sb.append("====================\n");

        registry.getCommands().forEach((name, command) -> {
            sb.append("\n").append(command.getUsage()).append("\n");
            sb.append("  Description: ").append(command.getDescription()).append("\n");
            
            List<Parameter> params = command.getParameters();
            if (!params.isEmpty()) {
                sb.append("  Parameters:\n");
                for (Parameter param : params) {
                    sb.append("    - ").append(param).append("\n");
                }
            }
        });

        sb.append("\nexit - Exits the interactive mode\n");
        sb.append("\nFor detailed help on a command, type: help <command-name>\n");
        return sb.toString();
    }

    private String getDetailedHelp(Command command) {
        StringBuilder sb = new StringBuilder();
        sb.append("Command: ").append(command.getName()).append("\n");
        sb.append("Description: ").append(command.getDescription()).append("\n");
        sb.append("Usage: ").append(command.getUsage()).append("\n");
        
        List<Parameter> params = command.getParameters();
        if (!params.isEmpty()) {
            sb.append("\nParameters:\n");
            for (Parameter param : params) {
                sb.append("  ").append(param).append("\n");
            }
        }
        
        return sb.toString();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays help information about commands";
    }

    @Override
    public String getUsage() {
        return "help [command-name] - Shows all available commands or detailed help for a specific command";
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter.Builder("command-name")
            .type(dev.evokerking.command.ParameterType.STRING)
            .description("Optional: Name of a command to get detailed help")
            .required(false)
            .build());
        return params;
    }
}
