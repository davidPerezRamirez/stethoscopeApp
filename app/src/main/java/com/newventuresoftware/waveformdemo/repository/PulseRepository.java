package com.newventuresoftware.waveformdemo.repository;

import com.newventuresoftware.waveform.utils.SamplingUtils;

import java.util.ArrayList;
import java.util.List;

public class PulseRepository {

    private SamplingUtils samplingUtils;

    public PulseRepository(short[] pulses, int samples) {
        this.samplingUtils = new SamplingUtils(pulses, samples);
    }

    public short[] getPulsesWithLowPassFilter(int dektaValueFilter) {
        short[] extremes = this.samplingUtils.getMaxExtremes();
        List<Short> pulses = new ArrayList<>();
        short[] pulsesWithFilter;

        for (short pulse : extremes) {
            if (pulse > dektaValueFilter) {
                pulses.add(pulse);
            }
        }
        pulsesWithFilter = new short[pulses.size()];
        for (int i = 0; i < pulses.size(); i++) {
            pulsesWithFilter[i] = pulses.get(i);
        }

        return pulsesWithFilter;
    }
}
