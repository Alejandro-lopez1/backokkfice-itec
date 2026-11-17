# 04 — Criterios de Aceptación

> Escenarios Gherkin (DADO QUE / CUANDO / ENTONCES) para las User Stories de
> `03_Epicas_y_User_Stories.md`, extraídos del código real. Cada escenario
> referencia las reglas de negocio (`RN-*`) de `02_Reglas_de_Negocio.md` que
> aplica, y los mensajes entre comillas son los strings **exactos** que
> devuelve el sistema (constantes `MSG_*` de los services).
>
> Cobertura: las **27 US implementadas del MVP** (incluidas US-AUTH-01 con
> login real por roles, US-PROF-04 vía comisión y US-NOT-02 automática). Para
> las US del Anexo (Fase 2) no se generan escenarios: ver nota al final.

---

## EPIC-AUTH — Gestión de Acceso

### US-AUTH-01 — Iniciar sesión

**Escenario 1 — Login exitoso como ADMIN (menú completo)**
- DADO QUE existe la cuenta sembrada admin / admin123 con rol ADMIN
- CUANDO el usuario ingresa esas credenciales en el login
- ENTONCES el sistema muestra `Bienvenido, admin (ADMIN)`
- Y el menú principal presenta los 7 módulos del backoffice (Alumnos, Profesores, Materias, Comisiones e Inscripciones, Notas, Asistencias, Carreras) y la opción Salir
- Reglas aplicadas: RN-AUTH-01, RN-AUTH-03

**Escenario 2 — Login exitoso como DOCENTE (menú filtrado)**
- DADO QUE existe la cuenta sembrada docente / docente123 con rol DOCENTE
- CUANDO el usuario ingresa esas credenciales en el login
- ENTONCES el sistema muestra `Bienvenido, docente (DOCENTE)`
- Y el menú principal presenta solo Notas, Asistencias y Salir (sin los módulos de gestión del backoffice)
- Reglas aplicadas: RN-AUTH-01, RN-AUTH-03

**Escenario 3 — Login exitoso como ALUMNO (placeholder)**
- DADO QUE existe la cuenta sembrada alumno / alumno123 con rol ALUMNO
- CUANDO el usuario ingresa esas credenciales en el login
- ENTONCES el sistema muestra `Bienvenido, alumno (ALUMNO)`
- Y el menú principal presenta solo la consulta de sus datos (placeholder con aviso de funcionalidad en desarrollo) y Salir
- Reglas aplicadas: RN-AUTH-01, RN-AUTH-03

**Escenario 4 — Credenciales inválidas con reintento**
- DADO QUE se solicita usuario y contraseña en el login
- CUANDO se ingresan credenciales inexistentes o con contraseña errónea
- ENTONCES el sistema rechaza el acceso
- Y muestra `Credenciales invalidas` y `Intento n de 3`
- Y permite reintentar hasta un máximo de 3 intentos
- Reglas aplicadas: RN-AUTH-01, RN-AUTH-02

**Escenario 5 — Bloqueo por 3 intentos fallidos**
- DADO QUE se ingresaron 2 credenciales inválidas consecutivas
- CUANDO se falla el tercer intento
- ENTONCES el sistema finaliza la ejecución de forma controlada
- Y muestra `Sesion bloqueada: se agotaron los intentos de acceso` (sin llegar al menú principal)
- Reglas aplicadas: RN-AUTH-02

**Escenario 6 — Opción inválida no corta la sesión**
- DADO QUE el usuario ya está logueado y se muestra su menú principal
- CUANDO ingresa una opción inexistente o no numérica
- ENTONCES el sistema muestra `ERROR: opcion invalida`
- Y reitera el menú sin cerrar la sesión

**Escenario 7 — Cierre de sesión**
- DADO QUE el usuario ya está logueado y se muestra su menú principal
- CUANDO ingresa la opción Salir (`0`)
- ENTONCES el sistema finaliza la ejecución de forma controlada
- Y cierra la sesión activa

---

## EPIC-ALU — Gestión de Alumnos

### US-ALU-01 — Crear alumno

