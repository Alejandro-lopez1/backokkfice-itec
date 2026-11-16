# Documentación de Defensa — Backoffice Académico (ITEC N°1)

> Material de estudio para el final. Recorre: el problema que resuelve, cómo está
> diseñado, qué decisiones técnicas se tomaron (y por qué), cómo se cumplieron los
> requerimientos y el guion de demostración en vivo.

---

## 1. Escenario y Planteamiento

### El problema

El instituto ITEC necesita un sistema interno (**backoffice**) para administrar su
operación académica: inscripción de alumnos, dictado de materias en comisiones,
seguimiento de notas y asistencias, y estructura de carreras con planes de estudio.

Hoy esa información se maneja sin un sistema central: no hay control de unicidad
(no se detectan DNIs duplicados), no existe trazabilidad de correlatividades, y las
notas/asistencias se registran en planillas sueltas donde las reglas (por ejemplo,
"promedio ≥ 6 para aprobar") se calculan a mano.

### Restricciones del enunciado

| Restricción | Cómo se cumple |
|---|---|
| Java puro (JDK 17) | Solo librería estándar; **cero dependencias** externas en el `pom.xml` |
| Maven | Build gestionado; el Maven Wrapper hace reproducible el entorno |
| Sin GUI ni web | Interfaz 100% de consola (`System.out` + `Scanner`) |
| Persistencia en memoria | `HashMap` / `ArrayList` estáticos en clases Repository |
| Arquitectura MVC estricta | Capas con reglas de dependencia explícitas (ver sección 2) |

### La solución

Un sistema de consola por menús que cubre el ciclo completo: **personas → oferta
académica → dictado → seguimiento**. Arranca con datos de demostración coherentes
(`DataSeeder`) para poder operar sin carga previa.

---

## 2. Arquitectura

### 2.1 MVC (Modelo - Vista - Controlador)

MVC separa el programa en tres responsabilidades, y a cada capa le asignamos una
regla de dependencia **unidireccional**:

| Capa | Responsabilidad | Puede depender de | Nunca |
|---|---|---|---|
| **View** (`view`) | Menús, entrada/salida de consola | Controllers | Imprimir lógica de negocio, tocar repositorios |
| **Controller** (`controller`) | Orquestar la conversación usuario-sistema | Services | Contener validaciones de negocio |
| **Model** (`model`) | Entidades del dominio (POJOs + enums) | — (solo datos) | Saber quién la usa |

¿Por qué MVC y no todo junto? Porque si mañana la interfaz cambiara (GUI, web),
solo se reescribiría la capa `view`: los controladores, services, repos y entidades
no cambian ni una línea. La separación **compra reemplazabilidad de la UI**.

### 2.2 Capa Service (lógica de negocio pura)

Entre Controller y Repository agregamos `service` con las **validaciones y reglas
académicas**: unicidad de DNI, mail con `@`, materia no correlativa de sí misma,
promedio ≥ 6 para aprobar, alumno activo para inscribirse, etc.

Justificación: si el controlador validara, esa lógica quedaría atrapada en la
interfaz de consola (inaccesible desde otra UI o desde tests). Si el repositorio
validara, mezclaría "dónde se guarda" con "qué reglas existen". El Service es la
única capa que **sabe las reglas**, y todo el sistema las consulta.

Además, el Controller quedó **delgado** (thin controller): cada método es una
línea que delega. La Vista nunca importa un Service ni un Repository: solo ve el
Controller (que reexporta las constantes de mensajes que necesita).

### 2.3 Interface-Impl e Inversión de Dependencias

Cada service y cada repositorio existe como **interfaz** (`AlumnoService`) y
**implementación** (`AlumnoServiceImpl`). Los `Impl` de los services reciben por
constructor las **interfaces** de los repositorios.

Esto materializa el **principio de Inversión de Dependencias (la "D" de SOLID)**:
las capas de arriba no dependen de detalles concretos, sino de abstracciones.

