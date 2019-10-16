package com.newventuresoftware.waveform.utils;

import java.util.ArrayList;
import java.util.List;

public class HeartRate {

    private List<Integer> pulses;
    private Integer audioLength;

    public HeartRate(int audioLength) {
        this.pulses = new ArrayList<>();
        this.audioLength = audioLength;
    }

    public void savePulse(short pulse) {

        int intPulse = Math.abs(pulse);

        this.pulses.add(intPulse);
    }

    private int averagePulse() {

        int sumatoria = 0;

        for (int pulse : this.pulses) {
            sumatoria += pulse;
        }

        return sumatoria / this.pulses.size();
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

    private List<Integer> getMaxPulses() {
        int average = this.averagePulse();
        int desviacionStandard = this.getStartDeviation() / 2;
        List<Integer> maximos = new ArrayList<>();

        for (int pulse : this.pulses) {
            if (pulse >= (average - desviacionStandard) && pulse <= (average + desviacionStandard)) {
                maximos.add(pulse);
            }
        }

        return maximos;
    }

    public int getStartDeviation() {
        double sum = 0;
        double finalsum = 0;
        double average = 0;

        for (double i : this.pulses) {
            finalsum = (sum += i);
        }

        average = finalsum / (this.pulses.size());
        System.out.println("Average: " + average);

        double sumX = 0;
        double finalsumX = 0;
        double[] x1_average = new double[2000];
        for (int i = 0; i < this.pulses.size(); i++) {
            double fvalue = (Math.pow((this.pulses.get(i) - average), 2));
            x1_average[i] = fvalue;
            System.out.println("test: " + fvalue);
        }

        for (double i : x1_average) {
            finalsumX = (sumX += i);
        }

        Double AverageX = finalsumX / (this.pulses.size());

        Double SquareRoot = Math.sqrt(AverageX);

        return SquareRoot.intValue();

    }

}
