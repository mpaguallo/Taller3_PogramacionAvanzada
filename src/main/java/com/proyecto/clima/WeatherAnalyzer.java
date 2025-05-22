package com.proyecto.clima;

import java.util.*;
import java.util.function.*;

public class WeatherAnalyzer {

    public static class WeatherStats {
        double sum = 0;
        int count = 0;
        WeatherData min = null;
        WeatherData max = null;

        void update(double value, WeatherData data, ToDoubleFunction<WeatherData> extractor) {
            sum += value;
            count++;
            if (min == null || extractor.applyAsDouble(min) > value) min = data;
            if (max == null || extractor.applyAsDouble(max) < value) max = data;
        }

        double average() {
            return count == 0 ? 0 : sum / count;
        }
    }

    public static Map<String, WeatherStats> analyze(List<WeatherData> data) {
        WeatherStats tempStats = new WeatherStats();
        WeatherStats humStats = new WeatherStats();
        WeatherStats windStats = new WeatherStats();
        WeatherStats visStats = new WeatherStats();
        WeatherStats presStats = new WeatherStats();

        for (WeatherData d : data) {
            tempStats.update(d.temperature, d, x -> x.temperature);
            humStats.update(d.humidity, d, x -> x.humidity);
            windStats.update(d.windSpeed, d, x -> x.windSpeed);
            visStats.update(d.visibility, d, x -> x.visibility);
            presStats.update(d.pressure, d, x -> x.pressure);
        }

        Map<String, WeatherStats> statsMap = new HashMap<>();
        statsMap.put("Temperature", tempStats);
        statsMap.put("Humidity", humStats);
        statsMap.put("Wind", windStats);
        statsMap.put("Visibility", visStats);
        statsMap.put("Pressure", presStats);
        return statsMap;
    }
}