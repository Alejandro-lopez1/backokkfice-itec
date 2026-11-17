package com.itec.model;

import java.time.LocalDate;

/**
 * Entidad Asistencia: registro de presencia de un alumno inscripto en una fecha.
 * <p>
 * Segun el UML, la linea con la ComisionMateria tiene el diamante en este
 * extremo: cada asistencia referencia a su comision (ademas de llegar a
 * ella via el alumno inscripto).
 */
public class Asistencia {

    private LocalDate fecha;
    private boolean presente;
    private AlumnoInscripto alumnoInscripto;
    private ComisionMateria comision;

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

    /** Comision donde se registro la asistencia (diamante en este extremo segun el UML). */
    public ComisionMateria getComision() {
        return comision;
    }

    public void setComision(ComisionMateria comision) {
        this.comision = comision;
    }

    @Override
    public String toString() {
        return "Asistencia{"
                + "fecha=" + fecha
                + ", presente=" + presente
                + ", alumnoInscripto=" + (alumnoInscripto != null && alumnoInscripto.getAlumno() != null
                    ? alumnoInscripto.getAlumno().getApellido() + ", " + alumnoInscripto.getAlumno().getNombre()
                    : "null")
                + ", comision=" + (comision != null && comision.getMateria() != null
                    ? comision.getMateria().getNombre()
                    : "null")
                + '}';
    }
}
