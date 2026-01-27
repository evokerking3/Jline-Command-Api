package dev.evokerking.impl;

import com.google.gson.Gson;

import dev.evokerking.command.Command;

public class FromJsonCommand implements Command {

    private final Gson gson;

    public FromJsonCommand(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String execute(String args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsage'");
    }


}
