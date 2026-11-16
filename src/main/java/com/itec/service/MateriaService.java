package com.itec.service;

import com.itec.model.Materia;

/**
 * Contrato del servicio de materias (patron Interface-Impl).
 * <p>
 * La logica de negocio vive en MateriaServiceImpl; los controladores y el
 * resto del sistema dependen solo de esta interfaz.
 */
public interface MateriaService {

    /** Mensajes de resultado para la Vista. */
    String MSG_CREADA = "Materia creada con exito";
    String MSG_NOMBRE_OBLIGATORIO = "ERROR: el nombre de la materia es obligatorio";
    String MSG_DATOS_INVALIDOS = "ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2";
    String MSG_DUPLICADA = "ERROR: ya existe una materia con ese nombre";
    String MSG_NO_ENCONTRADA = "ERROR: no existe una materia con ese nombre";
    String MSG_MODIFICADA = "Materia modificada con exito";
    String MSG_YA_INACTIVA = "ERROR: la materia ya se encuentra inactiva";
    String MSG_BAJA = "Materia dada de baja (baja logica)";
    String MSG_CORRELATIVA_AGREGADA = "Correlativa agregada con exito";
    String MSG_MISMA_MATERIA = "ERROR: una materia no puede ser correlativa de si misma";
    String MSG_YA_CORRELATIVA = "ERROR: esa materia ya figura como correlativa";

    /** Crea una materia nueva, activa por defecto. */
    String crearMateria(String nombre, int anio, int cuatrimestre);

    /** Busca una materia por nombre. Devuelve la Materia, o null si no existe. */
    Materia buscarMateria(String nombre);

    /** Devuelve todas las materias registradas (para listados de la Vista). */
    java.util.List<Materia> listarTodas();

    /** Modifica nombre, anio y cuatrimestre de una materia existente. */
    String modificarMateria(String nombreOriginal, String nuevoNombre, int nuevoAnio, int nuevoCuatrimestre);

    /** Da de baja logicamente a una materia (activa = false). */
    String darDeBajaLogica(String nombre);

    /** Agrega una correlativa a la lista de la materia indicada. */
    String agregarCorrelativa(String nombreMateria, String nombreCorrelativa);
}
