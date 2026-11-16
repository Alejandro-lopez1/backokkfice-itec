package com.itec.service;

import com.itec.model.Alumno;

/**
 * Contrato del servicio de alumnos (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en AlumnoServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface AlumnoService {

    /** Mensajes de resultado para la Vista. */
    String MSG_CREADO = "Alumno creado con exito";
    String MSG_DNI_DUPLICADO = "ERROR: DNI ya registrado";
    String MSG_MAIL_INVALIDO = "ERROR: mail invalido (debe contener '@')";
    String MSG_DATOS_OBLIGATORIOS = "ERROR: nombre, apellido y DNI son obligatorios";
    String MSG_NO_ENCONTRADO = "ERROR: no existe un alumno con ese DNI";
    String MSG_YA_INACTIVO = "ERROR: el alumno ya se encuentra inactivo";
    String MSG_DADO_DE_BAJA = "Alumno dado de baja (baja logica)";

    /** Crea y guarda un alumno nuevo. Devuelve mensaje de exito o de error. */
    String crearAlumno(String nombre, String apellido, String dni, String mail, String telefono);

    /** Busca un alumno por DNI. Devuelve el Alumno, o null si no existe. */
    Alumno buscarAlumno(String dni);

    /** Devuelve todos los alumnos registrados (para listados de la Vista). */
    java.util.List<Alumno> listarTodos();

    /** Da de baja logicamente a un alumno (activo = false). */
    String darDeBajaLogica(String dni);
}
