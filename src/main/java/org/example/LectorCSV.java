package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// Esta clase se encarga de todo el trabajo sucio de leer el archivo
public class LectorCSV {

    // Método que lee el archivo y nos regresa una lista lista para usar
    public List<Videojuego> cargarJuegos(String rutaArchivo) {

        // Aquí vamos a ir guardando los juegos conforme los leamos
        List<Videojuego> listaJuegos = new ArrayList<>();

        // Usamos try-with-resources para que el archivo se cierre solo al terminar, es más limpio
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;
            // Nos saltamos la primera línea porque son los encabezados de las columnas
            lector.readLine();

            // Leemos línea por línea hasta que se acabe el archivo
            while ((linea = lector.readLine()) != null) {

                // Separamos los datos por coma
                // Ojo: si un título tiene comas adentro, esto puede fallar, pero sirve para empezar
                String[] datos = linea.split(",");

                // Checamos que la línea tenga al menos los datos básicos para no quebrar el programa
                if (datos.length >= 7) {
                    try {
                        // Limpiamos un poco los datos y los convertimos a lo que ocupamos
                        String titulo = datos[1].replace("\"", "");
                        String fecha = datos[2].replace("\"", "");

                        // Parseamos los números, si están vacíos ponemos un cero para evitar errores
                        double calificacion = datos[4].isEmpty() ? 0.0 : Double.parseDouble(datos[4]);

                        // Omitimos la columna 5 ("Times Listed") por ahora, pasamos a las reviews en la 6
                        // Igual, si hay letras raras o está vacío, lo atrapamos abajo
                        int reviews = 0;

                        // Armamos el juego y lo metemos a la lista
                        Videojuego juego = new Videojuego(titulo, fecha, calificacion, reviews);
                        listaJuegos.add(juego);

                    } catch (NumberFormatException e) {
                        // Si un número viene mal formateado, simplemente ignoramos ese juego y seguimos
                        // No queremos que un dato feo nos detenga todo el proceso
                    }
                }
            }
        } catch (Exception e) {
            // Si no encuentra el archivo o pasa algo raro, avisamos en consola
            System.out.println("Hubo un bronca al leer el archivo: " + e.getMessage());
        }

        return listaJuegos;
    }
}