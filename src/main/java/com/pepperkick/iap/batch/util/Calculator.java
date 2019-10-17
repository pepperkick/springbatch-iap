package com.pepperkick.iap.batch.util;

import java.util.List;

public class Calculator {
    private Calculator() {}

    public static double calculateSum(List<Double> nums) {
        int sum = 0;
        for (double n : nums) {
            sum += n;
        }

        return sum;
    }

    public static double calculateAverage(double sum, double size) {
        return sum / size;
    }

    public static double calculateAverage(List<Double> nums) {
        return calculateSum(nums) / nums.size();
    }

    public static double calculateVariance(double avg, List<Double> nums) {
        double v = 0;

        for (double i : nums) {
            v += Math.pow(i - avg, 2);
        }

        return v / nums.size();
    }

    public static double calculateVariance(List<Double> nums) {
        return calculateVariance(calculateAverage(nums), nums);
    }

    public static double calculateStandardDeviation(double v) {
        return Math.sqrt(v);
    }

    public static double calculateStandardDeviation(List<Double> nums) {
        return Math.sqrt(calculateVariance(nums));
    }
}
