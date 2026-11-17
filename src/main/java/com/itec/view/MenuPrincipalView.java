package com.itec.view;

import com.itec.model.Rol;

/**
 * Menu principal de la aplicacion por consola, filtrado por rol del
 * usuario logueado (US-AUTH-01).
 * <p>
 * Se repite hasta que el usuario elige salir. Delega a las vistas
 * de cada modulo (alumnos, profesores, materias, inscripciones, notas,
 * asistencias y carreras).
 * <p>
 * Alcance por rol:
 * <ul>
 *   <li>ADMIN: todos los modulos del backoffice.</li>
 *   <li>DOCENTE: solo Notas y Asistencias.</li>
 *   <li>ALUMNO: solo la consulta de sus datos (placeholder).</li>
 * </ul>
 */
public class MenuPrincipalView {

    private final Rol rol;
    private final AlumnoView alumnoView;
    private final ProfesorView profesorView;
    private final MateriaView materiaView;
    private final InscripcionView inscripcionView;
    private final NotaView notaView;
    private final AsistenciaView asistenciaView;
    private final CarreraView carreraView;

    public MenuPrincipalView(Rol rol, AlumnoView alumnoView, ProfesorView profesorView,
                             MateriaView materiaView, InscripcionView inscripcionView,
                             NotaView notaView, AsistenciaView asistenciaView,
                             CarreraView carreraView) {
        this.rol = rol;
        this.alumnoView = alumnoView;
        this.profesorView = profesorView;
        this.materiaView = materiaView;
        this.inscripcionView = inscripcionView;
        this.notaView = notaView;
        this.asistenciaView = asistenciaView;
        this.carreraView = carreraView;
    }

    /** Menu principal: se repite hasta que el usuario elige salir. */
    public void mostrar() {
        switch (rol) {
            case ADMIN:
                mostrarMenuAdmin();
                break;
            case DOCENTE:
                mostrarMenuDocente();
                break;
            case ALUMNO:
                mostrarMenuAlumno();
                break;
            default:
                System.out.println("ERROR: rol sin menu asociado");
        }
    }

    /** Menu completo del rol ADMIN. */
    private void mostrarMenuAdmin() {
        int opcion = 1;
        do {
            System.out.println();
            System.out.println("############################");
            System.out.println("#  BACKOFFICE ACADEMICO    #");
            System.out.println("#  Sesion: " + rol);
            System.out.println("############################");
            System.out.println("1. Gestion de Alumnos");
            System.out.println("2. Gestion de Profesores");
            System.out.println("3. Gestion de Materias");
            System.out.println("4. Comisiones e Inscripciones");
            System.out.println("5. Notas");
            System.out.println("6. Asistencias");
            System.out.println("7. Gestion de Carreras");
            System.out.println("0. Salir");
            int eleccion = leerOpcion("Seleccione una opcion: ");

            switch (eleccion) {
                case 1:
                    alumnoView.mostrarMenuAlumnos();
                    break;
                case 2:
                    profesorView.mostrarMenuProfesores();
                    break;
                case 3:
                    materiaView.mostrarMenuMaterias();
                    break;
                case 4:
                    inscripcionView.mostrarMenuInscripciones();
                    break;
                case 5:
                    notaView.mostrarMenuNotas();
                    break;
                case 6:
                    asistenciaView.mostrarMenuAsistencias();
                    break;
                case 7:
                    carreraView.mostrarMenuCarreras();
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

    /** Menu filtrado del rol DOCENTE: solo Notas y Asistencias. */
    private void mostrarMenuDocente() {
        int opcion = 1;
        do {
            System.out.println();
            System.out.println("############################");
            System.out.println("#  BACKOFFICE ACADEMICO    #");
            System.out.println("#  Sesion: " + rol);
            System.out.println("############################");
            System.out.println("1. Notas");
            System.out.println("2. Asistencias");
            System.out.println("0. Salir");
            int eleccion = leerOpcion("Seleccione una opcion: ");

            switch (eleccion) {
                case 1:
                    notaView.mostrarMenuNotas();
                    break;
                case 2:
                    asistenciaView.mostrarMenuAsistencias();
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

    /** Menu filtrado del rol ALUMNO: placeholder de consulta de datos. */
    private void mostrarMenuAlumno() {
        int opcion = 1;
        do {
            System.out.println();
            System.out.println("############################");
            System.out.println("#  BACKOFFICE ACADEMICO    #");
            System.out.println("#  Sesion: " + rol);
            System.out.println("############################");
            System.out.println("1. Consultar mis datos");
            System.out.println("0. Salir");
            int eleccion = leerOpcion("Seleccione una opcion: ");

            switch (eleccion) {
                case 1:
                    System.out.println("Funcionalidad en desarrollo: proximamente podra consultar sus datos academicos");
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
