package org.example;

// Esta clase nos sirve como molde para guardar la info de cada juego
// Solo pondremos los atributos que realmente nos interesan del CSV
public class Videojuego {

    // Variables para guardar los datos de cada columna
    private String titulo;
    private String fechaLanzamiento;
    private double calificacion;
    private int numeroDeReviews;

    // Constructor para armar el objeto rápido cuando leamos el archivo
    public Videojuego(String titulo, String fechaLanzamiento, double calificacion, int numeroDeReviews) {
        this.titulo = titulo;
        this.fechaLanzamiento = fechaLanzamiento;
        this.calificacion = calificacion;
        this.numeroDeReviews = numeroDeReviews;
    }

    // Agregamos los getters para que la tabla de JavaFX pueda leer los datos
    public String getTitulo() {
        return titulo;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public int getNumeroDeReviews() {
        return numeroDeReviews;
    }

    // Por ahora no ocupamos setters porque no vamos a editar la info, solo a ordenarla
}