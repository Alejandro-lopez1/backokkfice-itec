package com.itec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Alumno del dominio academico.
 * <p>
 * Ademas de los atributos del UML, sostiene la asociacion con
 * AlumnoCarrera (inscripciones del alumno a carreras) y el flag activo
 * que exige la baja logica de las reglas de negocio.
 */
public class Alumno {

    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String telefono;
    private boolean activo;
    private List<AlumnoCarrera> carreras;

    public Alumno() {
        this.carreras = new ArrayList<>();
    }

    public Alumno(String nombre, String apellido, String dni, String mail, String telefono, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;
        this.activo = activo;
        this.carreras = new ArrayList<>();
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

    /** Inscripciones del alumno a carreras (asociacion con AlumnoCarrera). */
    public List<AlumnoCarrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<AlumnoCarrera> carreras) {
        // Copia defensiva: nunca guardar directo una lista recibida (puede ser inmutable).
        this.carreras = carreras != null ? new ArrayList<>(carreras) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Alumno{"
                + "nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", dni='" + dni + '\''
                + ", mail='" + mail + '\''
                + ", telefono='" + telefono + '\''
                + ", activo=" + activo
                + ", carreras=" + carreras.size()
                + '}';
    }
}
