package com.java.voteup.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupGenerator {
    private static final int GROUP_SIZE = 9;

    public static List<Integer> generateGroup() {
        List<Integer> group = new ArrayList<>();

        for (int i = 1; i <= GROUP_SIZE; i++) {
            group.add(i);
        }

        return group;
    }

    public static int getRandomValueFromGroup() {
        List<Integer> group = generateGroup();
        Random random = new Random();
        int randomIndex = random.nextInt(group.size());
        return group.get(randomIndex);
    }
}
