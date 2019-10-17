package com.newventuresoftware.waveformdemo.model;

import com.newventuresoftware.waveformdemo.repository.PulseRepository;

import java.util.ArrayList;
import java.util.List;

public class HeartRate {

    private static final int DELTA_MIN_VALUE_PULSE = 220;

    private short[] pulses;
    private Integer audioLength;

    public HeartRate(short[] pulses, int audioLength, int samplesSize) {

        PulseRepository pulseRepository = new PulseRepository(pulses, samplesSize);
        this.pulses = pulseRepository.getPulsesWithLowPassFilter(DELTA_MIN_VALUE_PULSE);
        this.audioLength = audioLength;
    }

    private int averagePulse() {
        int sumatoria = 0;

        for (int pulse : this.pulses) {
            sumatoria += pulse;
        }

        return sumatoria / this.pulses.length;
    }

    private int getCantMaxPulses() {
        int cantMaxPulses = 0;
        int average = this.averagePulse();
        int desviacionStandard = this.getStartDeviation() / 2;

        for (int pulse : this.pulses) {
            if (pulse >= average - desviacionStandard) {
                cantMaxPulses++;
            }
        }

        return cantMaxPulses;
    }

    public int calculateHeartRate() {
        this.pulses = getMaxPulses();
        int heartRateTimeInSeconds = this.audioLength / 1000;
        float heartRate = (float) this.getCantMaxPulses() / heartRateTimeInSeconds;

        if (heartRateTimeInSeconds < 60) {
            heartRate = (heartRate / this.audioLength) * 60000;
        } else {
            heartRate = heartRate * 60;
        }

        return (int) (heartRate);
    }

    private short[] getMaxPulses() {
        int average = this.averagePulse();
        int desviacionStandard = this.getStartDeviation() / 2;
        List<Short> maximos = new ArrayList<>();
        short[] arrayMaximos;

        for (short pulse : this.pulses) {
            if (pulse >= (average - desviacionStandard) && pulse <= (average + desviacionStandard)) {
                maximos.add(pulse);
            }
        }

        arrayMaximos = new short[maximos.size()];
        for (int i = 0; i < maximos.size(); i++) {
            arrayMaximos[i] = maximos.get(i);
        }

        return arrayMaximos;
    }

    private int getStartDeviation() {
        double average;
        double sumX = 0;
        double finalsumX = 0;
        Double AverageX;
        Double SquareRoot;
        double[] x1_average = new double[2000];

        average = this.averagePulse();
        System.out.println("Average: " + average);
        for (int i = 0; i < this.pulses.length; i++) {
            double fvalue = (Math.pow((this.pulses[i] - average), 2));
            x1_average[i] = fvalue;
            System.out.println("test: " + fvalue);
        }
        for (double i : x1_average) {
            finalsumX = (sumX += i);
        }
        AverageX = finalsumX / (this.pulses.length);
        SquareRoot = Math.sqrt(AverageX);

        return SquareRoot.intValue();
    }

}
