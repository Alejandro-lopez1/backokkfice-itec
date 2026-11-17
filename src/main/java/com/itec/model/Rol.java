package com.itec.model;

/**
 * Roles de acceso al sistema (US-AUTH-01).
 * <p>
 * Determinan que opciones del menu principal ve cada usuario:
 * ADMIN todas, DOCENTE solo notas y asistencias, ALUMNO solo la
 * consulta de sus datos (placeholder).
 */
public enum Rol {
    ADMIN,
    DOCENTE,
    ALUMNO
}
