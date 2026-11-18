# Documentación Funcional — Backoffice Académico (ITEC N°1)

> Documentación funcional formal del proyecto. Refleja **exactamente lo que el
> código implementa hoy** (commit actual), organizada en Épicas, User Stories,
> Reglas de Negocio, Criterios de Aceptación (Gherkin) y Casos de Prueba (TC).
>
> Convención: los TC marcados como **Aprobado** fueron verificados **manualmente**
> contra la aplicación real durante el desarrollo (pruebas por consola sobre
> datos sembrados por el `DataSeeder`). **No hay suite automatizada**: el
> repositorio no incluye JUnit ni directorio `src/test`. La suite canónica
> con el detalle por módulo y el conteo total (74 TC) vive en
> `05_Casos_de_Prueba.md`; las tablas por épicas de este documento son el
> detalle narrativo de cada épica.

---

## 1. Épicas y User Stories Implementadas

### EPIC-AUTH — Acceso y Roles

**Descripción**: control de acceso al sistema. Al arrancar, `Main` ejecuta el
login (`AuthView.mostrarLogin`) **antes** de cualquier menú: usuario y
contraseña con máximo 3 intentos; si se agotan, el programa termina con
`Sesion bloqueada: se agotaron los intentos de acceso`. Autenticado el
usuario, `MenuPrincipalView` arma el menú principal **filtrado según su rol**.

**US-AUTH-01 — Iniciar sesión (roles ADMIN, DOCENTE, ALUMNO)**
- *Como* personal del ITEC, *quiero* autenticarme con mi cuenta para acceder
  solo a las funciones que me corresponden.
- **Prioridad**: Alta (definida en el enunciado) — **Estado: IMPLEMENTADA**.
- **Reglas de negocio (código actual)**: RN-AUTH-01, RN-AUTH-02, RN-AUTH-03,
  RN-AUTH-04.
  - Las 3 cuentas están **precargadas** en `UsuarioRepositoryImpl`
    (`admin/admin123`, `docente/docente123`, `alumno/alumno123`), con
    contraseñas en **texto plano**: alcance de demostración académica, sin
    gestión de usuarios ni cifrado (no es un sistema listo para producción).
  - Máximo 3 intentos → `Credenciales invalidas` con `Intento n de 3`; al
    agotarlos → `Sesion bloqueada: se agotaron los intentos de acceso` y fin.
  - Menú filtrado por rol: ADMIN (7 módulos), DOCENTE (Notas y Asistencias),
    ALUMNO (solo `1. Consultar mis datos`, **placeholder** que muestra
    `Funcionalidad en desarrollo...`: la consulta real es Fase 2).
  - **Límite de autorización**: el control ocurre solo al armar el menú; los
    `*Service` no reciben el usuario ni validan rol o comisión (RN-AUTH-04).
  - Opción no numérica o fuera de rango → `ERROR: opcion invalida`, el menú
    se reitera (no cierra); la sesión termina con la opción `0`.
- **Criterios de Aceptación (Gherkin)**: ver escenarios de US-AUTH-01 en
  `04_Criterios_de_Aceptacion.md`.

**Casos de prueba** (detallados en `05_Casos_de_Prueba.md`, TC-AUTH-01 a 07;
verificación **manual**, sin suite automatizada):

| ID | Pasos | Resultado esperado |
|---|---|---|
| TC-AUTH-01 | Ejecutar `mvnw clean compile exec:java`, ingresar `admin` / `admin123` | `Bienvenido, admin (ADMIN)`; menú con los 7 módulos + salir |
| TC-AUTH-02 | Ingresar `docente` / `docente123` | Menú solo con `1. Notas`, `2. Asistencias` y `0. Salir` |
| TC-AUTH-03 | Ingresar `alumno` / `alumno123` | Menú solo con `1. Consultar mis datos` (placeholder con aviso) y `0. Salir` |
| TC-AUTH-04 | Contraseña errónea y luego reintento válido | `Credenciales invalidas` / `Intento 1 de 3`; luego sesión iniciada |
| TC-AUTH-05 | Tres credenciales inválidas seguidas | Tres fallos; al 3° → `Sesion bloqueada: se agotaron los intentos de acceso` y fin |
| TC-AUTH-06 | Opción `9` y luego `abc` en el menú | Dos veces `ERROR: opcion invalida`; menú reiterado con la sesión activa |
| TC-AUTH-07 | Opción `0` | `Hasta luego!` y fin del programa |

---

### EPIC-ALU — Gestión de Alumnos

**Descripción**: administración del padrón de alumnos con clave de negocio DNI,
estado activo/inactivo (baja lógica) y validaciones de datos de contacto.

**US-ALU-01 — Alta de alumno**
- *Como* secretaría académica, *quiero* registrar un alumno con sus datos
  personales, *para* habilitar su legajo e inscribirlo luego en comisiones.
- **Prioridad**: Alta.
- **Reglas de negocio** (`AlumnoServiceImpl.crearAlumno`):
  - Nombre, apellido y DNI son obligatorios (se recortan espacios; vacío =
    faltante) → `ERROR: nombre, apellido y DNI son obligatorios`.
  - DNI **único** en el padrón → `ERROR: DNI ya registrado`.
  - Mail obligatorio y debe contener `@` → `ERROR: mail invalido (debe contener '@')`.
  - Teléfono opcional.
  - El alumno se crea **activo** por defecto.
- **Gherkin**:

