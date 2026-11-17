package com.itec.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Examen: evaluacion de tipo TP, parcial, recuperatorio o final,
 * tomada en una comision.
 * <p>
 * Ademas de los atributos del UML, sostiene la composicion con las notas
 * obtenidas en el examen (Examen -> Nota).
 */
public class Examen {

    private TipoEvaluacion tipoEvaluacion;
    private LocalDate fecha;
    private ComisionMateria comision;
    private List<Nota> notas;

    public Examen() {
        this.notas = new ArrayList<>();
    }

    public Examen(TipoEvaluacion tipoEvaluacion, LocalDate fecha, ComisionMateria comision) {
        this.tipoEvaluacion = tipoEvaluacion;
        this.fecha = fecha;
        this.comision = comision;
        this.notas = new ArrayList<>();
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

    /** Notas obtenidas en este examen (composicion hacia Nota). */
    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.notas = notas != null ? new ArrayList<>(notas) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Examen{"
                + "tipoEvaluacion=" + tipoEvaluacion
                + ", fecha=" + fecha
                + ", comision=" + (comision != null && comision.getMateria() != null
                    ? comision.getMateria().getNombre()
                    : "null")
                + ", notas=" + notas.size()
                + '}';
    }
}