**Escenario 1 — Alta exitosa**
- DADO QUE el ADM ingresa datos válidos (nombre, apellido, DNI único, mail con `@`, teléfono)
- CUANDO confirma el alta
- ENTONCES el sistema crea el alumno con estado ACTIVO
- Y muestra `Alumno creado con exito`
- Reglas aplicadas: RN-ALU-01, RN-ALU-02, RN-ALU-03, RN-ALU-04, RN-TRA-06

**Escenario 2 — DNI duplicado**
- DADO QUE ya existe un alumno con el DNI ingresado
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: DNI ya registrado`
- Reglas aplicadas: RN-ALU-01

**Escenario 3 — Mail inválido**
- DADO QUE el ADM completa los datos con un mail que no contiene `@`
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: mail invalido (debe contener '@')`
- Reglas aplicadas: RN-ALU-02

**Escenario 4 — Campos obligatorios vacíos**
- DADO QUE el ADM deja vacío el nombre, el apellido o el DNI
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: nombre, apellido y DNI son obligatorios`
- Reglas aplicadas: RN-ALU-03, RN-TRA-06

### US-ALU-02 — Consultar alumno por DNI

**Escenario 1 — Consulta exitosa**
- DADO QUE existe un alumno registrado con el DNI ingresado
- CUANDO el ADM solicita la búsqueda
- ENTONCES el sistema muestra nombre, apellido, mail, teléfono y estado del alumno

**Escenario 2 — Alumno inexistente**
- DADO QUE no existe ningún alumno con el DNI ingresado
- CUANDO el ADM solicita la búsqueda
- ENTONCES el sistema muestra `ERROR: no existe un alumno con ese DNI`
- Reglas aplicadas: RN-ALU-01

### US-ALU-03 — Modificar alumno

**Escenario 1 — Modificación exitosa**
- DADO QUE existe un alumno registrado con el DNI ingresado
- CUANDO el ADM confirma los nuevos datos (nombre, apellido, mail con `@`, teléfono)
- ENTONCES el sistema actualiza el alumno **sin cambiar su DNI**
- Y muestra `Alumno modificado con exito`
- Reglas aplicadas: RN-ALU-02, RN-ALU-03, RN-ALU-08, RN-TRA-06

**Escenario 2 — Alumno inexistente**
- DADO QUE no existe un alumno con el DNI ingresado
- CUANDO el ADM confirma la modificación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe un alumno con ese DNI`
- Reglas aplicadas: RN-ALU-08

**Escenario 3 — Mail inválido en modificación**
- DADO QUE existe el alumno
- CUANDO el ADM ingresa un mail sin `@`
- ENTONCES el sistema rechaza la operación sin modificar datos
- Y muestra `ERROR: mail invalido (debe contener '@')`
- Reglas aplicadas: RN-ALU-02

### US-ALU-04 — Baja lógica de alumno

**Escenario 1 — Baja exitosa**
- DADO QUE existe un alumno con estado ACTIVO
- CUANDO el ADM solicita su baja
- ENTONCES el sistema pasa su estado a INACTIVO sin eliminar el registro
- Y muestra `Alumno dado de baja (baja logica)`
- Reglas aplicadas: RN-ALU-05

**Escenario 2 — Doble baja**
- DADO QUE el alumno ya se encuentra INACTIVO
- CUANDO el ADM solicita su baja nuevamente
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el alumno ya se encuentra inactivo`
- Reglas aplicadas: RN-ALU-06

**Escenario 3 — Alumno inexistente**
- DADO QUE no existe un alumno con el DNI ingresado
- CUANDO el ADM solicita la baja
- ENTONCES el sistema muestra `ERROR: no existe un alumno con ese DNI`
- Reglas aplicadas: RN-ALU-05

### US-ALU-05 — Listar todos los alumnos

**Escenario 1 — Listado del padrón**
- DADO QUE existen alumnos activos e inactivos en el repositorio
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra todos los alumnos con DNI, apellido y nombre, mail y estado (ACTIVO/INACTIVO)
- Reglas aplicadas: RN-TRA-01

**Escenario 2 — Padrón vacío**
- DADO QUE no hay alumnos registrados
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra `(no hay alumnos registrados)` sin error

---

## EPIC-PROF — Gestión de Profesores

### US-PROF-01 — Crear profesor

**Escenario 1 — Alta exitosa**
- DADO QUE el ADM ingresa datos válidos (nombre, apellido, DNI único, mail con `@`, teléfono)
- CUANDO confirma el alta
- ENTONCES el sistema crea el profesor con estado ACTIVO
- Y muestra `Profesor creado con exito`
- Reglas aplicadas: RN-PRO-01, RN-PRO-02, RN-PRO-03, RN-PRO-04

**Escenario 2 — DNI duplicado**
- DADO QUE ya existe un profesor con el DNI ingresado
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: DNI ya registrado`
- Reglas aplicadas: RN-PRO-01