```gherkin
Escenario: alta exitosa
  DADO un DNI "41111222" no registrado
  CUANDO creo el alumno con nombre, apellido, DNI, mail y telefono validos
  ENTONCES el sistema responde "Alumno creado con exito"
  Y el alumno figura en el listado con estado ACTIVO

Escenario: DNI duplicado
  DADO un alumno activo con DNI "40123456"
  CUANDO intento crear otro alumno con el mismo DNI
  ENTONCES el sistema responde "ERROR: DNI ya registrado"
  Y el padron no se modifica

Escenario: mail invalido
  CUANDO intento crear un alumno con mail sin "@"
  ENTONCES el sistema responde "ERROR: mail invalido (debe contener '@')"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ALU-TC01 | Menú 1 → 1: nombre, apellido, DNI nuevo, mail con `@` | "Alumno creado con exito" |
| ALU-TC02 | Repetir alta con DNI `40123456` (sembrado) | "ERROR: DNI ya registrado" |
| ALU-TC03 | Alta con mail `juanmail` (sin `@`) | "ERROR: mail invalido (debe contener '@')" |
| ALU-TC04 | Alta con apellido vacío (solo Enter) | "ERROR: nombre, apellido y DNI son obligatorios" |

**US-ALU-02 — Búsqueda por DNI**
- *Como* secretaría, *quiero* buscar un alumno por su DNI, *para* consultar sus
  datos puntuales.
- **Prioridad**: Alta.
- **Reglas de negocio**: DNI obligatorio; si no existe → `ERROR: no existe un alumno con ese DNI`.
- **Gherkin**:

```gherkin
Escenario: busqueda exitosa
  DADO el alumno sembrado "40123456"
  CUANDO busco por ese DNI
  ENTONCES se muestran nombre, apellido, mail y estado ACTIVO

Escenario: busqueda inexistente
  CUANDO busco el DNI "99999999"
  ENTONCES el sistema responde "ERROR: no existe un alumno con ese DNI"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ALU-TC05 | Menú 1 → 2 → DNI `40123456` | Datos de Juan Perez, ACTIVO |
| ALU-TC06 | Menú 1 → 2 → DNI `99999999` | "ERROR: no existe un alumno con ese DNI" |

**US-ALU-03 — Listado del padrón**
- *Como* secretaría, *quiero* listar todos los alumnos, *para* generar
  reportes y atender consultas.
- **Prioridad**: Alta.
- **Reglas de negocio**: el listado muestra **DNI, nombre y apellido, mail y
  estado** de todos los alumnos (activos e inactivos).
- **Gherkin**:

```gherkin
Escenario: listado completo
  DADO un padron con alumnos activos e inactivos
  CUANDO solicito el listado
  ENTONCES se muestran todos, cada uno con su estado (ACTIVO/INACTIVO)
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ALU-TC07 | Menú 1 → 4 | 5 alumnos sembrados; Diego Torres `40567890` como INACTIVO |

**US-ALU-04 — Baja lógica**
- *Como* secretaría, *quiero* dar de baja un alumno, *para* depurar el padrón
  sin eliminar su historial académico.
- **Prioridad**: Alta.
- **Reglas de negocio**:
  - Baja **lógica**: `activo = false`; nunca se borra el registro.
  - Alumno inexistente → `ERROR: no existe un alumno con ese DNI`.
  - Doble baja → `ERROR: el alumno ya se encuentra inactivo`.
- **Gherkin**:

```gherkin
Escenario: baja exitosa
  DADO un alumno ACTIVO
  CUANDO solicito su baja
  ENTONCES el sistema responde "Alumno dado de baja (baja logica)"
  Y su estado pasa a INACTIVO sin eliminarse

Escenario: doble baja
  DADO un alumno ya INACTIVO
  CUANDO solicito su baja nuevamente
  ENTONCES el sistema responde "ERROR: el alumno ya se encuentra inactivo"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ALU-TC08 | Menú 1 → 3 → DNI de un alumno activo | "Alumno dado de baja (baja logica)" y estado INACTIVO en el listado |
| ALU-TC09 | Repetir baja sobre el mismo alumno | "ERROR: el alumno ya se encuentra inactivo" |

**US-ALU-05 — Modificación de alumno**
- *Como* secretaría, *quiero* modificar los datos de un alumno, *para*
  corregir errores de carga sin perder su legajo ni su historial.
- **Prioridad**: Media.
- **Reglas de negocio** (`AlumnoServiceImpl.modificarAlumno`):
  - El DNI **no es editable** (clave de negocio del repositorio); identifica
    al alumno a modificar.
  - Alumno inexistente → `ERROR: no existe un alumno con ese DNI`.
  - Nombre y apellido obligatorios (trim; vacío = faltante) →
    `ERROR: nombre, apellido y DNI son obligatorios`.
  - Mail con `@` → `ERROR: mail invalido (debe contener '@') (debe contener '@')`.
  - Teléfono opcional. Se modifican los datos del **mismo objeto**
    (sin re-claveo: la clave no cambia).
- **Gherkin**:

