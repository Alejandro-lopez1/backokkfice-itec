# Documentación de Defensa — Backoffice Académico (ITEC N°1)

> Guía de estudio para el final. Recorre: el problema que resuelve el MVP, las
> decisiones arquitectónicas (y su justificación), los problemas técnicos
> reales que se diagnosticaron y corrigieron, el cumplimiento de las user
> stories, el análisis de la Fase 2 (Anexo) y el guion de demostración en vivo.

---

## 1. Escenario y Planteamiento

### El problema del ITEC N°1

El instituto necesita un sistema interno (**backoffice**) para administrar su
operación académica: inscripción de alumnos, dictado de materias en comisiones,
seguimiento de notas y asistencias, y estructura de carreras con planes de estudio.

Hoy esa información se maneja sin un sistema central:

- **Sin control de unicidad**: DNIs y nombres duplicados no se detectan.
- **Sin trazabilidad de correlatividades**: la malla vive en papel.
- **Reglas calculadas a mano**: "promedio ≥ 6 para aprobar" en planillas sueltas.

### Por qué consola + memoria es un MVP correcto (no una limitación)

El enunciado pide un **Producto Mínimo Viable** que demuestre el modelo de
dominio y la arquitectura — y eso es exactamente lo que entrega esta solución:

| Decisión del MVP | Lo que demuestra | Lo que NO condiciona |
|---|---|---|
| Interfaz de **consola** | Que la lógica está separada de la UI (MVC) | Cambiar a GUI/web solo reescribe la capa `view` |
| Persistencia **en memoria** | Que la lógica está separada del almacenamiento (Interface-Impl) | Cambiar a archivo/BD solo agrega nuevos `*RepositoryImpl` |
| **Java puro** (JDK 17, cero dependencias) | Dominio del lenguaje y de la librería estándar, sin frameworks que "resuelvan solo" | El diseño es agnóstico del ecosistema |

Un MVP es válido cuando **cada restricción es reemplazable sin tocar el resto**:
esa es la tesis que la demo y esta documentación sostienen.

Al arrancar (después del login del `AuthView`), el `DataSeeder` carga datos
de demostración coherentes (alumnos, profesores, materias con correlativas,
comisiones, carreras y planes ya vinculados), para poder operar sin carga
previa.

---

## 2. Decisiones Arquitectónicas Clave

### 2.1 MVC + Capa Service, con Thin Controllers

MVC separa el programa en tres responsabilidades con una regla de dependencia
**unidireccional**. Entre Controller y Repository agregamos `service`, la única
capa que **sabe las reglas** (unicidad de DNI, mail con `@`, correlativas,
promedio ≥ 6, alumno activo para inscribirse, etc.).

| Capa | Responsabilidad | Puede depender de | Nunca |
|---|---|---|---|
| **View** (`view`) | Menús, entrada/salida de consola | Controllers | Lógica de negocio, repositorios |
| **Controller** (`controller`) | Orquestar la conversación usuario-sistema | Services | Validar negocio |
| **Service** (`service`) | Reglas académicas y mensajes de resultado | Repositories | — |
| **Model** (`model`) | Entidades POJO + enums del dominio | — (solo datos) | Saber quién la usa |

¿Por qué y no "todo en el controller"? Porque las validaciones serían dos cosas
malas: **atrapadas en la consola** (inaccesibles para otra UI o tests) o
**duplicadas** en cada vista. El Service centraliza; el Controller queda
**delgado**: cada método es una línea que delega. La vista nunca importa un
Service — el controller le reexporta solo las constantes de mensajes que
necesita.

**Frase para la defensa**: *"La separación compra reemplazabilidad: si mañana la
interfaz cambiara, se reescribe solo `view`; las reglas y los datos no cambian
ni una línea."*

### 2.2 Interfaces + `Impl` para Inversión de Dependencias (SOLID)

Cada service y cada repositorio existe como **interfaz** (`AlumnoService`) y
**implementación** (`AlumnoServiceImpl`). Los `Impl` de los services reciben por
constructor las **interfaces** de los repositorios.

Esto materializa el **principio de Inversión de Dependencias (la "D" de SOLID)**:
las capas de arriba dependen de abstracciones, no de detalles concretos.

**Beneficio demostrable**: si la persistencia pasa de memoria a archivo o a una
base de datos, se escribe `AlumnoRepositoryImplArchivo` que cumpla la misma
interfaz y se cambia **una sola línea del `Main`**. Nada más se toca.

