package com.itec.controller;

import com.itec.model.ComisionMateria;
import com.itec.service.ComisionService;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador delgado de comisiones e inscripciones (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * ComisionService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class ComisionController {

    private final ComisionService service;

    public ComisionController(ComisionService service) {
        this.service = service;
    }

    /** Delega en el Service la creacion validada de la comision. */
    public String crearComision(String nombreMateria, String dniProfesor,
                                int anio, int numero,
                                LocalDate fechaInicio, LocalDate fechaFin) {
        return service.crearComision(nombreMateria, dniProfesor, anio, numero, fechaInicio, fechaFin);
    }

    /** Delega en el Service el listado de comisiones. */
    public List<ComisionMateria> listarComisiones() {
        return service.listarComisiones();
    }

    /** Delega en el Service la busqueda por ID. */
    public ComisionMateria buscarComision(int idComision) {
        return service.buscarComision(idComision);
    }

    /** Delega en el Service la inscripcion del alumno. */
    public String inscribirAlumno(String dniAlumno, int idComision) {
        return service.inscribirAlumno(dniAlumno, idComision);
    }
}
