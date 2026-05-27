package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.abarrotes.app.Main;
import com.abarrotes.model.Producto;
import com.abarrotes.model.Venta;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Tablero Principal".
 * Funciona como un distribuidor: recibe al usuario y le permite viajar
 * a cualquier seccion del sistema (Ventas, Inventario, etc.).
 */

public class MenuPrincipal extends JFrame {
    private String rol;
    private JLabel lblProductos;
    private JLabel lblClientes;
    private JLabel lblProveedores;
    private JLabel lblOperaciones;
    private JLabel lblTotalVendido;
    private JLabel lblStockBajo;

    private JButton btnVentas;
    private JButton btnInventario;
    private JButton btnClientes;
    private JButton btnProveedores;
    private JButton btnUsuarios;
    private JButton btnCorte;
    private JButton btnCerrarSesion;

    public MenuPrincipal(String rol) {
        this.rol = rol;

        setTitle("Sistema de Abarrotes - Equipo 6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(AppTheme.FONDO);
        setContentPane(contentPane);

        contentPane.add(crearBarraSuperior(), BorderLayout.NORTH);
        contentPane.add(crearMenuLateral(), BorderLayout.WEST);
        contentPane.add(crearDashboard(), BorderLayout.CENTER);

        aplicarSeguridadRol();
        actualizarDashboard();
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.ROJO);
        panel.setBorder(AppTheme.bordeVacio(14, 22, 14, 22));

        JLabel titulo = new JLabel("Abarrotes");
        titulo.setFont(AppTheme.fuenteNegrita(24));
        titulo.setForeground(Color.WHITE);

        JLabel usuario = new JLabel("Rol actual: " + rol);
        usuario.setFont(AppTheme.fuenteNegrita(13));
        usuario.setForeground(Color.WHITE);
        usuario.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(titulo, BorderLayout.WEST);
        panel.add(usuario, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearMenuLateral() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(245, 0));
        panel.setBackground(AppTheme.ROJO_OSCURO);
        panel.setBorder(AppTheme.bordeVacio(18, 14, 18, 14));

        JPanel botones = new JPanel(new GridLayout(7, 1, 0, 10));
        botones.setOpaque(false);

        btnVentas = crearBotonNavegacion("Ventas", true);
        btnInventario = crearBotonNavegacion("Inventario", false);
        btnClientes = crearBotonNavegacion("Clientes", false);
        btnProveedores = crearBotonNavegacion("Proveedores", false);
        btnCorte = crearBotonNavegacion("Corte de caja", false);
        btnUsuarios = crearBotonNavegacion("Usuarios", false);
        btnCerrarSesion = crearBotonNavegacion("Cerrar sesion", false);

        btnVentas.addActionListener(e -> abrirVentas());
        btnInventario.addActionListener(e -> abrirInventario());
        btnClientes.addActionListener(e -> abrirClientes());
        btnProveedores.addActionListener(e -> abrirProveedores());
        btnCorte.addActionListener(e -> abrirCorte());
        btnUsuarios.addActionListener(e -> abrirUsuarios());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        botones.add(btnVentas);
        botones.add(btnInventario);
        botones.add(btnClientes);
        botones.add(btnProveedores);
        botones.add(btnCorte);
        botones.add(btnUsuarios);
        botones.add(btnCerrarSesion);

        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearDashboard() {
        JPanel panel = UiFactory.panelBase();

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(AppTheme.FONDO);
        JLabel titulo = UiFactory.tituloPanel("Resumen de operacion");
        encabezado.add(titulo, BorderLayout.CENTER);

        JPanel tarjetas = new JPanel(new GridLayout(2, 3, 14, 14));
        tarjetas.setBackground(AppTheme.FONDO);

        lblProductos = new JLabel("0");
        lblClientes = new JLabel("0");
        lblProveedores = new JLabel("0");
        lblOperaciones = new JLabel("0");
        lblTotalVendido = new JLabel("$0.00");
        lblStockBajo = new JLabel("0");

        tarjetas.add(crearTarjeta("Productos activos", lblProductos, AppTheme.ROJO));
        tarjetas.add(crearTarjeta("Clientes", lblClientes, AppTheme.TEXTO));
        tarjetas.add(crearTarjeta("Proveedores", lblProveedores, AppTheme.TEXTO));
        tarjetas.add(crearTarjeta("Ventas del dia", lblOperaciones, AppTheme.VERDE));
        tarjetas.add(crearTarjeta("Total vendido", lblTotalVendido, AppTheme.VERDE));
        tarjetas.add(crearTarjeta("Existencias bajas", lblStockBajo, AppTheme.PELIGRO));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        acciones.setBackground(AppTheme.FONDO);
        JButton btnNuevaVenta = UiFactory.botonPrimario("Nueva venta");
        JButton btnVerCorte = UiFactory.botonSecundario("Ver corte");
        JButton btnVerInventario = UiFactory.botonClaro("Revisar inventario");
        btnNuevaVenta.addActionListener(e -> abrirVentas());
        btnVerCorte.addActionListener(e -> abrirCorte());
        btnVerInventario.addActionListener(e -> abrirInventario());
        acciones.add(btnNuevaVenta);
        acciones.add(btnVerCorte);
        acciones.add(btnVerInventario);

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(tarjetas, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTarjeta(String titulo, JLabel valor, Color colorValor) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(AppTheme.fuenteNegrita(12));
        lblTitulo.setForeground(AppTheme.TEXTO_SUAVE);

        valor.setFont(AppTheme.fuenteNegrita(28));
        valor.setForeground(AppTheme.TEXTO);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonNavegacion(String texto, boolean destacado) {
        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(AppTheme.fuenteNegrita(14));
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BOTON_BORDE),
                AppTheme.bordeVacio(12, 14, 12, 14)
        ));
        boton.setBackground(AppTheme.BOTON_FONDO);
        boton.setForeground(AppTheme.TEXTO);
        return boton;
    }