### 2.3 El `Main` como contenedor de IoC manual

No usamos Spring ni ningún contenedor: la inyección es **por constructor y
manual**, en un único punto — el `Main` (composition root), de abajo hacia
arriba:

```java
AlumnoRepository repo = new AlumnoRepositoryImpl();          // 1. Repos
DataSeeder.cargarDatosIniciales(repo, ...);                  // 2. Datos demo
AlumnoService service = new AlumnoServiceImpl(repo);         // 3. Services
AlumnoController controller = new AlumnoController(service); // 4. Controllers
AlumnoView view = new AlumnoView(controller);                // 5. Views
```

Flujo normal sería que cada objeto cree sus propias dependencias (acoplamiento
fuerte). **Invertimos el control**: un punto externo (`Main`) construye el grafo
completo y decide las implementaciones. Es exactamente lo que hace Spring por
debajo, pero explícito y sin magia — `Main` es la **única** clase que conoce
implementaciones concretas; el resto del sistema trabaja solo con interfaces.

### 2.4 Colecciones `static final` como simulación de base de datos

Las colecciones de los `*RepositoryImpl` son `private static final`
(`Map` / `List`), con métodos de instancia sobre ellas:

- **`static`**: existe una sola "tabla" por repositorio, compartida por todas
  las instancias (las del `DataSeeder` y las del `Main` incluidas). Los datos
  viven durante toda la ejecución.
- **`final`**: nadie puede reemplazar la colección; solo se muta el contenido,
  como una tabla real.
- **Encapsulamiento**: `obtenerTodos()` devuelve una **copia** defensiva; las
  capas altas pueden leer/ordenar libremente sin poder borrar de la "tabla".
  Toda modificación pasa por los métodos del repositorio (upsert vía `guardar`).
- El contador `static int siguienteId` de `ComisionRepositoryImpl` simula un
  `AUTO_INCREMENT` de SQL para entidades sin clave natural.

---

## 3. Resolución de Problemas Técnicos

Esta sección es la más importante del proyecto: muestra que los errores se
detectaron por stacktrace, se diagnosticaron a nivel de diseño y se corrigieron
con criterio.

### 3.1 Bug 1: el doble `Scanner` sobre `System.in`

**Síntoma**: el menú principal y el menú de alumnos se "comían" líneas de
entrada entre sí (`NoSuchElementException` / opciones perdidas).

**Causa**: cada vista creaba su propio `new Scanner(System.in)`. Un `Scanner`
bufferiza el stream; con dos instancias leyendo el mismo `System.in`, los
buffers compiten y se pierden líneas.

**Solución**: clase `ConsoleInput` con un **único `Scanner` estático** y el
método `leerLinea(mensaje)`. Todas las vistas leen a través de él.

**Lección**: los recursos de I/O compartidos pertenecen a la aplicación, no a
cada clase de UI. Es un singleton de recurso por necesidad técnica.

### 3.2 Bug 2: inmutabilidad de `List.of()` → `UnsupportedOperationException`

**Síntoma**: al agregar una correlativa a una materia sembrada por el
`DataSeeder`:

```
java.lang.UnsupportedOperationException at java.util.ImmutableCollections.add
```

**Causa raíz**: el constructor de `Materia` guardaba la lista recibida **por
referencia**:

```java
// ANTES (bug)
this.listaCorrelativas = listaCorrelativas != null ? listaCorrelativas : new ArrayList<>();
```

El seeder llamaba `new Materia(..., List.of(programacionI))`. `List.of()` crea
una lista **inmutable** (su `add` lanza la excepción por diseño). La entidad
quedó "envenenada": su campo interno apuntaba a una lista que jamás podría
crecer. (El constructor vacío estaba bien; el bug estaba en el constructor con
parámetros.)

**Solución (dos frentes)**:

1. **Copia defensiva** en los constructores con lista de `Materia`,
   `PlanEstudio` y `Carrera` (los tres tenían el mismo patrón — revisión
   preventiva):

   ```java
   // DESPUÉS (fix)
   this.listaCorrelativas = listaCorrelativas != null ? new ArrayList<>(listaCorrelativas) : new ArrayList<>();
   ```

   La entidad **copia** lo recibido: su estado interno es propio y mutable.
   El `DataSeeder` sigue usando `List.of(...)` y eso es correcto: del lado del
   llamador expresa "este conjunto, tal como lo armo, no se modifica acá".

