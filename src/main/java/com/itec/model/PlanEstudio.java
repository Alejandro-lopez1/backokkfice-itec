package com.itec.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad PlanEstudio del dominio academico.
 */
public class PlanEstudio {

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Materia> materias;

    public PlanEstudio() {
        this.materias = new ArrayList<>();
    }

    public PlanEstudio(String nombre, LocalDate fechaInicio, LocalDate fechaFin, List<Materia> materias) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.materias = materias != null ? new ArrayList<>(materias) : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return "PlanEstudio{"
                + "nombre='" + nombre + '\''
                + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + ", materias=" + materias.size()
                + '}';
    }
}
