# Sistema de Abarrotes Mejorado - Equipo 6

Proyecto final de Programación IV para el escenario **Abarrotes**.

El objetivo es construir un sistema en Java Swing que permita administrar inventario, clientes, proveedores, ventas, corte de caja y usuarios del sistema, con datos iniciales suficientes para una demostración completa.

Esta versión es una copia mejorada con interfaz tipo punto de venta: login rediseñado, menú lateral, dashboard, ventas con buscador de productos, carrito de cobro, tablas no editables y catálogos con formulario lateral.

## Entregables

- Programa completo en Java.
- Reporte final en PDF.
- Presentación en PowerPoint.
- Proyecto completo comprimido en `.zip`.

## Requerimientos del sistema

- Inventario.
- Clientes.
- Proveedores.
- Ventas.
- Corte de caja.
- Tickets PDF de venta y corte de caja.
- Catálogo de usuarios que utilizan el sistema.
- 30 registros previamente cargados.
- Validaciones completas.

## Equipo

- Edson Manuel Zepeda Chávez: capitán, coordinación general, desarrollo y elaboración de diapositivas con plantilla propia.
- Pablo Israel Cisneros Rodríguez: apoyo principal en desarrollo.
- Audrey Rashid Marín Martínez: apoyo en evidencias, práctica de exposición y sección breve asignada.
- Marely Guadalupe Martínez Hernández: reporte, revisión de ortografía y práctica de exposición.
- Javier Emmanuel Ramírez Hernández: guion, práctica de exposición y preparación de respuestas.
- Abril Stephania Vázquez Trillo: apoyo en redacción, claridad del discurso y exposición.
- José Darío Virgen Zamora: evidencias, práctica de exposición y sección breve asignada.

## Estructura

```text
.settings/            Configuración de codificación UTF-8 para Eclipse
data/                 Datos semilla para pruebas y demo
docs/                 Planeación, roles, backlog y guías de entrega
lib/                  Driver SQLite JDBC para la base de datos local
scripts/              Scripts simples para compilar y ejecutar
src/main/java/        Código fuente Java
src/main/resources/   Imágenes y recursos del sistema
tickets/              Carpeta donde se guardan los tickets PDF generados
```

## Cómo ejecutar

Compilar:

```powershell
.\scripts\compile.ps1
```

Ejecutar:

```powershell
.\scripts\run.ps1
```

Al ejecutar el sistema se crea la base de datos local `data/abarrotes.db`.
El login inicial queda con `admin / 1234` y `empleado / 1234`.

Al cobrar una venta se genera automáticamente un ticket PDF en `tickets/` y se abre con el visor predeterminado.
En corte de caja se puede usar el botón `Imprimir corte PDF` para guardar y abrir el ticket del corte.
