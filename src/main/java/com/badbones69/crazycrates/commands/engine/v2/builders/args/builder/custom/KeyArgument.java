package com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom;

import com.badbones69.crazycrates.commands.engine.v2.builders.args.ArgumentType;
import com.badbones69.crazycrates.api.enums.KeyType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        return Arrays.stream(KeyType.values()).map(KeyType::getName).collect(Collectors.toList());
    }
}