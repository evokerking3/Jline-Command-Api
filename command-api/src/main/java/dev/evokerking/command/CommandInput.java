package dev.evokerking.command;

/**
 * Represents parsed command input with name and arguments.
 */
public class CommandInput {
    private final String commandName;
    private final String args;
    
    /**
     * Create a CommandInput instance.
     *
     * @param commandName The name of the command
     * @param args The arguments to pass to the command
     */
    public CommandInput(String commandName, String args) {
        this.commandName = commandName;
        this.args = args;
    }
    
    /**
     * Get the command name.
     *
     * @return The command name
     */
    public String getCommandName() {
        return commandName;
    }
    
    /**
     * Get the command arguments.
     *
     * @return The arguments
     */
    public String getArgs() {
        return args;
    }
}