2. **Encapsulamiento del modelo**: se agregó `Materia.agregarCorrelativa(Materia)`
   y el service llama a ese método en vez de `getListaCorrelativas().add(...)`.
   El modelo protege su invariante interno; el service expresa la intención.

**Lección para el final**: la inmutabilidad de `List.of()` se **propaga por
referencia**. Toda entidad que reciba colecciones debe decidir si las copia
(defensive copy) o asume la propiedad del objeto recibido — y documentarlo.

### 3.3 Bug 3: re-claveo de Materias al renombrar

**Problema**: el repositorio de materias usa el **nombre como clave** del
`HashMap`. Renombrar no es un simple `set`: la clave vieja queda huérfana y la
nueva no existe.

**Solución** en `MateriaServiceImpl.modificarMateria`:

```java
if (cambiaNombre) {
    repository.eliminar(originalLimpio);   // saca la clave vieja
    materia.setNombre(nuevoLimpio);
    repository.guardar(materia);           // reinserta con la clave nueva
}
```

Puntos finos que hay que poder defender:

- Se **re-inserta el mismo objeto** (`Materia`), no una copia: sus correlativas
  y las referencias desde los planes de estudio quedan intactas.
- El nombre nuevo valida contra `existeNombre` **solo si realmente cambia**
  (`equalsIgnoreCase`), para no chocar consigo mismo.
- `eliminar(String)` se agregó a la **interfaz** del repositorio (no como hack
  externo): la capacidad de re-clavear es parte del contrato de persistencia.

### 3.4 Otras decisiones defendibles

- **Mensajes de negocio como constantes en el Service** (`MSG_*`): el service
  *decide* qué pasó; la vista solo *muestra*.
- **Baja lógica** (personas y materias): nunca borrado físico; los históricos
  conservan sus referencias.
- **`LocalDate`** en vez de `String` para fechas: comparaciones correctas,
  parseo `dd/MM/yyyy` en las vistas.
- **Claves de negocio** (DNI, nombre) para entidades con identidad natural;
  ID autoincremental solo para comisiones.
- **DataSeeder escribe directo a los repositorios**: única excepción
  justificada (datos válidos por construcción, carga una sola vez, bandera
  `yaCargado`).

---

## 4. Cumplimiento de Requerimientos (User Stories)

Las cuatro US principales del enunciado y dónde quedó implementada cada una:

| User Story | Implementado en | Validaciones que la sostienen |
|---|---|---|
| **Alta de alumnos** | `AlumnoServiceImpl.crearAlumno` | DNI único, mail con `@`, campos obligatorios; baja lógica separada (`darDeBajaLogica` rechaza doble baja) |
| **Correlativas** | `MateriaServiceImpl.agregarCorrelativa` + `Materia.agregarCorrelativa(Materia)` | No puede ser correlativa de sí misma, no se duplica, materia existente y activa |
| **Inscripciones** | `ComisionServiceImpl.inscribirAlumno` | Alumno activo, comisión existente, sin duplicados; estado inicial `REGULAR` |
| **Notas** | `NotaServiceImpl.cargarNota` | Rango 0–10 por tipo de evaluación (TP, PARCIAL, RECUPERATORIO, FINAL); crea `Examen` + `Nota` sobre el `AlumnoInscripto`; **promedio ≥ 6 → APROBADO** con nota final |

Las US restantes siguen el mismo patrón de resolución (todas recorren las capas
de forma idéntica: **View pide datos → Controller delega → Service valida y
decide → Repository guarda/consulta**):

- **Profesores**: alta/búsqueda/listado/baja lógica (`ProfesorServiceImpl`).
- **Materias**: ABM con re-claveo al renombrar + listado completo con estado.
- **Comisiones**: alta con materia y profesor activos, fechas por cuatrimestre,
  ID autoincremental, anti-duplicados.
- **Asistencias**: presente/ausente con fecha, listado por comisión.
- **Carreras y planes**: alta con nombre único, planes con rango de años
  coherente, asociación de materias, listado jerárquico Carrera → Plan →
  Materias.

Esa uniformidad transversal es en sí un argumento de defensa: el sistema tiene
**un** estilo de flujo, no siete.

---

## 5. Análisis del Anexo (Fase 2)

El modelo actual no solo resuelve el MVP: **sienta las bases** de los
requerimientos del Anexo sin haber escrito todavía ni una línea de Fase 2.

### 5.1 Regularidad (70% de asistencia)

