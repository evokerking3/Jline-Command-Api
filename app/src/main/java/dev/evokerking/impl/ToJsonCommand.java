package dev.evokerking.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import dev.evokerking.command.Command;
import dev.evokerking.command.Parameter;
import dev.evokerking.command.ParameterType;

/**
 * Command to convert text to JSON format.
 */
public class ToJsonCommand implements Command {
    private final Gson gson;

    public ToJsonCommand(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String execute(String args) {
        String input = args.trim();
        if (input.isEmpty()) {
            input = "Convert this to JSON";
        }

        Map<String, String> data = new LinkedHashMap<>();
        data.put("message", input);
        return gson.toJson(data);
    }

    @Override
    public String getName() {
        return "toJson";
    }

    @Override
    public String getDescription() {
        return "Converts text to JSON format";
    }

    @Override
    public String getUsage() {
        return "toJson [text] - Converts the provided text to a JSON object with a 'message' field";
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter.Builder("text")
            .type(ParameterType.STRING)
            .description("Text to convert to JSON")
            .required(false)
            .build());
        return params;
    }
}