### US-PROF-02 — Modificar profesor

**Escenario 1 — Modificación exitosa**
- DADO QUE existe un profesor registrado con el DNI ingresado
- CUANDO el ADM confirma los nuevos datos válidos
- ENTONCES el sistema actualiza el profesor **sin cambiar su DNI**
- Y muestra `Profesor modificado con exito`
- Reglas aplicadas: RN-PRO-02, RN-PRO-03, RN-PRO-08

**Escenario 2 — Profesor inexistente**
- DADO QUE no existe un profesor con el DNI ingresado
- CUANDO el ADM confirma la modificación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe un profesor con ese DNI`
- Reglas aplicadas: RN-PRO-08

### US-PROF-03 — Baja lógica de profesor

**Escenario 1 — Baja exitosa**
- DADO QUE existe un profesor con estado ACTIVO
- CUANDO el ADM solicita su baja
- ENTONCES el sistema pasa su estado a INACTIVO sin eliminar el registro
- Y muestra `Profesor dado de baja (baja logica)`
- Reglas aplicadas: RN-PRO-05

**Escenario 2 — Doble baja**
- DADO QUE el profesor ya se encuentra INACTIVO
- CUANDO el ADM solicita su baja nuevamente
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el profesor ya se encuentra inactivo`
- Reglas aplicadas: RN-PRO-06

### US-PROF-04 — Asignar profesor a materia (vía comisión)

**Escenario 1 — Asignación al dictado**
- DADO QUE existe una materia ACTIVA y un profesor ACTIVO
- CUANDO el ADM crea una comisión seleccionándolos
- ENTONCES el sistema vincula el profesor al dictado de esa materia en ese cuatrimestre
- Y muestra `Comision creada con exito (ID: n)`
- Reglas aplicadas: RN-COM-01, RN-COM-03, RN-COM-04, RN-COM-07

**Escenario 2 — Profesor inactivo no asignable**
- DADO QUE el profesor seleccionado está INACTIVO
- CUANDO el ADM intenta crear la comisión
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el profesor esta inactivo`
- Reglas aplicadas: RN-COM-04, RN-PRO-07

### US-PROF-05 — Listar todos los profesores

**Escenario 1 — Listado del cuadro docente**
- DADO QUE existen profesores activos e inactivos
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra todos con DNI, apellido y nombre, mail y estado
- Reglas aplicadas: RN-TRA-01

---

## EPIC-MAT — Gestión de Materias y Correlativas

### US-MAT-01 — Crear materia

**Escenario 1 — Alta exitosa**
- DADO QUE el ADM ingresa un nombre nuevo, año ≥ 1 y cuatrimestre 1 o 2
- CUANDO confirma el alta
- ENTONCES el sistema crea la materia ACTIVA sin correlativas
- Y muestra `Materia creada con exito`
- Reglas aplicadas: RN-MAT-01, RN-MAT-02, RN-MAT-03, RN-MAT-04

**Escenario 2 — Nombre duplicado**
- DADO QUE ya existe una materia con el nombre ingresado
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: ya existe una materia con ese nombre`
- Reglas aplicadas: RN-MAT-02