    private void abrirVentas() {
        new VentaView(this).setVisible(true);
        actualizarDashboard();
    }

    private void abrirInventario() {
        new InventarioView(this, Main.inventario).setVisible(true);
        actualizarDashboard();
    }

    private void abrirClientes() {
        new ClienteView(this).setVisible(true);
        actualizarDashboard();
    }

    private void abrirProveedores() {
        new ProveedorView(this).setVisible(true);
        actualizarDashboard();
    }

    private void abrirUsuarios() {
        if (btnUsuarios.isEnabled()) {
            new UsuarioView(this).setVisible(true);
            actualizarDashboard();
        }
    }

    private void abrirCorte() {
        new CorteCajaView(this).setVisible(true);
        actualizarDashboard();
    }

    // Cierra el menu principal y muestra de nuevo la pantalla de acceso.
    private void cerrarSesion() {
        LoginView login = new LoginView();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        this.dispose();
    }

    private void aplicarSeguridadRol() {
        if ("Empleado".equalsIgnoreCase(rol)) {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setText("Usuarios");
            btnUsuarios.setToolTipText("Disponible para administrador");
        }
    }

    private void actualizarDashboard() {
        lblProductos.setText(String.valueOf(Main.inventario.size()));
        lblClientes.setText(String.valueOf(Main.listaClientes.size()));
        lblProveedores.setText(String.valueOf(Main.listaProveedores.size()));
        lblOperaciones.setText(String.valueOf(Main.ventasDelDia.size()));
        lblTotalVendido.setText(AppTheme.moneda(totalVendido()));
        lblStockBajo.setText(String.valueOf(productosStockBajo()));
    }

    private double totalVendido() {
        double total = 0;
        for (Venta venta : Main.ventasDelDia) {
            total += venta.getTotal();
        }
        return total;
    }

    private int productosStockBajo() {
        int total = 0;
        for (Producto producto : Main.inventario) {
            if (producto.getStock() <= 10) {
                total++;
            }
        }
        return total;
    }
}
