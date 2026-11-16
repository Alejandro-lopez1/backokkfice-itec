package com.itec.service;

import com.itec.model.Asistencia;

import java.util.List;

/**
 * Contrato del servicio de asistencias (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en AsistenciaServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface AsistenciaService {

    /** Mensajes de resultado para la Vista. */
    String MSG_REGISTRADA = "Asistencia registrada con exito";
    String MSG_COMISION_INEXISTENTE = "ERROR: no existe una comision con ese ID";
    String MSG_NO_INSCRIPTO = "ERROR: el alumno no esta inscripto en esa comision";

    /**
     * Registra la asistencia (presente/ausente) de un alumno inscripto,
     * con la fecha del dia.
     */
    String registrarAsistencia(String dniAlumno, int idComision, boolean presente);

    /** Devuelve las asistencias registradas para una comision. */
    List<Asistencia> obtenerAsistenciasPorComision(int idComision);
}