**Escenario 3 — Año o cuatrimestre inválidos**
- DADO QUE el ADM ingresa cuatrimestre distinto de 1 o 2 (o año menor a 1)
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el anio debe ser 1 o mayor, y el cuatrimestre 1 o 2`
- Reglas aplicadas: RN-MAT-03

### US-MAT-02 — Modificar materia

**Escenario 1 — Renombrado con re-claveo**
- DADO QUE existe la materia con el nombre original y no existe otra con el nombre nuevo
- CUANDO el ADM confirma el cambio de nombre
- ENTONCES el sistema re-clavea la materia: elimina la clave vieja y reinserta **el mismo objeto** con el nombre nuevo
- Y muestra `Materia modificada con exito`
- Reglas aplicadas: RN-MAT-11, RN-TRA-01

**Escenario 2 — Colisión de nombre**
- DADO QUE existe otra materia con el nombre nuevo
- CUANDO el ADM confirma el cambio
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: ya existe una materia con ese nombre`
- Reglas aplicadas: RN-MAT-02, RN-MAT-11

**Escenario 3 — Materia inexistente**
- DADO QUE no existe una materia con el nombre original
- CUANDO el ADM intenta modificar
- ENTONCES el sistema muestra `ERROR: no existe una materia con ese nombre`
- Reglas aplicadas: RN-MAT-11

### US-MAT-03 — Baja lógica de materia

**Escenario 1 — Baja exitosa**
- DADO QUE existe una materia ACTIVA
- CUANDO el ADM solicita su baja
- ENTONCES el sistema pasa su estado a INACTIVA sin eliminar el registro
- Y muestra `Materia dada de baja (baja logica)`
- Reglas aplicadas: RN-MAT-05

**Escenario 2 — Doble baja**
- DADO QUE la materia ya está INACTIVA
- CUANDO el ADM solicita su baja nuevamente
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: la materia ya se encuentra inactiva`
- Reglas aplicadas: RN-MAT-06

### US-MAT-04 — Definir correlativas

**Escenario 1 — Correlativa exitosa**
- DADO QUE existen la materia y la correlativa ingresadas, y la correlativa no está asociada
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema agrega la correlativa a la lista de la materia
- Y muestra `Correlativa agregada con exito`
- Reglas aplicadas: RN-MAT-08, RN-MAT-09, RN-MAT-10, RN-MAT-12

**Escenario 2 — Correlativa de sí misma**
- DADO QUE la materia y la correlativa ingresadas son la misma
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: una materia no puede ser correlativa de si misma`
- Reglas aplicadas: RN-MAT-08

**Escenario 3 — Correlativa duplicada**
- DADO QUE la correlativa ya figura en la lista de la materia
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: esa materia ya figura como correlativa`
- Reglas aplicadas: RN-MAT-09

**Escenario 4 — Materia inexistente**
- DADO QUE la materia o la correlativa no existen en el catálogo
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una materia con ese nombre`
- Reglas aplicadas: RN-MAT-10

### US-MAT-05 — Listar todas las materias

**Escenario 1 — Listado del catálogo**
- DADO QUE existen materias activas e inactivas con y sin correlativas
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra cada materia con año, cuatrimestre, estado y lista de correlativas
- Reglas aplicadas: RN-TRA-01

---

## EPIC-COM — Gestión de Comisiones e Inscripciones

### US-COM-01 — Crear comisión

**Escenario 1 — Creación exitosa**
- DADO QUE existen una materia ACTIVA y un profesor ACTIVO, y las fechas son coherentes (fin ≥ inicio)
- CUANDO el ADM confirma la creación
- ENTONCES el sistema crea la comisión con ID autoincremental
- Y muestra `Comision creada con exito (ID: n)`
- Reglas aplicadas: RN-COM-01, RN-COM-03, RN-COM-05, RN-COM-06, RN-COM-07

**Escenario 2 — Materia inexistente o inactiva**
- DADO QUE la materia no existe o está INACTIVA
- CUANDO el ADM confirma la creación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una materia con ese nombre` o `ERROR: la materia esta inactiva`
- Reglas aplicadas: RN-COM-01, RN-COM-02, RN-MAT-07

**Escenario 3 — Profesor inexistente o inactivo**
- DADO QUE el profesor no existe o está INACTIVO
- CUANDO el ADM confirma la creación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe un profesor con ese DNI` o `ERROR: el profesor esta inactivo`
- Reglas aplicadas: RN-COM-03, RN-COM-04, RN-PRO-07

