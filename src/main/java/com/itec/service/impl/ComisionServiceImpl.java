package com.itec.service.impl;

import com.itec.service.ComisionService;

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
 * Implementacion del servicio de comisiones e inscripciones: logica de
 * negocio y validaciones.
 * <p>
 * Recibe los repositorios por constructor (inyeccion de dependencias) e
 * interactua con las interfaces. No imprime en consola ni usa Scanner:
 * devuelve resultados y mensajes para que la Vista los presente.
 */
public class ComisionServiceImpl implements ComisionService {

    private final AlumnoRepository alumnoRepository;
    private final ProfesorRepository profesorRepository;
    private final MateriaRepository materiaRepository;
    private final ComisionRepository comisionRepository;

    public ComisionServiceImpl(AlumnoRepository alumnoRepository,
                               ProfesorRepository profesorRepository,
                               MateriaRepository materiaRepository,
                               ComisionRepository comisionRepository) {
        this.alumnoRepository = alumnoRepository;
        this.profesorRepository = profesorRepository;
        this.materiaRepository = materiaRepository;
        this.comisionRepository = comisionRepository;
    }

    @Override
    public String crearComision(String nombreMateria, String dniProfesor,
                                int anio, int numero,
                                LocalDate fechaInicio, LocalDate fechaFin) {
        Materia materia = materiaRepository.buscarPorNombre(nombreMateria == null ? "" : nombreMateria.trim());
        if (materia == null) {
            return MSG_MATERIA_INEXISTENTE;
        }
        if (!materia.isActiva()) {
            return MSG_MATERIA_INACTIVA;
        }

        Profesor profesor = profesorRepository.buscarPorDni(dniProfesor == null ? "" : dniProfesor.trim());
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

        if (comisionRepository.existe(comision)) {
            return MSG_COMISION_DUPLICADA;
        }

        comisionRepository.agregar(comision);
        return MSG_COMISION_CREADA + " (ID: " + comision.getId() + ")";
    }

    @Override
    public List<ComisionMateria> listarComisiones() {
        return comisionRepository.obtenerTodos();
    }

    @Override
    public ComisionMateria buscarComision(int idComision) {
        return comisionRepository.buscarPorId(idComision);
    }

    @Override
    public String inscribirAlumno(String dniAlumno, int idComision) {
        Alumno alumno = alumnoRepository.buscarPorDni(dniAlumno == null ? "" : dniAlumno.trim());
        if (alumno == null) {
            return MSG_ALUMNO_INEXISTENTE;
        }
        if (!alumno.isActivo()) {
            return MSG_ALUMNO_INACTIVO;
        }

        ComisionMateria comision = comisionRepository.buscarPorId(idComision);
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
