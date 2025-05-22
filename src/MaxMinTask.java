import java.util.*;
import java.util.concurrent.Callable;

public class MaxMinTask implements Callable<Map<String, String>> {
    private final List<String> data;

    public MaxMinTask(List<String> data) {
        this.data = data;
    }

    @Override
    public Map<String, String> call() {
        Map<String, String> resultado = new LinkedHashMap<>();

        Map<String, Double> max = new HashMap<>();
        Map<String, Double> min = new HashMap<>();
        Map<String, String> maxFecha = new HashMap<>();
        Map<String, String> minFecha = new HashMap<>();

        String[] labels = {"TempMax", "TempMin", "HumMax", "HumMin", "VientoMax", "VientoMin", "VisMax", "VisMin"};
        int[] columnas = {3, 3, 4, 4, 5, 5, 6, 6};

        for (int i = 0; i < labels.length; i++) {
            max.put(labels[i], Double.NEGATIVE_INFINITY);
            min.put(labels[i], Double.POSITIVE_INFINITY);
        }

        for (String linea : data) {
            String[] columnasStr = linea.split(",(?=(?:[^"]*"[^"]*")*[^"]*$)");
            if (columnasStr.length < 8) continue;
            String fecha = columnasStr[0];

            try {
                double[] valores = {
                    Double.parseDouble(columnasStr[3]), // Temp
                    Double.parseDouble(columnasStr[3]),
                    Double.parseDouble(columnasStr[4]), // Hum
                    Double.parseDouble(columnasStr[4]),
                    Double.parseDouble(columnasStr[5]), // Viento
                    Double.parseDouble(columnasStr[5]),
                    Double.parseDouble(columnasStr[6]), // Vis
                    Double.parseDouble(columnasStr[6])
                };

                for (int i = 0; i < labels.length; i++) {
                    if (valores[i] > max.get(labels[i])) {
                        max.put(labels[i], valores[i]);
                        maxFecha.put(labels[i], fecha);
                    }
                    if (valores[i] < min.get(labels[i])) {
                        min.put(labels[i], valores[i]);
                        minFecha.put(labels[i], fecha);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignorar
            }
        }

        resultado.put("Temperatura Máxima", maxFecha.get("TempMax"));
        resultado.put("Temperatura Mínima", minFecha.get("TempMin"));
        resultado.put("Humedad Máxima", maxFecha.get("HumMax"));
        resultado.put("Humedad Mínima", minFecha.get("HumMin"));
        resultado.put("Viento Máximo", maxFecha.get("VientoMax"));
        resultado.put("Viento Mínimo", minFecha.get("VientoMin"));
        resultado.put("Visibilidad Máxima", maxFecha.get("VisMax"));
        resultado.put("Visibilidad Mínima", minFecha.get("VisMin"));

        return resultado;
    }
}