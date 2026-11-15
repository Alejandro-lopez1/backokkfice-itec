package com.itec.service;

import com.itec.model.AlumnoInscripto;
import com.itec.model.ComisionMateria;
import com.itec.model.EstadoCursada;
import com.itec.model.Examen;
import com.itec.model.Nota;
import com.itec.model.TipoEvaluacion;
import com.itec.repository.ComisionRepository;

import java.time.LocalDate;

/**
 * Servicio de notas: carga examenes y calcula el estado de cursada.
 * <p>
 * Interactua directamente con el ComisionRepository. No imprime en consola
 * ni usa Scanner: devuelve resultados y mensajes para que la Vista los presente.
 */
public class NotaService {

    // --- Mensajes para la Vista ---
    public static final String MSG_NOTA_CARGADA = "Nota cargada con exito";
    public static final String MSG_TIPO_INVALIDO = "ERROR: tipo de evaluacion invalido";
    public static final String MSG_VALOR_INVALIDO = "ERROR: la nota debe estar entre 0 y 10";
    public static final String MSG_COMISION_INEXISTENTE = "ERROR: no existe una comision con ese ID";
    public static final String MSG_NO_INSCRIPTO = "ERROR: el alumno no esta inscripto en esa comision";
    public static final String MSG_APROBADO = "El alumno quedo APROBADO (promedio >= 6)";
    public static final String MSG_SIN_CAMBIO_ESTADO = "El alumno mantiene su estado actual (promedio < 6)";

    /** Nota minima para aprobar la cursada. */
    private static final double NOTA_APROBACION = 6.0;

    /**
     * Carga una nota para un alumno inscripto en una comision:
     * crea el Examen, crea la Nota y la agrega al historial del inscripto.
     * Si el promedio de sus notas alcanza 6 o mas, lo pone APROBADO
     * y guarda el promedio como nota final.
     *
     * @return mensaje de exito (incluye promedio) o de error.
     */
    public String cargarNota(String dniAlumno, int idComision, TipoEvaluacion tipo, double valor) {
        if (tipo == null) {
            return MSG_TIPO_INVALIDO;
        }
        if (valor < 0 || valor > 10) {
            return MSG_VALOR_INVALIDO;
        }

        ComisionMateria comision = ComisionRepository.buscarPorId(idComision);
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
