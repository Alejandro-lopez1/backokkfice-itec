package com.itec.model;

/**
 * Entidad AlumnoCarrera del diagrama de clases UML: inscripcion de un
 * alumno en una carrera, con el anio de ingreso.
 * <p>
 * Estructura pura disponible para la Fase 2; el MVP no la usa todavia.
 */
public class AlumnoCarrera {

    private Alumno alumno;
    private Carrera carrera;
    private int anioIngreso;

    public AlumnoCarrera() {
    }

    public AlumnoCarrera(Alumno alumno, Carrera carrera, int anioIngreso) {
        this.alumno = alumno;
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public int getAnioIngreso() {
        return anioIngreso;
    }

    public void setAnioIngreso(int anioIngreso) {
        this.anioIngreso = anioIngreso;
    }

    @Override
    public String toString() {
        return "AlumnoCarrera{"
                + "alumno=" + (alumno != null
                    ? alumno.getApellido() + ", " + alumno.getNombre()
                    : "null")
                + ", carrera=" + (carrera != null ? carrera.getNombre() : "null")
                + ", anioIngreso=" + anioIngreso
                + '}';
    }
}
