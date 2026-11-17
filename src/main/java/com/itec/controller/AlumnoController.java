package com.itec.controller;

import com.itec.model.Alumno;
import com.itec.service.AlumnoService;

/**
 * Controlador delgado de alumnos (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * AlumnoService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class AlumnoController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_NO_ENCONTRADO = AlumnoService.MSG_NO_ENCONTRADO;

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    /** Delega en el Service la creacion validada del alumno. */
    public String crearAlumno(String nombre, String apellido, String dni, String mail, String telefono) {
        return service.crearAlumno(nombre, apellido, dni, mail, telefono);
    }

    /** Delega en el Service la busqueda por DNI. */
    public Alumno buscarAlumno(String dni) {
        return service.buscarAlumno(dni);
    }

    /** Delega en el Service el listado de todos los alumnos. */
    public java.util.List<Alumno> listarTodos() {
        return service.listarTodos();
    }

    /** Delega en el Service la baja logica. */
    public String darDeBajaLogica(String dni) {
        return service.darDeBajaLogica(dni);
    }

    /** Delega en el Service la modificacion de datos del alumno. */
    public String modificarAlumno(String dni, String nuevoNombre, String nuevoApellido,
                                  String nuevoMail, String nuevoTelefono) {
        return service.modificarAlumno(dni, nuevoNombre, nuevoApellido, nuevoMail, nuevoTelefono);
    }
}
