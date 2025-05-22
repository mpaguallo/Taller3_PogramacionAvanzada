package com.proyecto.clima;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        List<WeatherData> data = loadWeatherData("weatherHistory.csv");

        ExecutorService pool = Executors.newFixedThreadPool(5);

        Callable<Map<String, WeatherAnalyzer.WeatherStats>> task = () -> WeatherAnalyzer.analyze(data);

        Future<Map<String, WeatherAnalyzer.WeatherStats>> future = pool.submit(task);
        Map<String, WeatherAnalyzer.WeatherStats> result = future.get();

        pool.shutdown();

        for (String key : result.keySet()) {
            var stats = result.get(key);
            System.out.printf("\n--- %s ---%n", key);
            System.out.printf("Promedio anual: %.2f%n", stats.average());
            System.out.printf("Mínimo: %.2f en %s%n", getValue(stats.min, key), stats.min.datetime);
            System.out.printf("Máximo: %.2f en %s%n", getValue(stats.max, key), stats.max.datetime);
        }
    }

    static double getValue(WeatherData data, String key) {
        return switch (key) {
            case "Temperature" -> data.temperature;
            case "Humidity" -> data.humidity;
            case "Wind" -> data.windSpeed;
            case "Visibility" -> data.visibility;
            case "Pressure" -> data.pressure;
            default -> 0;
        };
    }

    static List<WeatherData> loadWeatherData(String path) throws IOException {
        List<WeatherData> data = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (int i = 1; i < lines.size(); i++) {
            String[] col = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (col.length < 11) continue; // Evitar error si la línea tiene columnas incompletas
            try {
                String date = col[0];
                double temp = Double.parseDouble(col[3]);
                double humidity = Double.parseDouble(col[5]);
                double wind = Double.parseDouble(col[6]);
                double vis = Double.parseDouble(col[8]);
                double pres = Double.parseDouble(col[10]);
                data.add(new WeatherData(date, temp, humidity, wind, vis, pres));
            } catch (Exception ignored) {}
        }
        return data;
    }
}
