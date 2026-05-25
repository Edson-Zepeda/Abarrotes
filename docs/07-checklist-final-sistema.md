# Checklist final del sistema

Proyecto: Sistema de Abarrotes POS Mejorado - Equipo 6

Este checklist revisa solamente el programa o sistema solicitado para el escenario de Abarrotes. No evalua el reporte PDF ni la presentacion, salvo el ZIP del proyecto como entregable tecnico.

## Requisitos del proyecto para Equipo 6

- [x] Sistema desarrollado en Java.
- [x] Interfaz grafica en Java Swing.
- [x] Inventario implementado.
- [x] Clientes implementado.
- [x] Proveedores implementado.
- [x] Compras implementado.
- [x] Ventas implementado.
- [x] Corte de caja implementado.
- [x] Catalogo de usuarios implementado.
- [x] Busquedas disponibles en modulos principales.
- [x] 30 productos cargados previamente al sistema.
- [x] Programa validado en entradas principales.
- [x] Programa compilando correctamente.
- [x] Proyecto completo comprimido en archivo ZIP.

## Modulos del sistema

- [x] Login con usuarios reales en SQLite JDBC.
  - Usuario administrador: `admin / 1234`.
  - Usuario empleado: `empleado / 1234`.
  - Credenciales incorrectas son rechazadas.

- [x] Menu principal.
  - Navegacion lateral clara.
  - Boton para cerrar sesion.
  - Dashboard con productos, clientes, proveedores, ventas, compras y existencias bajas.
  - Acceso a usuarios bloqueado para rol Empleado.

- [x] Inventario.
  - Alta de productos.
  - Modificacion de productos.
  - Eliminacion de productos.
  - Busqueda por producto, precio o existencias.
  - Validacion de campos vacios.
  - Validacion de precio y existencias numericas.
  - Validacion contra valores negativos.
  - Validacion contra productos duplicados.

- [x] Clientes.
  - Alta de clientes.
  - Modificacion de clientes.
  - Eliminacion de clientes.
  - Busqueda por numero de cliente, nombre o telefono.
  - Validacion de campos vacios.
  - Validacion contra numero de cliente duplicado.

- [x] Proveedores.
  - Alta de proveedores.
  - Modificacion de proveedores.
  - Eliminacion de proveedores.
  - Busqueda por empresa, vendedor o telefono.
  - Validacion de campos vacios.
  - Validacion contra empresa duplicada.

- [x] Compras.
  - Busqueda de producto a comprar.
  - Seleccion de proveedor.
  - Captura de cantidad comprada.
  - Captura de costo de compra.
  - Calculo de total de compra.
  - Registro de compra en historial.
  - Aumento automatico de existencias al registrar la compra.
  - Validacion de producto seleccionado.
  - Validacion de proveedor seleccionado.
  - Validacion de costo numerico.
  - Validacion contra costo negativo.

- [x] Ventas.
  - Busqueda de productos sin depender de listas desplegables.
  - Carrito de venta.
  - Aumento y disminucion de cantidades.
  - Eliminacion de productos del carrito.
  - Vaciado de carrito.
  - Validacion de existencias suficientes.
  - Calculo de subtotal y total.
  - Captura de efectivo recibido.
  - Calculo de cambio.
  - Registro de venta terminada.
  - Descuento automatico de existencias al cobrar.
  - Registro de venta para corte de caja.

- [x] Corte de caja.
  - Total vendido.
  - Numero de operaciones.
  - Ticket promedio.
  - Historial de ventas del dia.
  - Estado claro cuando no hay ventas.

- [x] Usuarios.
  - Alta de usuarios en SQLite.
  - Modificacion de usuarios en SQLite.
  - Eliminacion de usuarios en SQLite.
  - Busqueda por usuario o rol.
  - Validacion de campos vacios.
  - Validacion contra usuarios duplicados.
  - Proteccion para que siempre exista al menos un administrador.

## Criterios de evaluacion del instrumento

- [x] Funcionalidad: los flujos principales compilan y funcionan.
- [x] Programa: existe ejecucion por scripts `compile.ps1` y `run.ps1`.
- [x] Ortografia y textos: etiquetas visibles revisadas para que sean claras y breves.
- [x] Facilidad de uso: menu lateral, botones de regreso, cerrar sesion, busquedas y formularios ordenados.
- [x] Diseno grafico: interfaz redisenada tipo punto de venta, con estilo consistente.
- [x] Utilidad: el sistema cubre venta, compra, inventario, clientes, proveedores, usuarios y corte.
- [x] Requerimientos del cliente: todos los modulos del escenario Abarrotes estan cubiertos.

## Verificacion realizada

- [x] Compilacion ejecutada con `.\scripts\compile.ps1`.
- [x] Clases actualizadas en carpeta `out`.
- [x] Clases actualizadas en carpeta `bin`.
- [x] Prueba de login con `admin / 1234`.
- [x] Prueba de datos iniciales con 30 productos.
- [x] Prueba de compra aumentando existencias.
- [x] Prueba de SQLite JDBC para usuarios.
- [x] ZIP final generado: `C:\Users\Lenovo\Downloads\abarrotes 2\Abarrotes_Equipo6_POS_Mejorado.zip`.

## Resultado final

- [x] El sistema queda listo para entrega como programa del proyecto final de Abarrotes.
- [x] No se detectan pendientes criticos del programa respecto a los requisitos del PDF.
