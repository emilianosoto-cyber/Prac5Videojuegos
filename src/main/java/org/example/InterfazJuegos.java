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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

// Ventana principal con diseño de panel de control (dashboard)
public class InterfazJuegos extends Application {

    private TableView<Videojuego> tabla;
    private List<Videojuego> listaJuegosOriginal;
    private BarChart<String, Number> grafica;
    private TextArea consolaResultados;

    @Override
    public void start(Stage ventanaPrincipal) {
        ventanaPrincipal.setTitle("Visualizador y Competencia de Algoritmos");

        // 1. Armamos la tabla
        tabla = new TableView<>();

        TableColumn<Videojuego, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Videojuego, String> colFecha = new TableColumn<>("Lanzamiento");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        TableColumn<Videojuego, Double> colCalificacion = new TableColumn<>("Calificación");
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        TableColumn<Videojuego, Integer> colReviews = new TableColumn<>("Reseñas");
        colReviews.setCellValueFactory(new PropertyValueFactory<>("numeroDeReviews"));

        tabla.getColumns().addAll(colTitulo, colFecha, colCalificacion, colReviews);

        // Cargamos los datos originales (verifica tu ruta)
        LectorCSV lector = new LectorCSV();
        listaJuegosOriginal = lector.cargarJuegos("C:\\Users\\TAPIAPC\\IdeaProjects\\Prac5Videojuegos\\src\\main\\java\\org\\example\\games.csv");

        ObservableList<Videojuego> datosParaTabla = FXCollections.observableArrayList(listaJuegosOriginal);
        tabla.setItems(datosParaTabla);

        // 2. Configuramos la gráfica
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Método de Ordenamiento");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo (Nanosegundos)");

        grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Rendimiento de los Algoritmos");
        grafica.setAnimated(false);

        // 3. Configuramos la consola de resultados
        consolaResultados = new TextArea();
        consolaResultados.setEditable(false);
        // Le damos un ancho fijo para que no se coma a la gráfica
        consolaResultados.setPrefWidth(350);
        consolaResultados.setText("Esperando para iniciar la competencia...\n");

        // Botón para arrancar
        Button btnCompetir = new Button("¡Iniciar Carrera de Algoritmos (Por Reseñas)!");
        btnCompetir.setOnAction(evento -> ejecutarComparacion());

        // 4. Agrupamos gráfica y consola lado a lado
        HBox zonaResultados = new HBox(15); // El 15 es el espacio entre ambos

        // Le decimos a JavaFX que la gráfica debe estirarse para ocupar el espacio sobrante
        HBox.setHgrow(grafica, Priority.ALWAYS);
        zonaResultados.getChildren().addAll(grafica, consolaResultados);

        // Acomodamos todo: Botón, Tabla arriba, y la Zona de Resultados abajo
        VBox contenedorPrincipal = new VBox(15);
        contenedorPrincipal.setStyle("-fx-padding: 20;");

        // Hacemos que la tabla y la zona de resultados se repartan el espacio vertical
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(zonaResultados, Priority.ALWAYS);

        contenedorPrincipal.getChildren().addAll(btnCompetir, tabla, zonaResultados);

        // Hacemos la ventana un poco más ancha (1100) para que entren bien ambos componentes
        Scene escena = new Scene(contenedorPrincipal, 1100, 700);

