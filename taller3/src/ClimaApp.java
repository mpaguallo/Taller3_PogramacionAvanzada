import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ClimaApp {
    public static void main(String[] args) throws Exception {
        List<String> lineas = Files.readAllLines(Paths.get("weatherClean.csv"));
        lineas.remove(0); // eliminar cabecera

        ExecutorService executor = Executors.newFixedThreadPool(6);

        Future<Double> promedioTemp = executor.submit(new PromedioTask(lineas, 3));
        Future<Double> promedioHum = executor.submit(new PromedioTask(lineas, 4));
        Future<Double> promedioViento = executor.submit(new PromedioTask(lineas, 5));
        Future<Double> promedioVis = executor.submit(new PromedioTask(lineas, 6));
        Future<Double> promedioPres = executor.submit(new PromedioTask(lineas, 7));
        Future<Map<String, String>> extremos = executor.submit(new MaxMinTask(lineas));

        System.out.printf("Promedio Anual Temperatura: %.2f °C%n", promedioTemp.get());
        System.out.printf("Promedio Anual Humedad: %.2f%n", promedioHum.get());
        System.out.printf("Promedio Anual Velocidad Viento: %.2f km/h%n", promedioViento.get());
        System.out.printf("Promedio Anual Visibilidad: %.2f km%n", promedioVis.get());
        System.out.printf("Promedio Anual Presión: %.2f mb%n%n", promedioPres.get());

        Map<String, String> extremosMap = extremos.get();
        extremosMap.forEach((k, v) -> System.out.println(k + ": " + v));

        executor.shutdown();
    }
}