```gherkin
Escenario: modificacion exitosa
  DADO un alumno registrado con DNI "40123456"
  CUANDO modifico su nombre, apellido y mail con datos validos
  ENTONCES el sistema responde "Alumno modificado con exito"
  Y la busqueda por el mismo DNI muestra los datos actualizados

Escenario: alumno inexistente
  CUANDO intento modificar el DNI "99999999"
  ENTONCES el sistema responde "ERROR: no existe un alumno con ese DNI"

Escenario: mail invalido en modificacion
  CUANDO intento modificar con un mail sin "@"
  ENTONCES el sistema responde "ERROR: mail invalido (debe contener '@') (debe contener '@')"
  Y los datos del alumno quedan sin cambios
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ALU-TC10 | Menú 1 → 5 → DNI `40123456` + datos válidos | "Alumno modificado con exito" |
| ALU-TC11 | Verificar por búsqueda (menú 1 → 2) | Datos actualizados bajo el mismo DNI |
| ALU-TC12 | Modificar DNI `99999999` | "ERROR: no existe un alumno con ese DNI" |
| ALU-TC13 | Modificar con mail sin `@` | "ERROR: mail invalido (debe contener '@') (debe contener '@')" |

---

### EPIC-PROF — Gestión de Profesores

**Descripción**: administración de docentes con clave DNI y baja lógica.
El módulo implementa el CRUD completo: **alta, búsqueda, baja lógica,
listado y modificación** (el DNI no es editable).

**US-PROF-01 — Alta de profesor**
- *Como* coordinación, *quiero* registrar un docente, *para* asignarlo luego a
  comisiones.
- **Prioridad**: Alta.
- **Reglas de negocio** (`ProfesorServiceImpl.crearProfesor`): mismas reglas
  que alumno — nombre/apellido/DNI obligatorios, DNI único, mail con `@`;
  se crea activo.
- **Gherkin**:

```gherkin
Escenario: alta exitosa
  DADO un DNI "20999888" no registrado
  CUANDO creo el profesor con datos validos
  ENTONCES el sistema responde "Profesor creado con exito"

Escenario: DNI duplicado
  DADO el profesor sembrado "20111222"
  CUANDO intento crear otro con ese DNI
  ENTONCES el sistema responde "ERROR: DNI ya registrado"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| PROF-TC01 | Menú 2 → 1: datos nuevos válidos | "Profesor creado con exito" |
| PROF-TC02 | Alta con DNI `20111222` (sembrado) | "ERROR: DNI ya registrado" |
| PROF-TC03 | Alta con mail sin `@` | "ERROR: mail invalido (debe contener '@')" |

**US-PROF-02 — Búsqueda por DNI**
- *Como* coordinación, *quiero* buscar un docente por DNI, *para* verificar sus
  datos y estado.
- **Prioridad**: Alta.
- **Reglas de negocio**: inexistente → `ERROR: no existe un profesor con ese DNI`.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| PROF-TC04 | Menú 2 → 2 → DNI `20111222` | Datos de Ana Rodriguez, ACTIVO |
| PROF-TC05 | Menú 2 → 2 → DNI `99999999` | "ERROR: no existe un profesor con ese DNI" |

**US-PROF-03 — Baja lógica**
- *Como* coordinación, *quiero* dar de baja un docente, *para* que no pueda ser
  asignado a nuevas comisiones sin perder su historial.
- **Prioridad**: Media.
- **Reglas de negocio**: baja lógica (`activo=false`); inexistente → no
  encontrado; doble baja → `ERROR: el profesor ya se encuentra inactivo`.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| PROF-TC06 | Menú 2 → 3 → DNI activo | "Profesor dado de baja (baja logica)" |
| PROF-TC07 | Repetir la baja | "ERROR: el profesor ya se encuentra inactivo" |

**US-PROF-04 — Listado de profesores**
- *Como* coordinación, *quiero* listar todos los profesores, *para* disponer
  del cuadro de docentes al asignar comisiones.
- **Prioridad**: Media.
- **Reglas de negocio** (`ProfesorServiceImpl.listarTodos`): el listado muestra
  **DNI, apellido y nombre, mail y estado** de todos los profesores (activos e
  inactivos); padrón vacío → "(no hay profesores registrados)".
- **Gherkin**:

```gherkin
Escenario: listado completo
  DADO docentes activos e inactivos en el repositorio
  CUANDO solicito el listado
  ENTONCES se muestran todos, cada uno con su estado (ACTIVO/INACTIVO)
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| PROF-TC08 | Menú 2 → 4 | Los 5 profesores sembrados con DNI, apellido/nombre, mail y estado |

**US-PROF-05 — Modificación de profesor**
- *Como* coordinación, *quiero* modificar los datos de un docente, *para*
  corregir su registro sin perder sus asignaciones históricas.
- **Prioridad**: Media.
- **Reglas de negocio** (`ProfesorServiceImpl.modificarProfesor`): mismas que
  la modificación de alumnos — DNI **no editable** (identifica al docente),
  existencia requerida, nombre/apellido obligatorios, mail con `@`, teléfono
  opcional; se modifica el mismo objeto (sin re-claveo).
- **Gherkin**:

```gherkin
Escenario: modificacion exitosa
  DADO un profesor registrado con DNI "20111222"
  CUANDO modifico sus datos con valores validos
  ENTONCES el sistema responde "Profesor modificado con exito"
  Y la busqueda por el mismo DNI muestra los datos actualizados

Escenario: profesor inexistente
  CUANDO intento modificar el DNI "99999999"
  ENTONCES el sistema responde "ERROR: no existe un profesor con ese DNI"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| PROF-TC09 | Menú 2 → 5 → DNI `20111222` + datos válidos | "Profesor modificado con exito" |
| PROF-TC10 | Verificar por búsqueda (menú 2 → 2) | Datos actualizados bajo el mismo DNI |
| PROF-TC11 | Modificar DNI `99999999` | "ERROR: no existe un profesor con ese DNI" |
| PROF-TC12 | Modificar con mail sin `@` | "ERROR: mail invalido (debe contener '@') (debe contener '@')" |

---

### EPIC-MAT — Gestión de Materias y Correlativas

**Descripción**: oferta académica con clave de negocio **nombre**, incluyendo
la relación reflexiva de materias correlativas y el re-claveo al renombrar.

**US-MAT-01 — Alta de materia**
- *Como* coordinación, *quiero* crear materias con año y cuatrimestre, *para*
  modelar la malla del plan de estudios.
