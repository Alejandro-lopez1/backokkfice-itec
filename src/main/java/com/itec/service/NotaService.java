package com.itec.service;

import com.itec.model.AlumnoInscripto;
import com.itec.model.ComisionMateria;
import com.itec.model.EstadoCursada;
import com.itec.model.Examen;
import com.itec.model.Nota;
import com.itec.model.TipoEvaluacion;

import java.util.List;

/**
 * Contrato del servicio de notas (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en NotaServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface NotaService {

    // --- Mensajes para la Vista ---
    String MSG_NOTA_CARGADA = "Nota cargada con exito";
    String MSG_TIPO_INVALIDO = "ERROR: tipo de evaluacion invalido";
    String MSG_VALOR_INVALIDO = "ERROR: la nota debe estar entre 0 y 10";
    String MSG_COMISION_INEXISTENTE = "ERROR: no existe una comision con ese ID";
    String MSG_NO_INSCRIPTO = "ERROR: el alumno no esta inscripto en esa comision";
    String MSG_APROBADO = "El alumno quedo APROBADO (promedio >= 6)";
    String MSG_SIN_CAMBIO_ESTADO = "El alumno mantiene su estado actual (promedio < 6)";

    /** Carga una nota para un alumno inscripto en una comision. */
    String cargarNota(String dniAlumno, int idComision, TipoEvaluacion tipo, double valor);

    /**
     * Busca las inscripciones del alumno (por DNI) en todas las comisiones,
     * con el historial de notas de cada una.
     * <p>
     * Decision documentada (US-NOT-03): si el DNI no corresponde a ningun
     * inscripto —incluido el caso de un alumno inexistente en el padron,
     * que nunca puede estar inscripto— retorna una lista vacia. La Vista
     * presenta el mismo mensaje en ambos casos ("El alumno no tiene notas
     * registradas"); no se inyecta el AlumnoRepository para esta consulta
     * para no ampliar el cableado del Main.
     */
    List<AlumnoInscripto> consultarNotasPorAlumno(String dni);
}
