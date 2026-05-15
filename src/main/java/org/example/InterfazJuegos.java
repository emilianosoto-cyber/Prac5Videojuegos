package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

// Esta es nuestra ventana principal
public class InterfazJuegos extends Application {

    // El método start es el punto de arranque de las interfaces en JavaFX
    @Override
    public void start(Stage ventanaPrincipal) {

        // Le ponemos un título chido a nuestra ventana
        ventanaPrincipal.setTitle("Visualizador de Videojuegos y Ordenamientos");

        // Creamos la tabla donde vamos a mostrar toda la información
        TableView<Videojuego> tabla = new TableView<>();

        // Creamos la primera columna y le decimos qué texto mostrar en el encabezado
        TableColumn<Videojuego, String> colTitulo = new TableColumn<>("Título del Juego");
        // Le indicamos que busque el atributo "titulo" exacto en nuestra clase Videojuego
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        // Hacemos lo mismo para las demás columnas
        TableColumn<Videojuego, String> colFecha = new TableColumn<>("Lanzamiento");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        TableColumn<Videojuego, Double> colCalificacion = new TableColumn<>("Calificación");
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        TableColumn<Videojuego, Integer> colReviews = new TableColumn<>("Reseñas");
        colReviews.setCellValueFactory(new PropertyValueFactory<>("numeroDeReviews"));

        // Agregamos todas las columnas a nuestra tabla para que sean visibles
        tabla.getColumns().add(colTitulo);
        tabla.getColumns().add(colFecha);
        tabla.getColumns().add(colCalificacion);
        tabla.getColumns().add(colReviews);

        // Traemos los datos usando el lector que hicimos en la fase anterior
        LectorCSV lector = new LectorCSV();
        // Ojo aquí: usa la misma ruta absoluta que te funcionó en la prueba de la Fase 1
        List<Videojuego> listaJuegos = lector.cargarJuegos("C:\\Users\\TAPIAPC\\IdeaProjects\\Prac5Videojuegos\\src\\main\\java\\org\\example\\games.csv");

        // JavaFX ocupa un tipo de lista especial para poder mostrar y actualizar la pantalla
        ObservableList<Videojuego> datosParaTabla = FXCollections.observableArrayList(listaJuegos);

        // Le inyectamos los datos a la tabla
        tabla.setItems(datosParaTabla);

        // Metemos la tabla en un contenedor vertical para que se expanda bien en la ventana
        VBox contenedor = new VBox(tabla);

        // Creamos la escena (el contenido visual) y le damos un tamaño inicial razonable
        Scene escena = new Scene(contenedor, 800, 600);

        // Ponemos la escena en la ventana y la mostramos en pantalla
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Este es el main que arranca la aplicación gráfica
    public static void main(String[] args) {
        launch(args);
    }
}