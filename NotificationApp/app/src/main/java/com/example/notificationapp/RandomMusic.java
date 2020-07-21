package com.example.notificationapp;

import java.util.Random;

public class RandomMusic {
    private Random random;
    public static int lengthRandom = 0, nextPlayMusic = 0, beforeMusic = 0;
    static int[] arrayMusic;
    RandomMusic(int length) {
        arrayMusic = new int[length];
        for (int i = 0; i < length; i++) {
            arrayMusic[i] = i;
        }
        lengthRandom = length;
    }
    public int init(int length) {
        int index;
        random = new Random();
        if(lengthRandom > 1) {
            index = random.nextInt(lengthRandom);
            if (lengthRandom == length && beforeMusic == arrayMusic[index]
            ) {
                index = random.nextInt(lengthRandom - 1) + 1;
            }

            nextPlayMusic = arrayMusic[index];
            arrayMusic[index] = arrayMusic[lengthRandom - 1];
            arrayMusic[lengthRandom - 1] = nextPlayMusic;
            lengthRandom--;
        } else {
            lengthRandom = length;
            beforeMusic = arrayMusic[0];
            return arrayMusic[0];
        }
        return nextPlayMusic;
    }
}
