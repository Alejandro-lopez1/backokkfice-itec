# 05 — Casos de Prueba

> Suite de Casos de Prueba (TC) para las funcionalidades **implementadas**,
> organizada por módulo en el mismo orden que las US de
> `03_Epicas_y_User_Stories.md`. Los resultados esperados citan los mensajes
> **exactos** del código (constantes `MSG_*`); los pasos usan la numeración
> real de los menús de consola, listos para ejecutar en la demo.
>
> Datos de referencia sembrados por el sistema: usuarios admin / admin123
> (ADMIN), docente / docente123 (DOCENTE) y alumno / alumno123 (ALUMNO);
> alumnos Juan Perez `40123456` (activo) y Diego Torres `40567890`
> (INACTIVO); profesores Ana Rodriguez `20111222` y Luis Martinez `20222333`;
> comisiones ID 1 (Programacion I) e ID 2 (Base de Datos); materias
> Programacion I, II, Matematica Discreta, Base de Datos y Redes de
> Computadoras; carreras Tecnicatura en Programacion y Licenciatura en
> Sistemas (Plan 2024).
>
> Estado `Aprobado`: verificado manualmente contra la aplicación real durante
> el desarrollo. Ningún TC está en `Pendiente` porque la suite cubre solo
> funcionalidad implementada (lo de Fase 2 está fuera de alcance del doc).

---

## 1. TC-AUTH — Acceso (login real por roles)

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-AUTH-01 | Login exitoso como ADMIN | 1. Ejecutar `.\mvnw.cmd clean compile exec:java`. 2. Ingresar `admin` y `admin123`. | `Bienvenido, admin (ADMIN)`; el menú principal muestra los 7 módulos del backoffice + `0. Salir`. | Aprobado |
| TC-AUTH-02 | Login exitoso como DOCENTE | 1. Ejecutar el sistema. 2. Ingresar `docente` y `docente123`. | `Bienvenido, docente (DOCENTE)`; el menú muestra solo `1. Notas`, `2. Asistencias` y `0. Salir`. | Aprobado |
| TC-AUTH-03 | Login exitoso como ALUMNO | 1. Ejecutar el sistema. 2. Ingresar `alumno` y `alumno123`. | `Bienvenido, alumno (ALUMNO)`; el menú muestra solo `1. Consultar mis datos` (placeholder con aviso de funcionalidad en desarrollo) y `0. Salir`. | Aprobado |
| TC-AUTH-04 | Credenciales inválidas con reintento | 1. Ejecutar el sistema. 2. Ingresar `admin` y una contraseña errónea. 3. Reintentar con credenciales válidas. | En el fallo: `Credenciales invalidas` y `Intento 1 de 3`; al reintentar bien, la sesión se inicia con el menú del rol. | Aprobado |
| TC-AUTH-05 | Bloqueo por 3 intentos fallidos | 1. Ejecutar el sistema. 2. Ingresar credenciales inválidas 3 veces seguidas. | Tres veces `Credenciales invalidas` (con `Intento n de 3`); al agotar el 3° intento se muestra `Sesion bloqueada: se agotaron los intentos de acceso` y el programa termina, sin llegar al menú principal. | Aprobado |
| TC-AUTH-06 | Opción inválida no corta la sesión | 1. Loguearse como `admin`. 2. En el menú principal ingresar `9` y luego `abc`. | Dos veces `ERROR: opcion invalida`; el menú se reitera con la sesión activa. | Aprobado |
| TC-AUTH-07 | Cierre de sesión | 1. Loguearse con cualquier rol. 2. En el menú principal ingresar la opción Salir (`0`). | `Hasta luego!` y fin del programa (sesión cerrada). | Aprobado |

