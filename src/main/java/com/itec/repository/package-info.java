/**
 * Capa de Repositorio: persistencia en memoria.
 * <p>
 * Reglas de la capa:
 * - Patron Interface-Impl: cada repositorio es una interfaz
 *   (ej. AlumnoRepository) y su implementacion concreta termina en Impl
 *   (ej. AlumnoRepositoryImpl). El resto del sistema depende solo de la interfaz.
 * - Las implementaciones usan HashMap o ArrayList para "simular" la base de
 *   datos; las colecciones son static para que los datos persistan durante
 *   toda la ejecucion compartiendose entre instancias.
 * - Expone operaciones CRUD. No conoce controladores, services ni vistas.
 */
package com.itec.repository;
