package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad AlumnoCarrera del diagrama de clases UML: inscripcion de un
 * alumno en una carrera, con el anio de ingreso y el plan de estudio
 * bajo el que cursa (composiciones hacia Alumno, Carrera y PlanEstudio
 * segun el UML), mas las inscripciones a comisiones (rol "inscripciones").
 */
public class AlumnoCarrera {

    private Alumno alumno;
    private Carrera carrera;
    private PlanEstudio planEstudio;
    private int anioIngreso;
    private List<AlumnoInscripto> inscripciones;

    public AlumnoCarrera() {
        this.inscripciones = new ArrayList<>();
    }

    public AlumnoCarrera(Alumno alumno, Carrera carrera, int anioIngreso) {
        this.alumno = alumno;
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.inscripciones = new ArrayList<>();
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    /** Plan de estudio bajo el que el alumno cursa la carrera (composicion del UML). */
    public PlanEstudio getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    public int getAnioIngreso() {
        return anioIngreso;
    }

    public void setAnioIngreso(int anioIngreso) {
        this.anioIngreso = anioIngreso;
    }

    /** Inscripciones a comisiones del alumno en la carrera (rol "inscripciones"). */
    public List<AlumnoInscripto> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<AlumnoInscripto> inscripciones) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.inscripciones = inscripciones != null ? new ArrayList<>(inscripciones) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "AlumnoCarrera{"
                + "alumno=" + (alumno != null
                    ? alumno.getApellido() + ", " + alumno.getNombre()
                    : "null")
                + ", carrera=" + (carrera != null ? carrera.getNombre() : "null")
                + ", planEstudio=" + (planEstudio != null ? planEstudio.getNombre() : "null")
                + ", anioIngreso=" + anioIngreso
                + ", inscripciones=" + inscripciones.size()
                + '}';
    }
}
