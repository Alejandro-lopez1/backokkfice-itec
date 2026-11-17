package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Profesor del dominio academico.
 * <p>
 * Ademas de los atributos del UML, sostiene la composicion con las
 * comisiones que dicta (Profesor -> ComisionMateria) y el flag activo
 * que exige la baja logica de las reglas de negocio.
 */
public class Profesor {

    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String telefono;
    private boolean activo;
    private List<ComisionMateria> comisiones;

    public Profesor() {
        this.comisiones = new ArrayList<>();
    }

    public Profesor(String nombre, String apellido, String dni, String mail, String telefono, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;
        this.activo = activo;
        this.comisiones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /** Comisiones que dicta el profesor (composicion hacia ComisionMateria). */
    public List<ComisionMateria> getComisiones() {
        return comisiones;
    }

    public void setComisiones(List<ComisionMateria> comisiones) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.comisiones = comisiones != null ? new ArrayList<>(comisiones) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Profesor{"
                + "nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", dni='" + dni + '\''
                + ", mail='" + mail + '\''
                + ", telefono='" + telefono + '\''
                + ", activo=" + activo
                + ", comisiones=" + comisiones.size()
                + '}';
    }
}
