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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

// Nuestra ventana principal, ahora con superpoderes de graficación
public class InterfazJuegos extends Application {

    // Variables que vamos a usar en toda la clase
    private TableView<Videojuego> tabla;
    private List<Videojuego> listaJuegosOriginal;
    private BarChart<String, Number> grafica;

    @Override
    public void start(Stage ventanaPrincipal) {
        ventanaPrincipal.setTitle("Visualizador y Competencia de Algoritmos");

        // 1. ARMAMOS LA TABLA
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

        // Cargamos los datos
        LectorCSV lector = new LectorCSV();
        listaJuegosOriginal = lector.cargarJuegos("C:\\Users\\TAPIAPC\\IdeaProjects\\Prac5Videojuegos\\src\\main\\java\\org\\example\\games.csv");

        ObservableList<Videojuego> datosParaTabla = FXCollections.observableArrayList(listaJuegosOriginal);
        tabla.setItems(datosParaTabla);

        // 2. ARMAMOS LA GRÁFICA DE BARRAS
        // El eje X será para los nombres de los algoritmos (texto)
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Método de Ordenamiento");

        // El eje Y será para el tiempo (números)
        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo (Milisegundos)");

        grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Rendimiento de los Algoritmos");
        // Quitamos la animación para que pinte rápido
        grafica.setAnimated(false);

        // 3. EL BOTÓN QUE ARRANCA LA COMPETENCIA
        Button btnCompetir = new Button("¡Iniciar Carrera de Algoritmos (Por Reseñas)!");

        // Cuando le den clic al botón, llamamos al método que hace toda la magia
        btnCompetir.setOnAction(evento -> ejecutarComparacion());

        // Acomodamos todo: Botón arriba, luego la tabla y abajo la gráfica
        VBox contenedorPrincipal = new VBox(10);
        contenedorPrincipal.getChildren().addAll(btnCompetir, tabla, grafica);

        Scene escena = new Scene(contenedorPrincipal, 900, 700);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Este es el método estrella que toma los tiempos
    private void ejecutarComparacion() {
        // Limpiamos la gráfica por si le pican al botón varias veces
        grafica.getData().clear();

        // Creamos la serie de datos para nuestra gráfica
        XYChart.Series<String, Number> serieTiempos = new XYChart.Series<>();
        serieTiempos.setName("Tiempo de ejecución");

        // Instanciamos nuestros algoritmos
        AlgoritmosOrdenamiento algoritmos = new AlgoritmosOrdenamiento();

        // Creamos la regla para comparar: vamos a ordenar por el número de reseñas
        Comparator<Videojuego> compResenas = Comparator.comparingInt(Videojuego::getNumeroDeReviews);

        // Para que la carrera sea justa, cada algoritmo necesita su propia copia desordenada
        int totalDatos = listaJuegosOriginal.size();

        // --- 1. ARRAYS.SORT NATIVO ---
        Videojuego[] arregloSort = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSort = System.nanoTime();
        algoritmos.usarSortNativo(arregloSort, compResenas);
        long finSort = System.nanoTime();
        // Dividimos entre un millón para pasar de nanosegundos a milisegundos
        long tiempoSort = (finSort - inicioSort) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Arrays.sort", tiempoSort));

        // --- 2. PARALLEL SORT ---
        Videojuego[] arregloParallel = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioParallel = System.nanoTime();
        algoritmos.usarParallelSort(arregloParallel, compResenas);
        long finParallel = System.nanoTime();
        long tiempoParallel = (finParallel - inicioParallel) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Parallel Sort", tiempoParallel));

        // --- 3. QUICKSORT ---
        Videojuego[] arregloQuick = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioQuick = System.nanoTime();
        algoritmos.quicksort(arregloQuick, 0, arregloQuick.length - 1, compResenas);
        long finQuick = System.nanoTime();
        long tiempoQuick = (finQuick - inicioQuick) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Quicksort", tiempoQuick));

        // --- 4. MERGESORT ---
        Videojuego[] arregloMerge = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioMerge = System.nanoTime();
        algoritmos.mergesort(arregloMerge, 0, arregloMerge.length - 1, compResenas);
        long finMerge = System.nanoTime();
        long tiempoMerge = (finMerge - inicioMerge) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Mergesort", tiempoMerge));

        // --- 5. SHELL SORT ---
        Videojuego[] arregloShell = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioShell = System.nanoTime();
        algoritmos.shellSort(arregloShell, compResenas);
        long finShell = System.nanoTime();
        long tiempoShell = (finShell - inicioShell) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Shell Sort", tiempoShell));

        // --- 6. RADIX SORT ---
        // Acuérdate que Radix no usa el comparador, lo amarramos directo a las reseñas en la fase 3
        Videojuego[] arregloRadix = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioRadix = System.nanoTime();
        algoritmos.radixSort(arregloRadix);
        long finRadix = System.nanoTime();
        long tiempoRadix = (finRadix - inicioRadix) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Radix Sort", tiempoRadix));

        // --- 7. SELECCIÓN DIRECTA ---
        // OJO: Este es muy lento, si tu archivo es muy grande, la compu se puede quedar pensando un rato
        Videojuego[] arregloSeleccion = listaJuegosOriginal.toArray(new Videojuego[totalDatos]);
        long inicioSeleccion = System.nanoTime();
        algoritmos.seleccionDirecta(arregloSeleccion, compResenas);
        long finSeleccion = System.nanoTime();
        long tiempoSeleccion = (finSeleccion - inicioSeleccion) / 1000000;
        serieTiempos.getData().add(new XYChart.Data<>("Selección", tiempoSeleccion));

        // Finalmente, le metemos todos los resultados a la gráfica para que los dibuje
        grafica.getData().add(serieTiempos);

        // Y actualizamos la tabla para que muestre el arreglo ya ordenadito
        // Usaremos el de Quicksort como muestra, pero podría ser cualquiera
        ObservableList<Videojuego> datosOrdenados = FXCollections.observableArrayList(arregloQuick);
        tabla.setItems(datosOrdenados);
    }

    public static void main(String[] args) {
        launch(args);
    }
}