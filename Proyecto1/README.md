# Centro de Rescate Animal: Gestión de Refugio y Adopciones
Proyecto 1 - IPC1 - USAC

## Cómo compilar y ejecutar (sin IDE)

```bash
cd Proyecto1
javac -d bin -encoding UTF-8 $(find src -name "*.java")
java -cp bin com.refugio.Main
```

## Cómo abrir en NetBeans / IntelliJ / Eclipse
1. Crea un proyecto Java nuevo (vacío).
2. Copia el contenido de `src/` dentro de la carpeta `src` de tu proyecto (respetando los paquetes `com.refugio...`).
3. Marca `com.refugio.Main` como clase principal.
4. Ejecuta.

## Usuarios de prueba (autenticación cargada desde memoria)
- Administrador: `admin1` / `Refugio2026` — puede eliminar (baja lógica) registros.
- Auxiliar: `auxiliar1` / `Auxiliar2026` — no puede eliminar.

3 intentos fallidos bloquean la sesión (hay que reiniciar el programa).

## Carpetas que se generan al ejecutar
- `data/` — archivos .txt de persistencia (se crean solos la primera vez).
- `reportes/` — reportes HTML generados desde el módulo "Reportes".

## Estructura de paquetes
```
com.refugio
 ├── Main.java              (punto de entrada)
 ├── ContextoApp.java       (instancias compartidas de los servicios, "base de datos en memoria")
 ├── modelo/                (Animal, Adoptante, Solicitud, Rescate, Usuario)
 ├── servicio/              (lógica de negocio: arreglos estáticos + validaciones)
 ├── bitacora/              (Bitacora de Acciones y Errores)
 ├── persistencia/          (lectura/escritura de archivos .txt)
 ├── reportes/              (generación de reportes HTML)
 └── gui/                   (Java Swing programado a mano: Login, MainFrame, Paneles)
```
