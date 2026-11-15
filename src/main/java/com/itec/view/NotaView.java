package com.itec.view;

import com.itec.controller.ComisionController;
import com.itec.controller.NotaController;
import com.itec.model.ComisionMateria;
import com.itec.model.TipoEvaluacion;

import java.util.List;

/**
 * Vista de carga de notas por consola.
 * <p>
 * Pide DNI, ID de comision, tipo de evaluacion y valor de la nota,
 * y delega en el NotaController.
 */
public class NotaView {

    private final NotaController controller;
    private final ComisionController comisionController;

    public NotaView(NotaController controller, ComisionController comisionController) {
        this.controller = controller;
        this.comisionController = comisionController;
    }

    /** Menu de notas: se repite hasta que el usuario elige volver. */
    public void mostrarMenuNotas() {
        int opcion = 0;
        do {
            System.out.println();
            System.out.println("===== NOTAS =====");
            System.out.println("1. Cargar Nota");
            System.out.println("2. Listar Comisiones (ayuda)");
            System.out.println("3. Volver al menu principal");
            opcion = leerOpcion("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    cargarNota();
                    break;
                case 2:
                    listarComisiones();
                    break;
                case 3:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 3);
    }

    private void cargarNota() {
        System.out.println("--- Cargar Nota ---");
        listarComisiones();

        String dni = ConsoleInput.leerLinea("DNI del alumno: ");
        Integer idComision = leerEntero("ID de la comision: ");
        if (idComision == null) {
            return;
        }

        TipoEvaluacion tipo = leerTipoEvaluacion();
        if (tipo == null) {
            return;
        }

        Double valor = leerNota("Valor de la nota (0 a 10): ");
        if (valor == null) {
            return;
        }

        String resultado = controller.cargarNota(dni, idComision, tipo, valor);
        System.out.println(resultado);
    }

    /** Muestra los tipos de evaluacion y devuelve el elegido, o null si es invalido. */
    private TipoEvaluacion leerTipoEvaluacion() {
        TipoEvaluacion[] tipos = TipoEvaluacion.values();
        System.out.println("Tipo de evaluacion:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i]);
        }
        Integer eleccion = leerEntero("Seleccione una opcion: ");
        if (eleccion == null || eleccion < 1 || eleccion > tipos.length) {
            System.out.println(NotaController.MSG_TIPO_INVALIDO);
            return null;
        }
        return tipos[eleccion - 1];
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

    /** Lee un numero decimal; devuelve null (y avisa) si la entrada no es numerica. */
    private Double leerNota(String mensaje) {
        String entrada = ConsoleInput.leerLinea(mensaje).trim();
        try {
            return Double.parseDouble(entrada.replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: debe ingresar un numero (ej: 7.5)");
            return null;
        }
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
