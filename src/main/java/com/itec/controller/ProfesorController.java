package com.itec.controller;

import com.itec.model.Profesor;
import com.itec.service.ProfesorService;

/**
 * Controlador delgado de profesores (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * ProfesorService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class ProfesorController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_NO_ENCONTRADO = ProfesorService.MSG_NO_ENCONTRADO;

    private final ProfesorService service;

    public ProfesorController(ProfesorService service) {
        this.service = service;
    }

    /** Delega en el Service la creacion validada del profesor. */
    public String crearProfesor(String nombre, String apellido, String dni, String mail, String telefono) {
        return service.crearProfesor(nombre, apellido, dni, mail, telefono);
    }

    /** Delega en el Service la busqueda por DNI. */
    public Profesor buscarProfesor(String dni) {
        return service.buscarProfesor(dni);
    }

    /** Delega en el Service la baja logica. */
    public String darDeBajaLogica(String dni) {
        return service.darDeBajaLogica(dni);
    }

    /** Delega en el Service el listado de todos los profesores. */
    public java.util.List<Profesor> listarTodos() {
        return service.listarTodos();
    }

    /** Delega en el Service la modificacion de datos del profesor. */
    public String modificarProfesor(String dni, String nuevoNombre, String nuevoApellido,
                                    String nuevoMail, String nuevoTelefono) {
        return service.modificarProfesor(dni, nuevoNombre, nuevoApellido, nuevoMail, nuevoTelefono);
    }
}
