package com.itec.model;

import java.time.LocalDate;

/**
 * Entidad Cuatrimestre del dominio academico.
 */
public class Cuatrimestre {

    private int anio;
    private int numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Cuatrimestre() {
    }

    public Cuatrimestre(int anio, int numero, LocalDate fechaInicio, LocalDate fechaFin) {
        this.anio = anio;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    @Override
    public String toString() {
        return "Cuatrimestre{"
                + "anio=" + anio
                + ", numero=" + numero
                + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + '}';
    }
}
