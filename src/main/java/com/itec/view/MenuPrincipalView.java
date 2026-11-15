package com.itec.view;

/**
 * Menu principal de la aplicacion por consola.
 * <p>
 * Se repite hasta que el usuario elige salir. Delega a las vistas
 * de cada modulo (alumnos, inscripciones y notas).
 */
public class MenuPrincipalView {

    private final AlumnoView alumnoView;
    private final InscripcionView inscripcionView;
    private final NotaView notaView;

    public MenuPrincipalView(AlumnoView alumnoView, InscripcionView inscripcionView, NotaView notaView) {
        this.alumnoView = alumnoView;
        this.inscripcionView = inscripcionView;
        this.notaView = notaView;
    }

    /** Menu principal: se repite hasta que el usuario elige salir. */
    public void mostrar() {
        int opcion = 1;
        do {
            System.out.println();
            System.out.println("############################");
            System.out.println("#  BACKOFFICE ACADEMICO    #");
            System.out.println("############################");
            System.out.println("1. Gestion de Alumnos");
            System.out.println("2. Comisiones e Inscripciones");
            System.out.println("3. Notas");
            System.out.println("0. Salir");
            int eleccion = leerOpcion("Seleccione una opcion: ");

            switch (eleccion) {
                case 1:
                    alumnoView.mostrarMenuAlumnos();
                    break;
                case 2:
                    inscripcionView.mostrarMenuInscripciones();
                    break;
                case 3:
                    notaView.mostrarMenuNotas();
                    break;
                case 0:
                    System.out.println("Hasta luego!");
                    opcion = 0;
                    break;
                default:
                    System.out.println("ERROR: opcion invalida");
            }
        } while (opcion != 0);
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
