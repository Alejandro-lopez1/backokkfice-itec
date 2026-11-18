# 02 — Reglas de Negocio

> Catálogo formal de TODAS las reglas de negocio que el código implementa
> hoy. Extraídas directamente de los `*ServiceImpl` y repositorios (nada de lo
> listado es aspiracional). El "Mensaje al violarla" es el string **exacto**
> que devuelve el sistema, tal como está definido en las constantes `MSG_*` de
> cada interfaz de service.
>
> Convención de IDs: `RN-<MOD>-<nn>`. La columna Módulo está implícita en el
> encabezado de cada sección.

---

## 1. Reglas de Alumnos (ALU)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-ALU-01 | **DNI único**: no pueden existir dos alumnos con el mismo DNI (clave de negocio del repositorio) | `AlumnoServiceImpl.crearAlumno` → `AlumnoRepositoryImpl.existeDni` | `ERROR: DNI ya registrado` |
| RN-ALU-02 | **Mail válido**: obligatorio y debe contener `@` | `AlumnoServiceImpl.crearAlumno` y `modificarAlumno` | `ERROR: mail invalido (debe contener '@')` |
| RN-ALU-03 | **Campos obligatorios**: nombre, apellido y DNI no pueden quedar vacíos (se recortan espacios; vacío = faltante) | `AlumnoServiceImpl.crearAlumno` y `modificarAlumno` (método `normalizar`) | `ERROR: nombre, apellido y DNI son obligatorios` |
| RN-ALU-04 | **Estado inicial ACTIVO**: todo alumno nuevo se crea con `activo = true` | `AlumnoServiceImpl.crearAlumno` (constructor `Alumno(..., true)`) | — (comportamiento por construcción) |
| RN-ALU-05 | **Baja lógica**: la baja pasa `activo = false`; el registro nunca se elimina | `AlumnoServiceImpl.darDeBajaLogica` | Exito: `Alumno dado de baja (baja logica)` |
| RN-ALU-06 | **Sin doble baja**: no se puede dar de baja a un alumno ya INACTIVO | `AlumnoServiceImpl.darDeBajaLogica` | `ERROR: el alumno ya se encuentra inactivo` |
| RN-ALU-07 | **Alumno INACTIVO no puede inscribirse** a nuevas comisiones | `ComisionServiceImpl.inscribirAlumno` | `ERROR: el alumno esta inactivo` |
| RN-ALU-08 | **DNI no editable** en la modificación: identifica al alumno, solo cambian nombre, apellido, mail y teléfono | `AlumnoServiceImpl.modificarAlumno` (nunca invoca `setDni`) | — (invariante por diseño) |

## 2. Reglas de Profesores (PRO)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-PRO-01 | **DNI único** entre profesores | `ProfesorServiceImpl.crearProfesor` → `ProfesorRepositoryImpl.existeDni` | `ERROR: DNI ya registrado` |
| RN-PRO-02 | **Mail válido**: obligatorio y con `@` | `ProfesorServiceImpl.crearProfesor` y `modificarProfesor` | `ERROR: mail invalido (debe contener '@')` |
| RN-PRO-03 | **Campos obligatorios**: nombre, apellido y DNI | `ProfesorServiceImpl.crearProfesor` y `modificarProfesor` (`normalizar`) | `ERROR: nombre, apellido y DNI son obligatorios` |
| RN-PRO-04 | **Estado inicial ACTIVO** | `ProfesorServiceImpl.crearProfesor` (constructor `Profesor(..., true)`) | — (comportamiento por construcción) |
| RN-PRO-05 | **Baja lógica**: `activo = false`, sin borrado físico | `ProfesorServiceImpl.darDeBajaLogica` | Exito: `Profesor dado de baja (baja logica)` |
| RN-PRO-06 | **Sin doble baja** | `ProfesorServiceImpl.darDeBajaLogica` | `ERROR: el profesor ya se encuentra inactivo` |
| RN-PRO-07 | **Profesor INACTIVO no puede asignarse a nuevas comisiones** | `ComisionServiceImpl.crearComision` | `ERROR: el profesor esta inactivo` |
| RN-PRO-08 | **DNI no editable** en la modificación | `ProfesorServiceImpl.modificarProfesor` (nunca invoca `setDni`) | — (invariante por diseño) |

