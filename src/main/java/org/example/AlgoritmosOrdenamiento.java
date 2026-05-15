package org.example;

import java.util.Arrays;
import java.util.Comparator;

// Aquí vamos a meter todos los métodos de ordenamiento de la práctica
public class AlgoritmosOrdenamiento {

    // Método 1: El sort() normalito que ya trae Java por defecto
    // Recibe el arreglo de juegos y el comparador que le dice qué columna revisar
    public void usarSortNativo(Videojuego[] arreglo, Comparator<Videojuego> comparador) {
        // Java hace todo el trabajo pesado aquí
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

}