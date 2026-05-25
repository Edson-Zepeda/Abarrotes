package com.abarrotes.app;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.abarrotes.model.Cliente;
import com.abarrotes.model.Producto;
import com.abarrotes.model.Proveedor;
import com.abarrotes.model.Usuario;
import com.abarrotes.model.Venta;
import com.abarrotes.view.LoginView;
import com.abarrotes.view.ui.AppTheme;

/**
 * Clase principal del sistema de Abarrotes.
 * Esta clase es para tener un control interno del programa, actuando como tipo base de datos, manteniendo los datos vivos
 * en memoria y lanzando la primera interfaz de usuario.
 */

public class Main {

    // Base de Datos temporal por medio de listas
    // Se usan ArrayLists estaticos para que los datos sean accesibles desde
    // cualquier ventana (Ventas, Inventario, etc.) sin perder la informacion.

    public static ArrayList<Producto> inventario = new ArrayList<>();

    public static ArrayList<Cliente> listaClientes = new ArrayList<>();

    public static ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    public static ArrayList<Proveedor> listaProveedores = new ArrayList<>();

    // Array para guardar las ventas terminadas y poder hacer corte de caja
    public static ArrayList<Venta> ventasDelDia = new ArrayList<>();

    public static void main(String[] args) {
        // Preparamos SQLite antes de abrir el login.
        BaseDatos.inicializarBaseDatos();

        // Cargamos los datos de prueba 30 registros.
        cargarDatosPrueba();

        // Swing debe abrirse en su propio hilo de interfaz.
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("No se pudo aplicar el estilo del sistema.");
            }
            AppTheme.instalar();

            // Muestra EL LOGIN PRIMERO
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }

    private static void cargarDatosPrueba() {
        // Registro de clientes del abarrotes
        listaClientes.add(new Cliente("C001", "Publico General", "000-0000"));
        listaClientes.add(new Cliente("C002", "Juan Perez", "555-0123"));

        // Proveedores de prueba
        listaProveedores.add(new Proveedor("Carlos Trejo", "Lala", "555-9876"));
        listaProveedores.add(new Proveedor("Ana Martinez", "Bimbo", "555-4321"));

        // Usuarios iniciales cargados desde SQLite para el Login
        listaUsuarios = BaseDatos.cargarUsuarios();

        // Inventario de abarrotes basico
        inventario.add(new Producto("Leche Entera 1L", 25.50, 50));
        inventario.add(new Producto("Huevo Blanco 1kg", 48.00, 20));
        inventario.add(new Producto("Arroz Extra 900g", 22.00, 35));
        inventario.add(new Producto("Frijol Negro 1kg", 35.00, 40));
        inventario.add(new Producto("Aceite Vegetal 1L", 42.00, 15));
        inventario.add(new Producto("Azucar Estandar 1kg", 28.00, 30));
        inventario.add(new Producto("Sal de Mesa 500g", 12.50, 25));
        inventario.add(new Producto("Cafe Soluble 200g", 85.00, 10));
        inventario.add(new Producto("Atun en Agua", 19.50, 45));
        inventario.add(new Producto("Mayonesa 400g", 38.00, 20));

        // Productos de limpieza (inventario)
        inventario.add(new Producto("Jabon de Trastes", 21.50, 18));
        inventario.add(new Producto("Pasta de Dientes", 35.00, 25));
        inventario.add(new Producto("Papel Higienico 4pzs", 32.00, 50));
        inventario.add(new Producto("Detergente 1kg", 45.00, 12));
        inventario.add(new Producto("Limpiador Multiusos", 18.00, 22));
        inventario.add(new Producto("Shampoo 400ml", 55.00, 15));
        inventario.add(new Producto("Jabon de Barra", 14.00, 40));
        inventario.add(new Producto("Cloro 1L", 16.50, 30));

        // Snacks y refrescos (inventario)
        inventario.add(new Producto("Refrescos Cola 600ml", 17.50, 60));
        inventario.add(new Producto("Papas Saladas", 18.00, 35));
        inventario.add(new Producto("Galletas de Chocolate", 16.00, 25));
        inventario.add(new Producto("Pan de Caja", 42.00, 10));
        inventario.add(new Producto("Mermelada Fresa", 34.00, 12));
        inventario.add(new Producto("Agua Natural 1.5L", 14.00, 45));

        // Etc inventario...
        inventario.add(new Producto("Sopa de Pasta", 8.50, 100));
        inventario.add(new Producto("Pure de Tomate", 9.00, 40));
        inventario.add(new Producto("Chiles en Vinagre", 15.50, 25));
        inventario.add(new Producto("Harina de Trigo 1kg", 20.00, 15));
        inventario.add(new Producto("Consome de Pollo", 12.00, 50));
        inventario.add(new Producto("Cereal de Maiz", 52.00, 8));

        System.out.println("SISTEMA ABARROTES: Inventario listo con 30 productos.");
    }
}