## 3. Reglas de Materias (MAT)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-MAT-01 | **Nombre obligatorio** (trim; vacío = faltante) | `MateriaServiceImpl.crearMateria` (`normalizar`) | `ERROR: el nombre de la materia es obligatorio` |
| RN-MAT-02 | **Nombre único** (clave de negocio del repositorio; case-insensitive en validación de modificación) | `MateriaServiceImpl.crearMateria` y `modificarMateria` → `MateriaRepositoryImpl.existeNombre` | `ERROR: ya existe una materia con ese nombre` |
| RN-MAT-03 | **Año ≥ 1 y cuatrimestre 1 o 2** (reglas de la malla) | `MateriaServiceImpl.datosValidos` (usado en alta y modificación) | `ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2` |
| RN-MAT-04 | **Estado inicial ACTIVA**, sin correlativas | `MateriaServiceImpl.crearMateria` (constructor `Materia(..., true, null)`) | — (comportamiento por construcción) |
| RN-MAT-05 | **Baja lógica**: `activa = false`, sin borrado físico | `MateriaServiceImpl.darDeBajaLogica` | Exito: `Materia dada de baja (baja logica)` |
| RN-MAT-06 | **Sin doble baja** | `MateriaServiceImpl.darDeBajaLogica` | `ERROR: la materia ya se encuentra inactiva` |
| RN-MAT-07 | **Materia INACTIVA no permite nuevas comisiones** | `ComisionServiceImpl.crearComision` | `ERROR: la materia esta inactiva` |
| RN-MAT-08 | **No puede ser correlativa de sí misma** (relación reflexiva; compara instancia e igualdad de nombre) | `MateriaServiceImpl.agregarCorrelativa` | `ERROR: una materia no puede ser correlativa de si misma` |
| RN-MAT-09 | **No se puede duplicar una correlativa** ya asociada | `MateriaServiceImpl.agregarCorrelativa` (`contains`) | `ERROR: esa materia ya figura como correlativa` |
| RN-MAT-10 | **Ambas materias deben existir** para crear la correlativa (la materia y la correlativa) | `MateriaServiceImpl.agregarCorrelativa` (`buscarMateria` x2) | `ERROR: no existe una materia con ese nombre` |
| RN-MAT-11 | **Re-claveo al renombrar**: se elimina la clave vieja y se reinserta el **mismo objeto** con el nombre nuevo (referencias de planes y correlativas intactas). Enter sin texto = mantener el nombre actual; el nuevo valida unicidad solo si realmente cambia | `MateriaServiceImpl.modificarMateria` → `MateriaRepositoryImpl.eliminar` + `guardar` | `ERROR: no existe una materia con ese nombre` (si la original no existe); `ERROR: ya existe una materia con ese nombre` (si colisiona) |
| RN-MAT-12 | **Encapsulamiento de correlativas**: la lista solo se muta vía `Materia.agregarCorrelativa` (el modelo protege su invariante con copia defensiva) | `Materia.agregarCorrelativa` | — (invariante por diseño) |

