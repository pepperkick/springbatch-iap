package com.pepperkick.iap.batch.processor;

import com.pepperkick.iap.batch.util.Calculator;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LineProcessor implements ItemProcessor<String, JSONObject> {
    @Override
    public JSONObject process(String line) throws Exception {
        String[] ints = line.split(",");
        List<Double> nums = new ArrayList<>();
        JSONObject json = new JSONObject();

        for (String n : ints) {
            nums.add(Double.parseDouble(n));
        }

        double sum = Calculator.calculateSum(nums);
        double avg = Calculator.calculateAverage(sum, nums.size());
        double v = Calculator.calculateVariance(avg, nums);
        double sd = Calculator.calculateStandardDeviation(v);
        DecimalFormat df = new DecimalFormat("#.##");

        json.put("sum", df.format(sum));
        json.put("average", df.format(avg));
        json.put("std_deviation", df.format(sd));

        return json;
    }
}
