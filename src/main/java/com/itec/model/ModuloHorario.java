package com.itec.model;

import java.time.LocalTime;

/**
 * Entidad ModuloHorario del diagrama de clases UML: un modulo de clase
 * numerado, con su hora de inicio y de fin.
 * <p>
 * Estructura pura disponible para la Fase 2; el MVP no la usa todavia.
 */
public class ModuloHorario {

    private int numero;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public ModuloHorario() {
    }

    public ModuloHorario(int numero, LocalTime horaInicio, LocalTime horaFin) {
        this.numero = numero;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "ModuloHorario{"
                + "numero=" + numero
                + ", horaInicio=" + horaInicio
                + ", horaFin=" + horaFin
                + '}';
    }
}