La regla es: un alumno mantiene la regularidad si asiste al menos al **70% de
las clases** de la comisión. Los datos necesarios ya existen en el modelo:

- `Asistencia` ya registra **PRESENTE / AUSENTE por fecha** y por alumno.
- `AlumnoInscripto` ya vincula alumno ↔ comisión con su estado de cursada.

Implementación futura: un método `calcularPorcentajeAsistencia(AlumnoInscripto)`
en un service (o en el mismo `AsistenciaService`) que divida presentes sobre el
total de registros — **lectura pura sobre datos que ya se persisten**, sin
cambiar el modelo.

### 5.2 Justificación de faltas

Hoy una ausencia es `AUSENTE`. La evolución natural es un tercer estado
(`AUSENTE_JUSTIFICADA`) en el dominio de asistencia. La arquitectura lo permite
directo:

- El modelo ya usa **enums** (`EstadoCursada`, `TipoEvaluacion`): agregar un
  valor es coherente con la convención existente.
- El repositorio guarda objetos (no filas fijas): **no hay migración de
  esquema**, solo se extiende el dominio y se agrega la regla en el service
  (ej: "una falta justificada no descuenta del 70%").

### 5.3 Auditoría (y `AuditService` sin tocar el resto)

Requerimiento: registrar quién modificó qué y cuándo. La arquitectura en capas
con Interface-Impl lo resuelve **sin refactor**:

1. Se crea `AuditService` (interfaz) + `AuditServiceImpl` y su
   `AuditRepositoryImpl` — capas nuevas, en su paquete.
2. Se cablea en el `Main` (composition root), que es el único lugar que toca
   implementaciones.
3. Los `*ServiceImpl` existentes lo reciben por constructor y **agregan una
   línea** donde corresponda (ej: al renombrar una materia, se registra el
   evento). Las vistas y los controllers **no cambian**: conocen services
   existentes, y la auditoría corre por debajo.

Este es el argumento fuerte de la defensa: *"porque dependemos de interfaces y
el flujo pasa por un único punto de composición, agregar un servicio
transversal como auditoría no exige modificar el sistema — solo extenderlo"*.
Otras extensiones del mismo estilo: **Reportes** (agregaciones sobre
`obtenerTodos()`, que ya devuelve copias seguras) y **Actas** (el vínculo
`AlumnoInscripto → Examen → Nota` con estado `APROBADO` ya está modelado).

---

## 6. Guía para la Demo en Vivo

Todo arranca con datos precargados (`DataSeeder`): la demo no requiere altas
previas. Referencia rápida de datos sembrados:

- **Alumnos**: Juan Perez `40123456` (activo), Maria Gomez `40234567`,
  Carlos Sanchez `40345678`, Lucia Fernandez `40456789`,
  **Diego Torres `40567890` (INACTIVO)**.
- **Profesores**: Ana Rodriguez `20111222`, Luis Martinez `20222333`.
- **Materias**: Programacion I, Programacion II (correlativa: P.I),
  Matematica Discreta, Base de Datos (correlativas: P.I y Mat.Discreta),
  Redes de Computadoras (correlativa: B.D).
- **Comisiones**: **ID 1** = Programacion I (2026-C1, Ana Rodriguez) —
  **ID 2** = Base de Datos (2026-C2, Luis Martinez).

Arranque: `.\mvnw.cmd clean compile exec:java`. **Paso 0 — login**: el
sistema pide usuario y contraseña antes que nada; ingresar `admin` /
`admin123` → `Bienvenido, admin (ADMIN)`. Mencionar: *"el seeder ya
cargó un semestre completo, opero de inmediato"* y que las cuentas son de
demostración (texto plano, alcance académico).

### Paso 1 — Listar Alumnos (opción 1 → 4)

Listado con DNI, nombre, mail y **estado**. Destacar a Diego Torres como
`INACTIVO`: *"la baja es lógica; el historial se conserva"*.

**Extra validación**: opción 3, intentar re-dar de baja a `40567890` →
`ERROR: el alumno ya se encuentra inactivo`.

### Paso 2 — Mostrar Materias y Correlativas (opción 3 → 2)

Buscar `Base de Datos` → `Correlativas: Programacion I, Matematica Discreta`.
Es la relación reflexiva del UML, visible en vivo.

**Extra validación** (opción 5): intentar `Programacion I` como correlativa de
sí misma → `ERROR: una materia no puede ser correlativa de si misma`; luego una
válida → `Correlativa agregada con exito` (y rebuscar para verla).

