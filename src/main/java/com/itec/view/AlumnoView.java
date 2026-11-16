package com.itec.view;

import com.itec.controller.AlumnoController;
import com.itec.model.Alumno;

/**
 * Vista de gestion de alumnos por consola.
 * <p>
 * Unica capa que imprime con System.out y lee con Scanner (via ConsoleInput).
 * Delega toda decision de negocio al AlumnoController.
 */
public class AlumnoView {

    private final AlumnoController controller;

    public AlumnoView(AlumnoController controller) {
        this.controller = controller;
    }

    /** Menu de alumnos: se repite hasta que el usuario elige volver. */
    public void mostrarMenuAlumnos() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== GESTION DE ALUMNOS =====");
            System.out.println("1. Crear Alumno");
            System.out.println("2. Buscar Alumno");
            System.out.println("3. Dar de Baja");
            System.out.println("4. Listar todos los alumnos");
            System.out.println("5. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearAlumno();
                    break;
                case 2:
                    buscarAlumno();
                    break;
                case 3:
                    darDeBaja();
                    break;
                case 4:
                    listarAlumnos();
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 5);
    }

    private void crearAlumno() {
        System.out.println("--- Crear Alumno ---");
        String nombre = ConsoleInput.leerLinea("Nombre: ");
        String apellido = ConsoleInput.leerLinea("Apellido: ");
        String dni = ConsoleInput.leerLinea("DNI: ");
        String mail = ConsoleInput.leerLinea("Mail: ");
        String telefono = ConsoleInput.leerLinea("Telefono: ");

        String resultado = controller.crearAlumno(nombre, apellido, dni, mail, telefono);
        System.out.println(resultado);
    }

    private void buscarAlumno() {
        System.out.println("--- Buscar Alumno ---");
        String dni = ConsoleInput.leerLinea("DNI: ");

        Alumno alumno = controller.buscarAlumno(dni);
        if (alumno == null) {
            System.out.println(AlumnoController.MSG_NO_ENCONTRADO);
        } else {
            System.out.println(alumno);
        }
    }

    private void darDeBaja() {
        System.out.println("--- Dar de Baja (logica) ---");
        String dni = ConsoleInput.leerLinea("DNI: ");

        String resultado = controller.darDeBajaLogica(dni);
        System.out.println(resultado);
    }

    /** Lista todos los alumnos: DNI, nombre completo, mail y estado. */
    private void listarAlumnos() {
        System.out.println("--- Alumnos registrados ---");
        java.util.List<Alumno> alumnos = controller.listarTodos();
        if (alumnos.isEmpty()) {
            System.out.println("(no hay alumnos registrados)");
            return;
        }
        for (Alumno alumno : alumnos) {
            String estado = alumno.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.println("- DNI " + alumno.getDni()
                    + " | " + alumno.getApellido() + ", " + alumno.getNombre()
                    + " | " + alumno.getMail()
                    + " | " + estado);
        }
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
