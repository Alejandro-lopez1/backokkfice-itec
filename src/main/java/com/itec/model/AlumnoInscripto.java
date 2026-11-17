package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad AlumnoInscripto: inscripcion de un alumno en una comision,
 * con su estado de cursada, nota final y las notas de sus examenes.
 * <p>
 * Ademas de los atributos del UML, sostiene la agregacion con las
 * asistencias del inscripto (rol "asistencias").
 */
public class AlumnoInscripto {

    private Alumno alumno;
    private ComisionMateria comision;
    private EstadoCursada estadoCursada;
    private double notaFinal;
    private List<Nota> notas;
    private List<Asistencia> asistencias;

    public AlumnoInscripto() {
        this.notas = new ArrayList<>();
        this.asistencias = new ArrayList<>();
    }

    public AlumnoInscripto(Alumno alumno, ComisionMateria comision, EstadoCursada estadoCursada, double notaFinal) {
        this.alumno = alumno;
        this.comision = comision;
        this.estadoCursada = estadoCursada;
        this.notaFinal = notaFinal;
        this.notas = new ArrayList<>();
        this.asistencias = new ArrayList<>();
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public ComisionMateria getComision() {
        return comision;
    }

    public void setComision(ComisionMateria comision) {
        this.comision = comision;
    }

    public EstadoCursada getEstadoCursada() {
        return estadoCursada;
    }

    public void setEstadoCursada(EstadoCursada estadoCursada) {
        this.estadoCursada = estadoCursada;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    /** Asistencias del inscripto (agregacion con rol "asistencias"). */
    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.asistencias = asistencias != null ? new ArrayList<>(asistencias) : new ArrayList<>();
    }

    /** Calcula el promedio de las notas cargadas (0 si no tiene notas). */
    public double promedioNotas() {
        if (notas.isEmpty()) {
            return 0.0;
        }
        double suma = 0.0;
        for (Nota nota : notas) {
            suma += nota.getValor();
        }
        return suma / notas.size();
    }

    @Override
    public String toString() {
        return "AlumnoInscripto{"
                + "alumno=" + (alumno != null ? alumno.getApellido() + ", " + alumno.getNombre() : "null")
                + ", comision=" + (comision != null && comision.getMateria() != null
                    ? comision.getMateria().getNombre()
                    : "null")
                + ", estadoCursada=" + estadoCursada
                + ", notaFinal=" + notaFinal
                + ", notas=" + notas.size()
                + ", asistencias=" + asistencias.size()
                + '}';
    }
}