**Beneficio concreto demostrable**: si mañana la persistencia pasa de memoria a
archivo o a una base de datos, se escribe `AlumnoRepositoryImplArchivo` que cumpla
la misma interfaz y se cambia **una sola línea del `Main`**. Nada más se toca.

### 2.4 El `Main` como cableador (IoC manual)

No usamos Spring ni contenedor de DI: la inyección es **por constructor, manual**,
y ocurre en un único lugar — el `Main` (composition root):

```java
AlumnoRepository repo = new AlumnoRepositoryImpl();
AlumnoService service = new AlumnoServiceImpl(repo);
AlumnoController controller = new AlumnoController(service);
AlumnoView view = new AlumnoView(controller);
```

Este patrón se llama **Inversión de Control manual**: el flujo normal sería que
cada objeto cree sus propias dependencias (acoplamiento fuerte); invirtiéndolo,
un punto externo (`Main`) construye el grafo completo de objetos y "invierte"
quién decide las implementaciones. Es exactamente lo que hace Spring por
debajo, pero explícito, sin magia — y en este proyecto, sin dependencias.

---

## 3. Decisiones Técnicas y Bugs Corregidos

Esta sección es oro para el final: muestra que los errores se diagnosticaron y
resolvieron con criterio, no por azar.

### 3.1 El doble `Scanner` sobre `System.in`

**Síntoma**: el menú principal y el menú de alumnos se "comían" líneas de entrada
entre sí (`NoSuchElementException` / opciones perdidas).

**Causa**: cada vista creaba su propio `new Scanner(System.in)`. Un `Scanner`
bufferiza el stream; con dos instancias leyendo el mismo `System.in`, los buffers
compiten y se pierden líneas.

**Solución**: clase `ConsoleInput` con un **único `Scanner` estático** y un método
`leerLinea(mensaje)`. Todas las vistas leen a través de él.

**Lección**: los recursos de I/O compartidos pertenecen a la aplicación, no a cada
clase de UI. Es un singleton de recurso, aplicado por necesidad técnica, no por moda.

### 3.2 Re-claveo de Materias al renombrar

**Problema**: el repositorio de materias usa el **nombre como clave** del `HashMap`.
Modificar el nombre de una materia no es un simple `set`: la clave vieja queda
huérfana y la nueva no existe.

**Solución**: en `MateriaServiceImpl.modificarMateria`:

```java
if (cambiaNombre) {
    repository.eliminar(originalLimpio);   // saca la clave vieja
    materia.setNombre(nuevoLimpio);
    repository.guardar(materia);           // reinserta con la clave nueva
}
```

Puntos finos que cuidamos:
- Se **re-inserta el mismo objeto** (`Materia`), no una copia: sus correlativas y
  las referencias desde planes de estudio quedan intactas.
- El nuevo nombre valida contra `existeNombre` **solo si realmente cambia**
  (comparación `equalsIgnoreCase`), para no chocar consigo mismo.
- Se agregó `eliminar(String)` a la **interfaz** del repositorio (no un hack
  externo): la capacidad de re-clavear es parte del contrato de persistencia.

### 3.3 Colecciones `static final` para simular la base de datos

**Decisión**: las colecciones de los `*RepositoryImpl` son `private static final`
(`Map` / `List`), y los métodos son de instancia sobre esas colecciones estáticas.

- `static`: existe una sola "tabla" por repositorio, compartida por todas las
  instancias (incluso las que crea el `DataSeeder` y las del `Main`): los datos
  viven durante toda la ejecución.
- `final`: nadie puede reemplazar la colección; solo se muta el contenido, como
  una tabla real de una base de datos.
- `obtenerTodos()` devuelve una **copia** (`new ArrayList<>(...)`): las capas
  altas pueden leer/ordenar libremente sin poder borrar de la "tabla"; toda
  modificación pasa por los métodos del repositorio (upsert vía `guardar`).