### Paso 3 — Inscribir Alumno (opción 4 → 3)

La vista **lista las comisiones antes de pedir el ID**. Inscribir a Juan Perez
(`40123456`) en la comisión **ID 1** → `Alumno inscripto con exito`.

**Extras de validación**: repetir → `ERROR: el alumno ya esta inscripto en esa
comision`; inscribir a Diego `40567890` → `ERROR: el alumno esta inactivo`.

### Paso 4 — Cargar Nota (opción 5 → 1)

Cargar a Juan, comisión 1, tipo **TP**, valor `8`:

```
Nota cargada con exito (promedio: 8,00). El alumno quedo APROBADO (promedio >= 6)
```

← **momento estrella de la demo**: la regla académica se aplica automáticamente.

**Extras**: otra nota (PARCIAL `6.5`) → recalcula a `6,75` sin degradar el
estado; valor `20` → `ERROR: la nota debe estar entre 0 y 10`.

### Paso 5 — Mostrar Asistencia (opción 6 → 1, luego 2)

- Registrar a Juan, comisión 1, `S` → `Asistencia registrada con exito (fecha)`.
- Registrar otra con `N` → en el listado aparece `AUSENTE`.
- Intentar con Maria `40234567` (no inscripta) →
  `ERROR: el alumno no esta inscripto en esa comision` (integridad referencial).
- Opción 2: listar asistencias de la comisión 1 →
  `16/11/2026 | PRESENTE | Perez, Juan`.

**Cierre técnico (1 min)**: abrir `Main.java` y leer el cableado en voz alta:
*"el Main es el único que conoce implementaciones concretas; todo lo demás
depende de interfaces. Si mañana la persistencia fuera una base de datos,
escribo un nuevo RepositoryImpl y cambio una línea acá"* — y señalar el
diagrama Mermaid del README.

### Tiempos sugeridos

| Bloque | Duración |
|---|---|
| Arranque + Paso 1 | 2 min |
| Pasos 2-3 (correlativas + inscripción) | 4 min |
| Pasos 4-5 (notas + asistencias) | 4 min |
| Cierre técnico | 1 min |
| **Total** | **~11 min** |

### Preguntas probables del tribunal (respuesta en una línea)

- *"¿Por qué una capa Service?"* → Las reglas de negocio viven en un solo
  lugar, reutilizables desde cualquier UI y testeables sin consola.
- *"¿Por qué interfaces si hay una sola implementación?"* → Inversión de
  dependencias: el día que exista una segunda (BD, archivo), el resto no cambia.
- *"¿Dónde está la lógica de negocio?"* → En los `*ServiceImpl`; controllers
  delgados, vistas que no deciden nada.
- *"¿Qué pasa si renombro una materia que está en un plan?"* → Se re-clavea el
  mismo objeto: el plan conserva la referencia.
- *"¿Cómo implementarían el 70% del Anexo?"* → Leyendo las `Asistencia` ya
  persistidas por `AlumnoInscripto`: es un cálculo sobre datos existentes.
- *"¿Los services verifican el rol del usuario?"* → No: el control ocurre
  **solo al armar el menú** (ADMIN/DOCENTE/ALUMNO); los `*Service` no
  reciben el usuario. Es el límite documentado en RN-AUTH-04, pendiente de
  Fase 2 junto con la gestión de cuentas.
- *"¿Los tests están automatizados?"* → No: los 74 TC de
  `05_Casos_de_Prueba.md` están **documentados** para ejecución manual y no
  se ejecutaron caso por caso (no hay JUnit ni `src/test` en el repo); lo
  verificado hasta ahora son pruebas de humo de los flujos principales. La
  arquitectura por capas permite automatizarlos sin tocar el código.
- *"¿Y la auditoría?"* → `AuditService` + `AuditRepositoryImpl` nuevos,
  cableados en el `Main`; los services existentes agregan una línea, nada más.

## 7. Alineación con el UML

La referencia visual del modelo es `document/Diagrama_UML_Modelo.png`.
El paquete `model` fue auditado contra ese diagrama y coincide **1:1 en
clases, atributos y relaciones**:

