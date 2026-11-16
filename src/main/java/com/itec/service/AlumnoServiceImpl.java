package com.itec.service;

import com.itec.model.Alumno;
import com.itec.repository.AlumnoRepository;

/**
 * Implementacion del servicio de alumnos: logica de negocio y validaciones.
 * <p>
 * Recibe el AlumnoRepository por constructor (inyeccion de dependencias) e
 * interactua con la interfaz del repositorio. No imprime en consola ni usa
 * Scanner: devuelve resultados y mensajes para que la Vista los presente.
 */
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository repository;

    public AlumnoServiceImpl(AlumnoRepository repository) {
        this.repository = repository;
    }

    @Override
    public String crearAlumno(String nombre, String apellido, String dni, String mail, String telefono) {
        nombre = normalizar(nombre);
        apellido = normalizar(apellido);
        dni = normalizar(dni);
        mail = normalizar(mail);
        telefono = normalizar(telefono);

        if (nombre == null || apellido == null || dni == null) {
            return MSG_DATOS_OBLIGATORIOS;
        }
        if (repository.existeDni(dni)) {
            return MSG_DNI_DUPLICADO;
        }
        if (mail == null || !mail.contains("@")) {
            return MSG_MAIL_INVALIDO;
        }

        Alumno alumno = new Alumno(nombre, apellido, dni, mail, telefono, true);
        repository.guardar(alumno);
        return MSG_CREADO;
    }

    @Override
    public Alumno buscarAlumno(String dni) {
        if (normalizar(dni) == null) {
            return null;
        }
        return repository.buscarPorDni(normalizar(dni));
    }

    @Override
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
