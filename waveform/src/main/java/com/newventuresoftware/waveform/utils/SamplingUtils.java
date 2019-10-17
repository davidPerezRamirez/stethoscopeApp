package com.newventuresoftware.waveform.utils;

import java.util.Arrays;

public class SamplingUtils {

    private short[] data;
    private int sampleSize;

    public SamplingUtils(short[] data, int sampleSize) {
        this.data = data;
        this.sampleSize = sampleSize;
    }

    public short[][] getExtremes() {
        short[][] newData = new short[sampleSize][];
        int groupSize = data.length / sampleSize;

        for (int i = 0; i < sampleSize; i++) {
            short[] group = Arrays.copyOfRange(data, i * groupSize,
                    Math.min((i + 1) * groupSize, data.length));

            // Fin min & max values
            short min = Short.MAX_VALUE, max = Short.MIN_VALUE;
            for (short a : group) {
                min = (short) Math.min(min, a);
                max = (short) Math.max(max, a);
            }
            newData[i] = new short[] { max, min };
        }

        return newData;
    }

    public short[] getMaxExtremes() {
        short[][] extremes = this.getExtremes();
        short[] maxExtremes = new short[sampleSize];

        for (int x = 0; x < sampleSize; x++) {
            short sample = extremes[x][0];

            maxExtremes[x] = sample;
        }

        return  maxExtremes;
    }

    public short[] getMinExtremes() {
        short[][] extremes = this.getExtremes();
        short[] minExtremes = new short[sampleSize];

        for (int x = sampleSize - 1; x >= 0; x--) {
            short sample = extremes[x][1];

            minExtremes[x] = sample;
        }

        return  minExtremes;
    }
}