## 4. Reglas de Comisiones (COM)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-COM-01 | **La materia debe existir** para crear la comisión | `ComisionServiceImpl.crearComision` → `MateriaRepositoryImpl.buscarPorNombre` | `ERROR: no existe una materia con ese nombre` |
| RN-COM-02 | **La materia debe estar ACTIVA** (RN-MAT-07 aplicada al dictado) | `ComisionServiceImpl.crearComision` | `ERROR: la materia esta inactiva` |
| RN-COM-03 | **El profesor debe existir** | `ComisionServiceImpl.crearComision` → `ProfesorRepositoryImpl.buscarPorDni` | `ERROR: no existe un profesor con ese DNI` |
| RN-COM-04 | **El profesor debe estar ACTIVO** (RN-PRO-07 aplicada al dictado) | `ComisionServiceImpl.crearComision` | `ERROR: el profesor esta inactivo` |
| RN-COM-05 | **Fechas coherentes**: inicio y fin obligatorias y `fin >= inicio` | `ComisionServiceImpl.crearComision` | `ERROR: fechas invalidas` |
| RN-COM-06 | **No duplicar comisiones**: misma materia (instancia), mismo cuatrimestre (comparado **por valor**: año y número) y mismo profesor | `ComisionRepositoryImpl.existe` + `mismoCuatrimestre`, invocado por `ComisionServiceImpl.crearComision` | `ERROR: ya existe una comision igual` |
| RN-COM-07 | **ID autoincremental**: el repositorio asigna el ID al insertar (contador `siguienteId`, simulando `AUTO_INCREMENT`) | `ComisionRepositoryImpl.agregar` | — (comportamiento por construcción) |
| RN-COM-08 | **La comisión debe existir** (por ID) para inscribir, cargar notas o registrar asistencias | `ComisionServiceImpl.inscribirAlumno`, `NotaServiceImpl.cargarNota`, `AsistenciaServiceImpl.registrarAsistencia` → `ComisionRepositoryImpl.buscarPorId` | `ERROR: no existe una comision con ese ID` |
| RN-COM-09 | **El alumno debe existir** para inscribirse | `ComisionServiceImpl.inscribirAlumno` → `AlumnoRepositoryImpl.buscarPorDni` | `ERROR: no existe un alumno con ese DNI` |
| RN-COM-10 | **El alumno debe estar ACTIVO** para inscribirse (RN-ALU-07) | `ComisionServiceImpl.inscribirAlumno` | `ERROR: el alumno esta inactivo` |
| RN-COM-11 | **No inscribir dos veces al mismo alumno** en la misma comisión | `ComisionServiceImpl.yaInscripto` (compara identidad del alumno) | `ERROR: el alumno ya esta inscripto en esa comision` |
| RN-COM-12 | **El inscripto ingresa como REGULAR** (estado inicial de cursada) | `ComisionServiceImpl.inscribirAlumno` (constructor `AlumnoInscripto(..., EstadoCursada.REGULAR, 0.0)`) | — (comportamiento por construcción) |

## 5. Reglas de Notas (NOT)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-NOT-01 | **Tipo de evaluación válido** (enum: TP, PARCIAL, RECUPERATORIO, FINAL) | `NotaServiceImpl.cargarNota` | `ERROR: tipo de evaluacion invalido` |
| RN-NOT-02 | **Nota entre 0 y 10** (inclusive) | `NotaServiceImpl.cargarNota` | `ERROR: la nota debe estar entre 0 y 10` |
| RN-NOT-03 | **Inscripción previa**: el alumno debe figurar entre los inscriptos de esa comisión | `NotaServiceImpl.buscarInscripto` | `ERROR: el alumno no esta inscripto en esa comision` |
| RN-NOT-04 | **Se crean Examen y Nota** vinculados: el `Examen` registra tipo y fecha del día, la `Nota` guarda el valor y referencia al examen, y ambos quedan en la historia del `AlumnoInscripto` | `NotaServiceImpl.cargarNota` | — (comportamiento por construcción) |
| RN-NOT-05 | **El promedio se calcula sobre todas las notas** del alumno en esa comisión | `AlumnoInscripto.promedioNotas`, invocado por `NotaServiceImpl.cargarNota` | — (cálculo) |
| RN-NOT-06 | **Promoción automática**: promedio ≥ 6.0 → estado `APROBADO` + nota final guardada | `NotaServiceImpl.cargarNota` (constante `NOTA_APROBACION = 6.0`) | Exito: `Nota cargada con exito (promedio: X,XX). El alumno quedo APROBADO (promedio >= 6)` |
| RN-NOT-07 | **No degradar estados**: si el alumno ya estaba APROBADO, cargas posteriores recalculan el promedio pero nunca cambian el estado | `NotaServiceImpl.cargarNota` | Informativo: `El alumno ya se encontraba APROBADO` |
| RN-NOT-08 | **Promedio < 6 mantiene el estado** actual del inscripto | `NotaServiceImpl.cargarNota` | Informativo: `El alumno mantiene su estado actual (promedio < 6)` |

