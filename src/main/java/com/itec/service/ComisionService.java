package com.itec.service;

import com.itec.model.ComisionMateria;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrato del servicio de comisiones e inscripciones (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en ComisionServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface ComisionService {

    // --- Mensajes para la Vista ---
    String MSG_COMISION_CREADA = "Comision creada con exito";
    String MSG_MATERIA_INEXISTENTE = "ERROR: no existe una materia con ese nombre";
    String MSG_MATERIA_INACTIVA = "ERROR: la materia esta inactiva";
    String MSG_PROFESOR_INEXISTENTE = "ERROR: no existe un profesor con ese DNI";
    String MSG_PROFESOR_INACTIVO = "ERROR: el profesor esta inactivo";
    String MSG_FECHAS_INVALIDAS = "ERROR: fechas invalidas";
    String MSG_COMISION_DUPLICADA = "ERROR: ya existe una comision igual";
    String MSG_ALUMNO_INEXISTENTE = "ERROR: no existe un alumno con ese DNI";
    String MSG_ALUMNO_INACTIVO = "ERROR: el alumno esta inactivo";
    String MSG_COMISION_INEXISTENTE = "ERROR: no existe una comision con ese ID";
    String MSG_YA_INSCRIPTO = "ERROR: el alumno ya esta inscripto en esa comision";
    String MSG_INSCRIPTO = "Alumno inscripto con exito";

    /** Crea una comision (materia + cuatrimestre + profesor). */
    String crearComision(String nombreMateria, String dniProfesor,
                         int anio, int numero,
                         LocalDate fechaInicio, LocalDate fechaFin);

    /** Devuelve todas las comisiones (solo lectura, para listados de la Vista). */
    List<ComisionMateria> listarComisiones();

    /** Busca una comision por id, o null si no existe. */
    ComisionMateria buscarComision(int idComision);

    /** Inscribe un alumno activo en una comision existente, con estado REGULAR. */
    String inscribirAlumno(String dniAlumno, int idComision);
}
