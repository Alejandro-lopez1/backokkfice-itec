/**
 * Capa de Servicio: logica de negocio pura, entre el Controlador y el Repositorio.
 * <p>
 * Reglas de la capa:
 * - Patron Interface-Impl: cada servicio es una interfaz (ej. AlumnoService)
 *   y su implementacion concreta termina en Impl (ej. AlumnoServiceImpl).
 *   Los controladores dependen solo de la interfaz.
 * - Los Impl reciben sus repositorios por constructor (inyeccion de dependencias).
 * - Contiene las validaciones y reglas de negocio (unicidad, formato, estados).
 * - Devuelve resultados y mensajes; no imprime en consola ni usa Scanner.
 * - El Controlador queda "delgado": solo pasa datos de la Vista al Service
 *   y devuelve la respuesta.
 */
package com.itec.service;
