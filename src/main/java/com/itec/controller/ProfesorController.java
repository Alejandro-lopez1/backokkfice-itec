package com.itec.controller;

import com.itec.model.Profesor;
import com.itec.repository.ProfesorRepository;

/**
 * Controlador de profesores: logica de negocio y validaciones.
 * <p>
 * No imprime en consola ni usa Scanner: devuelve resultados y objetos
 * para que la Vista los presente.
 */
public class ProfesorController {

    /** Mensajes de resultado para la Vista. */
    public static final String MSG_CREADO = "Profesor creado con exito";
    public static final String MSG_DNI_DUPLICADO = "ERROR: DNI ya registrado";
    public static final String MSG_MAIL_INVALIDO = "ERROR: mail invalido (debe contener '@')";
    public static final String MSG_DATOS_OBLIGATORIOS = "ERROR: nombre, apellido y DNI son obligatorios";
    public static final String MSG_NO_ENCONTRADO = "ERROR: no existe un profesor con ese DNI";
    public static final String MSG_YA_INACTIVO = "ERROR: el profesor ya se encuentra inactivo";
    public static final String MSG_DADO_DE_BAJA = "Profesor dado de baja (baja logica)";

    /**
     * Crea y guarda un profesor nuevo.
     *
     * @return mensaje de exito o de error para mostrar por consola.
     */
    public String crearProfesor(String nombre, String apellido, String dni, String mail, String telefono) {
        nombre = normalizar(nombre);
        apellido = normalizar(apellido);
        dni = normalizar(dni);
        mail = normalizar(mail);
        telefono = normalizar(telefono);

        if (nombre == null || apellido == null || dni == null) {
            return MSG_DATOS_OBLIGATORIOS;
        }
        if (ProfesorRepository.existeDni(dni)) {
            return MSG_DNI_DUPLICADO;
        }
        if (mail == null || !mail.contains("@")) {
            return MSG_MAIL_INVALIDO;
        }

        Profesor profesor = new Profesor(nombre, apellido, dni, mail, telefono, true);
        ProfesorRepository.guardar(profesor);
        return MSG_CREADO;
    }

    /**
     * Busca un profesor por DNI.
     *
     * @return el Profesor, o null si no existe.
     */
    public Profesor buscarProfesor(String dni) {
        if (normalizar(dni) == null) {
            return null;
        }
        return ProfesorRepository.buscarPorDni(normalizar(dni));
    }

    /**
     * Da de baja logicamente a un profesor (activo = false).
     * No borra el registro: solo lo desactiva.
     *
     * @return mensaje de exito o de error para mostrar por consola.
     */
    public String darDeBajaLogica(String dni) {
        Profesor profesor = buscarProfesor(dni);
        if (profesor == null) {
            return MSG_NO_ENCONTRADO;
        }
        if (!profesor.isActivo()) {
            return MSG_YA_INACTIVO;
        }
        profesor.setActivo(false);
        return MSG_DADO_DE_BAJA;
    }

    /** Quita espacios de los extremos; devuelve null si queda vacio. */
    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
