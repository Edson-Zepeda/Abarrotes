# Sistema de Abarrotes POS Mejorado - Equipo 6

Proyecto final de Programacion IV para el escenario **Abarrotes**.

El objetivo es construir un sistema en Java Swing que permita administrar inventario, clientes, proveedores, compras, ventas, corte de caja y usuarios del sistema, con datos iniciales suficientes para una demostracion completa.

Esta version es una copia mejorada con interfaz tipo punto de venta: login redisenado, menu lateral, dashboard, compras con entrada de mercancia, ventas con buscador de productos, carrito de cobro, tablas no editables y catalogos con formulario lateral.

## Entregables

- Programa completo en Java.
- Reporte final en PDF.
- Presentacion en PowerPoint.
- Proyecto completo comprimido en `.zip`.

## Requerimientos del sistema

- Inventario.
- Clientes.
- Proveedores.
- Compras.
- Ventas.
- Corte de caja.
- Catalogo de usuarios que utilizan el sistema.
- 30 registros previamente cargados.
- Validaciones completas.

## Equipo

- Edson Manuel Zepeda Chavez: capitan, coordinacion general, desarrollo y elaboracion de diapositivas con plantilla propia.
- Pablo Israel Cisneros Rodriguez: apoyo principal en desarrollo.
- Audrey Rashid Marin Martinez: apoyo en evidencias, practica de exposicion y seccion breve asignada.
- Marely Guadalupe Martinez Hernandez: reporte, revision de ortografia y practica de exposicion.
- Javier Emmanuel Ramirez Hernandez: guion, practica de exposicion y preparacion de respuestas.
- Abril Stephania Vazquez Trillo: apoyo en redaccion, claridad del discurso y exposicion.
- Jose Dario Virgen Zamora: evidencias, practica de exposicion y seccion breve asignada.

## Estructura

```text
data/                 Datos semilla para pruebas y demo
docs/                 Planeacion, roles, backlog y guias de entrega
lib/                  Driver SQLite JDBC para la base de datos local
scripts/              Scripts simples para compilar y ejecutar
src/main/java/        Codigo fuente Java
```

## Como ejecutar

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
