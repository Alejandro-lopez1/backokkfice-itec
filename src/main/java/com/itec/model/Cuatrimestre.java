package com.itec.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cuatrimestre del dominio academico.
 * <p>
 * Ademas de los atributos del UML, sostiene la composicion con las
 * comisiones que abarca (Cuatrimestre -> ComisionMateria).
 */
public class Cuatrimestre {

    private int anio;
    private int numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ComisionMateria> comisiones;

    public Cuatrimestre() {
        this.comisiones = new ArrayList<>();
    }

    public Cuatrimestre(int anio, int numero, LocalDate fechaInicio, LocalDate fechaFin) {
        this.anio = anio;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.comisiones = new ArrayList<>();
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

    /** Comisiones que abarca el cuatrimestre (composicion hacia ComisionMateria). */
    public List<ComisionMateria> getComisiones() {
        return comisiones;
    }

    public void setComisiones(List<ComisionMateria> comisiones) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.comisiones = comisiones != null ? new ArrayList<>(comisiones) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Cuatrimestre{"
                + "anio=" + anio
                + ", numero=" + numero
                + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + ", comisiones=" + comisiones.size()
                + '}';
    }
}