- **Prioridad**: Alta.
- **Reglas de negocio** (`MateriaServiceImpl.crearMateria`):
  - Nombre obligatorio (trim; vacío = faltante).
  - Año `>= 1` y cuatrimestre `1` o `2` → `ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2`.
  - Nombre **único** (clave del repositorio) → `ERROR: ya existe una materia con ese nombre`.
  - Se crea **activa** por defecto, sin correlativas.
- **Gherkin**:

```gherkin
Escenario: alta exitosa
  DADO un nombre de materia no registrado
  CUANDO la creo con anio >= 1 y cuatrimestre 1 o 2
  ENTONCES el sistema responde "Materia creada con exito"
  Y figura ACTIVA en el listado

Escenario: nombre duplicado
  DADO la materia "Programacion I"
  CUANDO intento crear otra con ese nombre
  ENTONCES el sistema responde "ERROR: ya existe una materia con ese nombre"

Escenario: datos invalidos
  CUANDO la creo con cuatrimestre 3
  ENTONCES el sistema responde "ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| MAT-TC01 | Menú 3 → 1: nombre nuevo, año 1, cuat 1 | "Materia creada con exito" |
| MAT-TC02 | Alta con nombre `Programacion I` | "ERROR: ya existe una materia con ese nombre" |
| MAT-TC03 | Alta con cuatrimestre `3` | "ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2" |

**US-MAT-02 — Búsqueda y listado**
- *Como* coordinación, *quiero* buscar una materia por nombre y listar todas,
  *para* consultar estado y correlatividades.
- **Prioridad**: Alta.
- **Reglas de negocio**: la búsqueda muestra nombre, año, cuatrimestre, estado
  y **lista de correlativas**; el listado incluye las inactivas.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| MAT-TC04 | Menú 3 → 2 → `Base de Datos` | Correlativas: Programacion I, Matematica Discreta |
| MAT-TC05 | Menú 3 → 7 | Las 5 materias sembradas con estado y correlativas |

**US-MAT-03 — Modificación con re-claveo**
- *Como* coordinación, *quiero* modificar el nombre, año o cuatrimestre de una
  materia, *para* corregir la malla sin perder sus relaciones.
- **Prioridad**: Media.
- **Reglas de negocio** (`MateriaServiceImpl.modificarMateria`):
  - La materia original debe existir → `ERROR: no existe una materia con ese nombre`.
  - **Enter sin texto en el nombre = mantener el nombre actual**.
  - Año/cuatrimestre validados igual que en el alta.
  - Si cambia el nombre, el nuevo no puede chocar con **otra** materia
    (comparación case-insensitive) → `ERROR: ya existe una materia con ese nombre`.
  - **Re-claveo**: se elimina la clave vieja del repositorio y se reinserta el
    **mismo objeto** con el nombre nuevo (referencias intactas).
- **Gherkin**:

```gherkin
Escenario: renombre con re-claveo
  DADO la materia "Programacion I" con correlativas asociadas
  CUANDO la renombro a "Programacion I - 2024"
  ENTONCES el sistema responde "Materia modificada con exito"
  Y se la encuentra bajo el nuevo nombre con sus datos intactos

Escenario: colision de nombre
  DADO las materias "Programacion I" y "Base de Datos"
  CUANDO renombro "Programacion I" a "Base de Datos"
  ENTONCES el sistema responde "ERROR: ya existe una materia con ese nombre"
  Y la materia original conserva su nombre
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| MAT-TC06 | Menú 3 → 3 → renombrar una materia | "Materia modificada con exito"; buscable solo bajo el nombre nuevo |
| MAT-TC07 | Renombrar a un nombre ya existente | "ERROR: ya existe una materia con ese nombre" |
| MAT-TC08 | Renombrar con Enter vacío y datos cambiados | Solo cambian año/cuatrimestre; nombre intacto |

**US-MAT-04 — Baja lógica**
- *Como* coordinación, *quiero* desactivar una materia, *para* retirarla de la
  oferta sin romper comisiones ni planes históricos.
- **Prioridad**: Media.
- **Reglas de negocio**: baja lógica; inexistente → no encontrada;
  doble baja → `ERROR: la materia ya se encuentra inactiva`.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| MAT-TC09 | Menú 3 → 4 → materia activa | "Materia dada de baja (baja logica)"; aparece INACTIVA en el listado |
| MAT-TC10 | Repetir la baja | "ERROR: la materia ya se encuentra inactiva" |

**US-MAT-05 — Agregar correlativa**
- *Como* coordinación, *quiero* definir correlativas entre materias, *para*
  modelar la secuencia académica de la malla.
- **Prioridad**: Alta.
- **Reglas de negocio** (`MateriaServiceImpl.agregarCorrelativa` + `Materia.agregarCorrelativa`):
  - Ambas materias deben existir → `ERROR: no existe una materia con ese nombre`.
  - **Relación reflexiva**: una materia no puede ser correlativa de sí misma →
    `ERROR: una materia no puede ser correlativa de si misma`.
  - No se admiten duplicados → `ERROR: esa materia ya figura como correlativa`.
  - La mutación se hace por el método encapsulado del modelo (copia defensiva
    garantiza lista mutable).
- **Gherkin**:

