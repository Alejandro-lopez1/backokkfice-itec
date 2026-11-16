package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Materia del dominio academico.
 */
public class Materia {

    private String nombre;
    private int anio;
    private int cuatrimestre;
    private boolean activa;
    private List<Materia> listaCorrelativas;

    public Materia() {
        this.listaCorrelativas = new ArrayList<>();
    }

    public Materia(String nombre, int anio, int cuatrimestre, boolean activa, List<Materia> listaCorrelativas) {
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.activa = activa;
        // Copia defensiva: la lista recibida (ej. List.of(), inmutable) nunca
        // se guarda directo, para que la entidad siempre tenga una lista mutable.
        this.listaCorrelativas = listaCorrelativas != null ? new ArrayList<>(listaCorrelativas) : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public List<Materia> getListaCorrelativas() {
        return listaCorrelativas;
    }

    public void setListaCorrelativas(List<Materia> listaCorrelativas) {
        this.listaCorrelativas = listaCorrelativas;
    }

    /**
     * Agrega una correlativa encapsulando el acceso a la lista interna.
     * Es la forma correcta de modificar la relacion desde fuera del modelo.
     */
    public void agregarCorrelativa(Materia correlativa) {
        this.listaCorrelativas.add(correlativa);
    }

    @Override
    public String toString() {
        return "Materia{"
                + "nombre='" + nombre + '\''
                + ", anio=" + anio
                + ", cuatrimestre=" + cuatrimestre
                + ", activa=" + activa
                + ", correlativas=" + listaCorrelativas.size()
                + '}';
    }
}