| UML | Código |
|---|---|
| **14 entidades** | `Carrera`, `PlanEstudio`, `Materia`, `Cuatrimestre`, `Profesor`, `ComisionMateria`, `HorarioClase`, `ModuloHorario`, `Examen`, `Nota`, `Asistencia`, `AlumnoInscripto`, `AlumnoCarrera`, `Alumno` |
| **3 enums de dominio** | `TipoEvaluacion` (TP, PARCIAL, RECUPERATORIO, FINAL), `EstadoCursada` (REGULAR, APROBADO, RECURSA), `DayOfWeek` (enum estándar del JDK, elegido en lugar de duplicarlo) |

Todas las relaciones del diagrama están materializadas como referencias o
listas en el extremo que marca el UML:

- Composiciones: `Carrera→PlanEstudio`, `PlanEstudio→Materia`,
  `PlanEstudio→AlumnoCarrera`, `Cuatrimestre→ComisionMateria`,
  `Profesor→ComisionMateria`, `Examen→Nota`, `AlumnoCarrera→Alumno/Carrera`.
- Agregaciones: `ComisionMateria◇horarios / examenes / alumnosInscriptos /
  asistencias`, `HorarioClase◇modulos`, `AlumnoInscripto◇notas / asistencias`,
  `Examen◇notas`, `Nota◇alumnoInscripto`.
- Asociaciones: `Materia↔Materia` (correlativas, reflexiva),
  `HorarioClase→DayOfWeek`, `Examen→TipoEvaluacion`,
  `AlumnoInscripto→EstadoCursada`, `Asistencia→ComisionMateria`,
  `Alumno→AlumnoCarrera` (con navegabilidad inversa `carreras`),
  `AlumnoCarrera→AlumnoInscripto` (rol `inscripciones`).
- Los servicios conectan las colecciones: `NotaServiceImpl.cargarNota`
  agrega el `Examen` a `comision.getExamenes()` y la `Nota` a
  `examen.getNotas()`; `AsistenciaServiceImpl.registrarAsistencia` agrega la
  `Asistencia` a `comision.getAsistencias()` y a
  `alumnoInscripto.getAsistencias()`, y le setea su comisión.

### Atributos extra presentes en el código (decisiones de implementación)

El código mantiene atributos que el diagrama no dibuja. **No son
desviaciones del modelo: son decisiones necesarias para que las reglas de
negocio y la persistencia en memoria funcionen**, y están documentadas en el
Javadoc de cada clase:

| Atributo | Clases | Justificación |
|---|---|---|
| `activo` / `activa` | `Alumno`, `Profesor`, `Materia` | Soporte de la **baja lógica** exigida por las RN (RN-ALU-04, RN-PRO-04, RN-MAT-04): el historial de inscripciones, notas y asistencias se conserva aunque la persona/materia esté dada de baja. |
| `id` | `ComisionMateria` | Clave del `ComisionRepository` en memoria (RN-COM-07): la inscripción, las notas y las asistencias se buscan "alumno + ID de comisión". |
| `comisiones` | `Cuatrimestre`, `Profesor` | Navegabilidad inversa: en el UML el diamante está en el extremo `ComisionMateria` (los campos `cuatrimestre`/`profesor` son el lado "todo"), pero poder listar las comisiones de un cuatrimestre o de un profesor desde el propio objeto simplifica los listados. |
| `carreras` | `Alumno` | Ídem, inversa de la asociación `Alumno—AlumnoCarrera`. |
| `planEstudio` | `AlumnoCarrera` | Compone la línea `PlanEstudio—AlumnoCarrera` del diagrama. |
| `comision` | `Asistencia` | En el diagrama el diamante de la línea `Asistencia—ComisionMateria` queda en el extremo `Asistencia`. |

Otras decisiones de nomenclatura/alcance, también documentadas:

- El rol `inscriptos` del UML se implementa como
  `ComisionMateria.alumnosInscriptos` (nombre descriptivo consolidado en
  todo el código, vistas y repositorios).
- `Usuario` y `Rol` pertenecen al módulo de autenticación (EPIC-AUTH),
  fuera del alcance del diagrama de dominio académico.
- La composición/agregación no es representable en Java (ambas son
  referencias); la semántica de "quién posee a quién" se refleja en qué
  clase declara el campo y en las copias defensivas de las listas
  (patrón RN-TRA-02).
- `AlumnoCarrera.inscripciones` queda estructural y lista para la Fase 2
  (el MVP no crea inscripciones por carrera).

Con esto, el modelo de objetos cumple el diagrama definitivo sin sacrificar
ninguna regla de negocio (74 casos de prueba **documentados**, no ejecutados
caso por caso — ver `05_Casos_de_Prueba.md`).
