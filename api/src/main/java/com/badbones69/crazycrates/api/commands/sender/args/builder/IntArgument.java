package com.badbones69.crazycrates.api.commands.sender.args.builder;

import com.badbones69.crazycrates.api.commands.sender.args.ArgumentType;

import java.util.ArrayList;
import java.util.List;

public class IntArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        List<String> numbers = new ArrayList<>();

        for (int value = 1; value <= 100; value += 1) numbers.add(String.valueOf(value));

        return numbers;
    }
}