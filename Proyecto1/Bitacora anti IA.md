# Bitácora Anti-IA — Proyecto 1: Centro de Rescate Animal

**Estudiante:** Rodrigo José Gutiérrez López — Carné 202500355

> Esta bitácora registra, día a día, qué módulo se trabajó, qué se probó, qué
> problemas surgieron y cómo se resolvieron. Los datos objetivos (fecha,
> módulo, qué se hizo) ya están anotados porque son hechos verificables.
> **Las secciones marcadas como "Mi reflexión" las debes escribir tú, en tus
> propias palabras**, antes de entregar el proyecto — son la parte que
> demuestra que entendiste el trabajo, no solo que corriste comandos.

---

## Lunes — Módulo de Autenticación (Login)

**Qué se hizo:**
- Se repasó la lógica de `AutenticacionService` (arreglos paralelos `usuarios`,
  `contrasenas`, `roles` alineados por índice; contador `intentosFallidos`;
  bloqueo tras 3 intentos).
- Se repasó `LoginFrame` (expresión lambda en el botón, los 3 casos de
  respuesta del servicio: OK / FALLIDO / BLOQUEADO).
- Se agregó una casilla "Mostrar contraseña" usando `JPasswordField.setEchoChar()`.
- Se encontró y corrigió un bug real: dos componentes (`chkMostrar` y
  `btnIngresar`) quedaron en la misma fila del `GridBagLayout` (mismo
  `gbc.gridy`), causando que se superpusieran visualmente. Se corrigió
  ajustando los números de fila.
- Se probó el login con credenciales correctas, incorrectas, y el bloqueo
  tras 3 intentos fallidos.

**Problema encontrado y solución:**
Al agregar la casilla de mostrar contraseña, el botón "Ingresar" quedó
encimado con ella. Causa: ambos componentes usaban `gbc.gridy = 3`. Solución:
se recorrieron los números de fila del formulario (`gridy`) para que cada
componente tuviera su propia fila.

**Mi reflexión:**
> Lo que más me costó fue hacer las validaciones para que el sistema
> distinguiera bien entre el rol de administrador y el de auxiliar. De paso
> se me ocurrió agregar la casilla de mostrar contraseña, para poder
> verificar que la estoy escribiendo bien antes de darle a Ingresar.

---

## Martes — Módulo de Animales

**Qué se hizo:**
- Se repasó `AnimalService`: el patrón `arreglo[cantidad] = nuevo; cantidad++;`
  para agregar registros, por qué el `for` recorre hasta `cantidad` y no hasta
  `MAX_ANIMALES`, y cómo funciona la baja lógica (el registro no se borra del
  arreglo, solo cambia su `estadoAdopcion` a `ELIMINADO`).
- Se repasó `PanelAnimales`: por qué se valida la edad tanto en el panel como
  en el servicio (programación defensiva).
- Se agregó una validación nueva: el nombre del animal ahora solo acepta
  letras y espacios (antes aceptaba cualquier carácter), igual que ya existía
  para el nombre del adoptante.
- Se probó registrar, buscar, actualizar estado clínico y dar de baja un animal.

**Mi reflexión:**
> `listarActivos()` recorre el arreglo dos veces porque necesita devolver un
> arreglo del mismo tamaño exacto que la cantidad de animales que no están
> eliminados, y ese número no se sabe hasta que se cuenta primero.

---

## Miércoles — Módulos de Adoptantes y Solicitudes

**Qué se hizo:**
- Se repasó la validación de DPI con expresión regular (`\d{13}`) en
  `AdoptanteService`.
- Se repasó a fondo `SolicitudService.aprobar()`: cómo, al aprobar una
  solicitud, cualquier otra solicitud `PENDIENTE` del mismo animal pasa
  automáticamente a `RECHAZADA` (y por qué el `if` necesita las 3 condiciones
  que tiene).
- Se agregó una validación nueva: `existeSolicitudPendiente()`, que evita que
  el mismo adoptante registre dos solicitudes pendientes para el mismo animal.
- Se probó el flujo completo: registro de adoptante, DPI duplicado rechazado,
  solicitud, aprobación, animal pasa a ADOPTADO, e intento de solicitud
  duplicada rechazado.

**Mi reflexión:**
> Si `aprobar()` no verificara que la otra solicitud esté PENDIENTE, dañaría
> el historial real de qué pasó con cada solicitud: le cambiaría el estado
> también a solicitudes que ya estaban RECHAZADA o COMPLETADA, sin sentido.

---

## Jueves — Módulos de Rescates y Ubicaciones

**Qué se hizo:**
- Se repasó `codigoAnimalSugerido()` en `RescateService`: cómo genera el
  código del animal reutilizando el mismo número consecutivo del rescate
  (ej. `R-009` con `substring(2)` da `"009"`, y se le pega `"A-"` adelante).
- Se repasó la matriz `String[FILAS][COLUMNAS]` de `UbicacionService`: cómo
  `matriz[fila][columna]` accede a una celda específica, y cómo `asignar()`
  valida rango e disponibilidad antes de ocupar una celda.
- Se agregó una validación nueva: no se puede volver a atender un caso de
  rescate que ya estaba `ATENDIDO` (antes se podía sobrescribir el animal
  vinculado sin avisar).
- Se probó el flujo completo: registrar rescate → atender (crear animal
  nuevo) → asignar ubicación → validar celda ocupada → liberar, y se
  confirmó que intentar atender un caso ya atendido muestra el mensaje
  correcto.

**Mi reflexión:**
> Lo más particular de estos módulos es que en Rescates se reutiliza el
> mismo número del rescate para generar el código del animal, ya que la
> matriz de ubicaciones es un arreglo de 2 dimensiones donde cada celda
> guarda el código de un animal (o queda vacía si está libre).

---

## Viernes — Diseño visual, capturas finales y pruebas de rol

**Qué se hizo:**
- Se reemplazaron los íconos dibujados a mano por íconos reales de Material
  Symbols de Google (licencia Apache 2.0), convertidos a PNG e incluidos
  como recursos del proyecto (`src/com/refugio/gui/recursos/`), cargados en
  tiempo de ejecución con `ImageIO` (sin librerías externas).
- Se agregó el ícono de la aplicación en la barra de título de las
  ventanas (`setIconImage`).
- Se tomaron las capturas finales de todos los módulos para completar el
  Manual de Usuario.
- Se probó cerrar sesión como `admin1` y volver a entrar como `auxiliar1`,
  confirmando que todos los datos (animales, adoptantes, solicitudes,
  rescates, ubicaciones) se cargaron igual gracias a la persistencia en
  archivos `.txt`.
- Se probó el botón "Eliminar" estando logueado como `auxiliar1`,
  confirmando que el sistema bloquea la acción con el mensaje "Solo el
  administrador puede eliminar registros." — la restricción de rol funciona
  de verdad, no solo visualmente.

**Mi reflexión:**
> Usé Material Symbols en vez de dibujar los íconos a mano porque se ve más
> estético, y de la parte técnica del proyecto con lo que me siento más
> seguro explicando es el login y cómo funciona, sin ser tan técnico —
> todavía tengo que repasar más a fondo Rescates y Ubicaciones antes de la
> defensa.

---

## Reflexión general del proyecto

> La parte que mejor puedo explicar es el módulo de Login/Autenticación,
> incluyendo la casilla de mostrar contraseña que agregué yo mismo. Antes de
> la defensa todavía tengo que repasar con más calma Rescates y Ubicaciones
> (por qué no supe qué responder sobre ellos de entrada la primera vez que
> me preguntaron).
