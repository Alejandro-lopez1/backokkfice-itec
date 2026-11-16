package com.itec.view;

import com.itec.controller.AsistenciaController;
import com.itec.controller.ComisionController;
import com.itec.model.AlumnoInscripto;
import com.itec.model.Asistencia;
import com.itec.model.ComisionMateria;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Vista de registro de asistencias por consola.
 * <p>
 * Unica capa que imprime con System.out y lee con Scanner (via ConsoleInput).
 * Delega toda decision de negocio al AsistenciaController.
 */
public class AsistenciaView {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AsistenciaController controller;
    private final ComisionController comisionController;

    public AsistenciaView(AsistenciaController controller, ComisionController comisionController) {
        this.controller = controller;
        this.comisionController = comisionController;
    }

    /** Menu de asistencias: se repite hasta que el usuario elige volver. */
    public void mostrarMenuAsistencias() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== ASISTENCIAS =====");
            System.out.println("1. Registrar Asistencia");
            System.out.println("2. Ver Asistencias de una Comision");
            System.out.println("3. Listar Comisiones (ayuda)");
            System.out.println("4. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    registrarAsistencia();
                    break;
                case 2:
                    verAsistencias();
                    break;
                case 3:
                    listarComisiones();
                    break;
                case 4:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 4);
    }

    private void registrarAsistencia() {
        System.out.println("--- Registrar Asistencia ---");
        listarComisiones();

        String dni = ConsoleInput.leerLinea("DNI del alumno: ");
        Integer idComision = leerEntero("ID de la comision: ");
        if (idComision == null) {
            return;
        }

        Boolean presente = leerPresente("Estuvo presente (S/N): ");
        if (presente == null) {
            return;
        }

        String resultado = controller.registrarAsistencia(dni, idComision, presente);
        System.out.println(resultado);
    }

    private void verAsistencias() {
        System.out.println("--- Asistencias de una Comision ---");
        Integer idComision = leerEntero("ID de la comision: ");
        if (idComision == null) {
            return;
        }

        List<Asistencia> asistencias = controller.obtenerAsistenciasPorComision(idComision);
        if (asistencias.isEmpty()) {
            System.out.println("(no hay asistencias registradas para esa comision)");
            return;
        }
        for (Asistencia asistencia : asistencias) {
            System.out.println(describir(asistencia));
        }
    }

    /** Arma la linea de detalle de una asistencia para el listado. */
    private String describir(Asistencia asistencia) {
        String estado = asistencia.isPresente() ? "PRESENTE" : "AUSENTE";
        String fecha = asistencia.getFecha() != null ? asistencia.getFecha().format(FORMATO_FECHA) : "?";
        return "- " + fecha + " | " + estado + " | " + nombreAlumno(asistencia.getAlumnoInscripto());
    }

    /** Nombre del alumno de la inscripcion, o "?" si faltara el enlace. */
    private String nombreAlumno(AlumnoInscripto inscripto) {
        if (inscripto == null || inscripto.getAlumno() == null) {
            return "?";
        }
        return inscripto.getAlumno().getApellido() + ", " + inscripto.getAlumno().getNombre();
    }

    private void listarComisiones() {
        System.out.println("--- Comisiones ---");
        List<ComisionMateria> comisiones = comisionController.listarComisiones();
        if (comisiones.isEmpty()) {
            System.out.println("(no hay comisiones registradas)");
            return;
        }
        for (ComisionMateria c : comisiones) {
            System.out.println(c.aTextoCorto());
        }
    }

    /** Lee S/N (case-insensitive); devuelve null (y avisa) si la entrada es invalida. */
    private Boolean leerPresente(String mensaje) {
        String entrada = ConsoleInput.leerLinea(mensaje).trim();
        if (entrada.equalsIgnoreCase("S")) {
            return true;
        }
        if (entrada.equalsIgnoreCase("N")) {
            return false;
        }
        System.out.println("ERROR: debe ingresar S o N");
        return null;
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

    /** Lee un numero de opcion; si la entrada no es numerica devuelve -1. */
    private int leerOpcion(String mensaje) {
        try {
            return Integer.parseInt(ConsoleInput.leerLinea(mensaje).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
