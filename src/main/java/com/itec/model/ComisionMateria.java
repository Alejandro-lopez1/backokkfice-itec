package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad ComisionMateria: una materia dictada en un cuatrimestre,
 * a cargo de un profesor, con los alumnos inscriptos en ella.
 * <p>
 * El id lo asigna el ComisionRepository al agregar la comision
 * (0 significa "sin id asignado todavia").
 */
public class ComisionMateria {

    private int id;
    private Materia materia;
    private Cuatrimestre cuatrimestre;
    private Profesor profesor;
    private List<AlumnoInscripto> alumnosInscriptos;
    private List<HorarioClase> horarios;
    private List<Examen> examenes;
    private List<Asistencia> asistencias;

    public ComisionMateria() {
        this.alumnosInscriptos = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.examenes = new ArrayList<>();
        this.asistencias = new ArrayList<>();
    }

    public ComisionMateria(Materia materia, Cuatrimestre cuatrimestre, Profesor profesor) {
        this.materia = materia;
        this.cuatrimestre = cuatrimestre;
        this.profesor = profesor;
        this.alumnosInscriptos = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.examenes = new ArrayList<>();
        this.asistencias = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Cuatrimestre getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Cuatrimestre cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<AlumnoInscripto> getAlumnosInscriptos() {
        return alumnosInscriptos;
    }

    public void setAlumnosInscriptos(List<AlumnoInscripto> alumnosInscriptos) {
        this.alumnosInscriptos = alumnosInscriptos;
    }

    public List<HorarioClase> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioClase> horarios) {
        this.horarios = horarios;
    }

    /** Examenes tomados en la comision (agregacion con rol "examenes"). */
    public List<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<Examen> examenes) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.examenes = examenes != null ? new ArrayList<>(examenes) : new ArrayList<>();
    }

    /** Asistencias registradas en la comision (agregacion; nombre inferido del UML). */
    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.asistencias = asistencias != null ? new ArrayList<>(asistencias) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ComisionMateria{"
                + "id=" + id
                + ", materia=" + (materia != null ? materia.getNombre() : "null")
                + ", cuatrimestre=" + cuatrimestre
                + ", profesor=" + (profesor != null
                    ? profesor.getApellido() + ", " + profesor.getNombre()
                    : "null")
                + ", inscriptos=" + alumnosInscriptos.size()
                + ", horarios=" + horarios.size()
                + ", examenes=" + examenes.size()
                + ", asistencias=" + asistencias.size()
                + '}';
    }

    /** Representacion corta para listados de consola. */
    public String aTextoCorto() {
        String nombreMateria = materia != null ? materia.getNombre() : "?";
        String nombreProfesor = profesor != null
                ? profesor.getApellido() + ", " + profesor.getNombre()
                : "sin profesor";
        String periodo = cuatrimestre != null ? cuatrimestre.getAnio() + "-C" + cuatrimestre.getNumero() : "?";
        return "ID " + id + " | " + nombreMateria + " | " + periodo + " | Prof: " + nombreProfesor
                + " | Inscriptos: " + alumnosInscriptos.size();
    }
}
