package com.itec.service;

import com.itec.model.Profesor;

/**
 * Contrato del servicio de profesores (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en ProfesorServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface ProfesorService {

    /** Mensajes de resultado para la Vista. */
    String MSG_CREADO = "Profesor creado con exito";
    String MSG_DNI_DUPLICADO = "ERROR: DNI ya registrado";
    String MSG_MAIL_INVALIDO = "ERROR: mail invalido (debe contener '@')";
    String MSG_DATOS_OBLIGATORIOS = "ERROR: nombre, apellido y DNI son obligatorios";
    String MSG_NO_ENCONTRADO = "ERROR: no existe un profesor con ese DNI";
    String MSG_YA_INACTIVO = "ERROR: el profesor ya se encuentra inactivo";
    String MSG_DADO_DE_BAJA = "Profesor dado de baja (baja logica)";
    String MSG_MODIFICADO = "Profesor modificado con exito";

    /** Crea y guarda un profesor nuevo. Devuelve mensaje de exito o de error. */
    String crearProfesor(String nombre, String apellido, String dni, String mail, String telefono);

    /** Busca un profesor por DNI. Devuelve el Profesor, o null si no existe. */
    Profesor buscarProfesor(String dni);

    /** Devuelve todos los profesores registrados (para listados de la Vista). */
    java.util.List<Profesor> listarTodos();

    /** Da de baja logicamente a un profesor (activo = false). */
    String darDeBajaLogica(String dni);

    /**
     * Modifica los datos de un profesor identificado por su DNI. El DNI no es
     * editable (es la clave de negocio): solo cambian nombre, apellido, mail
     * y telefono, que son obligatorios.
     */
    String modificarProfesor(String dni, String nuevoNombre, String nuevoApellido,
                             String nuevoMail, String nuevoTelefono);
}
