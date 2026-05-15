package org.example;

// Esta es la clase principal donde arranca nuestro programa
public class Main {
    public static void main(String[] args) {

        // Creamos una instancia de nuestro lector
        LectorCSV lector = new LectorCSV();

        // Le pedimos que cargue los datos del archivo que pusiste en la raíz
        // Importamos la lista (asegúrate de que IntelliJ importe java.util.List)
        java.util.List<Videojuego> lista = lector.cargarJuegos("games.csv");

        // Checamos rápido si jaló o no
        if (lista.isEmpty()) {
            System.out.println("No se encontraron juegos. Revisa que el archivo se llame 'games.csv' y esté en el lugar correcto.");
        } else {
            System.out.println("¡Todo un éxito! Se cargaron " + lista.size() + " juegos.");
            System.out.println("Aquí te van los primeros 5 para comprobar:");

            // Un ciclo rapidito para imprimir los primeros 5 juegos y ver que la info esté bien
            for (int i = 0; i < 5; i++) {
                Videojuego juego = lista.get(i);
                System.out.println("- " + juego.getTitulo() + " | Calificación: " + juego.getCalificacion());
            }
        }
    }
}