- El contador `static int siguienteId` de `ComisionRepository` simula un
  `AUTO_INCREMENT` de SQL: las comisiones no tienen clave natural, el repositorio
  les asigna id al insertarlas.

### 3.4 Copia defensiva: `List.of()` vs `ArrayList` (el bug de `UnsupportedOperationException`)

**Síntoma**: al agregar una correlativa a una materia sembrada por el `DataSeeder`:

```
java.lang.UnsupportedOperationException at java.util.ImmutableCollections.add
```

**Causa raíz**: el constructor de `Materia` guardaba la lista recibida **por
referencia**:

```java
// ANTES (bug)
this.listaCorrelativas = listaCorrelativas != null ? listaCorrelativas : new ArrayList<>();
```

El seeder llamaba `new Materia(..., List.of(programacionI))`. `List.of()` crea una
lista **inmutable** (su `add` lanza `UnsupportedOperationException` por diseño).
La entidad quedó "envenenada": su campo interno apuntaba a una lista que jamás
podría crecer. Nota: el constructor vacío estaba bien; el bug estaba en el
constructor con parámetros.

**Solución (dos frentes)**:

1. **Copia defensiva** en los constructores con lista de `Materia`, `PlanEstudio`
   y `Carrera` (los tres tenían el mismo patrón):

   ```java
   // DESPUÉS (fix)
   this.listaCorrelativas = listaCorrelativas != null ? new ArrayList<>(listaCorrelativas) : new ArrayList<>();
   ```

   La entidad **copia** lo recibido y a partir de ahí su estado interno es propio
   y mutable. El `DataSeeder` sigue usando `List.of(...)` y eso es correcto: del
   lado del llamador expresa "este conjunto, tal como lo armo, no se modifica acá".

2. **Encapsulamiento del modelo**: se agregó `Materia.agregarCorrelativa(Materia)`
   y el service llama a ese método en vez de `getListaCorrelativas().add(...)`.
   El modelo protege su propio invariante; el service expresa la intención.

**Lección para el final**: en Java, la inmutabilidad de `List.of()` se propaga por
referencia. Toda entidad que reciba colecciones debe decidir si las copia
(defensive copy) o si asume la propiedad del objeto recibido — y documentarlo.
Fue un bug real, detectado por stacktrace, diagnosticado a nivel de diseño y
corregido en los tres lugares donde podía repetirse (revisión preventiva).

### 3.5 Otras decisiones que conviene poder defender

- **Mensajes de negocio en el Service como constantes** (`MSG_DNI_DUPLICADO`, ...):
  el service *decide* qué pasó; la vista solo *muestra*. El controller reexporta
  las constantes que la vista necesita (la vista sigue sin conocer el service).
- **Baja lógica** en personas y materias (nunca borrado físico): se desactiva con
  un booleano; los históricos (inscripciones, notas) mantienen sus referencias.
- **`LocalDate`** para fechas en vez de `String`: comparaciones correctas y parseo
  con `DateTimeFormatter dd/MM/yyyy` en las vistas.
- **IDs autoincrementales** solo donde no hay clave natural (comisiones): el resto
  usa claves de negocio (DNI, nombre).
- **DataSeeder escribe directo a los repositorios** (sin pasar por services): único
  caso justificado, porque los datos son válidos por construcción y la carga es
  una sola vez por arranque (bandera `yaCargado`).

---

## 4. Cumplimiento de User Stories / Casos de Uso

