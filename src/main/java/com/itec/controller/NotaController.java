package com.itec.controller;

import com.itec.model.AlumnoInscripto;
import com.itec.model.TipoEvaluacion;
import com.itec.service.NotaService;

import java.util.List;

/**
 * Controlador delgado de notas (patron MVC + Service).
 * <p>
 * No contiene logica de negocio: recibe los datos de la Vista, los pasa al
 * NotaService y devuelve la respuesta. La capa de presentacion sigue
 * acoplada solo al Controlador, nunca al Service ni al Repository.
 */
public class NotaController {

    /** Constantes reexportadas del Service para uso exclusivo de la Vista. */
    public static final String MSG_TIPO_INVALIDO = NotaService.MSG_TIPO_INVALIDO;

    private final NotaService service;

    public NotaController(NotaService service) {
        this.service = service;
    }

    /** Delega en el Service la carga validada de la nota. */
    public String cargarNota(String dniAlumno, int idComision, TipoEvaluacion tipo, double valor) {
        return service.cargarNota(dniAlumno, idComision, tipo, valor);
    }

    /** Delega en el Service la consulta de notas del alumno en todas sus comisiones. */
    public List<AlumnoInscripto> consultarNotasPorAlumno(String dniAlumno) {
        return service.consultarNotasPorAlumno(dniAlumno);
    }
}
