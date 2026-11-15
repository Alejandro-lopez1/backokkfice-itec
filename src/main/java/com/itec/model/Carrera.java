package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Carrera del dominio academico.
 */
public class Carrera {

    private String nombre;
    private List<PlanEstudio> planes;

    public Carrera() {
        this.planes = new ArrayList<>();
    }

    public Carrera(String nombre, List<PlanEstudio> planes) {
        this.nombre = nombre;
        this.planes = planes != null ? planes : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PlanEstudio> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanEstudio> planes) {
        this.planes = planes;
    }

    @Override
    public String toString() {
        return "Carrera{"
                + "nombre='" + nombre + '\''
                + ", planes=" + planes.size()
                + '}';
    }
}
