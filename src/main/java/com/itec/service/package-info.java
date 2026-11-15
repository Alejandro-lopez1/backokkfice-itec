/**
 * Capa de Servicio: logica de negocio pura, entre el Controlador y el Repositorio.
 * <p>
 * Reglas de la capa:
 * - Contiene las validaciones y reglas de negocio (unicidad, formato, estados).
 * - Interactua directamente con los Repositorios.
 * - Devuelve resultados y mensajes; no imprime en consola ni usa Scanner.
 * - El Controlador queda "delgado": solo pasa datos de la Vista al Service
 *   y devuelve la respuesta.
 */
package com.itec.service;