**Escenario 4 — Fechas incoherentes**
- DADO QUE la fecha de fin es anterior a la de inicio
- CUANDO el ADM confirma la creación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: fechas invalidas`
- Reglas aplicadas: RN-COM-05

**Escenario 5 — Comisión duplicada**
- DADO QUE ya existe una comisión con la misma materia (instancia), mismo cuatrimestre (año y número por valor) y mismo profesor
- CUANDO el ADM confirma la creación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: ya existe una comision igual`
- Reglas aplicadas: RN-COM-06

### US-COM-02 — Listar comisiones

**Escenario 1 — Listado de comisiones**
- DADO QUE existen comisiones creadas
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra cada comisión con su ID, materia, cuatrimestre y profesor
- Reglas aplicadas: RN-COM-07, RN-TRA-01

### US-COM-03 — Inscribir alumno a comisión

**Escenario 1 — Inscripción exitosa**
- DADO QUE el alumno existe, está ACTIVO y no está inscripto en la comisión
- CUANDO el ADM confirma la inscripción con el ID de comisión
- ENTONCES el sistema crea el inscripto con estado REGULAR
- Y muestra `Alumno inscripto con exito (comision ID n)`
- Reglas aplicadas: RN-COM-08, RN-COM-09, RN-COM-10, RN-COM-11, RN-COM-12, RN-ALU-07

**Escenario 2 — Inscripción duplicada**
- DADO QUE el alumno ya figura entre los inscriptos de esa comisión
- CUANDO el ADM confirma la inscripción
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el alumno ya esta inscripto en esa comision`
- Reglas aplicadas: RN-COM-11

**Escenario 3 — Alumno inactivo**
- DADO QUE el alumno existe pero está INACTIVO
- CUANDO el ADM confirma la inscripción
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el alumno esta inactivo`
- Reglas aplicadas: RN-COM-10, RN-ALU-07

**Escenario 4 — Alumno o comisión inexistentes**
- DADO QUE el DNI no corresponde a un alumno, o el ID no corresponde a una comisión
- CUANDO el ADM confirma la inscripción
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe un alumno con ese DNI` o `ERROR: no existe una comision con ese ID`
- Reglas aplicadas: RN-COM-08, RN-COM-09

---

## EPIC-NOT — Gestión de Notas

### US-NOT-01 — Cargar nota de examen

**Escenario 1 — Carga exitosa**
- DADO QUE el alumno está inscripto en la comisión indicada
- CUANDO el DOC/ADM carga una nota de tipo válido (TP, PARCIAL, RECUPERATORIO o FINAL) con valor entre 0 y 10
- ENTONCES el sistema crea el Examen (con fecha del día) y la Nota, y los agrega a la historia del inscripto
- Y muestra `Nota cargada con exito (promedio: X,XX)` seguido del informe de estado
- Reglas aplicadas: RN-NOT-01, RN-NOT-02, RN-NOT-03, RN-NOT-04, RN-COM-08

**Escenario 2 — Valor fuera de rango**
- DADO QUE el inscripto existe
- CUANDO se carga una nota con valor menor a 0 o mayor a 10
- ENTONCES el sistema rechaza la operación sin crear el examen
- Y muestra `ERROR: la nota debe estar entre 0 y 10`
- Reglas aplicadas: RN-NOT-02

**Escenario 3 — Alumno no inscripto**
- DADO QUE el alumno no figura entre los inscriptos de la comisión
- CUANDO se intenta cargarle una nota
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el alumno no esta inscripto en esa comision`
- Reglas aplicadas: RN-NOT-03

