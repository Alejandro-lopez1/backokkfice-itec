# 01 — Alcance del Proyecto

> Documento formal de alcance del sistema **Backoffice Académico — ITEC N°1**.
> Describe qué incluye el MVP entregado, qué queda para la Fase 2, los actores
> y las restricciones tecnológicas bajo las que fue construido.

---

## 1. Introducción

El **Backoffice Académico** es un sistema interno de gestión para el instituto
ITEC N°1, desarrollado en Java puro por consola. Permite administrar el ciclo
académico completo desde la línea de comandos: padrón de personas (alumnos y
profesores), oferta académica (materias con correlativas, carreras y planes de
estudio), dictado (comisiones por cuatrimestre con inscripción de alumnos),
seguimiento (notas por evaluación con promoción automática a APROBADO, y
asistencias presente/ausente).

El sistema aplica una arquitectura **MVC en capas** con capa de Service y el
patrón **Interface-Impl** en las capas Service y Repository, sin frameworks ni
dependencias externas. Al arrancar, carga un set de datos de demostración
coherente (`DataSeeder`) para operar de inmediato.

---

## 2. Objetivo

**Problema que resuelve**: la operación académica del instituto se maneja sin
un sistema central — no hay control de unicidad (DNIs y nombres duplicados no
se detectan), no existe trazabilidad de correlatividades, y las reglas
académicas ("promedio ≥ 6 para aprobar") se calculan a mano en planillas sueltas.

**Se espera lograr**:

- Un registro **único y validado** de personas, materias y estructura académica.
- Reglas de negocio **centralizadas y aplicadas por el sistema**, no por el
  operador (unicidad, estados, correlativas, promoción).
- Trazabilidad del cursado: inscripción → notas → estado de cursada → asistencias.
- Una base arquitectónica extensible hacia la Fase 2 sin refactor del existente.

---

## 3. Alcance incluido (MVP implementado)

| Módulo | Funcionalidad implementada |
|---|---|
| **Alumnos** | Alta (DNI único, mail con `@`, campos obligatorios), búsqueda por DNI, modificación de datos (DNI no editable), baja lógica (sin doble baja), listado del padrón con estado |
| **Profesores** | Alta (mismas validaciones que alumnos), búsqueda por DNI, modificación, baja lógica, listado del cuadro docente |
| **Materias** | Alta (nombre único, año ≥ 1, cuatrimestre 1/2), búsqueda y listado con estado y correlativas, modificación **con re-claveo** al renombrar, baja lógica, **agregado de correlativas** (relación reflexiva: no puede ser correlativa de sí misma ni duplicarse) |
| **Comisiones e Inscripciones** | Alta de comisión (materia y profesor activos, fechas coherentes, ID autoincremental, anti-duplicados), listado, inscripción de alumnos (alumno activo, sin duplicados, estado inicial `REGULAR`) |
| **Notas** | Carga de nota 0–10 por tipo de evaluación (TP, PARCIAL, RECUPERATORIO, FINAL) sobre alumnos inscriptos; cálculo de promedio y **promoción automática a APROBADO** si es ≥ 6, con nota final guardada (sin degradar estados) |
| **Asistencias** | Registro binario PRESENTE/AUSENTE con fecha del día (solo inscriptos), listado por comisión |
| **Carreras y Planes de Estudio** | Alta de carreras (nombre único), planes con vigencia por rango de años, asociación de materias existentes al plan (sin duplicados), listado jerárquico Carrera → Plan → Materias |

Detalles de implementación por módulo: ver `Documentacion_Funcional.md`
(épicas, user stories, criterios de aceptación y casos de prueba).

---

## 4. Alcance excluido (Fase 2 — Anexo)

Requerimientos del Anexo **no implementados** en esta entrega, con la
priorización que los dejó fuera del MVP:

