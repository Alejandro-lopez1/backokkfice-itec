# 03 — Épicas y User Stories

> Catálogo de épicas y user stories del sistema, basado en el código actual
> (services, controllers y vistas) y en los requerimientos del proyecto. No se
> documenta ninguna funcionalidad que no esté implementada: donde una US del
> enunciado aún no tiene código, se declara explícitamente.
>
> **Nota**: los documentos originales (`Requerimientos.docx` y
> `Anexo Requerimientos.docx`) no están versionados en el repositorio; el
> detalle normativo de la Fase 2 remite a `01_Alcance.md §4` y a
> `02_Reglas_de_Negocio.md`.
>
> Reglas de negocio asociadas: ver IDs `RN-*` en `02_Reglas_de_Negocio.md`.

---

## EPIC-AUTH — Gestión de Acceso

**Descripción**: control de acceso por rol al sistema. En el MVP opera una
sesión única de operador con permisos ADM+DOC unificados (sin login real).

### US-AUTH-01 — Iniciar sesión

- **ID**: US-AUTH-01
- **Título**: Iniciar sesión (roles ADM, DOC, ALU)
- **Como**: ADM / DOC / ALU
- **Quiero**: autenticarme con mi rol para acceder solo a las funciones que me corresponden
- **Para**: proteger la información académica y diferenciar permisos por perfil
- **Prioridad**: Alta (definida en el enunciado) — **Estado: SIMULADA en el MVP**
- **Dependencias**: ninguna (es el punto de entrada)
- **Reglas de Negocio asociadas**: RN-TRA-05
- **Implementación actual**: no existe login. `Main` arranca directamente
  `MenuPrincipalView`, que asume una sesión única de operador (permisos ADM+DOC
  unificados). El desdoblamiento real por roles es Fase 2.

---

## EPIC-ALU — Gestión de Alumnos

**Descripción**: mantenimiento del padrón de alumnos (CRUD completo con baja
lógica y clave de negocio DNI).

### US-ALU-01 — Crear alumno

- **ID**: US-ALU-01
- **Título**: Crear alumno
- **Como**: ADM
- **Quiero**: registrar un alumno con nombre, apellido, DNI, mail y teléfono
- **Para**: habilitar su legajo y poder inscribirlo luego en comisiones
- **Prioridad**: Alta
- **Dependencias**: ninguna
- **Reglas de Negocio asociadas**: RN-ALU-01, RN-ALU-02, RN-ALU-03, RN-ALU-04
- **Implementación**: `AlumnoServiceImpl.crearAlumno` · `AlumnoView` (opción 1)

### US-ALU-02 — Consultar alumno por DNI

- **ID**: US-ALU-02
- **Título**: Consultar alumno por DNI
- **Como**: ADM
- **Quiero**: buscar un alumno por su DNI
- **Para**: verificar sus datos y estado puntualmente
- **Prioridad**: Alta
- **Dependencias**: US-ALU-01
- **Reglas de Negocio asociadas**: RN-ALU-01 (identificación por DNI)
- **Implementación**: `AlumnoServiceImpl.buscarAlumno` · `AlumnoView` (opción 2)

### US-ALU-03 — Modificar alumno

- **ID**: US-ALU-03
- **Título**: Modificar alumno
- **Como**: ADM
- **Quiero**: corregir nombre, apellido, mail y teléfono de un alumno
- **Para**: mantener el padrón actualizado sin perder su historial
- **Prioridad**: Media
- **Dependencias**: US-ALU-01
- **Reglas de Negocio asociadas**: RN-ALU-02, RN-ALU-03, RN-ALU-08 (DNI no editable)
- **Implementación**: `AlumnoServiceImpl.modificarAlumno` · `AlumnoView` (opción 5)

### US-ALU-04 — Baja lógica de alumno

- **ID**: US-ALU-04
- **Título**: Baja lógica de alumno
- **Como**: ADM
- **Quiero**: dar de baja un alumno (activo → inactivo)
- **Para**: depurar el padrón sin eliminar su historial académico
- **Prioridad**: Alta
- **Dependencias**: US-ALU-01
- **Reglas de Negocio asociadas**: RN-ALU-05, RN-ALU-06
- **Implementación**: `AlumnoServiceImpl.darDeBajaLogica` · `AlumnoView` (opción 3)