| Como... | quiero... | para... | Implementado en |
|---|---|---|---|
| Secretaría | registrar un alumno nuevo | habilitar su legajo | `AlumnoServiceImpl.crearAlumno` (DNI único, mail, campos obligatorios) |
| Secretaría | dar de baja un alumno | depurar el padrón sin perder historial | `darDeBajaLogica` (activo=false; rechaza doble baja) |
| Secretaría | consultar el padrón | reportes y atención | `listarTodos` (DNI, nombre, mail, estado) |
| Coordinación | cargar profesores | asignar docentes | `ProfesorServiceImpl` (mismo patrón) |
| Coordinación | crear materias con correlatividades | modelar la malla | `MateriaServiceImpl` + `Materia.agregarCorrelativa` (relación reflexiva) |
| Coordinación | definir carreras y planes | ordenar la oferta académica | `CarreraServiceImpl` (Carrera → Plan → Materias) |
| Coordinación | abrir comisiones por cuatrimestre | programar el dictado | `ComisionServiceImpl.crearComision` (materia y profesor activos, fechas, sin duplicados, ID autoincremental) |
| Alumno | inscribirse a una comisión | cursar | `inscribirAlumno` (alumno activo, comisión existente, sin duplicados; estado inicial `REGULAR`) |
| Docente | cargar notas por evaluación | evaluar | `NotaServiceImpl.cargarNota` (rango 0–10, crea `Examen`+`Nota` sobre el `AlumnoInscripto`) |
| Docente | saber quién aprobó | cerrar cursada | promedio ≥ 6 → estado `APROBADO` + nota final automática |
| Docente | tomar asistencia | control de cursada | `AsistenciaServiceImpl.registrarAsistencia` (presente/ausente con `LocalDate.now()`) + listado por comisión |

Cada caso de uso atraviesa las capas de forma idéntica: **View (pide datos) →
Controller (delega) → Service (valida y decide) → Repository (guarda/consulta)**.
Esa uniformidad es lo que hace mantenible el sistema.

---

## 5. Guía de Demo (Paso a paso para el final)

Todo arranca con datos precargados, así que la demo no requiere altas previas.
Referencias rápidas de datos sembrados:

- **Alumnos**: Juan Perez `40123456` (activo), Maria Gomez `40234567`,
  Carlos Sanchez `40345678`, Lucia Fernandez `40456789`, **Diego Torres
  `40567890` (INACTIVO, para probar la baja lógica)**.
- **Profesores**: Ana Rodriguez `20111222`, Luis Martinez `20222333`, etc.
- **Materias**: Programacion I, Programacion II (correlativa de P.I), Matematica
  Discreta, Base de Datos (correlativas de P.I y Mat.Discreta), Redes de
  Computadoras (correlativa de B.D).
- **Comisiones**: **ID 1** = Programacion I (2026-C1, Ana Rodriguez) — **ID 2** =
  Base de Datos (2026-C2, Luis Martinez).
- **Carreras**: Tecnicatura en Programacion y Licenciatura en Sistemas, ambas con
  "Plan 2024" y materias asociadas.

### Paso 0 — Arranque (30 seg)

```bash
.\mvnw.cmd clean compile exec:java
```

Mostrar el menú principal (7 módulos) y mencionar: "el `DataSeeder` ya cargó un
semestre completo, puedo operar de inmediato".

### Paso 1 — Listar alumnos (opción 1 → 4)

Mostrar el listado con DNI, nombre, mail y **estado**. Destacar a Diego Torres
como `INACTIVO`: "la baja es lógica, el historial se conserva".

**Mini-extra (opción 3)**: intentar dar de baja de nuevo a `40567890` →
`ERROR: el alumno ya se encuentra inactivo` (regla de negocio defendida).

### Paso 2 — Validaciones de alta (opción 1 → 1)

Crear un alumno con el **DNI de un sembrado** (`40123456`) →
`ERROR: DNI ya registrado`. Reintentar con mail sin `@` → `ERROR: mail invalido`.
Finalmente crear uno válido → `Alumno creado con exito`.

### Paso 3 — Correlativas (opción 3 → 2 / 5)

- **Buscar** (opción 2) `Base de Datos` → mostrar `Correlativas: Programacion I,
  Matematica Discreta`.