```gherkin
Escenario: correlativa exitosa
  DADO las materias "Redes de Computadoras" y "Programacion II"
  CUANDO agrego "Programacion II" como correlativa de Redes
  ENTONCES el sistema responde "Correlativa agregada con exito"
  Y la busqueda de Redes la muestra en su lista

Escenario: autorreferencia
  DADO la materia "Programacion I"
  CUANDO intento agregarla como correlativa de si misma
  ENTONCES el sistema responde
    "ERROR: una materia no puede ser correlativa de si misma"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| MAT-TC11 | Menú 3 → 5: `Redes de Computadoras` ← `Programacion II` | "Correlativa agregada con exito" |
| MAT-TC12 | Agregar `Programacion I` de sí misma | "ERROR: una materia no puede ser correlativa de si misma" |
| MAT-TC13 | Repetir MAT-TC11 | "ERROR: esa materia ya figura como correlativa" |
| MAT-TC14 | Correlativa con nombre inexistente | "ERROR: no existe una materia con ese nombre" |

---

### EPIC-COM — Comisiones e Inscripciones

**Descripción**: programación del dictado (materia + cuatrimestre + profesor)
con ID autoincremental, y inscripción de alumnos con estado inicial REGULAR.

**US-COM-01 — Crear comisión**
- *Como* coordinación, *quiero* abrir una comisión por cuatrimestre, *para*
  programar el dictado de una materia a cargo de un docente.
- **Prioridad**: Alta.
- **Reglas de negocio** (`ComisionServiceImpl.crearComision`):
  - La materia debe existir → `ERROR: no existe una materia con ese nombre` y estar
    **activa** → `ERROR: la materia esta inactiva`.
  - El profesor debe existir → `ERROR: profesor inexistente` y estar
    **activo** → `ERROR: el profesor esta inactivo`.
  - Fechas: inicio y fin obligatorias y `fin >= inicio` → `ERROR: fechas invalidas`.
  - **Anti-duplicado**: no se admite otra comisión con la misma materia,
    profesor y cuatrimestre (año/número) → `ERROR: ya existe una comision igual`.
  - El ID lo asigna el repositorio (contador autoincremental) y se informa al
    crear: `Comision creada con exito (ID: n)`.
- **Gherkin**:

```gherkin
Escenario: creacion exitosa
  DADO una materia activa y un profesor activo
  CUANDO creo una comision con fechas coherentes
  ENTONCES el sistema responde "Comision creada con exito (ID: n)"
  Y la comision figura en el listado con su ID

Escenario: materia inactiva
  DADO una materia INACTIVA
  CUANDO intento crear una comision para ella
  ENTONCES el sistema responde "ERROR: la materia esta inactiva"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| COM-TC01 | Menú 4 → 1: materia, profesor y fechas válidas | "Comision creada con exito (ID: n)" |
| COM-TC02 | Repetir la misma comisión | "ERROR: ya existe una comision igual" |
| COM-TC03 | Crear con materia inactiva | "ERROR: la materia esta inactiva" |
| COM-TC04 | Crear con profesor inactivo | "ERROR: el profesor esta inactivo" |
| COM-TC05 | Fechas fin anteriores a inicio | "ERROR: fechas invalidas" |
| COM-TC06 | Menú 4 → 2 | Listado con las comisiones existentes (ID, materia, cuatrimestre, profesor) |

**US-COM-02 — Inscribir alumno**
- *Como* alumno (por secretaría), *quiero* inscribirme a una comisión, *para*
  cursar la materia en el cuatrimestre.
- **Prioridad**: Alta.
- **Reglas de negocio** (`ComisionServiceImpl.inscribirAlumno`):
  - El alumno debe existir → `ERROR: alumno inexistente` y estar
    **activo** → `ERROR: el alumno esta inactivo`.
  - La comisión debe existir → `ERROR: no existe una comision con ese ID`.
  - **Anti-duplicado**: no re-inscripción en la misma comisión →
    `ERROR: el alumno ya esta inscripto en esa comision`.
  - El inscripto se crea con estado `REGULAR`.
  - La vista lista las comisiones antes de pedir el ID (usabilidad).
- **Gherkin**:

```gherkin
Escenario: inscripcion exitosa
  DADO un alumno activo no inscripto en la comision 1
  CUANDO lo inscribo
  ENTONCES el sistema responde "Alumno inscripto con exito (comision ID 1)"

Escenario: inscripcion duplicada
  DADO un alumno ya inscripto en la comision 1
  CUANDO intento inscribirlo nuevamente
  ENTONCES el sistema responde "ERROR: el alumno ya esta inscripto en esa comision"

Escenario: alumno inactivo
  DADO un alumno INACTIVO
  CUANDO intento inscribirlo
  ENTONCES el sistema responde "ERROR: el alumno esta inactivo"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| COM-TC07 | Menú 4 → 3: `40123456` en comisión 1 | "Alumno inscripto con exito (comision ID 1)" |
| COM-TC08 | Repetir COM-TC07 | "ERROR: el alumno ya esta inscripto en esa comision" |
| COM-TC09 | Inscribir a `40567890` (Diego, inactivo) | "ERROR: el alumno esta inactivo" |
| COM-TC10 | Inscribir con ID de comisión inexistente | "ERROR: no existe una comision con ese ID" |

---

### EPIC-NOT — Notas y Promoción

**Descripción**: registro de evaluaciones por inscripto con cálculo de promedio
y promoción automática a APROBADO.

**US-NOT-01 — Cargar nota**
- *Como* docente, *quiero* cargar una nota por tipo de evaluación, *para*
  registrar el desempeño del alumno y determinar su estado de cursada.
- **Prioridad**: Alta.
- **Reglas de negocio** (`NotaServiceImpl.cargarNota`):
  - Tipo de evaluación del enum (`TP`, `PARCIAL`, `RECUPERATORIO`, `FINAL`);
    nulo/inválido → `ERROR: tipo de evaluacion invalido`.
  - Valor entre **0 y 10** → `ERROR: la nota debe estar entre 0 y 10`.
  - Comisión existente → `ERROR: no existe una comision con ese ID`.
  - El alumno debe estar **inscripto** en esa comisión →
    `ERROR: el alumno no esta inscripto en esa comision`.
  - Se crean `Examen` (con fecha del día) y `Nota` vinculados al inscripto.
  - Se informa el promedio con 2 decimales.
  - **Promedio ≥ 6.0** → estado `APROBADO` + `notaFinal` guardada
    (`El alumno quedo APROBADO (promedio >= 6)`).
  - Si ya estaba APROBADO → `El alumno ya se encontraba APROBADO` (nunca
    se degrada el estado).
  - Promedio < 6 → `El alumno mantiene su estado de cursada`.
- **Gherkin**:

```gherkin
Escenario: nota que promociona
  DADO un alumno inscripto en la comision 1 sin notas
  CUANDO cargo un TP con valor 8
  ENTONCES el sistema responde "Nota cargada con exito (promedio: 8,00).
    El alumno quedo APROBADO (promedio >= 6)"
  Y su estado de cursada es APROBADO con notaFinal 8,00