### US-ALU-05 — Listar todos los alumnos

- **ID**: US-ALU-05
- **Título**: Listar todos los alumnos
- **Como**: ADM
- **Quiero**: ver el padrón completo con DNI, nombre, mail y estado
- **Para**: disponer del padrón para reportes y atención
- **Prioridad**: Alta
- **Dependencias**: US-ALU-01
- **Reglas de Negocio asociadas**: RN-TRA-01 (listado sobre copia defensiva)
- **Implementación**: `AlumnoServiceImpl.listarTodos` · `AlumnoView` (opción 4)

---

## EPIC-PROF — Gestión de Profesores

**Descripción**: mantenimiento del cuadro docente y su vinculación al dictado
de materias a través de comisiones.

### US-PROF-01 — Crear profesor

- **ID**: US-PROF-01
- **Título**: Crear profesor
- **Como**: ADM
- **Quiero**: registrar un docente con nombre, apellido, DNI, mail y teléfono
- **Para**: contar con el cuadro docente para asignar comisiones
- **Prioridad**: Alta
- **Dependencias**: ninguna
- **Reglas de Negocio asociadas**: RN-PRO-01, RN-PRO-02, RN-PRO-03, RN-PRO-04
- **Implementación**: `ProfesorServiceImpl.crearProfesor` · `ProfesorView` (opción 1)

### US-PROF-02 — Modificar profesor

- **ID**: US-PROF-02
- **Título**: Modificar profesor
- **Como**: ADM
- **Quiero**: corregir los datos de un docente (DNI no editable)
- **Para**: mantener el registro actualizado sin perder asignaciones históricas
- **Prioridad**: Media
- **Dependencias**: US-PROF-01
- **Reglas de Negocio asociadas**: RN-PRO-02, RN-PRO-03, RN-PRO-08
- **Implementación**: `ProfesorServiceImpl.modificarProfesor` · `ProfesorView` (opción 5)

### US-PROF-03 — Baja lógica de profesor

- **ID**: US-PROF-03
- **Título**: Baja lógica de profesor
- **Como**: ADM
- **Quiero**: dar de baja un docente (activo → inactivo)
- **Para**: que no pueda asignarse a nuevas comisiones sin perder su historial
- **Prioridad**: Media
- **Dependencias**: US-PROF-01
- **Reglas de Negocio asociadas**: RN-PRO-05, RN-PRO-06, RN-PRO-07 (efecto en dictado)
- **Implementación**: `ProfesorServiceImpl.darDeBajaLogica` · `ProfesorView` (opción 3)

### US-PROF-04 — Asignar profesor a materia

- **ID**: US-PROF-04
- **Título**: Asignar profesor a materia (dictado)
- **Como**: ADM
- **Quiero**: asignar un docente activo al dictado de una materia
- **Para**: programar la enseñanza de la oferta académica
- **Prioridad**: Alta
- **Dependencias**: US-PROF-01, US-MAT-01, US-COM-01
- **Reglas de Negocio asociadas**: RN-COM-03, RN-COM-04 (existencia y estado activo del docente)
- **Estado**: IMPLEMENTADA **a través de la creación de comisión** — el
  sistema no tiene una operación aislada "asignar profesor": la asignación se
  materializa al crear la comisión (materia + cuatrimestre + profesor). Ver
  US-COM-01.

### US-PROF-05 — Listar todos los profesores

- **ID**: US-PROF-05
- **Título**: Listar todos los profesores
- **Como**: ADM
- **Quiero**: ver el cuadro docente completo con DNI, nombre, mail y estado
- **Para**: disponer del listado al planificar comisiones
- **Prioridad**: Media
- **Dependencias**: US-PROF-01
- **Reglas de Negocio asociadas**: RN-TRA-01
- **Implementación**: `ProfesorServiceImpl.listarTodos` · `ProfesorView` (opción 4)

---

## EPIC-MAT — Gestión de Materias y Correlativas

**Descripción**: oferta académica con clave de negocio nombre, relación
reflexiva de correlativas y re-claveo al renombrar.

### US-MAT-01 — Crear materia

