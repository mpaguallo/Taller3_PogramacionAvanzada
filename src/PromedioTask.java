import java.util.List;
import java.util.concurrent.Callable;

public class PromedioTask implements Callable<Double> {
    private final List<String> data;
    private final int columna;

    public PromedioTask(List<String> data, int columna) {
        this.data = data;
        this.columna = columna;
    }

    @Override
    public Double call() {
        double suma = 0;
        int contador = 0;

        for (String linea : data) {
            String[] columnas = linea.split(",(?=(?:[^"]*"[^"]*")*[^"]*$)");
            try {
                suma += Double.parseDouble(columnas[columna]);
                contador++;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Ignorar filas mal formateadas
            }
        }

        return contador == 0 ? 0 : suma / contador;
    }
}