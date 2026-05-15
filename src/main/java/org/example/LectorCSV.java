package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// Esta clase se encarga de todo el trabajo sucio de leer el archivo
public class LectorCSV {

    // Método que lee el archivo y nos regresa la lista completa
    public List<Videojuego> cargarJuegos(String rutaArchivo) {

        List<Videojuego> listaJuegos = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;
            // Nos saltamos la primera línea de los encabezados
            lector.readLine();

            while ((linea = lector.readLine()) != null) {

                // Esta "magia" separa por comas, EXCEPTO si la coma está adentro de unas comillas
                // Así evitamos que fechas como "Feb 25, 2022" nos rompan las columnas
                String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Checamos que la línea tenga las columnas suficientes
                if (datos.length >= 7) {
                    try {
                        // Limpiamos las comillas extra y espacios de los textos
                        String titulo = datos[1].replace("\"", "").trim();
                        String fecha = datos[2].replace("\"", "").trim();

                        // Si la calificación está vacía le ponemos 0, si no, la convertimos
                        double calificacion = datos[4].isEmpty() ? 0.0 : Double.parseDouble(datos[4].replace("\"", ""));

                        // La columna 6 tiene las reseñas. Las limpiamos de comillas y usamos nuestro método especial
                        String reseñasTexto = datos[6].replace("\"", "").trim();
                        int reviews = procesarNumeroConK(reseñasTexto);

                        // Armamos el juego y lo metemos a la lista
                        Videojuego juego = new Videojuego(titulo, fecha, calificacion, reviews);
                        listaJuegos.add(juego);

                    } catch (Exception e) {
                        // Si un juego viene con un formato súper raro que no se puede leer,
                        // simplemente lo ignoramos y pasamos al siguiente para no detener el programa
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Hubo una bronca al leer el archivo: " + e.getMessage());
        }

        return listaJuegos;
    }

    // Método auxiliar para traducir textos como "3.9K" a números reales como 3900
    private int procesarNumeroConK(String texto) {
        // Si no hay texto o dice "null", devolvemos 0
        if (texto == null || texto.isEmpty() || texto.equalsIgnoreCase("null")) {
            return 0;
        }

        // Pasamos todo a mayúsculas por si acaso
        texto = texto.toUpperCase();

        if (texto.contains("K")) {
            // Le quitamos la K
            String numeroSinK = texto.replace("K", "").trim();
            // Lo convertimos a decimal y lo multiplicamos por 1000
            return (int) (Double.parseDouble(numeroSinK) * 1000);
        } else {
            // Si no tiene K (ej. "850"), lo leemos directo
            return (int) Double.parseDouble(texto);
        }
    }
}