## 2. TC-ALU — Alumnos

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-ALU-01 | Alta de alumno válida | 1. Menú principal → `1. Gestion de Alumnos` → `1. Crear Alumno`. 2. Ingresar nombre, apellido, DNI único, mail con `@`, teléfono. | `Alumno creado con exito`; el alumno aparece ACTIVO en el listado (opción 4). | Aprobado |
| TC-ALU-02 | DNI duplicado | 1. Repetir el alta con el DNI `40123456` (sembrado). | `ERROR: DNI ya registrado`; no se crea un segundo alumno. | Aprobado |
| TC-ALU-03 | Mail inválido | 1. Alta con mail sin `@` (ej. `juanmail`). | `ERROR: mail invalido (debe contener '@')`. | Aprobado |
| TC-ALU-04 | Campos obligatorios vacíos | 1. Alta dejando vacío (solo Enter) el nombre, el apellido o el DNI. | `ERROR: nombre, apellido y DNI son obligatorios`. | Aprobado |
| TC-ALU-05 | Búsqueda por DNI existente | 1. Menú 1 → `2. Buscar Alumno` → DNI `40123456`. | Se muestran nombre, apellido, mail, teléfono y estado ACTIVO. | Aprobado |
| TC-ALU-06 | Búsqueda por DNI inexistente | 1. Menú 1 → 2 → DNI `99999999`. | `ERROR: no existe un alumno con ese DNI`. | Aprobado |
| TC-ALU-07 | Modificación de alumno válida | 1. Menú 1 → `5. Modificar Alumno` → DNI `40123456`. 2. Ingresar nuevo nombre, apellido, mail con `@` y teléfono. | `Alumno modificado con exito`. | Aprobado |
| TC-ALU-08 | Verificación post-modificación | 1. Menú 1 → 2 → DNI `40123456`. | Los datos modificados aparecen bajo el **mismo DNI** (la clave no cambió). | Aprobado |
| TC-ALU-09 | Modificación de alumno inexistente | 1. Menú 1 → 5 → DNI `99999999` + datos. | `ERROR: no existe un alumno con ese DNI`. | Aprobado |
| TC-ALU-10 | Modificación con mail inválido | 1. Menú 1 → 5 → DNI existente + mail sin `@`. | `ERROR: mail invalido (debe contener '@')`; los datos quedan sin cambios. | Aprobado |
| TC-ALU-11 | Baja lógica | 1. Menú 1 → `3. Dar de Baja` → DNI de un alumno ACTIVO. | `Alumno dado de baja (baja logica)`; en el listado figura INACTIVO. | Aprobado |
| TC-ALU-12 | Doble baja | 1. Repetir la baja sobre el mismo alumno. | `ERROR: el alumno ya se encuentra inactivo`. | Aprobado |
| TC-ALU-13 | Listado del padrón | 1. Menú 1 → `4. Listar todos los alumnos`. | Todos los alumnos con DNI, apellido y nombre, mail y estado (incluye a `40567890` INACTIVO). | Aprobado |

## 3. TC-PROF — Profesores

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-PROF-01 | Alta de profesor válida | 1. Menú 2 → `1. Crear Profesor` → datos nuevos válidos. | `Profesor creado con exito`. | Aprobado |
| TC-PROF-02 | DNI duplicado | 1. Alta con DNI `20111222` (sembrado). | `ERROR: DNI ya registrado`. | Aprobado |
| TC-PROF-03 | Mail inválido | 1. Alta con mail sin `@`. | `ERROR: mail invalido (debe contener '@')`. | Aprobado |
| TC-PROF-04 | Modificación de profesor válida | 1. Menú 2 → `5. Modificar Profesor` → DNI `20111222` + datos nuevos válidos. | `Profesor modificado con exito`; la búsqueda (opción 2) confirma los cambios bajo el mismo DNI. | Aprobado |
| TC-PROF-05 | Modificación de profesor inexistente | 1. Menú 2 → 5 → DNI `99999999` + datos. | `ERROR: no existe un profesor con ese DNI`. | Aprobado |
| TC-PROF-06 | Baja lógica | 1. Menú 2 → `3. Dar de Baja Profesor` → DNI activo. | `Profesor dado de baja (baja logica)`. | Aprobado |
| TC-PROF-07 | Doble baja | 1. Repetir la baja. | `ERROR: el profesor ya se encuentra inactivo`. | Aprobado |
| TC-PROF-08 | Listado del cuadro docente | 1. Menú 2 → `4. Listar todos los profesores`. | Los 5 profesores con DNI, apellido y nombre, mail y estado. | Aprobado |

