# Manual Técnico — Centro de Rescate Animal

Este documento resume las decisiones de arquitectura para que puedas explicarlas
y defenderlas. **Léelo antes de la revisión**; el auxiliar puede pedirte modificar
código en vivo y solo lo lograrás si entiendes esto.

## 1. Por qué esta arquitectura

Se separó el proyecto en capas simples porque el enunciado prohíbe usar
`ArrayList`/`HashMap`/etc. y exige Swing sin editores visuales:

- **modelo**: clases "planas" (POJO) que solo guardan datos de un registro
  (Animal, Adoptante, Solicitud, Rescate). Cada una sabe convertirse a una
  línea de texto (`toLineaArchivo()`) y reconstruirse desde una línea
  (`desdeLinea()`), para la persistencia.
- **servicio**: aquí viven los **arreglos estáticos** (`private Animal[] animales = new Animal[MAX]`)
  y toda la lógica: registrar, buscar, editar, baja lógica, validar.
  Esta es la capa que debes dominar para la defensa: aquí NO hay Swing,
  solo arreglos, ciclos `for`, condicionales y `String.split("|")`.
- **bitacora**: registra cada acción/error en arreglos + archivo de texto.
- **persistencia**: lee/escribe los `.txt`. Para leer un archivo sin usar
  ArrayList, se cuenta cuántas líneas tiene (primera pasada) y luego se
  crea un arreglo de ese tamaño exacto (segunda pasada).
- **reportes**: arma un `String` con etiquetas HTML y lo guarda en un archivo.
- **gui**: solo Swing. Cada panel llama métodos de `servicio`, nunca
  manipula los arreglos directamente.
- **ContextoApp**: mantiene una única instancia de cada servicio mientras
  el programa corre (simula una "base de datos en memoria" compartida
  entre todas las ventanas, para no pasar 6 parámetros a cada panel).

## 2. Arreglos y matrices usados (dónde están y su tamaño)

| Entidad     | Dónde                          | Tamaño máximo | Código   |
|-------------|----------------------------------|---------------|----------|
| Animales    | `AnimalService.animales`        | 50            | A-001... |
| Adoptantes  | `AdoptanteService.adoptantes`   | 30            | AD-001...|
| Solicitudes | `SolicitudService.solicitudes`  | 50            | S-001... |
| Rescates    | `RescateService.rescates`       | 30            | R-001... |
| Ubicaciones | `UbicacionService.matriz`       | 2 filas × 5 columnas (matriz `String[2][5]`) | fila/columna |
| Bitácora    | `Bitacora.entradasAcciones/Errores` | 500 c/u  | —        |

**Matriz de ubicaciones**: fila 0 = Zona Perros, fila 1 = Zona Gatos.
Cada columna (0 a 4) es un espacio individual. Capacidad total = 10 animales
(5 por zona). Cada celda guarda el código del animal (String) o `""` si está libre.

## 3. Validaciones implementadas
- Animal: especie solo "Perro"/"Gato", edad 0–25, estado clínico limitado a 3 valores.
- Adoptante: nombre solo letras, DPI 13 dígitos (único), teléfono 8 dígitos.
- Solicitud: el animal debe existir y estar DISPONIBLE; solo una APROBADA por animal
  (al aprobar una, cualquier otra PENDIENTE del mismo animal se marca RECHAZADA
  automáticamente — ver `SolicitudService.aprobar()`).
- Rescate: prioridad limitada a ALTA/MEDIA/BAJA; al "atender" se crea o vincula
  un Animal (reutilizando el consecutivo: R-009 → A-009).
- Ubicaciones: no se permite asignar una celda ya ocupada; al eliminar
  lógicamente un animal, su celda se libera automáticamente
  (`AnimalService.eliminarLogico` + `ubicacionService.liberarPorCodigo` en el panel).
- Login: 3 intentos, luego bloqueo hasta reiniciar.