Escenario: nota fuera de rango
  CUANDO cargo una nota con valor 20
  ENTONCES el sistema responde "ERROR: la nota debe estar entre 0 y 10"
  Y no se crea ningun examen

Escenario: no degradar el estado
  DADO un alumno APROBADO con promedio 8,00
  CUANDO cargo un examen con valor 2
  ENTONCES el sistema informa el nuevo promedio
  Y el alumno NO cambia de estado (sigue APROBADO)
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| NOT-TC01 | Menú 5 → 1: inscripto de comisión 1, TP `8` | "Nota cargada... El alumno quedo APROBADO (promedio >= 6)" |
| NOT-TC02 | Cargar PARCIAL `6.5` al mismo alumno | Informe con promedio recalculado; "ya se encontraba APROBADO" |
| NOT-TC03 | Valor `20` | "ERROR: la nota debe estar entre 0 y 10" |
| NOT-TC04 | Nota a alumno no inscripto (`40234567` en comisión 1) | "ERROR: el alumno no esta inscripto en esa comision" |
| NOT-TC05 | Nota con ID de comisión inexistente | "ERROR: no existe una comision con ese ID" |

---

### EPIC-ASIS — Asistencias

**Descripción**: control de cursada con registro binario presente/ausente por
fecha del día, y consulta por comisión.

**US-ASIS-01 — Registrar asistencia**
- *Como* docente, *quiero* registrar presente o ausente de un inscripto, *para*
  controlar la asistencia a la cursada.
- **Prioridad**: Alta.
- **Reglas de negocio** (`AsistenciaServiceImpl.registrarAsistencia`):
  - Comisión existente → `ERROR: no existe una comision con ese ID`.
  - Alumno **inscripto** en esa comisión →
    `ERROR: el alumno no esta inscripto en esa comision`.
  - Registro **binario**: PRESENTE (`S`) / AUSENTE (`N`); la fecha es
    `LocalDate.now()` (no se ingresa manualmente).
  - No hay validación anti-duplicado por día (un mismo alumno puede tener
    varios registros el mismo día: ver Anexo §3).
- **Gherkin**:

```gherkin
Escenario: presente
  DADO un alumno inscripto en la comision 1
  CUANDO registro su asistencia respondiendo "S"
  ENTONCES el sistema responde "Asistencia registrada con exito (fecha de hoy)"

Escenario: no inscripto
  DADO un alumno NO inscripto en la comision 1
  CUANDO intento registrarle asistencia
  ENTONCES el sistema responde "ERROR: el alumno no esta inscripto en esa comision"
```

- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ASIS-TC01 | Menú 6 → 1: inscripto, comisión 1, `S` | "Asistencia registrada con exito (fecha)" |
| ASIS-TC02 | Registrar con `N` | Registro AUSENTE visible en el listado |
| ASIS-TC03 | Intento con alumno no inscripto | "ERROR: el alumno no esta inscripto en esa comision" |

**US-ASIS-02 — Listar asistencias por comisión**
- *Como* docente, *quiero* listar las asistencias de una comisión, *para*
  revisar el control de cursada.
- **Prioridad**: Media.
- **Reglas de negocio**: cada línea muestra **fecha | PRESENTE/AUSENTE |
  alumno**; comisión sin registros → lista vacía.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| ASIS-TC04 | Menú 6 → 2 → comisión 1 | Registros en formato `dd/MM/yyyy | PRESENTE/AUSENTE | alumno` |
| ASIS-TC05 | Listar comisión sin registros | Lista vacía (sin error) |

---

### EPIC-CAR — Carreras y Planes de Estudio

**Descripción**: estructura académica jerárquica Carrera → PlanEstudio →
Materias, con materias compartidas por instancia con el repositorio.

**US-CAR-01 — Crear carrera**
- *Como* coordinación, *quiero* crear una carrera, *para* ordenar la oferta
  académica del instituto.
- **Prioridad**: Alta.
- **Reglas de negocio** (`CarreraServiceImpl.crearCarrera`): nombre
  obligatorio; nombre **único** → `ERROR: ya existe una carrera con ese nombre`.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| CAR-TC01 | Menú 7 → 1: nombre nuevo | "Carrera creada con exito" |
| CAR-TC02 | Alta con `Tecnicatura en Programacion` | "ERROR: ya existe una carrera con ese nombre" |

**US-CAR-02 — Crear plan de estudio**
- *Como* coordinación, *quiero* definir planes de estudio por carrera, *para*
  versionar la malla vigente.
