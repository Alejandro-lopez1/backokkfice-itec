package com.itec.model;

import java.time.LocalDate;

/**
 * Entidad Examen: evaluacion de tipo TP, parcial, recuperatorio o final,
 * tomada en una comision.
 */
public class Examen {

    private TipoEvaluacion tipoEvaluacion;
    private LocalDate fecha;
    private ComisionMateria comision;

    public Examen() {
    }

    public Examen(TipoEvaluacion tipoEvaluacion, LocalDate fecha, ComisionMateria comision) {
        this.tipoEvaluacion = tipoEvaluacion;
        this.fecha = fecha;
        this.comision = comision;
    }

    public TipoEvaluacion getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(TipoEvaluacion tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ComisionMateria getComision() {
        return comision;
    }

    public void setComision(ComisionMateria comision) {
        this.comision = comision;
    }

    @Override
    public String toString() {
        return "Examen{"
                + "tipoEvaluacion=" + tipoEvaluacion
                + ", fecha=" + fecha
                + ", comision=" + (comision != null && comision.getMateria() != null
                    ? comision.getMateria().getNombre()
                    : "null")
                + '}';
    }
}