## 4. Persistencia (formato de archivos, separador "|")
- `data/animales.txt` → codigo|nombre|especie|edad|estadoClinico|estadoAdopcion
- `data/adoptantes.txt` → codigo|nombre|dpi|telefono
- `data/solicitudes.txt` → codigo|codigoAnimal|codigoAdoptante|fecha|estado
- `data/rescates.txt` → codigo|prioridad|estado|fecha|codigoAnimalVinculado
- `data/ubicaciones.txt` → fila|columna|codigoAnimal (solo celdas ocupadas)
- `data/bitacora_acciones.txt` y `data/bitacora_errores.txt` → fecha|usuario|modulo|evento|descripcion(o motivo)

Se cargan al iniciar sesión correctamente (`PersistenciaUtil.cargarTodo()`)
y se guardan al cerrar la ventana principal si el usuario confirma
(`PersistenciaUtil.guardarTodo()`).

## 5. Reportes HTML
Cada botón de la pestaña "Reportes" arma un `StringBuilder` con etiquetas
`<table>` y lo escribe con nombre `reporte_x_AAAAMMDD_HHmmss.html` dentro de
`reportes/`. Se puede abrir con cualquier navegador.

## 6. Cosas que DEBES poder explicar en la defensa
1. Por qué se usan arreglos de tamaño fijo (`MAX_ANIMALES`, etc.) y qué pasa
   si se llenan (el método `registrar...` retorna `false`).
2. Cómo se generan los códigos automáticos (consecutivo interno + `String.format`).
3. Cómo funciona la baja lógica y por qué no se borra el elemento del arreglo.
4. Cómo se recorre la matriz de ubicaciones y cómo se valida capacidad.
5. Cómo se lee un archivo de texto sin ArrayList (conteo de líneas + arreglo exacto).
6. El flujo completo: Login → Cargar datos → Panel → Servicio → Bitácora → Guardar.

## 7. Diseño visual (paquete gui, todo por código)

Para que la interfaz no se vea "genérica" se agregaron dos clases de apoyo,
sin usar ningún editor visual ni librería externa:

- **`Estilos.java`**: define la paleta de colores (azul primario, acento
  naranja, rojo para eliminar, verde para aprobar/atender), las fuentes,
  y métodos reutilizables:
  - `aplicarNimbus()` — activa el Look and Feel *Nimbus* (viene incluido
    en el JDK desde Java 6, no es una librería externa ni un editor
    visual; solo cambia cómo se pintan los componentes estándar de Swing).
  - `estilizarBotonPrimario(JButton)` y `estilizarPanelBotones(JPanel)` —
    colorean automáticamente los botones según su texto (ej. "Eliminar" se
    pinta rojo, "Aprobar"/"Atender" verde).
  - `estilizarTabla(JTable)` — encabezado azul, filas alternadas
    (blanco / celeste claro) mediante un `DefaultTableCellRenderer`.
  - `crearEncabezado(...)` — arma la franja de color con título e ícono
    que aparece arriba en el Login y en cada panel.
- **`IconoSimple.java`**: implementa la interfaz `Icon` de Swing y dibuja
  figuras vectoriales (pata, persona, documento, alerta, mapa, gráfica,
  candado) usando `Graphics2D` (óvalos, arcos, polígonos). No son archivos
  de imagen: son instrucciones de dibujo, por lo que no hay que cargar ni
  distribuir ningún `.png`/`.jpg`, y no hay ningún tema de derechos de autor.

**Por qué se hizo así y no con imágenes descargadas de internet**: el
enunciado exige que la interfaz se construya "únicamente mediante código
Java Swing" y prohíbe editores visuales. Usar imágenes de internet además
metería archivos externos al proyecto y posibles problemas de licencia.
Dibujar los íconos con `Graphics2D` cumple la letra y el espíritu del
requisito, y es más fácil de explicar en la defensa ("¿cómo se dibuja la
pata? — son 5 óvalos superpuestos calculados a partir del tamaño del ícono").

## 8. Diagramas pendientes de crear (para el manual técnico entregable)
Debes dibujar tú mismo (o pedírmelo aparte) estos 3 diagramas que pide la rúbrica:
- Diagrama de flujo del menú principal (Login → MainFrame → Pestañas).
- Diagrama de módulos (paquetes: modelo/servicio/bitacora/persistencia/reportes/gui).
- Diagrama de la matriz de ubicaciones (2x5, con nombres de zona).
