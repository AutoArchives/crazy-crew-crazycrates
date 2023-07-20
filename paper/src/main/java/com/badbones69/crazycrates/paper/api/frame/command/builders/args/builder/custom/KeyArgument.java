package com.badbones69.crazycrates.paper.api.frame.command.builders.args.builder.custom;

import com.badbones69.crazycrates.core.enums.KeyType;
import com.badbones69.crazycrates.paper.api.frame.command.builders.args.ArgumentType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        return Arrays.stream(KeyType.values()).map(KeyType::getName).collect(Collectors.toList());
    }
}