## 4. TC-MAT — Materias y Correlativas

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-MAT-01 | Alta de materia válida | 1. Menú 3 → `1. Crear Materia` → nombre nuevo, año 1, cuatrimestre 1. | `Materia creada con exito`; ACTIVA en el listado (opción 7). | Aprobado |
| TC-MAT-02 | Nombre duplicado | 1. Alta con nombre `Programacion I`. | `ERROR: ya existe una materia con ese nombre`. | Aprobado |
| TC-MAT-03 | Cuatrimestre inválido | 1. Alta con cuatrimestre `3`. | `ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2`. | Aprobado |
| TC-MAT-04 | Búsqueda con correlativas | 1. Menú 3 → `2. Buscar Materia` → `Base de Datos`. | Datos de la materia y `Correlativas: Programacion I, Matematica Discreta`. | Aprobado |
| TC-MAT-05 | Listado de todas las materias | 1. Menú 3 → `7. Listar todas las materias`. | Las 5 materias con año, cuatrimestre, estado y correlativas. | Aprobado |
| TC-MAT-06 | Modificación con re-claveo | 1. Menú 3 → `3. Modificar Materia` → nombre original. 2. Ingresar nombre nuevo (único) y datos. | `Materia modificada con exito`; la materia se encuentra solo bajo el nombre nuevo (re-claveo). | Aprobado |
| TC-MAT-07 | Colisión de nombre al modificar | 1. Modificar una materia usando el nombre de otra existente. | `ERROR: ya existe una materia con ese nombre`; la original conserva su nombre. | Aprobado |
| TC-MAT-08 | Modificar manteniendo nombre | 1. Modificar dejando el nombre vacío (solo Enter) y cambiando año/cuatrimestre. | `Materia modificada con exito`; el nombre no cambia, sí año/cuatrimestre. | Aprobado |
| TC-MAT-09 | Baja lógica | 1. Menú 3 → `4. Dar de Baja Materia` → materia ACTIVA. | `Materia dada de baja (baja logica)`; INACTIVA en el listado. | Aprobado |
| TC-MAT-10 | Doble baja | 1. Repetir la baja. | `ERROR: la materia ya se encuentra inactiva`. | Aprobado |
| TC-MAT-11 | Correlativa válida | 1. Menú 3 → `5. Agregar Correlativa` → materia `Redes de Computadoras`, correlativa `Programacion II`. | `Correlativa agregada con exito`; visible al rebuscar (opción 2). | Aprobado |
| TC-MAT-12 | Correlativa de sí misma | 1. Agregar `Programacion I` como correlativa de `Programacion I`. | `ERROR: una materia no puede ser correlativa de si misma`. | Aprobado |
| TC-MAT-13 | Correlativa duplicada | 1. Repetir TC-MAT-11. | `ERROR: esa materia ya figura como correlativa`. | Aprobado |
| TC-MAT-14 | Materia o correlativa inexistentes | 1. Agregar correlativa con un nombre inexistente. | `ERROR: no existe una materia con ese nombre`. | Aprobado |

## 5. TC-COM — Comisiones e Inscripciones

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-COM-01 | Creación de comisión válida | 1. Menú 4 → `1. Crear Comision` → materia activa, profesor activo, año/número de cuatrimestre, fechas coherentes. | `Comision creada con exito (ID: n)`; visible en el listado (opción 2). | Aprobado |
| TC-COM-02 | Comisión duplicada | 1. Repetir TC-COM-01 con los mismos datos. | `ERROR: ya existe una comision igual`. | Aprobado |
| TC-COM-03 | Materia inactiva | 1. Crear comisión para una materia INACTIVA. | `ERROR: la materia esta inactiva`. | Aprobado |
| TC-COM-04 | Profesor inactivo | 1. Crear comisión con un profesor INACTIVO. | `ERROR: el profesor esta inactivo`. | Aprobado |
| TC-COM-05 | Fechas incoherentes | 1. Crear comisión con fecha de fin anterior a la de inicio. | `ERROR: fechas invalidas`. | Aprobado |
| TC-COM-06 | Listado de comisiones | 1. Menú 4 → `2. Listar Comisiones`. | Comisiones con ID, materia, cuatrimestre y profesor. | Aprobado |
| TC-COM-07 | Inscripción válida | 1. Menú 4 → `3. Inscribir Alumno` → DNI `40123456`, comisión ID 1. | `Alumno inscripto con exito (comision ID 1)`. | Aprobado |
| TC-COM-08 | Inscripción duplicada | 1. Repetir TC-COM-07. | `ERROR: el alumno ya esta inscripto en esa comision`. | Aprobado |
| TC-COM-09 | Inscripción de alumno inactivo | 1. Inscribir a `40567890` (Diego, INACTIVO). | `ERROR: el alumno esta inactivo`. | Aprobado |
| TC-COM-10 | Alumno o comisión inexistentes | 1. Inscribir DNI `99999999`. 2. Inscribir con ID de comisión inexistente. | `ERROR: no existe un alumno con ese DNI` y `ERROR: no existe una comision con ese ID`. | Aprobado |