        try {
            String rutaCss = new java.io.File("C:\\Users\\TAPIAPC\\IdeaProjects\\Prac5Videojuegos\\src\\main\\java\\org\\example\\estilo.css").toURI().toString();
            escena.getStylesheets().add(rutaCss);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el CSS: " + e.getMessage());
        }

        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Método que hace las pruebas y va reportando en la consola
    private void ejecutarComparacion() {
        grafica.getData().clear();
        int totalDatos = listaJuegosOriginal.size();

        // Limpiamos la consola y avisamos qué vamos a hacer
        consolaResultados.setText("--- INICIANDO COMPETENCIA ---\n");
        consolaResultados.appendText("Total de registros: " + totalDatos + "\n\n");

        XYChart.Series<String, Number> serieTiempos = new XYChart.Series<>();
        serieTiempos.setName("Tiempo en nanosegundos");

        AlgoritmosOrdenamiento algoritmos = new AlgoritmosOrdenamiento();
        Comparator<Videojuego> compResenas = Comparator.comparingInt(Videojuego::getNumeroDeReviews);

        // --- 1. ARRAYS.SORT NATIVO ---
        Videojuego[] arregloSort = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSort = System.nanoTime();
        algoritmos.usarSortNativo(arregloSort, compResenas);
        long finSort = System.nanoTime();
        long tiempoSort = finSort - inicioSort;
        serieTiempos.getData().add(new XYChart.Data<>("Arrays.sort", tiempoSort));
        consolaResultados.appendText("✓ Arrays.sort:\n   " + tiempoSort + " ns.\n");

        // --- 2. PARALLEL SORT ---
        Videojuego[] arregloParallel = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioParallel = System.nanoTime();
        algoritmos.usarParallelSort(arregloParallel, compResenas);
        long finParallel = System.nanoTime();
        long tiempoParallel = finParallel - inicioParallel;
        serieTiempos.getData().add(new XYChart.Data<>("Parallel Sort", tiempoParallel));
        consolaResultados.appendText("✓ Parallel Sort:\n   " + tiempoParallel + " ns.\n");

        // --- 3. QUICKSORT ---
        Videojuego[] arregloQuick = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioQuick = System.nanoTime();
        algoritmos.quicksort(arregloQuick, 0, arregloQuick.length - 1, compResenas);
        long finQuick = System.nanoTime();
        long tiempoQuick = finQuick - inicioQuick;
        serieTiempos.getData().add(new XYChart.Data<>("Quicksort", tiempoQuick));
        consolaResultados.appendText("✓ Quicksort:\n   " + tiempoQuick + " ns.\n");

        // --- 4. MERGESORT ---
        Videojuego[] arregloMerge = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioMerge = System.nanoTime();
        algoritmos.mergesort(arregloMerge, 0, arregloMerge.length - 1, compResenas);
        long finMerge = System.nanoTime();
        long tiempoMerge = finMerge - inicioMerge;
        serieTiempos.getData().add(new XYChart.Data<>("Mergesort", tiempoMerge));
        consolaResultados.appendText("✓ Mergesort:\n   " + tiempoMerge + " ns.\n");

        // --- 5. SHELL SORT ---
        Videojuego[] arregloShell = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioShell = System.nanoTime();
        algoritmos.shellSort(arregloShell, compResenas);
        long finShell = System.nanoTime();
        long tiempoShell = finShell - inicioShell;
        serieTiempos.getData().add(new XYChart.Data<>("Shell Sort", tiempoShell));
        consolaResultados.appendText("✓ Shell Sort:\n   " + tiempoShell + " ns.\n");

        // --- 6. RADIX SORT ---
        Videojuego[] arregloRadix = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioRadix = System.nanoTime();
        algoritmos.radixSort(arregloRadix);
        long finRadix = System.nanoTime();
        long tiempoRadix = finRadix - inicioRadix;
        serieTiempos.getData().add(new XYChart.Data<>("Radix Sort", tiempoRadix));
        consolaResultados.appendText("✓ Radix Sort:\n   " + tiempoRadix + " ns.\n");

        // --- 7. SELECCIÓN DIRECTA ---
        Videojuego[] arregloSeleccion = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSeleccion = System.nanoTime();
        algoritmos.seleccionDirecta(arregloSeleccion, compResenas);
        long finSeleccion = System.nanoTime();
        long tiempoSeleccion = finSeleccion - inicioSeleccion;
        serieTiempos.getData().add(new XYChart.Data<>("Selección", tiempoSeleccion));
        consolaResultados.appendText("✓ Selección:\n   " + tiempoSeleccion + " ns.\n");

        consolaResultados.appendText("\n--- COMPETENCIA FINALIZADA ---\n");

        // Dibujamos la gráfica
        grafica.getData().add(serieTiempos);

        // Actualizamos la tabla
        ObservableList<Videojuego> datosOrdenados = FXCollections.observableArrayList(arregloQuick);
        tabla.setItems(datosOrdenados);
    }

    public static void main(String[] args) {
        launch(args);
    }
}