- **Prioridad**: Alta.
- **Reglas de negocio** (`crearPlanEstudio`): carrera existente →
  `ERROR: carrera inexistente`; nombre de plan obligatorio; rango
  `anioFin >= anioInicio` → `ERROR: el anio de fin no puede ser anterior al de inicio`; nombre de plan único
  dentro de la carrera → `ERROR: ya existe un plan con ese nombre en la carrera`. Internamente
  el plan se materializa con vigencia `01/01/anioInicio` a `31/12/anioFin`.
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| CAR-TC03 | Menú 7 → 2: carrera existente, plan nuevo, 2024–2026 | "Plan de estudio creado con exito" |
| CAR-TC04 | Plan con anioFin 2020 < anioInicio 2024 | "ERROR: el anio de fin no puede ser anterior al de inicio" |
| CAR-TC05 | Plan con nombre repetido en la misma carrera | "ERROR: ya existe un plan con ese nombre en la carrera" |

**US-CAR-03 — Asociar materia a plan**
- *Como* coordinación, *quiero* asociar materias existentes a un plan, *para*
  componer la malla del plan.
- **Prioridad**: Alta.
- **Reglas de negocio** (`asociarMateriaAPlan`): carrera, plan y materia deben
  existir → errores específicos por elemento; **no se admiten duplicados** en
  el plan → `ERROR: esa materia ya esta asociada al plan`. El plan guarda la
  **misma instancia** de la materia del repositorio (no una copia).
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| CAR-TC06 | Asociar materia existente a un plan | "Materia asociada al plan con exito" |
| CAR-TC07 | Repetir la asociación | "ERROR: esa materia ya esta asociada al plan" |
| CAR-TC08 | Materia inexistente | "ERROR: no existe una materia con ese nombre" |

**US-CAR-04 — Listar carreras con planes**
- *Como* coordinación, *quiero* un listado jerárquico de carreras y planes,
  *para* visualizar la estructura académica completa.
- **Prioridad**: Media.
- **Reglas de negocio**: listado jerárquico Carrera → Plan (con rango de
  vigencia) → Materias (con año, cuatrimestre, estado y correlativas).
- **Casos de prueba**:

| ID | Pasos | Resultado esperado |
|---|---|---|
| CAR-TC09 | Menú 7 → 4 | Jerarquía completa de las 2 carreras sembradas con sus Plan 2024 y materias |

---

## 2. Matriz de Trazabilidad

| Módulo | User Story | ¿Implementado? | ¿Dónde está en el código? |
|---|---|---|---|
| AUTH | US-AUTH-01 Iniciar sesión (roles) | Sí (login real) | `AuthView.mostrarLogin` · `AuthServiceImpl.autenticar` · `UsuarioRepositoryImpl` (3 cuentas sembradas) · `MenuPrincipalView` (menú filtrado por rol; opción ALUMNO es placeholder) |
| ALU | US-ALU-01 Alta de alumno | Sí | `AlumnoServiceImpl.crearAlumno` · `AlumnoRepositoryImpl.guardar/existeDni` |
| ALU | US-ALU-02 Búsqueda por DNI | Sí | `AlumnoServiceImpl.buscarAlumno` · `AlumnoRepositoryImpl.buscarPorDni` |
| ALU | US-ALU-03 Listado del padrón | Sí | `AlumnoServiceImpl.listarTodos` · `AlumnoRepositoryImpl.obtenerTodos` · `AlumnoView` (opción 4) |
| ALU | US-ALU-04 Baja lógica | Sí | `AlumnoServiceImpl.darDeBajaLogica` |
| ALU | US-ALU-05 Modificación de alumno | Sí | `AlumnoServiceImpl.modificarAlumno` · `AlumnoView` (opción 5) |
| PROF | US-PROF-01 Alta de profesor | Sí | `ProfesorServiceImpl.crearProfesor` |
| PROF | US-PROF-02 Búsqueda por DNI | Sí | `ProfesorServiceImpl.buscarProfesor` |
| PROF | US-PROF-03 Baja lógica | Sí | `ProfesorServiceImpl.darDeBajaLogica` |
| PROF | Listado de profesores | Sí | `ProfesorServiceImpl.listarTodos` · `ProfesorView` (opción 4) |
| PROF | US-PROF-05 Modificación de profesor | Sí | `ProfesorServiceImpl.modificarProfesor` · `ProfesorView` (opción 5) |
| MAT | US-MAT-01 Alta de materia | Sí | `MateriaServiceImpl.crearMateria` |
| MAT | US-MAT-02 Búsqueda y listado | Sí | `MateriaServiceImpl.buscarMateria/listarTodas` |
| MAT | US-MAT-03 Modificación con re-claveo | Sí | `MateriaServiceImpl.modificarMateria` · `MateriaRepository.eliminar/guardar` |
| MAT | US-MAT-04 Baja lógica | Sí | `MateriaServiceImpl.darDeBajaLogica` |
| MAT | US-MAT-05 Agregar correlativa | Sí | `MateriaServiceImpl.agregarCorrelativa` · `Materia.agregarCorrelativa` |
| COM | US-COM-01 Crear comisión | Sí | `ComisionServiceImpl.crearComision` · `ComisionRepositoryImpl.agregar` (ID autoincremental) |
| COM | Listar comisiones | Sí | `ComisionServiceImpl.listarComisiones` |
| COM | US-COM-02 Inscribir alumno | Sí | `ComisionServiceImpl.inscribirAlumno` · `AlumnoInscripto` (estado REGULAR) |
| NOT | US-NOT-01 Cargar nota + promoción | Sí | `NotaServiceImpl.cargarNota` · `Examen`/`Nota` · `AlumnoInscripto.promedioNotas` |
| ASIS | US-ASIS-01 Registrar asistencia | Sí | `AsistenciaServiceImpl.registrarAsistencia` · `AsistenciaRepositoryImpl.agregar` |
| ASIS | US-ASIS-02 Listar por comisión | Sí | `AsistenciaServiceImpl.obtenerAsistenciasPorComision` |
| CAR | US-CAR-01 Crear carrera | Sí | `CarreraServiceImpl.crearCarrera` |
| CAR | US-CAR-02 Crear plan de estudio | Sí | `CarreraServiceImpl.crearPlanEstudio` · `PlanEstudio` |
| CAR | US-CAR-03 Asociar materia a plan | Sí | `CarreraServiceImpl.asociarMateriaAPlan` |
| CAR | US-CAR-04 Listado jerárquico | Sí | `CarreraServiceImpl.listarCarreras` · `CarreraView` (opción 4) |
| Anexo | Justificación de faltas | **No** (Fase 2) | — |
| Anexo | Regularidad ≥ 70% | **No** (Fase 2) | — |
| Anexo | Múltiples parciales/recuperatorios con lógica propia | **No** (Fase 2: el tipo existe, la política no) | `TipoEvaluacion` (enum ya disponible) |
| Anexo | Cierre de acta y auditoría | **No** (Fase 2) | — |
| Anexo | Reportes (riesgo, libro de actas) | **No** (Fase 2) | — |
| Anexo | Autorización en services / gestión de cuentas | **No** (Fase 2: el login y el menú por rol sí están implementados) | `MenuPrincipalView` hoy es el único punto de control (RN-AUTH-04) |

