package com.itec.view;

import com.itec.controller.ProfesorController;
import com.itec.model.Profesor;

/**
 * Vista de gestion de profesores por consola.
 * <p>
 * Unica capa que imprime con System.out y lee con Scanner (via ConsoleInput).
 * Delega toda decision de negocio al ProfesorController.
 */
public class ProfesorView {

    private final ProfesorController controller;

    public ProfesorView(ProfesorController controller) {
        this.controller = controller;
    }

    /** Menu de profesores: se repite hasta que el usuario elige volver. */
    public void mostrarMenuProfesores() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== GESTION DE PROFESORES =====");
            System.out.println("1. Crear Profesor");
            System.out.println("2. Buscar Profesor por DNI");
            System.out.println("3. Dar de Baja Profesor");
            System.out.println("4. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearProfesor();
                    break;
                case 2:
                    buscarProfesor();
                    break;
                case 3:
                    darDeBaja();
                    break;
                case 4:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 4);
    }

    private void crearProfesor() {
        System.out.println("--- Crear Profesor ---");
        String nombre = ConsoleInput.leerLinea("Nombre: ");
        String apellido = ConsoleInput.leerLinea("Apellido: ");
        String dni = ConsoleInput.leerLinea("DNI: ");
        String mail = ConsoleInput.leerLinea("Mail: ");
        String telefono = ConsoleInput.leerLinea("Telefono: ");

        String resultado = controller.crearProfesor(nombre, apellido, dni, mail, telefono);
        System.out.println(resultado);
    }

    private void buscarProfesor() {
        System.out.println("--- Buscar Profesor ---");
        String dni = ConsoleInput.leerLinea("DNI: ");

        Profesor profesor = controller.buscarProfesor(dni);
        if (profesor == null) {
            System.out.println(ProfesorController.MSG_NO_ENCONTRADO);
        } else {
            System.out.println(profesor);
        }
    }

    private void darDeBaja() {
        System.out.println("--- Dar de Baja (logica) ---");
        String dni = ConsoleInput.leerLinea("DNI: ");

        String resultado = controller.darDeBajaLogica(dni);
        System.out.println(resultado);
    }

    /** Lee un numero de opcion; si la entrada no es numerica devuelve -1. */
    private int leerOpcion(String mensaje) {
        try {
            return Integer.parseInt(ConsoleInput.leerLinea(mensaje).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
