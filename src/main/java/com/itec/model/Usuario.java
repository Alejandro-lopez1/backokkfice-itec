package com.itec.model;

/**
 * Cuenta de acceso al sistema (US-AUTH-01).
 * <p>
 * Entidad simple de seguridad: username (clave de negocio, inmutable),
 * password y rol asociado.
 */
public class Usuario {

    private final String username;
    private String password;
    private Rol rol;

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
