package com.badbones69.crazycrates.commands.engine.v2.builders.args.builder;

import com.badbones69.crazycrates.commands.engine.v2.builders.args.ArgumentType;

import java.util.List;

public class BooleanArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        return List.of("true", "false");
    }
}