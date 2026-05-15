package org.example;

// Esta clase solo sirve para engañar al sistema de Java
// Como no hereda de "Application", Java no se pone estricto y nos deja arrancar
public class Launcher {

    public static void main(String[] args) {
        // Desde aquí mandamos a llamar a nuestra ventana real
        InterfazJuegos.main(args);
    }
}