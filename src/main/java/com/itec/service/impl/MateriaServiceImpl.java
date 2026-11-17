package com.itec.service.impl;

import com.itec.service.MateriaService;

import com.itec.model.Materia;
import com.itec.repository.MateriaRepository;

/**
 * Implementacion del servicio de materias: logica de negocio y validaciones,
 * incluyendo la relacion reflexiva de materias correlativas.
 * <p>
 * Recibe el MateriaRepository por constructor (inyeccion de dependencias).
 * No imprime en consola ni usa Scanner: devuelve resultados y mensajes para
 * que la Vista los presente.
 */
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository repository;

    public MateriaServiceImpl(MateriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String crearMateria(String nombre, int anio, int cuatrimestre) {
        String nombreLimpio = normalizar(nombre);
        if (nombreLimpio == null) {
            return MSG_NOMBRE_OBLIGATORIO;
        }
        if (!datosValidos(anio, cuatrimestre)) {
            return MSG_DATOS_INVALIDOS;
        }
        if (repository.existeNombre(nombreLimpio)) {
            return MSG_DUPLICADA;
        }

        Materia materia = new Materia(nombreLimpio, anio, cuatrimestre, true, null);
        repository.guardar(materia);
        return MSG_CREADA;
    }

    @Override
    public Materia buscarMateria(String nombre) {
        if (normalizar(nombre) == null) {
            return null;
        }
        return repository.buscarPorNombre(normalizar(nombre));
    }

    @Override
    public java.util.List<Materia> listarTodas() {
        return repository.obtenerTodos();
    }

    @Override
    public String modificarMateria(String nombreOriginal, String nuevoNombre, int nuevoAnio, int nuevoCuatrimestre) {
        String originalLimpio = normalizar(nombreOriginal);
        if (originalLimpio == null) {
            return MSG_NO_ENCONTRADA;
        }
        Materia materia = repository.buscarPorNombre(originalLimpio);
        if (materia == null) {
            return MSG_NO_ENCONTRADA;
        }

        // Enter sin texto = dejar el nombre como esta.
        String nuevoLimpio = normalizar(nuevoNombre);
        if (nuevoLimpio == null) {
            nuevoLimpio = originalLimpio;
        }
        if (!datosValidos(nuevoAnio, nuevoCuatrimestre)) {
            return MSG_DATOS_INVALIDOS;
        }

        // Si cambia el nombre, el nuevo no puede chocar con OTRA materia
        // (la clave del repositorio es el nombre).
        boolean cambiaNombre = !nuevoLimpio.equalsIgnoreCase(originalLimpio);
        if (cambiaNombre && repository.existeNombre(nuevoLimpio)) {
            return MSG_DUPLICADA;
        }

        if (cambiaNombre) {
            repository.eliminar(originalLimpio);
            materia.setNombre(nuevoLimpio);
            repository.guardar(materia);
        }
        materia.setAnio(nuevoAnio);
        materia.setCuatrimestre(nuevoCuatrimestre);
        return MSG_MODIFICADA;
    }

    @Override
    public String darDeBajaLogica(String nombre) {
        Materia materia = buscarMateria(nombre);
        if (materia == null) {
            return MSG_NO_ENCONTRADA;
        }
        if (!materia.isActiva()) {
            return MSG_YA_INACTIVA;
        }
        materia.setActiva(false);
        return MSG_BAJA;
    }

    @Override
    public String agregarCorrelativa(String nombreMateria, String nombreCorrelativa) {
        Materia materia = buscarMateria(nombreMateria);
        if (materia == null) {
            return MSG_NO_ENCONTRADA;
        }

        Materia correlativa = buscarMateria(nombreCorrelativa);
        if (correlativa == null) {
            return MSG_NO_ENCONTRADA;
        }

        // Relacion reflexiva: una materia no puede ser correlativa de si misma.
        if (materia == correlativa
                || materia.getNombre().equalsIgnoreCase(correlativa.getNombre())) {
            return MSG_MISMA_MATERIA;
        }

        if (materia.getListaCorrelativas().contains(correlativa)) {
            return MSG_YA_CORRELATIVA;
        }

        materia.agregarCorrelativa(correlativa);
        return MSG_CORRELATIVA_AGREGADA;
    }

    /** Reglas basicas de la malla: anio >= 1 y cuatrimestre 1 o 2. */
    private boolean datosValidos(int anio, int cuatrimestre) {
        return anio >= 1 && (cuatrimestre == 1 || cuatrimestre == 2);
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