**Escenario 4 — Comisión inexistente**
- DADO QUE el ID no corresponde a una comisión
- CUANDO se intenta cargar una nota
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una comision con ese ID`
- Reglas aplicadas: RN-COM-08

### US-NOT-02 — Calcular promedio y estado (SYS, automática)

**Escenario 1 — Promoción a APROBADO**
- DADO QUE el inscripto no estaba APROBADO
- CUANDO se carga una nota y el promedio resultante es ≥ 6.0
- ENTONCES el sistema cambia su estado a APROBADO y guarda la nota final
- Y muestra `El alumno quedo APROBADO (promedio >= 6)`
- Reglas aplicadas: RN-NOT-05, RN-NOT-06

**Escenario 2 — Sin cambio de estado**
- DADO QUE el inscripto no estaba APROBADO
- CUANDO se carga una nota y el promedio resultante es < 6.0
- ENTONCES el sistema mantiene su estado actual
- Y muestra `El alumno mantiene su estado actual (promedio < 6)`
- Reglas aplicadas: RN-NOT-08

**Escenario 3 — Sin degradación del estado**
- DADO QUE el inscripto ya se encontraba APROBADO
- CUANDO se carga una nota adicional (aunque baje el promedio)
- ENTONCES el sistema recalcula el promedio pero **no cambia el estado**
- Y muestra `El alumno ya se encontraba APROBADO`
- Reglas aplicadas: RN-NOT-07

### US-NOT-03 — Consultar notas por alumno

**Escenario 1 — Consulta con notas cargadas**
- DADO QUE el alumno está inscripto en una o más comisiones y tiene notas cargadas
- CUANDO el DOC/ADM consulta las notas por su DNI
- ENTONCES el sistema muestra el nombre completo del alumno con su DNI
- Y por cada inscripción: comisión (ID y materia), estado de cursada, cada nota con tipo de evaluación, fecha y valor, y el promedio
- Reglas aplicadas: RN-NOT-04, RN-TRA-01

**Escenario 2 — Inscripción sin notas cargadas**
- DADO QUE el alumno está inscripto en una comisión pero sin notas
- CUANDO el DOC/ADM consulta las notas por su DNI
- ENTONCES el sistema muestra la inscripción con su estado y `(sin notas cargadas)`
- Reglas aplicadas: RN-NOT-04

**Escenario 3 — Alumno inexistente o sin inscripciones**
- DADO QUE el DNI no corresponde a ningún inscripto (incluye alumno inexistente en el padrón)
- CUANDO el DOC/ADM consulta las notas por su DNI
- ENTONCES el sistema no muestra inscripciones
- Y presenta `El alumno no tiene notas registradas` (mismo mensaje para ambos casos: un alumno inexistente nunca puede estar inscripto)
- Reglas aplicadas: RN-NOT-04, RN-ALU-01

---

## EPIC-ASIS — Gestión de Asistencia

### US-ASIS-01 — Registrar asistencia

**Escenario 1 — Registro de PRESENTE**
- DADO QUE el alumno está inscripto en la comisión indicada
- CUANDO el DOC/ADM registra la asistencia respondiendo `S`
- ENTONCES el sistema crea el registro PRESENTE con la fecha del día
- Y muestra `Asistencia registrada con exito (fecha)`
- Reglas aplicadas: RN-ASI-01, RN-ASI-02, RN-ASI-03, RN-COM-08

**Escenario 2 — Registro de AUSENTE**
- DADO QUE el alumno está inscripto
- CUANDO el DOC/ADM registra la asistencia respondiendo `N`
- ENTONCES el sistema crea el registro AUSENTE con la fecha del día
- Reglas aplicadas: RN-ASI-02, RN-ASI-03

**Escenario 3 — Alumno no inscripto**
- DADO QUE el alumno no figura entre los inscriptos de la comisión
- CUANDO se intenta registrarle asistencia
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el alumno no esta inscripto en esa comision`
- Reglas aplicadas: RN-ASI-01

**Escenario 4 — Comisión inexistente**
- DADO QUE el ID no corresponde a una comisión
- CUANDO se intenta registrar asistencia
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una comision con ese ID`
- Reglas aplicadas: RN-COM-08

### US-ASIS-02 — Consultar asistencias por comisión

**Escenario 1 — Listado con registros**
- DADO QUE existen asistencias registradas para la comisión
- CUANDO el DOC/ADM solicita el listado por ID de comisión
- ENTONCES el sistema muestra solo las asistencias de esa comisión, cada una con fecha, estado (PRESENTE/AUSENTE) y alumno
- Reglas aplicadas: RN-ASI-04

**Escenario 2 — Comisión sin registros**
- DADO QUE la comisión existe pero no tiene asistencias
- CUANDO se solicita el listado
- ENTONCES el sistema muestra la lista vacía sin error
- Reglas aplicadas: RN-ASI-04

---

## EPIC-CAR — Gestión de Carreras y Planes de Estudio

### US-CAR-01 — Crear carrera

**Escenario 1 — Alta exitosa**
- DADO QUE el ADM ingresa un nombre de carrera nuevo
- CUANDO confirma el alta
- ENTONCES el sistema crea la carrera sin planes
- Y muestra `Carrera creada con exito`
- Reglas aplicadas: RN-CAR-01, RN-CAR-02

**Escenario 2 — Carrera duplicada**
- DADO QUE ya existe una carrera con el nombre ingresado
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: ya existe una carrera con ese nombre`
- Reglas aplicadas: RN-CAR-02

