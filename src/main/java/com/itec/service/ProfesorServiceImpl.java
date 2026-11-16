package com.itec.service;

import com.itec.model.Profesor;
import com.itec.repository.ProfesorRepository;

/**
 * Implementacion del servicio de profesores: logica de negocio y validaciones.
 * <p>
 * Recibe el ProfesorRepository por constructor (inyeccion de dependencias) e
 * interactua con la interfaz del repositorio. No imprime en consola ni usa
 * Scanner: devuelve resultados y mensajes para que la Vista los presente.
 */
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository repository;

    public ProfesorServiceImpl(ProfesorRepository repository) {
        this.repository = repository;
    }

    @Override
    public String crearProfesor(String nombre, String apellido, String dni, String mail, String telefono) {
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

        Profesor profesor = new Profesor(nombre, apellido, dni, mail, telefono, true);
        repository.guardar(profesor);
        return MSG_CREADO;
    }

    @Override
    public Profesor buscarProfesor(String dni) {
        if (normalizar(dni) == null) {
            return null;
        }
        return repository.buscarPorDni(normalizar(dni));
    }

    @Override
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