- **Agregar correlativa** (opción 5): intentar `Programacion I` de sí misma →
  `ERROR: una materia no puede ser correlativa de si misma` (relación reflexiva
  del UML validada). Luego agregar una válida (ej: `Redes de Computadoras` ←
  `Programacion II`) → `Correlativa agregada con exito`, y rebuscar para verla.

### Paso 4 — Carreras y planes (opción 7 → 4)

Mostrar el listado jerárquico: carrera → plan (rango de años) → materias con año,
cuatrimestre, estado y correlativas. Frase: "el plan comparte las mismas
instancias de materia del repositorio; no hay copias desincronizadas".

### Paso 5 — Inscripción (opción 4 → 3)

Inscribir a Juan Perez (`40123456`) en la comisión **ID 1**:
- La vista **lista las comisiones antes de pedir el ID** (usabilidad).
- Mostrar `Alumno inscripto con exito (comision ID 1)`.
- Repetir → `ERROR: el alumno ya esta inscripto en esa comision` (unicidad).
- Inscribir a Diego `40567890` → `ERROR: el alumno esta inactivo` (regla).

### Paso 6 — Notas y promoción (opción 5 → 1)

Cargar nota a Juan en comisión 1:
1. Tipo **TP**, valor `8` → `Nota cargada con exito (promedio: 8,00).
   El alumno quedo APROBADO (promedio >= 6)` ← **momento estrella**.
2. Cargar otra (PARCIAL, `6.5`) → `promedio: 6,75. El alumno ya se encontraba
   APROBADO` (el sistema recalcula y no degrada el estado).
3. Valor inválido `20` → `ERROR: la nota debe estar entre 0 y 10`.

### Paso 7 — Asistencias (opción 6 → 1, luego 2)

- Registrar a Juan, comisión 1, `S` → `Asistencia registrada con exito (fecha)`.
- Registrar de nuevo con `N` → en el listado aparece como `AUSENTE`.
- Intentar con alguien no inscripto (Maria `40234567`) →
  `ERROR: el alumno no esta inscripto en esa comision` (integridad referencial).
- Opción 2: listar asistencias de la comisión 1 →
  `16/11/2026 | PRESENTE | Perez, Juan` etc.

### Paso 8 — Cierre técnico (1 min)

Abrir `Main.java` y leer el cableado en voz alta: *"el Main es el único que conoce
implementaciones concretas; todo lo demás depende de interfaces. Si mañana la
persistencia fuera una base de datos, escribo un nuevo RepositoryImpl y cambio una
línea acá"* — y señalar el diagrama del README (View → Controller → Service →
Repository).

### Tiempos sugeridos

| Bloque | Duración |
|---|---|
| Paso 0-1 (arranque + padrón) | 2 min |
| Paso 2-3 (validaciones + correlativas) | 3 min |
| Paso 4 (carreras) | 1 min |
| Paso 5-7 (inscripción, notas, asistencias) | 5 min |
| Paso 8 (cierre técnico) | 1 min |
| **Total** | **~12 min** |

### Preguntas probables del tribunal (y respuesta en una línea)

- *"¿Por qué una capa Service?"* → Para que las reglas de negocio vivan en un solo
  lugar, reutilizables desde cualquier UI y testeables sin consola.
- *"¿Por qué interfaces si hay una sola implementación?"* → Inversión de
  dependencias: el día que agregue una segunda implementación (BD, archivo), el
  resto del sistema no cambia.
- *"¿Dónde está la lógica de negocio?"* → En `*ServiceImpl`. Los controllers son
  delgados y las vistas no deciden nada.
- *"¿Qué pasa si renombro una materia que está en un plan?"* → Se re-clavea en el
  repositorio manteniendo el mismo objeto: el plan conserva la referencia.
- *"¿Cómo evitan inscribir dos veces al mismo alumno?"* → El service recorre los
  inscriptos de la comisión y compara la identidad del alumno.