---

## 3. Anexo: Requerimientos No Implementados (Roadmap Fase 2)

Priorización MVP: el alcance actual cubre el ciclo académico esencial con
reglas de integridad verificables. Los siguientes requerimientos del Anexo
quedaron para Fase 2 — y en cada caso, la arquitectura actual ya provee el
punto de extensión.

| Requerimiento | Por qué no entró al MVP | Cómo lo habilita la arquitectura actual |
|---|---|---|
| **Justificación de inasistencias** (estado `JUSTIFICADA`) | El MVP definió asistencia binaria (S/N) para validar primero el ciclo de cursada mínimo | El modelo ya usa enums (`EstadoCursada`, `TipoEvaluacion`): agregar un valor al dominio de asistencia no requiere migrar el repositorio (guarda objetos). Nueva regla en `AsistenciaServiceImpl` + opción en `AsistenciaView` |
| **Cálculo de regularidad (≥ 70%)** | Requiere un umbral de clases programadas y una política de conteo que no formaba parte del núcleo | Los datos ya existen: `Asistencia` (fecha, presente, `AlumnoInscripto`). Es un método de lectura (`calcularPorcentaje(inscripto)`) sobre `obtenerAsistenciasPorComision` — sin cambiar el modelo |
| **Múltiples parciales y recuperatorios con lógica propia** | El MVP trata toda evaluación igual en el promedio (≥ 6 aprueba) | `TipoEvaluacion` ya distingue TP/PARCIAL/RECUPERATORIO/FINAL; una política por tipo es una regla nueva en `NotaServiceImpl` (el vínculo `Examen`↔`Nota`↔`AlumnoInscripto` ya modela la historia completa) |
| **Cierre de acta y auditoría** | Requiere concepto de "cierre" (estado final de comisión) y trazabilidad de quién modificó qué, fuera del MVP | **Auditoría**: `AuditService` + `AuditRepositoryImpl` nuevos, cableados en el `Main`; los `*ServiceImpl` agregan una línea por operación; vistas y controllers no cambian. **Acta**: el estado `APROBADO` con `notaFinal` por inscripto ya es la materia prima del acta |
| **Reportes (alumnos en riesgo, libro de actas)** | Agregaciones estadísticas no esenciales para el ciclo MVP | Los repositorios exponen `obtenerTodos()` con copias defensivas: un `ReporteService` se agrega sobre las interfaces existentes (ej: inscriptos con % asistencia < 70% = "en riesgo") |
| **Autorización en services, gestión de usuarios y contraseñas cifradas** | El login real y el filtrado de menú por rol ya están en el MVP (US-AUTH-01), pero la verificación de permisos ocurre **solo al armar el menú**: los services no reciben el usuario ni validan rol/comisión | Los `*ServiceImpl` ya son el punto único de decisión: agregarles una validación de sesión/rol no cambia vistas ni repositorios; las cuentas viven en `UsuarioRepositoryImpl`, listo para un `UsuarioService` de alta/edición |

Además, queda como mejora técnica menor detectada y no priorizada:
anti-duplicado de asistencia por día por inscripto.

---

## 4. Resumen de Casos de Prueba

> Verificación **manual** por consola; **no hay suite automatizada** en el
> repositorio (sin JUnit ni directorio `src/test`). El detalle fila por fila
> de los TC está en `05_Casos_de_Prueba.md` (suite canónica); esta tabla
> resume el conteo por módulo.

| Módulo | Total TC | Aprobados (verificación manual) | Pendientes |
|---|---|---|---|
| AUTH (login real por roles) | 7 | 7 | 0 |
| ALU — Alumnos | 13 | 13 | 0 |
| PROF — Profesores | 8 | 8 | 0 |
| MAT — Materias y Correlativas | 14 | 14 | 0 |
| COM — Comisiones e Inscripciones | 10 | 10 | 0 |
| NOT — Notas | 9 | 9 | 0 |
| ASIS — Asistencias | 5 | 5 | 0 |
| CAR — Carreras y Planes | 8 | 8 | 0 |
| **Total** | **74** | **74** | **0** |

Los requerimientos excluidos del MVP (regularidad por asistencia,
justificaciones, cierre de actas, auditoría e informes) no tienen TC: son
Fase 2 y quedan documentados como pendientes en `01_Alcance.md`.
