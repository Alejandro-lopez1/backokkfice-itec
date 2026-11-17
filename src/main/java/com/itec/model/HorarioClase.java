package com.itec.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad HorarioClase del diagrama de clases UML: el dictado de una
 * comision en un dia de la semana, compuesto por modulos horarios.
 * <p>
 * Estructura pura disponible para la Fase 2; el MVP no la usa todavia.
 * El dia se modela con el enum estandar {@link DayOfWeek} del JDK y la
 * lista de modulos se recibe por copia defensiva (patron RN-TRA-02).
 */
public class HorarioClase {

    private DayOfWeek diaSemana;
    private List<ModuloHorario> modulos;

    public HorarioClase() {
        this.modulos = new ArrayList<>();
    }

    public HorarioClase(DayOfWeek diaSemana, List<ModuloHorario> modulos) {
        this.diaSemana = diaSemana;
        this.modulos = new ArrayList<>(modulos);
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public List<ModuloHorario> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloHorario> modulos) {
        this.modulos = new ArrayList<>(modulos);
    }

    @Override
    public String toString() {
        return "HorarioClase{"
                + "diaSemana=" + diaSemana
                + ", modulos=" + modulos.size()
                + '}';
    }
}
