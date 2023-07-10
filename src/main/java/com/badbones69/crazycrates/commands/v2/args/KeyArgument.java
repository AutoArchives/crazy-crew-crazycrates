package com.badbones69.crazycrates.commands.v2.args;

import com.badbones69.crazycrates.api.enums.KeyType;
import com.ryderbelserion.stick.paper.commands.sender.args.ArgumentType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        return Arrays.stream(KeyType.values()).map(KeyType::getName).collect(Collectors.toList());
    }
}