## 6. TC-NOT — Notas

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-NOT-01 | Nota que promociona | 1. Menú 5 → `1. Cargar Nota` → inscripto de la comisión 1, tipo TP, valor `8`. | `Nota cargada con exito (promedio: 8,00). El alumno quedo APROBADO (promedio >= 6)`. | Aprobado |
| TC-NOT-02 | Recalculo sin degradar estado | 1. Cargar otra nota (PARCIAL, `6.5`) al mismo inscripto. | Nuevo promedio informado; `El alumno ya se encontraba APROBADO`. | Aprobado |
| TC-NOT-03 | Promedio menor a 6 | 1. A un inscripto sin APROBADO, cargar una nota que deje el promedio < 6. | `El alumno mantiene su estado actual (promedio < 6)`. | Aprobado |
| TC-NOT-04 | Valor fuera de rango | 1. Cargar valor `20`. | `ERROR: la nota debe estar entre 0 y 10`; no se crea el examen. | Aprobado |
| TC-NOT-05 | Alumno no inscripto | 1. Cargar nota a `40234567` (no inscripto) en comisión 1. | `ERROR: el alumno no esta inscripto en esa comision`. | Aprobado |
| TC-NOT-06 | Comisión inexistente | 1. Cargar nota con ID de comisión inexistente. | `ERROR: no existe una comision con ese ID`. | Aprobado |

## 7. TC-ASIS — Asistencias

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-ASIS-01 | Registro de PRESENTE | 1. Menú 6 → `1. Registrar Asistencia` → inscripto de comisión 1, responder `S`. | `Asistencia registrada con exito (fecha)`. | Aprobado |
| TC-ASIS-02 | Registro de AUSENTE | 1. Registrar otra asistencia respondiendo `N`. | Registro AUSENTE visible en el listado. | Aprobado |
| TC-ASIS-03 | Alumno no inscripto | 1. Intentar registrar asistencia a `40234567` (no inscripto). | `ERROR: el alumno no esta inscripto en esa comision`. | Aprobado |
| TC-ASIS-04 | Listado con registros | 1. Menú 6 → `2. Ver Asistencias de una Comision` → comisión 1. | Registros en formato `dd/MM/yyyy | PRESENTE/AUSENTE | alumno`. | Aprobado |
| TC-ASIS-05 | Listado sin registros | 1. Ver asistencias de una comisión sin registros. | Lista vacía, sin error. | Aprobado |

## 8. TC-CAR — Carreras y Planes

| ID | Descripción | Pasos | Resultado Esperado | Estado |
|---|---|---|---|---|
| TC-CAR-01 | Alta de carrera válida | 1. Menú 7 → `1. Crear Carrera` → nombre nuevo. | `Carrera creada con exito`. | Aprobado |
| TC-CAR-02 | Carrera duplicada | 1. Alta con `Tecnicatura en Programacion` (sembrada). | `ERROR: ya existe una carrera con ese nombre`. | Aprobado |
| TC-CAR-03 | Plan de estudio válido | 1. Menú 7 → `2. Crear Plan de Estudio para una Carrera` → carrera existente, plan nuevo, 2024–2026. | `Plan de estudio creado con exito`. | Aprobado |
| TC-CAR-04 | Rango de años inválido | 1. Crear plan con fin 2020 e inicio 2024. | `ERROR: el anio de fin no puede ser anterior al de inicio`. | Aprobado |
| TC-CAR-05 | Plan duplicado en la carrera | 1. Crear otro plan con el mismo nombre en la misma carrera. | `ERROR: ya existe un plan con ese nombre en la carrera`. | Aprobado |
| TC-CAR-06 | Asociación de materia a plan | 1. Menú 7 → `3. Asociar Materia a un Plan` → carrera, plan y materia existentes. | `Materia asociada al plan con exito`; visible en el listado (opción 4). | Aprobado |
| TC-CAR-07 | Asociación duplicada | 1. Repetir TC-CAR-06 con la misma materia. | `ERROR: esa materia ya esta asociada al plan`. | Aprobado |
| TC-CAR-08 | Listado jerárquico | 1. Menú 7 → `4. Listar Carreras y sus Planes`. | Jerarquía completa: Carrera → Plan (vigencia) → Materias (año, cuatrimestre, estado, correlativas). | Aprobado |

---

## Resumen

| Módulo | Total TC | Aprobados | Pendientes |
|---|---|---|---|
| AUTH (login real por roles) | 7 | 7 | 0 |
| ALU — Alumnos | 13 | 13 | 0 |
| PROF — Profesores | 8 | 8 | 0 |
| MAT — Materias y Correlativas | 14 | 14 | 0 |
| COM — Comisiones e Inscripciones | 10 | 10 | 0 |
| NOT — Notas | 6 | 6 | 0 |
| ASIS — Asistencias | 5 | 5 | 0 |
| CAR — Carreras y Planes | 8 | 8 | 0 |
| **Total** | **71** | **71** | **0** |
