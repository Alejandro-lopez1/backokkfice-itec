package com.itec.view;

import com.itec.controller.ComisionController;
import com.itec.model.ComisionMateria;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Vista de comisiones e inscripciones por consola.
 * <p>
 * Permite crear comisiones (necesarias para poder inscribir alumnos),
 * listarlas e inscribir alumnos por DNI + ID de comision.
 */
public class InscripcionView {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ComisionController controller;

    public InscripcionView(ComisionController controller) {
        this.controller = controller;
    }

    /** Menu de inscripciones: se repite hasta que el usuario elige volver. */
    public void mostrarMenuInscripciones() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== COMISIONES E INSCRIPCIONES =====");
            System.out.println("1. Crear Comision");
            System.out.println("2. Listar Comisiones");
            System.out.println("3. Inscribir Alumno");
            System.out.println("4. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearComision();
                    break;
                case 2:
                    listarComisiones();
                    break;
                case 3:
                    inscribirAlumno();
                    break;
                case 4:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 4);
    }

    private void crearComision() {
        System.out.println("--- Crear Comision ---");
        String nombreMateria = ConsoleInput.leerLinea("Nombre de la materia: ");
        String dniProfesor = ConsoleInput.leerLinea("DNI del profesor: ");
        Integer anio = leerEntero("Anio del cuatrimestre: ");
        if (anio == null) {
            return;
        }
        Integer numero = leerEntero("Numero de cuatrimestre (1 o 2): ");
        if (numero == null) {
            return;
        }
        LocalDate fechaInicio = leerFecha("Fecha de inicio (dd/MM/yyyy): ");
        if (fechaInicio == null) {
            return;
        }
        LocalDate fechaFin = leerFecha("Fecha de fin (dd/MM/yyyy): ");
        if (fechaFin == null) {
            return;
        }

        String resultado = controller.crearComision(nombreMateria, dniProfesor, anio, numero, fechaInicio, fechaFin);
        System.out.println(resultado);
    }

    private void listarComisiones() {
        System.out.println("--- Comisiones ---");
        List<ComisionMateria> comisiones = controller.listarComisiones();
        if (comisiones.isEmpty()) {
            System.out.println("(no hay comisiones registradas)");
            return;
        }
        for (ComisionMateria c : comisiones) {
            System.out.println(c.aTextoCorto());
        }
    }

    private void inscribirAlumno() {
        System.out.println("--- Inscribir Alumno ---");
        listarComisiones();

        String dni = ConsoleInput.leerLinea("DNI del alumno: ");
        Integer idComision = leerEntero("ID de la comision: ");
        if (idComision == null) {
            return;
        }

        String resultado = controller.inscribirAlumno(dni, idComision);
        System.out.println(resultado);
    }

    /** Lee un entero; devuelve null (y avisa) si la entrada no es numerica. */
    private Integer leerEntero(String mensaje) {
        String entrada = ConsoleInput.leerLinea(mensaje).trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: debe ingresar un numero entero");
            return null;
        }
    }

    /** Lee una fecha dd/MM/yyyy; devuelve null (y avisa) si el formato es invalido. */
    private LocalDate leerFecha(String mensaje) {
        String entrada = ConsoleInput.leerLinea(mensaje).trim();
        try {
            return LocalDate.parse(entrada, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            System.out.println("ERROR: fecha invalida (formato dd/MM/yyyy)");
            return null;
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
