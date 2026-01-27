package dev.evokerking.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for command implementations in the command-based framework.
 */
public interface Command {
    /**
     * Execute the command with the given arguments.
     *
     * @param args The arguments passed to the command (after the command name)
     * @return The result/output of the command execution
     */
    String execute(String args);

    /**
     * Get the name of the command.
     *
     * @return The command name (used to invoke the command)
     */
    String getName();

    /**
     * Get a description of what this command does.
     *
     * @return A brief description of the command
     */
    String getDescription();

    /**
     * Get usage information for this command.
     *
     * @return Usage information/examples
     */
    String getUsage();

    /**
     * Get the parameters this command accepts.
     *
     * @return A list of parameters (empty list if no parameters)
     */
    default List<Parameter> getParameters() {
        return new ArrayList<>();
    }
}