- **ID**: US-MAT-01
- **Título**: Crear materia
- **Como**: ADM
- **Quiero**: registrar una materia con nombre, año y cuatrimestre
- **Para**: modelar la malla del plan de estudios
- **Prioridad**: Alta
- **Dependencias**: ninguna
- **Reglas de Negocio asociadas**: RN-MAT-01, RN-MAT-02, RN-MAT-03, RN-MAT-04
- **Implementación**: `MateriaServiceImpl.crearMateria` · `MateriaView` (opción 1)

### US-MAT-02 — Modificar materia

- **ID**: US-MAT-02
- **Título**: Modificar materia (con re-claveo)
- **Como**: ADM
- **Quiero**: cambiar nombre, año o cuatrimestre de una materia
- **Para**: corregir la malla sin perder correlativas ni referencias de planes
- **Prioridad**: Media
- **Dependencias**: US-MAT-01
- **Reglas de Negocio asociadas**: RN-MAT-02, RN-MAT-03, RN-MAT-11
- **Implementación**: `MateriaServiceImpl.modificarMateria` · `MateriaView` (opción 3)

### US-MAT-03 — Baja lógica de materia

- **ID**: US-MAT-03
- **Título**: Baja lógica de materia
- **Como**: ADM
- **Quiero**: desactivar una materia
- **Para**: retirarla de la oferta sin romper comisiones ni planes históricos
- **Prioridad**: Media
- **Dependencias**: US-MAT-01
- **Reglas de Negocio asociadas**: RN-MAT-05, RN-MAT-06, RN-MAT-07 (efecto en dictado)
- **Implementación**: `MateriaServiceImpl.darDeBajaLogica` · `MateriaView` (opción 4)

### US-MAT-04 — Definir correlativas

- **ID**: US-MAT-04
- **Título**: Definir correlativas
- **Como**: ADM
- **Quiero**: asociar materias existentes como correlativas de otra
- **Para**: modelar la secuencia académica exigida por la malla
- **Prioridad**: Alta
- **Dependencias**: US-MAT-01 (ambas materias)
- **Reglas de Negocio asociadas**: RN-MAT-08, RN-MAT-09, RN-MAT-10, RN-MAT-12
- **Implementación**: `MateriaServiceImpl.agregarCorrelativa` + `Materia.agregarCorrelativa` · `MateriaView` (opción 5)

### US-MAT-05 — Listar todas las materias

- **ID**: US-MAT-05
- **Título**: Listar todas las materias
- **Como**: ADM
- **Quiero**: ver el catálogo completo con año, cuatrimestre, estado y correlativas
- **Para**: disponer del catálogo al armar planes y comisiones
- **Prioridad**: Alta
- **Dependencias**: US-MAT-01, US-MAT-04
- **Reglas de Negocio asociadas**: RN-TRA-01
- **Implementación**: `MateriaServiceImpl.listarTodas` / `buscarMateria` · `MateriaView` (opciones 2 y 7)

---

## EPIC-COM — Gestión de Comisiones e Inscripciones

**Descripción**: programación del dictado por cuatrimestre e inscripción de
alumnos con estado inicial REGULAR.

### US-COM-01 — Crear comisión

- **ID**: US-COM-01
- **Título**: Crear comisión
- **Como**: ADM
- **Quiero**: abrir una comisión (materia activa + profesor activo + cuatrimestre con fechas)
- **Para**: programar el dictado de la oferta académica
- **Prioridad**: Alta
- **Dependencias**: US-MAT-01, US-PROF-01
- **Reglas de Negocio asociadas**: RN-COM-01 a RN-COM-07
- **Implementación**: `ComisionServiceImpl.crearComision` · `InscripcionView` (opción 1)

### US-COM-02 — Listar comisiones

- **ID**: US-COM-02
- **Título**: Listar comisiones
- **Como**: ADM
- **Quiero**: ver las comisiones con su ID, materia, cuatrimestre y profesor
- **Para**: localizar la comisión correcta al inscribir o registrar notas/asistencias
- **Prioridad**: Alta
- **Dependencias**: US-COM-01
- **Reglas de Negocio asociadas**: RN-COM-07 (identificación por ID)
- **Implementación**: `ComisionServiceImpl.listarComisiones` · `InscripcionView` (opción 2; también listado de ayuda en NotaView y AsistenciaView)

### US-COM-03 — Inscribir alumno a comisión