**Escenario 3 — Nombre vacío**
- DADO QUE el ADM deja el nombre vacío
- CUANDO confirma el alta
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el nombre es obligatorio`
- Reglas aplicadas: RN-CAR-01, RN-TRA-06

### US-CAR-02 — Crear plan de estudio

**Escenario 1 — Plan exitoso**
- DADO QUE existe la carrera indicada y el nombre de plan es nuevo en ella
- CUANDO el ADM confirma el plan con año de fin ≥ año de inicio
- ENTONCES el sistema crea el plan con vigencia del 01/01/inicio al 31/12/fin
- Y muestra `Plan de estudio creado con exito`
- Reglas aplicadas: RN-CAR-03, RN-CAR-04, RN-CAR-05, RN-CAR-06

**Escenario 2 — Rango de años inválido**
- DADO QUE el año de fin es anterior al de inicio
- CUANDO el ADM confirma el plan
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: el anio de fin no puede ser anterior al de inicio`
- Reglas aplicadas: RN-CAR-05

**Escenario 3 — Plan duplicado en la carrera**
- DADO QUE ya existe un plan con ese nombre dentro de la misma carrera
- CUANDO el ADM confirma el plan
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: ya existe un plan con ese nombre en la carrera`
- Reglas aplicadas: RN-CAR-06

**Escenario 4 — Carrera inexistente**
- DADO QUE la carrera no existe
- CUANDO el ADM intenta crear el plan
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una carrera con ese nombre`
- Reglas aplicadas: RN-CAR-03

### US-CAR-03 — Asociar materia a plan

**Escenario 1 — Asociación exitosa**
- DADO QUE existen la carrera, el plan y la materia, y la materia no está en el plan
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema agrega la **misma instancia** de la materia al plan
- Y muestra `Materia asociada al plan con exito`
- Reglas aplicadas: RN-CAR-07, RN-CAR-08, RN-CAR-09, RN-CAR-10

**Escenario 2 — Materia duplicada en el plan**
- DADO QUE la materia ya está asociada al plan
- CUANDO el ADM confirma la asociación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: esa materia ya esta asociada al plan`
- Reglas aplicadas: RN-CAR-09

**Escenario 3 — Elementos inexistentes**
- DADO QUE la carrera, el plan o la materia no existen
- CUANDO el ADM intenta la asociación
- ENTONCES el sistema rechaza la operación
- Y muestra `ERROR: no existe una carrera con ese nombre`, `ERROR: no existe un plan con ese nombre en la carrera` o `ERROR: no existe una materia con ese nombre`
- Reglas aplicadas: RN-CAR-03, RN-CAR-07, RN-CAR-08

### US-CAR-04 — Listar carreras con planes y materias

**Escenario 1 — Listado jerárquico**
- DADO QUE existen carreras con planes y materias asociadas
- CUANDO el ADM solicita el listado
- ENTONCES el sistema muestra la jerarquía Carrera → Plan (con rango de vigencia) → Materias (con año, cuatrimestre, estado y correlativas)
- Reglas aplicadas: RN-CAR-10, RN-TRA-01

---

## Nota — US sin escenarios

Las siguientes US **no tienen escenarios** porque **no están implementadas**
(no se generan criterios de aceptación de funcionalidad inexistente):

- **US-ASIS-03, US-ASIS-04, US-ASIS-05 y US-CAL-03 a US-CAL-06** — Anexo,
  roadmap Fase 2 (ver `01_Alcance.md §4` y `03_Epicas_y_User_Stories.md`).

Cuando se implementen, este documento debe completarse con sus escenarios.
