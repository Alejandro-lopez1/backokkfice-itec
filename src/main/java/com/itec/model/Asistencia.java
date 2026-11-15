package com.itec.model;

import java.time.LocalDate;

/**
 * Entidad Asistencia: registro de presencia de un alumno inscripto en una fecha.
 */
public class Asistencia {

    private LocalDate fecha;
    private boolean presente;
    private AlumnoInscripto alumnoInscripto;

    public Asistencia() {
    }

    public Asistencia(LocalDate fecha, boolean presente, AlumnoInscripto alumnoInscripto) {
        this.fecha = fecha;
        this.presente = presente;
        this.alumnoInscripto = alumnoInscripto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public AlumnoInscripto getAlumnoInscripto() {
        return alumnoInscripto;
    }

    public void setAlumnoInscripto(AlumnoInscripto alumnoInscripto) {
        this.alumnoInscripto = alumnoInscripto;
    }

    @Override
    public String toString() {
        return "Asistencia{"
                + "fecha=" + fecha
                + ", presente=" + presente
                + ", alumnoInscripto=" + (alumnoInscripto != null && alumnoInscripto.getAlumno() != null
                    ? alumnoInscripto.getAlumno().getApellido() + ", " + alumnoInscripto.getAlumno().getNombre()
                    : "null")
                + '}';
    }
}