- **ID**: US-COM-03
- **Título**: Inscribir alumno a comisión
- **Como**: ADM
- **Quiero**: inscribir un alumno activo en una comisión existente
- **Para**: habilitar su cursada con estado REGULAR
- **Prioridad**: Alta
- **Dependencias**: US-ALU-01, US-COM-01, US-COM-02
- **Reglas de Negocio asociadas**: RN-COM-08 a RN-COM-12, RN-ALU-07
- **Implementación**: `ComisionServiceImpl.inscribirAlumno` · `InscripcionView` (opción 3)

---

## EPIC-NOT — Gestión de Notas

**Descripción**: registro de evaluaciones por inscripto con promoción
automática a APROBADO.

### US-NOT-01 — Cargar nota de examen

- **ID**: US-NOT-01
- **Título**: Cargar nota de examen
- **Como**: DOC (operada por ADM en el MVP)
- **Quiero**: registrar la nota de un examen (TP, PARCIAL, RECUPERATORIO o FINAL) para un inscripto
- **Para**: llevar el historial de evaluación del alumno en la comisión
- **Prioridad**: Alta
- **Dependencias**: US-COM-03
- **Reglas de Negocio asociadas**: RN-NOT-01 a RN-NOT-04
- **Implementación**: `NotaServiceImpl.cargarNota` · `NotaView` (opción 1)

### US-NOT-02 — Calcular promedio y estado

- **ID**: US-NOT-02
- **Título**: Calcular promedio y estado de cursada
- **Como**: SYS (comportamiento automático del sistema)
- **Quiero**: que el sistema calcule el promedio de las notas del inscripto y determine su estado
- **Para**: que la promoción a APROBADO (promedio ≥ 6) sea aplicada por el sistema, no a mano
- **Prioridad**: Alta
- **Dependencias**: US-NOT-01
- **Reglas de Negocio asociadas**: RN-NOT-05 a RN-NOT-08
- **Estado**: IMPLEMENTADA como **comportamiento automático dentro de
  US-NOT-01** (no es una operación independiente): al cargar cada nota el
  service recalcula el promedio y actualiza el estado.

### US-NOT-03 — Consultar notas por alumno

- **ID**: US-NOT-03
- **Título**: Consultar notas por alumno
- **Como**: DOC / ADM
- **Quiero**: ver las notas cargadas de un alumno en una comisión
- **Para**: revisar su desempeño antes de cerrar la cursada
- **Prioridad**: Media
- **Dependencias**: US-NOT-01
- **Reglas de Negocio asociadas**: RN-NOT-04 (historial en `AlumnoInscripto`)
- **Estado**: **NO IMPLEMENTADA** — `NotaView` solo expone la carga de notas;
  la consulta puntual queda para Fase 2 (el dato ya existe en el modelo).

---

## EPIC-ASIS — Gestión de Asistencia

**Descripción**: control de cursada con registro binario por fecha del día.

### US-ASIS-01 — Registrar asistencia

- **ID**: US-ASIS-01
- **Título**: Registrar asistencia
- **Como**: DOC (operada por ADM en el MVP)
- **Quiero**: registrar presente o ausente de un inscripto
- **Para**: controlar la asistencia a la cursada
- **Prioridad**: Alta
- **Dependencias**: US-COM-03
- **Reglas de Negocio asociadas**: RN-ASI-01, RN-ASI-02, RN-ASI-03, RN-COM-08
- **Implementación**: `AsistenciaServiceImpl.registrarAsistencia` · `AsistenciaView` (opción 1)

### US-ASIS-02 — Consultar asistencias por comisión

- **ID**: US-ASIS-02
- **Título**: Consultar asistencias por comisión
- **Como**: DOC / ADM
- **Quiero**: listar las asistencias registradas de una comisión
- **Para**: revisar el control de cursada del grupo
- **Prioridad**: Media
- **Dependencias**: US-ASIS-01, US-COM-02
- **Reglas de Negocio asociadas**: RN-ASI-04
- **Implementación**: `AsistenciaServiceImpl.obtenerAsistenciasPorComision` · `AsistenciaView` (opción 2)

---

## EPIC-CAR — Gestión de Carreras y Planes de Estudio

**Descripción**: estructura académica jerárquica Carrera → Plan → Materias.

### US-CAR-01 — Crear carrera

