# Checklist final de entrega

Proyecto: Sistema de Abarrotes Mejorado - Equipo 6

Este checklist resume el estado final del sistema después de los ajustes solicitados. Sirve como revisión previa para entregar el ZIP, importar en Eclipse y hacer la demostración.

## Entrega principal

- [x] Proyecto final separado de las versiones anteriores.
- [x] Carpeta principal importable con nombre `Abarrotes`.
- [x] ZIP final generado en `C:\Users\Lenovo\Downloads\abarrotes 2\Abarrotes_Eclipse_Final.zip`.
- [x] ZIP con archivo `.project` para Eclipse.
- [x] ZIP con archivo `.classpath` para Eclipse.
- [x] ZIP con librerías necesarias en `lib`.
- [x] ZIP sin `.class`, base de datos generada, carpetas de compilación ni basura de IDE.
- [x] Repositorio de GitHub actualizado en la rama de trabajo.

## Alcance final del sistema

- [x] Login.
- [x] Menú principal.
- [x] Inventario.
- [x] Clientes.
- [x] Proveedores.
- [x] Ventas.
- [x] Corte de caja.
- [x] Usuarios.
- [x] Dashboard principal.
- [x] Datos iniciales con 30 productos.
- [x] Catálogo retirado por solicitud final eliminado del menú, código y documentación.

## Base de datos y acceso

- [x] SQLite JDBC integrado.
- [x] Base de datos local creada en `data/abarrotes.db` al ejecutar.
- [x] Login valida usuarios reales desde SQLite.
- [x] Credenciales iniciales disponibles.
- [x] Administrador: `admin / 1234`.
- [x] Empleado: `empleado / 1234`.
- [x] Credenciales incorrectas muestran error sin cerrar el sistema.
- [x] Usuarios se pueden crear, modificar, eliminar y buscar.
- [x] Se protege que siempre exista al menos un administrador.

## Interfaz y experiencia

- [x] Rediseño visual más profesional.
- [x] Estilo consistente en ventanas principales.
- [x] Menú lateral para navegar entre secciones.
- [x] Botón `Cerrar sesión` en el menú principal.
- [x] Botones de regreso al menú en las secciones.
- [x] Textos visibles revisados para ser claros y breves.
- [x] Se retiró la palabra técnica innecesaria de las pantallas.
- [x] Se quitaron textos descriptivos sobrantes.
- [x] Paneles de trabajo en blanco para mejor lectura.
- [x] Encabezados, barras laterales, bordes y tablas con rojo institucional.
- [x] Texto blanco sobre encabezados principales rojos.
- [x] Encabezados de tablas con texto negro.
- [x] Botones y barra de búsqueda con fondo blanco y texto negro.
- [x] Logo `mercado.png` agregado como icono del sistema sin recorte.
- [x] Rol del usuario mostrado sin texto sobrante.
- [x] Ventanas ampliadas moderadamente para evitar botones cortados.
- [x] El botón `Usuarios` conserva su nombre aunque el rol empleado no pueda abrirlo.
- [x] El botón `Proveedores` se deshabilita para empleado por ser función de administrador.
- [x] `Proveedores` queda debajo de `Corte de caja` y arriba de `Usuarios`.
- [x] Login editable desde la pestaña `Design` de Eclipse.
- [x] Login sin `GridBagLayout`, sin `UiFactory`, sin `AppTheme` y sin método auxiliar de restricciones.
- [x] Componentes del login declarados directamente para poder moverlos en WindowBuilder.

## Funcionamiento por módulo

- [x] Inventario permite agregar productos.
- [x] Inventario permite modificar productos.
- [x] Inventario permite eliminar productos.
- [x] Inventario permite buscar por producto, precio o existencias.
- [x] Inventario valida campos vacíos.
- [x] Inventario valida precio y existencias numéricas.
- [x] Inventario evita valores negativos.
- [x] Inventario evita productos duplicados.

- [x] Clientes permite agregar registros.
- [x] Clientes permite modificar registros.
- [x] Clientes permite eliminar registros.
- [x] Clientes permite buscar por número, nombre o teléfono.
- [x] Clientes valida campos vacíos.
- [x] Clientes evita número duplicado.

- [x] Proveedores permite agregar registros.
- [x] Proveedores permite modificar registros.
- [x] Proveedores permite eliminar registros.
- [x] Proveedores permite buscar por empresa, vendedor o teléfono.
- [x] Proveedores valida campos vacíos.
- [x] Proveedores evita empresa duplicada.

- [x] Ventas usa buscador y tabla de productos.
- [x] Ventas no depende de listas desplegables para elegir producto.
- [x] Ventas permite agregar productos al carrito.
- [x] Ventas permite aumentar cantidades.
- [x] Ventas permite disminuir cantidades.
- [x] Ventas permite quitar productos del carrito.
- [x] Ventas permite vaciar carrito.
- [x] Ventas valida existencias suficientes.
- [x] Ventas calcula total.
- [x] Ventas captura efectivo recibido.
- [x] Ventas calcula cambio.
- [x] Ventas descuenta existencias al cobrar.
- [x] Ventas registra operaciones para corte de caja.
- [x] Ventas genera ticket PDF al cobrar.
- [x] Ventas abre automáticamente el ticket PDF generado.

- [x] Corte de caja muestra total vendido.
- [x] Corte de caja muestra número de operaciones.
- [x] Corte de caja muestra ticket promedio.
- [x] Corte de caja muestra historial de ventas.
- [x] Corte de caja muestra estado claro cuando no hay ventas.
- [x] Corte de caja genera ticket PDF desde botón dedicado.
- [x] Corte de caja abre automáticamente el ticket PDF generado.

## Limpieza del proyecto

- [x] Estructura final clara: `data`, `docs`, `lib`, `scripts`, `src`.
- [x] `README.md` actualizado.
- [x] Documentación actualizada al alcance final.
- [x] Librerías externas conservadas solo en `lib`.
- [x] Archivos generados excluidos del ZIP.
- [x] Archivos innecesarios eliminados de la entrega.
- [x] Nombre final del sistema: `Abarrotes`.
- [x] Carpeta `tickets` agregada para guardar los PDF generados.
- [x] Paquete `com.abarrotes.ticket` separado para la creación de tickets.

## Verificación final

- [x] Compilación ejecutada con `.\scripts\compile.ps1`.
- [x] Proyecto extraído desde el ZIP compila correctamente.
- [x] No quedan referencias al texto retirado de la marca anterior.
- [x] No quedan referencias al módulo retirado.
- [x] No queda el texto de acceso restringido que se pidió quitar.
- [x] Login listo para abrir y editar en Eclipse WindowBuilder.
- [x] Proyecto listo para importar en Eclipse como `Abarrotes`.
- [x] Vistas principales corregidas para WindowBuilder sin reutilizar `GridBagConstraints`.
- [x] `ClienteView`, `InventarioView`, `ProveedorView`, `UsuarioView` y `VentaView` sin error de doble asociación `gbc`.
- [x] Textos visibles sin color blanco.
- [x] Botones unificados con el mismo fondo y texto oscuro.
- [x] Proyecto listo para entrega.
