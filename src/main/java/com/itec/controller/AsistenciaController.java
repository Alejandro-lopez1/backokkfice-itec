package com.itec.controller;

import com.itec.model.Asistencia;
import com.itec.service.AsistenciaService;

import java.util.List;

/**
 * Controlador delgado de asistencias (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * AsistenciaService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class AsistenciaController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_COMISION_INEXISTENTE = AsistenciaService.MSG_COMISION_INEXISTENTE;

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

    /** Delega en el Service el registro validado de la asistencia. */
    public String registrarAsistencia(String dniAlumno, int idComision, boolean presente) {
        return service.registrarAsistencia(dniAlumno, idComision, presente);
    }

    /** Delega en el Service el listado de asistencias por comision. */
    public List<Asistencia> obtenerAsistenciasPorComision(int idComision) {
        return service.obtenerAsistenciasPorComision(idComision);
    }
}