- **ID**: US-CAR-01
- **Título**: Crear carrera
- **Como**: ADM
- **Quiero**: registrar una carrera con nombre único
- **Para**: ordenar la oferta académica del instituto
- **Prioridad**: Alta
- **Dependencias**: ninguna
- **Reglas de Negocio asociadas**: RN-CAR-01, RN-CAR-02
- **Implementación**: `CarreraServiceImpl.crearCarrera` · `CarreraView` (opción 1)

### US-CAR-02 — Crear plan de estudio

- **ID**: US-CAR-02
- **Título**: Crear plan de estudio
- **Como**: ADM
- **Quiero**: definir un plan con vigencia por rango de años dentro de una carrera
- **Para**: versionar la malla vigente
- **Prioridad**: Alta
- **Dependencias**: US-CAR-01
- **Reglas de Negocio asociadas**: RN-CAR-03, RN-CAR-04, RN-CAR-05, RN-CAR-06
- **Implementación**: `CarreraServiceImpl.crearPlanEstudio` · `CarreraView` (opción 2)

### US-CAR-03 — Asociar materia a plan

- **ID**: US-CAR-03
- **Título**: Asociar materia a plan
- **Como**: ADM
- **Quiero**: incorporar materias existentes al plan de estudio
- **Para**: componer la malla que se dicta por carrera
- **Prioridad**: Alta
- **Dependencias**: US-CAR-02, US-MAT-01
- **Reglas de Negocio asociadas**: RN-CAR-07, RN-CAR-08, RN-CAR-09, RN-CAR-10
- **Implementación**: `CarreraServiceImpl.asociarMateriaAPlan` · `CarreraView` (opción 3)

### US-CAR-04 — Listar carreras con planes y materias

- **ID**: US-CAR-04
- **Título**: Listar carreras con planes y materias
- **Como**: ADM
- **Quiero**: ver la jerarquía completa Carrera → Plan → Materias
- **Para**: visualizar la estructura académica vigente
- **Prioridad**: Media
- **Dependencias**: US-CAR-01, US-CAR-02, US-CAR-03
- **Reglas de Negocio asociadas**: RN-CAR-10 (instancia compartida)
- **Implementación**: `CarreraServiceImpl.listarCarreras` · `CarreraView` (opción 4)

---

## Anexo: Funcionalidades No Implementadas (Fase 2 — Roadmap)

Las siguientes US del Anexo quedan **fuera del MVP** como roadmap. El detalle
normativo está en `01_Alcance.md §4`; ninguna tiene código implementado hoy.

| ID | Módulo (Anexo) | Estado |
|---|---|---|
| US-ASIS-03 | Justificación de inasistencias (tercer estado en el dominio de asistencia) | ❌ No implementada — roadmap Fase 2 |
| US-ASIS-04 | Cálculo de porcentaje de asistencia por inscripto (insumo de regularidad) | ❌ No implementada — roadmap Fase 2 |
| US-ASIS-05 | Consulta de asistencias por alumno (además de por comisión) | ❌ No implementada — roadmap Fase 2 |
| US-CAL-03 | Cálculo de regularidad (≥ 70% de asistencia por comisión) | ❌ No implementada — roadmap Fase 2 |
| US-CAL-04 | Recuperatorios con lógica de cálculo propia (pesos y reemplazos por tipo de evaluación) | ❌ No implementada — roadmap Fase 2 |
| US-CAL-05 | Cierre de acta de cursada por comisión | ❌ No implementada — roadmap Fase 2 |
| US-CAL-06 | Reportes del Anexo (alumnos en riesgo, libro de actas) | ❌ No implementada — roadmap Fase 2 |

> **Aclaración**: los títulos de las US-CAL son la lectura de los requerimientos
> del Anexo según `01_Alcance.md §4`. Los documentos `.docx` originales no
> están en el repositorio; si se requiere la cita literal del enunciado,
> incorporarlos a `document/` y se ajustará esta tabla.

---

## Matriz de Trazabilidad