## 6. Reglas de Asistencias (ASI)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-ASI-01 | **El alumno debe estar inscripto** en la comisión para registrarle asistencia | `AsistenciaServiceImpl.buscarInscripto` | `ERROR: el alumno no esta inscripto en esa comision` |
| RN-ASI-02 | **Fecha del sistema**: la asistencia se registra con `LocalDate.now()` (no se ingresa manualmente) | `AsistenciaServiceImpl.registrarAsistencia` | — (comportamiento por construcción) |
| RN-ASI-03 | **Asistencia binaria**: PRESENTE (`S`) o AUSENTE (`N`); no existen estados intermedios (la justificación es Fase 2) | `AsistenciaServiceImpl.registrarAsistencia` (parámetro `boolean presente`) | — (comportamiento por construcción) |
| RN-ASI-04 | **El listado es por comisión**: solo se devuelven las asistencias cuyo inscripto pertenece a la comisión solicitada | `AsistenciaServiceImpl.obtenerAsistenciasPorComision` | — (consulta; lista vacía si no hay registros) |

## 7. Reglas de Carreras y Planes (CAR)

| ID | Descripción | Implementación | Mensaje al violarla |
|---|---|---|---|
| RN-CAR-01 | **Nombre de carrera obligatorio** (trim; vacío = faltante) | `CarreraServiceImpl.crearCarrera` (`normalizar`) | `ERROR: el nombre es obligatorio` |
| RN-CAR-02 | **Nombre de carrera único** | `CarreraServiceImpl.crearCarrera` → `CarreraRepositoryImpl.existeNombre` | `ERROR: ya existe una carrera con ese nombre` |
| RN-CAR-03 | **El plan se asocia a una carrera existente** | `CarreraServiceImpl.crearPlanEstudio` y `asociarMateriaAPlan` | `ERROR: no existe una carrera con ese nombre` |
| RN-CAR-04 | **Nombre de plan obligatorio** | `CarreraServiceImpl.crearPlanEstudio` (`normalizar`) | `ERROR: el nombre es obligatorio` |
| RN-CAR-05 | **Vigencia coherente**: año de fin no anterior al de inicio (el plan se materializa del 01/01/inicio al 31/12/fin) | `CarreraServiceImpl.crearPlanEstudio` | `ERROR: el anio de fin no puede ser anterior al de inicio` |
| RN-CAR-06 | **Nombre de plan único dentro de la carrera** (case-insensitive; nombres iguales en carreras distintas sí se permiten) | `CarreraServiceImpl.buscarPlan` + `crearPlanEstudio` | `ERROR: ya existe un plan con ese nombre en la carrera` |
| RN-CAR-07 | **El plan debe existir** (por nombre dentro de la carrera) para asociar materias | `CarreraServiceImpl.asociarMateriaAPlan` | `ERROR: no existe un plan con ese nombre en la carrera` |
| RN-CAR-08 | **La materia debe existir** para asociarse a un plan | `CarreraServiceImpl.asociarMateriaAPlan` → `MateriaRepositoryImpl.buscarPorNombre` | `ERROR: no existe una materia con ese nombre` |
| RN-CAR-09 | **No duplicar materia en el mismo plan** | `CarreraServiceImpl.asociarMateriaAPlan` (`contains`) | `ERROR: esa materia ya esta asociada al plan` |
| RN-CAR-10 | **Instancia compartida**: el plan guarda la misma referencia de la materia del repositorio (no una copia), garantizando consistencia entre malla y catálogo | `CarreraServiceImpl.asociarMateriaAPlan` / `DataSeeder.cargarCarreras` | — (invariante por diseño) |

## 8. Reglas Transversales (TRA)

