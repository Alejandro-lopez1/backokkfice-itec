package com.itec.controller;

import com.itec.model.Alumno;
import com.itec.repository.AlumnoRepository;

/**
 * Controlador de alumnos: logica de negocio y validaciones.
 * <p>
 * No imprime en consola ni usa Scanner: devuelve resultados y objetos
 * para que la Vista los presente.
 */
public class AlumnoController {

    /** Mensajes de resultado para la Vista. */
    public static final String MSG_CREADO = "Alumno creado con exito";
    public static final String MSG_DNI_DUPLICADO = "ERROR: DNI ya registrado";
    public static final String MSG_MAIL_INVALIDO = "ERROR: mail invalido (debe contener '@')";
    public static final String MSG_DATOS_OBLIGATORIOS = "ERROR: nombre, apellido y DNI son obligatorios";
    public static final String MSG_NO_ENCONTRADO = "ERROR: no existe un alumno con ese DNI";
    public static final String MSG_YA_INACTIVO = "ERROR: el alumno ya se encuentra inactivo";
    public static final String MSG_DADO_DE_BAJA = "Alumno dado de baja (baja logica)";

    /**
     * Crea y guarda un alumno nuevo.
     *
     * @return mensaje de exito o de error para mostrar por consola.
     */
    public String crearAlumno(String nombre, String apellido, String dni, String mail, String telefono) {
        nombre = normalizar(nombre);
        apellido = normalizar(apellido);
        dni = normalizar(dni);
        mail = normalizar(mail);
        telefono = normalizar(telefono);

        if (nombre == null || apellido == null || dni == null) {
            return MSG_DATOS_OBLIGATORIOS;
        }
        if (AlumnoRepository.existeDni(dni)) {
            return MSG_DNI_DUPLICADO;
        }
        if (mail == null || !mail.contains("@")) {
            return MSG_MAIL_INVALIDO;
        }

        Alumno alumno = new Alumno(nombre, apellido, dni, mail, telefono, true);
        AlumnoRepository.guardar(alumno);
        return MSG_CREADO;
    }

    /**
     * Busca un alumno por DNI.
     *
     * @return el Alumno, o null si no existe.
     */
    public Alumno buscarAlumno(String dni) {
        if (normalizar(dni) == null) {
            return null;
        }
        return AlumnoRepository.buscarPorDni(normalizar(dni));
    }

    /**
     * Da de baja logicamente a un alumno (activo = false).
     * No borra el registro: solo lo desactiva.
     *
     * @return mensaje de exito o de error para mostrar por consola.
     */
    public String darDeBajaLogica(String dni) {
        Alumno alumno = buscarAlumno(dni);
        if (alumno == null) {
            return MSG_NO_ENCONTRADO;
        }
        if (!alumno.isActivo()) {
            return MSG_YA_INACTIVO;
        }
        alumno.setActivo(false);
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
