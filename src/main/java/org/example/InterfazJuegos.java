package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

// Ventana principal con la gráfica midiendo en nanosegundos
public class InterfazJuegos extends Application {

    // Variables globales para la ventana
    private TableView<Videojuego> tabla;
    private List<Videojuego> listaJuegosOriginal;
    private BarChart<String, Number> grafica;

    @Override
    public void start(Stage ventanaPrincipal) {
        ventanaPrincipal.setTitle("Visualizador y Competencia de Algoritmos");

        // Armamos la tabla con sus columnas
        tabla = new TableView<>();

        TableColumn<Videojuego, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Videojuego, String> colFecha = new TableColumn<>("Lanzamiento");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        TableColumn<Videojuego, Double> colCalificacion = new TableColumn<>("Calificación");
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        TableColumn<Videojuego, Integer> colReviews = new TableColumn<>("Reseñas");
        colReviews.setCellValueFactory(new PropertyValueFactory<>("numeroDeReviews"));

        tabla.getColumns().add(colTitulo);
        tabla.getColumns().add(colFecha);
        tabla.getColumns().add(colCalificacion);
        tabla.getColumns().add(colReviews);

        // Cargamos los datos con la ruta de tu compu
        LectorCSV lector = new LectorCSV();
        listaJuegosOriginal = lector.cargarJuegos("C:\\Users\\TAPIAPC\\IdeaProjects\\Prac5Videojuegos\\src\\main\\java\\org\\example\\games.csv");

        ObservableList<Videojuego> datosParaTabla = FXCollections.observableArrayList(listaJuegosOriginal);
        tabla.setItems(datosParaTabla);

        // Configuramos la gráfica
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Método de Ordenamiento");

        // Cambiamos la etiqueta para que indique Nanosegundos
        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo (Nanosegundos)");

        grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Rendimiento de los Algoritmos");
        grafica.setAnimated(false);

        // Botón para arrancar
        Button btnCompetir = new Button("¡Iniciar Carrera de Algoritmos (Por Reseñas)!");
        btnCompetir.setOnAction(evento -> ejecutarComparacion());

        // Acomodamos todo en la pantalla
        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.getChildren().addAll(btnCompetir, tabla, grafica);

        Scene escena = new Scene(contenedorPrincipal, 900, 700);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Método que hace las pruebas de tiempo
    private void ejecutarComparacion() {
        // Limpiamos resultados anteriores
        grafica.getData().clear();

        XYChart.Series<String, Number> serieTiempos = new XYChart.Series<>();
        serieTiempos.setName("Tiempo de ejecución");

        AlgoritmosOrdenamiento algoritmos = new AlgoritmosOrdenamiento();
        Comparator<Videojuego> compResenas = Comparator.comparingInt(Videojuego::getNumeroDeReviews);

        int totalDatos = listaJuegosOriginal.size();

        // --- 1. ARRAYS.SORT NATIVO ---
        Videojuego[] arregloSort = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSort = System.nanoTime();
        algoritmos.usarSortNativo(arregloSort, compResenas);
        long finSort = System.nanoTime();
        // Guardamos el tiempo directo sin conversiones
        long tiempoSort = finSort - inicioSort;
        serieTiempos.getData().add(new XYChart.Data<>("Arrays.sort", tiempoSort));

        // --- 2. PARALLEL SORT ---
        Videojuego[] arregloParallel = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioParallel = System.nanoTime();
        algoritmos.usarParallelSort(arregloParallel, compResenas);
        long finParallel = System.nanoTime();
        long tiempoParallel = finParallel - inicioParallel;
        serieTiempos.getData().add(new XYChart.Data<>("Parallel Sort", tiempoParallel));

        // --- 3. QUICKSORT ---
        Videojuego[] arregloQuick = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioQuick = System.nanoTime();
        algoritmos.quicksort(arregloQuick, 0, arregloQuick.length - 1, compResenas);
        long finQuick = System.nanoTime();
        long tiempoQuick = finQuick - inicioQuick;
        serieTiempos.getData().add(new XYChart.Data<>("Quicksort", tiempoQuick));

        // --- 4. MERGESORT ---
        Videojuego[] arregloMerge = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioMerge = System.nanoTime();
        algoritmos.mergesort(arregloMerge, 0, arregloMerge.length - 1, compResenas);
        long finMerge = System.nanoTime();
        long tiempoMerge = finMerge - inicioMerge;
        serieTiempos.getData().add(new XYChart.Data<>("Mergesort", tiempoMerge));

        // --- 5. SHELL SORT ---
        Videojuego[] arregloShell = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioShell = System.nanoTime();
        algoritmos.shellSort(arregloShell, compResenas);
        long finShell = System.nanoTime();
        long tiempoShell = finShell - inicioShell;
        serieTiempos.getData().add(new XYChart.Data<>("Shell Sort", tiempoShell));

        // --- 6. RADIX SORT ---
        Videojuego[] arregloRadix = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioRadix = System.nanoTime();
        algoritmos.radixSort(arregloRadix);
        long finRadix = System.nanoTime();
        long tiempoRadix = finRadix - inicioRadix;
        serieTiempos.getData().add(new XYChart.Data<>("Radix Sort", tiempoRadix));

        // --- 7. SELECCIÓN DIRECTA ---
        Videojuego[] arregloSeleccion = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSeleccion = System.nanoTime();
        algoritmos.seleccionDirecta(arregloSeleccion, compResenas);
        long finSeleccion = System.nanoTime();
        long tiempoSeleccion = finSeleccion - inicioSeleccion;
        serieTiempos.getData().add(new XYChart.Data<>("Selección", tiempoSeleccion));

        // Dibujamos la gráfica con los nanosegundos
        grafica.getData().add(serieTiempos);

        // Actualizamos la tabla
        ObservableList<Videojuego> datosOrdenados = FXCollections.observableArrayList(arregloQuick);
        tabla.setItems(datosOrdenados);
    }

    public static void main(String[] args) {
        launch(args);
    }
}