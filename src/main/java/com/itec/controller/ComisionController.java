package com.itec.controller;

import com.itec.model.Alumno;
import com.itec.model.AlumnoInscripto;
import com.itec.model.ComisionMateria;
import com.itec.model.Cuatrimestre;
import com.itec.model.EstadoCursada;
import com.itec.model.Materia;
import com.itec.model.Profesor;
import com.itec.repository.AlumnoRepository;
import com.itec.repository.ComisionRepository;
import com.itec.repository.MateriaRepository;
import com.itec.repository.ProfesorRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de comisiones e inscripciones: logica de negocio y validaciones.
 */
public class ComisionController {

    // --- Mensajes para la Vista ---
    public static final String MSG_COMISION_CREADA = "Comision creada con exito";
    public static final String MSG_MATERIA_INEXISTENTE = "ERROR: no existe una materia con ese nombre";
    public static final String MSG_MATERIA_INACTIVA = "ERROR: la materia esta inactiva";
    public static final String MSG_PROFESOR_INEXISTENTE = "ERROR: no existe un profesor con ese DNI";
    public static final String MSG_PROFESOR_INACTIVO = "ERROR: el profesor esta inactivo";
    public static final String MSG_FECHAS_INVALIDAS = "ERROR: fechas invalidas";
    public static final String MSG_COMISION_DUPLICADA = "ERROR: ya existe una comision igual";
    public static final String MSG_ALUMNO_INEXISTENTE = "ERROR: no existe un alumno con ese DNI";
    public static final String MSG_ALUMNO_INACTIVO = "ERROR: el alumno esta inactivo";
    public static final String MSG_COMISION_INEXISTENTE = "ERROR: no existe una comision con ese ID";
    public static final String MSG_YA_INSCRIPTO = "ERROR: el alumno ya esta inscripto en esa comision";
    public static final String MSG_INSCRIPTO = "Alumno inscripto con exito";

    /**
     * Crea una comision (materia + cuatrimestre + profesor).
     *
     * @return mensaje de exito (incluye el ID asignado) o de error.
     */
    public String crearComision(String nombreMateria, String dniProfesor,
                                int anio, int numero,
                                LocalDate fechaInicio, LocalDate fechaFin) {
        Materia materia = MateriaRepository.buscarPorNombre(nombreMateria == null ? "" : nombreMateria.trim());
        if (materia == null) {
            return MSG_MATERIA_INEXISTENTE;
        }
        if (!materia.isActiva()) {
            return MSG_MATERIA_INACTIVA;
        }

        Profesor profesor = ProfesorRepository.buscarPorDni(dniProfesor == null ? "" : dniProfesor.trim());
        if (profesor == null) {
            return MSG_PROFESOR_INEXISTENTE;
        }
        if (!profesor.isActivo()) {
            return MSG_PROFESOR_INACTIVO;
        }

        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            return MSG_FECHAS_INVALIDAS;
        }

        Cuatrimestre cuatrimestre = new Cuatrimestre(anio, numero, fechaInicio, fechaFin);
        ComisionMateria comision = new ComisionMateria(materia, cuatrimestre, profesor);

        if (ComisionRepository.existe(comision)) {
            return MSG_COMISION_DUPLICADA;
        }

        ComisionRepository.agregar(comision);
        return MSG_COMISION_CREADA + " (ID: " + comision.getId() + ")";
    }

    /** Devuelve todas las comisiones (solo lectura, para listados de la Vista). */
    public List<ComisionMateria> listarComisiones() {
        return ComisionRepository.obtenerTodos();
    }

    /** Busca una comision por id, o null si no existe. */
    public ComisionMateria buscarComision(int idComision) {
        return ComisionRepository.buscarPorId(idComision);
    }

    /**
     * Inscribe un alumno activo en una comision existente, con estado REGULAR.
     *
     * @return mensaje de exito o de error.
     */
    public String inscribirAlumno(String dniAlumno, int idComision) {
        Alumno alumno = AlumnoRepository.buscarPorDni(dniAlumno == null ? "" : dniAlumno.trim());
        if (alumno == null) {
            return MSG_ALUMNO_INEXISTENTE;
        }
        if (!alumno.isActivo()) {
            return MSG_ALUMNO_INACTIVO;
        }

        ComisionMateria comision = ComisionRepository.buscarPorId(idComision);
        if (comision == null) {
            return MSG_COMISION_INEXISTENTE;
        }

        if (yaInscripto(comision, alumno)) {
            return MSG_YA_INSCRIPTO;
        }

        AlumnoInscripto inscripto = new AlumnoInscripto(alumno, comision, EstadoCursada.REGULAR, 0.0);
        comision.getAlumnosInscriptos().add(inscripto);
        return MSG_INSCRIPTO + " (comision ID " + idComision + ")";
    }

    /** Indica si el alumno ya figura entre los inscriptos de la comision. */
    private boolean yaInscripto(ComisionMateria comision, Alumno alumno) {
        for (AlumnoInscripto i : comision.getAlumnosInscriptos()) {
            if (i.getAlumno() == alumno) {
                return true;
            }
        }
        return false;
    }
}