| ID | Descripción | Implementación |
|---|---|---|
| RN-TRA-01 | **Persistencia en memoria**: colecciones `private static final` (una "tabla" por repositorio, compartida por todas las instancias); `obtenerTodos()` devuelve una **copia** defensiva y toda mutación pasa por los métodos del repositorio (upsert vía `guardar`) | `*RepositoryImpl` (`repository.impl`) |
| RN-TRA-02 | **Copia defensiva**: los constructores de entidades que reciben listas (`Materia`, `PlanEstudio`, `Carrera`) copian la lista entrante (`new ArrayList<>(...)`); una `List.of()` inmutable no puede envenenar el estado interno. En `Materia` el encapsulamiento es total: el setter también copia y el getter de correlativas devuelve una **vista no modificable**, de modo que la relación solo se modifica vía `Materia.agregarCorrelativa` | Constructores de `Materia`, `PlanEstudio`, `Carrera` + `Materia.setListaCorrelativas/getListaCorrelativas` |
| RN-TRA-03 | **`LocalDate` para fechas**: comparaciones correctas y parseo `dd/MM/yyyy` en las vistas; nunca `String` como fecha de dominio | `Cuatrimestre`, `PlanEstudio`, `Examen`, `Asistencia` + vistas |
| RN-TRA-04 | **Enums para estados y tipos**: `EstadoCursada` (REGULAR, APROBADO, RECURSA), `TipoEvaluacion` (TP, PARCIAL, RECUPERATORIO, FINAL); no se usan strings mágicos | `model.EstadoCursada`, `model.TipoEvaluacion` |
| RN-TRA-05 | **Inyección de dependencias desde el `Main`**: única clase que referencia los `*Impl` (composition root); el resto del sistema depende solo de interfaces (`repository.*` y `service.*`) | `Main.main` |
| RN-TRA-06 | **Normalización de entradas**: los services recortan espacios (`trim`) y tratan vacío como faltante antes de validar | Método `normalizar` en cada `*ServiceImpl` |
| RN-TRA-07 | **Mensajes de negocio centralizados**: el Service *decide* el resultado y lo devuelve como string; la vista solo lo muestra. Los controllers reexportan las constantes que la vista necesita | Constantes `MSG_*` en cada interfaz de service |

## 9. Reglas de Acceso (AUTH)

| ID | Descripción | Implementación | Mensaje |
|---|---|---|---|
| RN-AUTH-01 | **Credenciales válidas**: el username debe existir en el repositorio de usuarios y la contraseña debe coincidir exactamente. Si ambas son correctas se inicia la sesión y se da la bienvenida con el formato `Bienvenido, {username} ({ROL})`; si no, la autenticación devuelve null | `AuthServiceImpl.autenticar` / `AuthView.mostrarLogin` | `Credenciales invalidas` |
| RN-AUTH-02 | **Máximo 3 intentos de login**: tras 3 credenciales inválidas consecutivas se bloquea la sesión y el programa termina de forma controlada; en cada fallo se informa el intento consumido (`Intento n de 3`) | `AuthView.mostrarLogin` (`MAX_INTENTOS`) | `Sesion bloqueada: se agotaron los intentos de acceso` |
| RN-AUTH-03 | **Menú filtrado por rol**: el menú principal muestra solo las opciones del rol logueado — ADMIN: todos los módulos del backoffice; DOCENTE: solo Notas y Asistencias; ALUMNO: solo la consulta de sus datos (placeholder) y Salir | `MenuPrincipalView.mostrarMenuAdmin/Docente/Alumno` | — (filtrado por diseño) |
| RN-AUTH-04 | **Límite del filtrado (documentado)**: la verificación de rol ocurre **solo al armar el menú principal**; los `*Service` no reciben el usuario ni validan rol ni pertenencia a la comisión — no hay autorización a nivel de negocio. Además las contraseñas se comparan en texto plano y las 3 cuentas están precargadas (alcance de demostración, sin gestión de usuarios ni cifrado) | `MenuPrincipalView` (único punto de control) · `AuthServiceImpl.autenticar` (comparación exacta) | — (límite de alcance, Fase 2) |

---

### Resumen

**73 reglas documentadas**: 8 de Alumnos, 8 de Profesores, 12 de Materias,
12 de Comisiones, 8 de Notas, 4 de Asistencias, 10 de Carreras y Planes,
7 transversales y 4 de Acceso. Cada una es verificable en el código citado
y sus mensajes coinciden exactamente con las constantes `MSG_*` de los services.
