package com.itec.view;

import com.itec.controller.CarreraController;
import com.itec.model.Carrera;
import com.itec.model.Materia;
import com.itec.model.PlanEstudio;

import java.util.List;

/**
 * Vista de gestion de carreras y planes de estudio por consola.
 * <p>
 * Unica capa que imprime con System.out y lee con Scanner (via ConsoleInput).
 * Delega toda decision de negocio al CarreraController.
 */
public class CarreraView {

    private final CarreraController controller;

    public CarreraView(CarreraController controller) {
        this.controller = controller;
    }

    /** Menu de carreras: se repite hasta que el usuario elige volver. */
    public void mostrarMenuCarreras() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== GESTION DE CARRERAS Y PLANES DE ESTUDIO =====");
            System.out.println("1. Crear Carrera");
            System.out.println("2. Crear Plan de Estudio para una Carrera");
            System.out.println("3. Asociar Materia a un Plan");
            System.out.println("4. Listar Carreras y sus Planes");
            System.out.println("5. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearCarrera();
                    break;
                case 2:
                    crearPlanEstudio();
                    break;
                case 3:
                    asociarMateria();
                    break;
                case 4:
                    listarCarrerasConPlanes();
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 5);
    }

    private void crearCarrera() {
        System.out.println("--- Crear Carrera ---");
        String nombre = ConsoleInput.leerLinea("Nombre de la carrera: ");

        String resultado = controller.crearCarrera(nombre);
        System.out.println(resultado);
    }

    private void crearPlanEstudio() {
        System.out.println("--- Crear Plan de Estudio ---");
        listarCarrerasConPlanes();
        String nombreCarrera = ConsoleInput.leerLinea("Carrera: ");
        String nombrePlan = ConsoleInput.leerLinea("Nombre del plan: ");
        Integer anioInicio = leerEntero("Anio de inicio: ");
        if (anioInicio == null) {
            return;
        }
        Integer anioFin = leerEntero("Anio de fin: ");
        if (anioFin == null) {
            return;
        }

        String resultado = controller.crearPlanEstudio(nombreCarrera, nombrePlan, anioInicio, anioFin);
        System.out.println(resultado);
    }

    private void asociarMateria() {
        System.out.println("--- Asociar Materia a un Plan ---");
        String nombreCarrera = ConsoleInput.leerLinea("Carrera: ");
        String nombrePlan = ConsoleInput.leerLinea("Nombre del plan: ");
        String nombreMateria = ConsoleInput.leerLinea("Nombre de la materia: ");

        String resultado = controller.asociarMateriaAPlan(nombreCarrera, nombrePlan, nombreMateria);
        System.out.println(resultado);
    }

    /** Lista cada carrera con sus planes y las materias detalladas de cada plan. */
    private void listarCarrerasConPlanes() {
        System.out.println("--- Carreras y Planes de Estudio ---");
        List<Carrera> carreras = controller.listarCarreras();
        if (carreras.isEmpty()) {
            System.out.println("(no hay carreras registradas)");
            return;
        }
        for (Carrera carrera : carreras) {
            System.out.println("- " + carrera.getNombre());
            List<PlanEstudio> planes = carrera.getPlanes();
            if (planes.isEmpty()) {
                System.out.println("    (sin planes de estudio)");
                continue;
            }
            for (PlanEstudio plan : planes) {
                System.out.println("    * " + plan.getNombre()
                        + " (" + anios(plan) + ")");
                List<Materia> materias = plan.getMaterias();
                if (materias.isEmpty()) {
                    System.out.println("        (sin materias asociadas)");
                    continue;
                }
                for (Materia materia : materias) {
                    String estado = materia.isActiva() ? "ACTIVA" : "INACTIVA";
                    System.out.println("        - " + materia.getNombre()
                            + " | Anio " + materia.getAnio()
                            + " | Cuat " + materia.getCuatrimestre()
                            + " | " + estado
                            + " | Correlativas: " + correlativas(materia));
                }
            }
        }
    }

    /** Rango de anios del plan en formato "2020 - 2024". */
    private String anios(PlanEstudio plan) {
        String inicio = plan.getFechaInicio() != null ? String.valueOf(plan.getFechaInicio().getYear()) : "?";
        String fin = plan.getFechaFin() != null ? String.valueOf(plan.getFechaFin().getYear()) : "?";
        return inicio + " - " + fin;
    }

    /** Nombres de correlativas de la materia, o "(ninguna)". */
    private String correlativas(Materia materia) {
        if (materia.getListaCorrelativas().isEmpty()) {
            return "(ninguna)";
        }
        StringBuilder sb = new StringBuilder();
        for (Materia correlativa : materia.getListaCorrelativas()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(correlativa.getNombre());
        }
        return sb.toString();
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
