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
            System.out.println("4. Listar todos los profesores");
            System.out.println("5. Modificar Profesor");
            System.out.println("6. Volver al menu principal");
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
                    listarProfesores();
                    break;
                case 5:
                    modificarProfesor();
                    break;
                case 6:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 6);
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

    /** Lista todos los profesores: DNI, nombre completo, mail y estado. */
    private void listarProfesores() {
        System.out.println("--- Profesores registrados ---");
        java.util.List<Profesor> profesores = controller.listarTodos();
        if (profesores.isEmpty()) {
            System.out.println("(no hay profesores registrados)");
            return;
        }
        for (Profesor profesor : profesores) {
            String estado = profesor.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.println("- DNI " + profesor.getDni()
                    + " | " + profesor.getApellido() + ", " + profesor.getNombre()
                    + " | " + profesor.getMail()
                    + " | " + estado);
        }
    }

    /** Pide el DNI y los nuevos datos, y delega la modificacion. */
    private void modificarProfesor() {
        System.out.println("--- Modificar Profesor ---");
        String dni = ConsoleInput.leerLinea("DNI del profesor a modificar: ");
        String nombre = ConsoleInput.leerLinea("Nuevo nombre: ");
        String apellido = ConsoleInput.leerLinea("Nuevo apellido: ");
        String mail = ConsoleInput.leerLinea("Nuevo mail: ");
        String telefono = ConsoleInput.leerLinea("Nuevo telefono: ");

        String resultado = controller.modificarProfesor(dni, nombre, apellido, mail, telefono);
        System.out.println(resultado);
    }
}