| Épica | User Story | ¿Implementada? | Módulo del código |
|---|---|---|---|
| AUTH | US-AUTH-01 Iniciar sesión | Simulada (sesión única, sin login) | `Main.main` · `view/MenuPrincipalView` |
| ALU | US-ALU-01 Crear alumno | Sí | `service/impl/AlumnoServiceImpl.crearAlumno` |
| ALU | US-ALU-02 Consultar alumno por DNI | Sí | `service/impl/AlumnoServiceImpl.buscarAlumno` |
| ALU | US-ALU-03 Modificar alumno | Sí | `service/impl/AlumnoServiceImpl.modificarAlumno` |
| ALU | US-ALU-04 Baja lógica alumno | Sí | `service/impl/AlumnoServiceImpl.darDeBajaLogica` |
| ALU | US-ALU-05 Listar todos los alumnos | Sí | `service/impl/AlumnoServiceImpl.listarTodos` |
| PROF | US-PROF-01 Crear profesor | Sí | `service/impl/ProfesorServiceImpl.crearProfesor` |
| PROF | US-PROF-02 Modificar profesor | Sí | `service/impl/ProfesorServiceImpl.modificarProfesor` |
| PROF | US-PROF-03 Baja lógica profesor | Sí | `service/impl/ProfesorServiceImpl.darDeBajaLogica` |
| PROF | US-PROF-04 Asignar profesor a materia | Sí (vía US-COM-01) | `service/impl/ComisionServiceImpl.crearComision` |
| PROF | US-PROF-05 Listar todos los profesores | Sí | `service/impl/ProfesorServiceImpl.listarTodos` |
| MAT | US-MAT-01 Crear materia | Sí | `service/impl/MateriaServiceImpl.crearMateria` |
| MAT | US-MAT-02 Modificar materia | Sí | `service/impl/MateriaServiceImpl.modificarMateria` |
| MAT | US-MAT-03 Baja lógica materia | Sí | `service/impl/MateriaServiceImpl.darDeBajaLogica` |
| MAT | US-MAT-04 Definir correlativas | Sí | `service/impl/MateriaServiceImpl.agregarCorrelativa` + `model/Materia.agregarCorrelativa` |
| MAT | US-MAT-05 Listar todas las materias | Sí | `service/impl/MateriaServiceImpl.listarTodas` |
| COM | US-COM-01 Crear comisión | Sí | `service/impl/ComisionServiceImpl.crearComision` + `repository/impl/ComisionRepositoryImpl.agregar` |
| COM | US-COM-02 Listar comisiones | Sí | `service/impl/ComisionServiceImpl.listarComisiones` |
| COM | US-COM-03 Inscribir alumno a comisión | Sí | `service/impl/ComisionServiceImpl.inscribirAlumno` |
| NOT | US-NOT-01 Cargar nota de examen | Sí | `service/impl/NotaServiceImpl.cargarNota` |
| NOT | US-NOT-02 Calcular promedio y estado | Sí (automática, dentro de US-NOT-01) | `service/impl/NotaServiceImpl.cargarNota` + `model/AlumnoInscripto.promedioNotas` |
| NOT | US-NOT-03 Consultar notas por alumno | **No** (Fase 2) | — |
| ASIS | US-ASIS-01 Registrar asistencia | Sí | `service/impl/AsistenciaServiceImpl.registrarAsistencia` |
| ASIS | US-ASIS-02 Consultar asistencias por comisión | Sí | `service/impl/AsistenciaServiceImpl.obtenerAsistenciasPorComision` |
| CAR | US-CAR-01 Crear carrera | Sí | `service/impl/CarreraServiceImpl.crearCarrera` |
| CAR | US-CAR-02 Crear plan de estudio | Sí | `service/impl/CarreraServiceImpl.crearPlanEstudio` |
| CAR | US-CAR-03 Asociar materia a plan | Sí | `service/impl/CarreraServiceImpl.asociarMateriaAPlan` |
| CAR | US-CAR-04 Listar carreras con planes y materias | Sí | `service/impl/CarreraServiceImpl.listarCarreras` |
| Anexo | US-ASIS-03 a US-ASIS-05 | No (Fase 2) | — |
| Anexo | US-CAL-03 a US-CAL-06 | No (Fase 2) | — |

**Resumen**: 26 de 27 US del MVP implementadas (US-AUTH-01 simulada; US-PROF-04
realizada vía creación de comisión; US-NOT-02 automática dentro de la carga de
notas) + 1 no implementada (US-NOT-03) + 7 del Anexo como roadmap.
