package com.itec.model;

/**
 * Entidad Nota: valor obtenido por el alumno en un examen.
 */
public class Nota {

    private double valor;
    private Examen examen;

    public Nota() {
    }

    public Nota(double valor, Examen examen) {
        this.valor = valor;
        this.examen = examen;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    @Override
    public String toString() {
        return "Nota{"
                + "valor=" + valor
                + ", examen=" + (examen != null ? examen.getTipoEvaluacion() + " " + examen.getFecha() : "null")
                + '}';
    }
}