| Requerimiento | Motivo de exclusión del MVP |
|---|---|
| **Justificación de inasistencias** (estado `JUSTIFICADA`) | El MVP definió asistencia binaria (presente/ausente) para validar primero el ciclo mínimo de cursada |
| **Cálculo de regularidad (≥ 70%)** | Requiere un umbral de clases programadas y una política de conteo aún no definida por el instituto |
| **Recuperatorios con lógica propia** | Las evaluaciones ya tipifican RECUPERATORIO, pero la política diferenciada de cálculo (peso, reemplazo de parciales) quedó para Fase 2 |
| **Cierre de actas** | Requiere el concepto de "cierre" (estado final de comisión) y volcado formal del resultado |
| **Auditoría** | Registro de quién modificó qué y cuándo: transversal, se agregará como servicio sin alterar los existentes |
| **Reportes** (alumnos en riesgo, libro de actas) | Agregaciones estadísticas no esenciales para el ciclo básico del MVP |
| **Seguridad por roles (ADM/DOC)** | Autenticación y perfiles diferenciados no formaban parte del núcleo; hoy opera una sesión única de operador |

La arquitectura actual permite incorporar cada punto sin refactor: los
servicios nuevos se cablean en el `Main` y los existentes agregan reglas, sin
tocar vistas ni controladores.

---

## 5. Actores

Según los documentos originales del enunciado:

| Actor | Descripción | Interacción con el sistema |
|---|---|---|
| **ADM** (Administrativo / Secretaría) | Personal de secretaría académica | Mantiene el padrón de alumnos y profesores, la oferta académica (materias, carreras, planes) y las comisiones; gestiona inscripciones |
| **DOC** (Docente) | Profesor a cargo de una comisión | Registra notas por evaluación y asistencias de sus inscriptos; consulta el listado de su comisión |
| **ALU** (Alumno) | Estudiante del instituto | (Fase 2) Consultaría sus materias, notas y estado de regularidad. En el MVP su interacción es indirecta, operada por ADM |
| **SYS** (Sistema) | El propio Backoffice | Aplica automáticamente las reglas: unicidad de claves, validez de datos, correlativas, cálculo de promedio y promoción a APROBADO, fechas de asistencia |

> **Nota de alcance actual**: el MVP opera con una **sesión única de operador**
> que unifica los permisos de ADM y DOC (sin login). El desdoblamiento de
> roles con pantallas diferenciadas pertenece a la Fase 2.

---

## 6. Supuestos y restricciones

| Tipo | Enunciado | Implicancia |
|---|---|---|
| **Restricción** | Persistencia **en memoria** (`HashMap`/`ArrayList` estáticos en los repositorios) | Los datos viven solo durante la ejecución; cada arranque reinicia el set de demostración del `DataSeeder` |
| **Restricción** | Interfaz **100% por consola** (`System.out` + `Scanner` único vía `ConsoleInput`) | Sin GUI ni web; el flujo es por menús numerados |
| **Restricción** | **Java 17** (solo librería estándar) y **Maven** como build | Cero dependencias externas: sin Spring, Hibernate ni Lombok |
| **Supuesto** | Un solo operador por ejecución | No hay concurrencia ni multiusuario |
| **Supuesto** | El DNI es la clave de identidad de personas y el nombre la de materias/carreras | Los DNI y nombres no son editables como claves (la modificación conserva la identidad) |
| **Supuesto** | La baja de personas y materias es **lógica** | Los registros históricos (inscripciones, notas, asistencias) conservan sus referencias |
| **Supuesto** | Las fechas de asistencia y examen son "hoy" (`LocalDate.now()`) | No se permite cargar movimientos de fechas pasadas o futuras |

---

## 7. Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| **Java (JDK)** | 17 | Único lenguaje; solo librería estándar (`java.util`, `java.time`, `java.util.Scanner`) |
| **Maven** | 3.9 (con Maven Wrapper `mvnw`) | Compilación, empaquetado y ejecución (`exec:java`, `jar`); reproducible sin instalar Maven |
| **Git / GitHub** | — | Control de versiones con commits por funcionalidad y respaldo remoto |
| **Mermaid** | — | Diagramas de arquitectura embebidos en el `README.md` (renderizado nativo de GitHub) |
