package com.itec.view;

import com.itec.controller.MateriaController;
import com.itec.model.Materia;

/**
 * Vista de gestion de materias por consola.
 * <p>
 * Unica capa que imprime con System.out y lee con Scanner (via ConsoleInput).
 * Delega toda decision de negocio al MateriaController, incluyendo la
 * gestion de materias correlativas.
 */
public class MateriaView {

    private final MateriaController controller;

    public MateriaView(MateriaController controller) {
        this.controller = controller;
    }

    /** Menu de materias: se repite hasta que el usuario elige volver. */
    public void mostrarMenuMaterias() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== GESTION DE MATERIAS =====");
            System.out.println("1. Crear Materia");
            System.out.println("2. Buscar Materia");
            System.out.println("3. Modificar Materia");
            System.out.println("4. Dar de Baja Materia");
            System.out.println("5. Agregar Correlativa");
            System.out.println("6. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearMateria();
                    break;
                case 2:
                    buscarMateria();
                    break;
                case 3:
                    modificarMateria();
                    break;
                case 4:
                    darDeBaja();
                    break;
                case 5:
                    agregarCorrelativa();
                    break;
                case 6:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 6);
    }

    private void crearMateria() {
        System.out.println("--- Crear Materia ---");
        String nombre = ConsoleInput.leerLinea("Nombre: ");
        Integer anio = leerEntero("Anio (1 o mayor): ");
        if (anio == null) {
            return;
        }
        Integer cuatrimestre = leerEntero("Cuatrimestre (1 o 2): ");
        if (cuatrimestre == null) {
            return;
        }

        String resultado = controller.crearMateria(nombre, anio, cuatrimestre);
        System.out.println(resultado);
    }

    private void buscarMateria() {
        System.out.println("--- Buscar Materia ---");
        String nombre = ConsoleInput.leerLinea("Nombre: ");

        Materia materia = controller.buscarMateria(nombre);
        if (materia == null) {
            System.out.println(MateriaController.MSG_NO_ENCONTRADA);
        } else {
            System.out.println(materia);
            System.out.println("Correlativas: " + nombresCorrelativas(materia));
        }
    }

    private void modificarMateria() {
        System.out.println("--- Modificar Materia ---");
        String nombreOriginal = ConsoleInput.leerLinea("Nombre actual: ");
        String nuevoNombre = ConsoleInput.leerLinea("Nuevo nombre (enter para dejar igual): ");
        Integer nuevoAnio = leerEntero("Nuevo anio: ");
        if (nuevoAnio == null) {
            return;
        }
        Integer nuevoCuatrimestre = leerEntero("Nuevo cuatrimestre (1 o 2): ");
        if (nuevoCuatrimestre == null) {
            return;
        }

        String resultado = controller.modificarMateria(nombreOriginal, nuevoNombre, nuevoAnio, nuevoCuatrimestre);
        System.out.println(resultado);
    }

    private void darDeBaja() {
        System.out.println("--- Dar de Baja (logica) ---");
        String nombre = ConsoleInput.leerLinea("Nombre: ");

        String resultado = controller.darDeBajaLogica(nombre);
        System.out.println(resultado);
    }

    private void agregarCorrelativa() {
        System.out.println("--- Agregar Correlativa ---");
        String nombreMateria = ConsoleInput.leerLinea("Nombre de la materia: ");
        String nombreCorrelativa = ConsoleInput.leerLinea("Nombre de la correlativa: ");

        String resultado = controller.agregarCorrelativa(nombreMateria, nombreCorrelativa);
        System.out.println(resultado);
    }

    /** Arma la lista de nombres de correlativas para mostrarla. */
    private String nombresCorrelativas(Materia materia) {
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
