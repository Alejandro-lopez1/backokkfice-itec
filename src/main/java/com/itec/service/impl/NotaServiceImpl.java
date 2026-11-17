package com.itec.service.impl;

import com.itec.service.NotaService;

import com.itec.model.AlumnoInscripto;
import com.itec.model.ComisionMateria;
import com.itec.model.EstadoCursada;
import com.itec.model.Examen;
import com.itec.model.Nota;
import com.itec.model.TipoEvaluacion;
import com.itec.repository.ComisionRepository;

import java.time.LocalDate;

/**
 * Implementacion del servicio de notas: carga examenes y calcula el estado
 * de cursada.
 * <p>
 * Recibe el ComisionRepository por constructor (inyeccion de dependencias) e
 * interactua con la interfaz. No imprime en consola ni usa Scanner: devuelve
 * resultados y mensajes para que la Vista los presente.
 */
public class NotaServiceImpl implements NotaService {

    /** Nota minima para aprobar la cursada. */
    private static final double NOTA_APROBACION = 6.0;

    private final ComisionRepository comisionRepository;

    public NotaServiceImpl(ComisionRepository comisionRepository) {
        this.comisionRepository = comisionRepository;
    }

    @Override
    public String cargarNota(String dniAlumno, int idComision, TipoEvaluacion tipo, double valor) {
        if (tipo == null) {
            return MSG_TIPO_INVALIDO;
        }
        if (valor < 0 || valor > 10) {
            return MSG_VALOR_INVALIDO;
        }

        ComisionMateria comision = comisionRepository.buscarPorId(idComision);
        if (comision == null) {
            return MSG_COMISION_INEXISTENTE;
        }

        AlumnoInscripto inscripto = buscarInscripto(comision, dniAlumno);
        if (inscripto == null) {
            return MSG_NO_INSCRIPTO;
        }

        Examen examen = new Examen(tipo, LocalDate.now(), comision);
        Nota nota = new Nota(valor, examen);
        inscripto.getNotas().add(nota);

        double promedio = inscripto.promedioNotas();
        String detalle = MSG_NOTA_CARGADA + " (promedio: " + String.format("%.2f", promedio) + "). ";
        if (promedio >= NOTA_APROBACION) {
            if (inscripto.getEstadoCursada() != EstadoCursada.APROBADO) {
                inscripto.setEstadoCursada(EstadoCursada.APROBADO);
                inscripto.setNotaFinal(promedio);
                return detalle + MSG_APROBADO;
            }
            return detalle + "El alumno ya se encontraba APROBADO";
        }
        return detalle + MSG_SIN_CAMBIO_ESTADO;
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
