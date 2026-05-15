package org.example;

import java.util.Arrays;
import java.util.Comparator;

// Aquí vamos a meter todos los métodos de ordenamiento de la práctica
public class AlgoritmosOrdenamiento {

    // Método 1: El sort() normalito que ya trae Java por defecto
    // Recibe el arreglo de juegos y el comparador que le dice qué columna revisar
    public void usarSortNativo(Videojuego[] arreglo, Comparator<Videojuego> comparador) {
        // Java hace  el trabajo pesado aquí
        Arrays.sort(arreglo, comparador);
    }

    // Método 2: El parallelSort() de Java
    // Este divide el arreglo y usa varios núcleos del procesador al mismo tiempo
    public void usarParallelSort(Videojuego[] arreglo, Comparator<Videojuego> comparador) {
        // Súper útil cuando tenemos miles y miles de datos
        Arrays.parallelSort(arreglo, comparador);
    }

    // Método 3: Selección Directa (Visto en clase)
    // Su complejidad es O(n^2), así que va a ser de los más lentos
    public void seleccionDirecta(Videojuego[] arreglo, Comparator<Videojuego> comparador) {
        int n = arreglo.length;

        // Recorremos todo el arreglo
        for (int i = 0; i < n - 1; i++) {

            // Asumimos que el primero que vemos es el menor
            int indiceDelMenor = i;

            // Buscamos si hay uno más pequeño en lo que resta del arreglo
            for (int j = i + 1; j < n; j++) {

                // Usamos el comparador. Si da menor a 0, significa que el de la posición j es más pequeño
                if (comparador.compare(arreglo[j], arreglo[indiceDelMenor]) < 0) {
                    indiceDelMenor = j;
                }
            }

            // Si encontramos uno menor, los intercambiamos de lugar
            if (indiceDelMenor != i) {
                Videojuego temporal = arreglo[indiceDelMenor];
                arreglo[indiceDelMenor] = arreglo[i];
                arreglo[i] = temporal;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Método 4: Quicksort (El más rápido en la práctica)
    // -------------------------------------------------------------------------

    // Método principal que llamamos desde la interfaz
    public void quicksort(Videojuego[] arreglo, int inicio, int fin, Comparator<Videojuego> comparador) {
        if (inicio < fin) {
            // Encontramos el punto donde partimos el arreglo
            int indiceParticion = particion(arreglo, inicio, fin, comparador);

            // Ordenamos la mitad izquierda
            quicksort(arreglo, inicio, indiceParticion - 1, comparador);
            // Ordenamos la mitad derecha
            quicksort(arreglo, indiceParticion + 1, fin, comparador);
        }
    }

    // Método auxiliar para el Quicksort que acomoda los elementos
    private int particion(Videojuego[] arreglo, int inicio, int fin, Comparator<Videojuego> comparador) {
        // Tomamos el último elemento como pivote
        Videojuego pivote = arreglo[fin];
        int i = (inicio - 1);

        for (int j = inicio; j < fin; j++) {
            // Si el elemento actual es menor o igual al pivote, lo movemos a la izquierda
            if (comparador.compare(arreglo[j], pivote) <= 0) {
                i++;
                // Intercambiamos
                Videojuego temporal = arreglo[i];
                arreglo[i] = arreglo[j];
                arreglo[j] = temporal;
            }
        }

        // Ponemos el pivote en su lugar correcto
        Videojuego temporal = arreglo[i + 1];
        arreglo[i + 1] = arreglo[fin];
        arreglo[fin] = temporal;

        return i + 1;
    }

    // -------------------------------------------------------------------------
    // Método 5: Mergesort (Divide y vencerás)
    // -------------------------------------------------------------------------

    public void mergesort(Videojuego[] arreglo, int inicio, int fin, Comparator<Videojuego> comparador) {
        if (inicio < fin) {
            // Calculamos el punto medio
            int medio = inicio + (fin - inicio) / 2;

            // Ordenamos la primera y segunda mitad
            mergesort(arreglo, inicio, medio, comparador);
            mergesort(arreglo, medio + 1, fin, comparador);

            // Juntamos las dos mitades ya ordenadas
            mezclar(arreglo, inicio, medio, fin, comparador);
        }
    }

    // Método auxiliar para juntar las mitades en el Mergesort
    private void mezclar(Videojuego[] arreglo, int inicio, int medio, int fin, Comparator<Videojuego> comparador) {
        // Tamaños de los dos sub-arreglos a juntar
        int n1 = medio - inicio + 1;
        int n2 = fin - medio;

        // Arreglos temporales
        Videojuego[] izq = new Videojuego[n1];
        Videojuego[] der = new Videojuego[n2];

        // Copiamos los datos a los arreglos temporales
        System.arraycopy(arreglo, inicio, izq, 0, n1);
        for (int j = 0; j < n2; ++j) {
            der[j] = arreglo[medio + 1 + j];
        }

        // Índices iniciales de los sub-arreglos
        int i = 0, j = 0;
        int k = inicio;

        // Juntamos los arreglos comparando elemento por elemento
        while (i < n1 && j < n2) {
            if (comparador.compare(izq[i], der[j]) <= 0) {
                arreglo[k] = izq[i];
                i++;
            } else {
                arreglo[k] = der[j];
                j++;
            }
            k++;
        }

        // Copiamos los elementos restantes si es que quedaron algunos en la izquierda
        while (i < n1) {
            arreglo[k] = izq[i];
            i++;
            k++;
        }

        // Copiamos los elementos restantes de la derecha
        while (j < n2) {
            arreglo[k] = der[j];
            j++;
            k++;
        }
    }

    // -------------------------------------------------------------------------
    // Método 6: Shell Sort (Inserción con saltos)
    // -------------------------------------------------------------------------

    public void shellSort(Videojuego[] arreglo, Comparator<Videojuego> comparador) {
        int n = arreglo.length;

        // Empezamos con un salto grande y lo vamos reduciendo a la mitad
        for (int salto = n / 2; salto > 0; salto /= 2) {

            // Hacemos una inserción directa para este tamaño de salto
            for (int i = salto; i < n; i++) {
                Videojuego temporal = arreglo[i];
                int j;

                // Recorremos los elementos que ya revisamos y los movemos si son mayores
                for (j = i; j >= salto && comparador.compare(arreglo[j - salto], temporal) > 0; j -= salto) {
                    arreglo[j] = arreglo[j - salto];
                }

                // Ponemos el elemento temporal en su lugar correcto
                arreglo[j] = temporal;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Método 7: Radix Sort (Ordenamiento por dígitos)
    // -------------------------------------------------------------------------

    // Ojo: Radix Sort no usa comparaciones, así que lo amarramos a la columna de reseñas (números enteros)
    public void radixSort(Videojuego[] arreglo) {
        // Si el arreglo está vacío, no hacemos nada
        if (arreglo.length == 0) return;

        // Primero encontramos el número máximo para saber cuántos dígitos tiene
        int max = arreglo[0].getNumeroDeReviews();
        for (int i = 1; i < arreglo.length; i++) {
            if (arreglo[i].getNumeroDeReviews() > max) {
                max = arreglo[i].getNumeroDeReviews();
            }
        }

        // Hacemos un counting sort por cada dígito (unidades, decenas, centenas...)
        // La variable exp es 1, 10, 100, etc.
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortParaRadix(arreglo, exp);
        }
    }

    // Método auxiliar de Counting Sort adaptado para el Radix Sort
    private void countingSortParaRadix(Videojuego[] arreglo, int exp) {
        int n = arreglo.length;
        Videojuego[] salida = new Videojuego[n];
        int[] conteo = new int[10];

        // Contamos cuántas veces aparece cada dígito (del 0 al 9)
        for (int i = 0; i < n; i++) {
            int digito = (arreglo[i].getNumeroDeReviews() / exp) % 10;
            conteo[digito]++;
        }

        // Cambiamos el conteo para que contenga la posición real en el arreglo de salida
        for (int i = 1; i < 10; i++) {
            conteo[i] += conteo[i - 1];
        }

        // Construimos el arreglo de salida acomodando los elementos de atrás hacia adelante
        for (int i = n - 1; i >= 0; i--) {
            int digito = (arreglo[i].getNumeroDeReviews() / exp) % 10;
            salida[conteo[digito] - 1] = arreglo[i];
            conteo[digito]--;
        }

        // Copiamos la salida al arreglo original para guardar los cambios de esta pasada
        System.arraycopy(salida, 0, arreglo, 0, n);
    }

}