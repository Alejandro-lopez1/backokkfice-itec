package com.itec.service.impl;

import com.itec.service.AsistenciaService;

import com.itec.model.AlumnoInscripto;
import com.itec.model.Asistencia;
import com.itec.model.ComisionMateria;
import com.itec.repository.AsistenciaRepository;
import com.itec.repository.ComisionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del servicio de asistencias: logica de negocio y validaciones.
 * <p>
 * Recibe los repositorios por constructor (inyeccion de dependencias). Para
 * ubicar al AlumnoInscripto necesita el ComisionRepository (la lista de
 * inscriptos vive dentro de cada comision) y guarda en el
 * AsistenciaRepository. No imprime en consola ni usa Scanner.
 */
public class AsistenciaServiceImpl implements AsistenciaService {

    private final ComisionRepository comisionRepository;
    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaServiceImpl(ComisionRepository comisionRepository,
                                 AsistenciaRepository asistenciaRepository) {
        this.comisionRepository = comisionRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    @Override
    public String registrarAsistencia(String dniAlumno, int idComision, boolean presente) {
        ComisionMateria comision = comisionRepository.buscarPorId(idComision);
        if (comision == null) {
            return MSG_COMISION_INEXISTENTE;
        }

        AlumnoInscripto inscripto = buscarInscripto(comision, dniAlumno);
        if (inscripto == null) {
            return MSG_NO_INSCRIPTO;
        }

        Asistencia asistencia = new Asistencia(LocalDate.now(), presente, inscripto);
        asistenciaRepository.agregar(asistencia);
        return MSG_REGISTRADA + " (" + LocalDate.now() + ")";
    }

    @Override
    public List<Asistencia> obtenerAsistenciasPorComision(int idComision) {
        List<Asistencia> resultado = new ArrayList<>();
        for (Asistencia a : asistenciaRepository.obtenerTodas()) {
            if (a.getAlumnoInscripto() != null
                    && a.getAlumnoInscripto().getComision() != null
                    && a.getAlumnoInscripto().getComision().getId() == idComision) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    /** Busca el inscripto de esa comision cuyo alumno tenga ese DNI. */
    private AlumnoInscripto buscarInscripto(ComisionMateria comision, String dniAlumno) {
        String dniLimpio = dniAlumno == null ? "" : dniAlumno.trim();
        for (AlumnoInscripto i : comision.getAlumnosInscriptos()) {
            if (i.getAlumno() != null && dniLimpio.equals(i.getAlumno().getDni())) {
                return i;
            }
        }
        return null;
    }
}
