package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.abarrotes.app.Main;
import com.abarrotes.model.Cliente;
import com.abarrotes.model.Producto;
import com.abarrotes.model.Venta;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta es la ventana principal de operacion: La Caja Registradora.
 * Aqui se seleccionan los productos, se calcula el total y se descuenta del inventario.
 */

public class VentaView extends JDialog {
    private NonEditableTableModel modeloProductos;
    private NonEditableTableModel modeloCarrito;
    private JTable tablaProductos;
    private JTable tablaCarrito;
    private JTextField txtBuscarProducto;
    private JTextField txtBuscarCliente;
    private JTextField txtRecibido;
    private JSpinner spinnerCantidad;
    private JLabel lblTotal;
    private JLabel lblArticulos;
    private JLabel lblCambio;
    private JLabel lblEstado;
    private DefaultListModel<Cliente> modeloClientes;
    private JList<Cliente> listaClientes;

    private ArrayList<Producto> productosFiltrados = new ArrayList<>();
    private ArrayList<Cliente> clientesFiltrados = new ArrayList<>();
    private ArrayList<DetalleVenta> carrito = new ArrayList<>();

    public VentaView(JFrame parent) {
        super(parent, "Terminal de Ventas - Abarrotes POS", true);
        setSize(1180, 720);
        setMinimumSize(new Dimension(1060, 650));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        add(UiFactory.encabezado("Terminal de ventas", "Busqueda rapida, carrito y cobro"), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarVentana();
            }
        });

        registrarAtajos();
        actualizarProductos();
        actualizarClientes();
        actualizarCarrito();
    }

    private JPanel crearContenido() {
        JPanel panel = UiFactory.panelBase();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelProductos(), crearPanelCarrito());
        split.setResizeWeight(0.52);
        split.setBorder(null);
        split.setDividerSize(8);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(AppTheme.FONDO);

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.setBackground(AppTheme.FONDO);

        JLabel titulo = UiFactory.tituloPanel("Productos disponibles");
        txtBuscarProducto = UiFactory.campoTexto();
        txtBuscarProducto.setToolTipText("F2 para buscar producto");
        superior.add(titulo, BorderLayout.NORTH);
        superior.add(txtBuscarProducto, BorderLayout.CENTER);

        modeloProductos = new NonEditableTableModel(new Object[]{"Producto", "Precio", "Existencias"}, 0);
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tablaProductos);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(AppTheme.rendererMoneda());
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(AppTheme.rendererCentro());
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    agregarSeleccionado();
                }
            }
        });

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inferior.setBackground(AppTheme.FONDO);
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerCantidad.setPreferredSize(new Dimension(72, 36));
        JButton btnAgregar = UiFactory.botonPrimario("Agregar");
        btnAgregar.addActionListener(e -> agregarSeleccionado());
        inferior.add(UiFactory.etiqueta("Cantidad"));
        inferior.add(spinnerCantidad);
        inferior.add(btnAgregar);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);

        txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarProductos(); }
            public void removeUpdate(DocumentEvent e) { actualizarProductos(); }
            public void changedUpdate(DocumentEvent e) { actualizarProductos(); }
        });

        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(AppTheme.FONDO);

        JPanel superior = new JPanel(new BorderLayout(10, 10));
        superior.setBackground(AppTheme.FONDO);
        superior.add(crearPanelCliente(), BorderLayout.WEST);
        superior.add(crearPanelCobro(), BorderLayout.CENTER);

        modeloCarrito = new NonEditableTableModel(new Object[]{"Producto", "Precio", "Cantidad", "Subtotal", "Existencias"}, 0);
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tablaCarrito);
        tablaCarrito.getColumnModel().getColumn(1).setCellRenderer(AppTheme.rendererMoneda());
        tablaCarrito.getColumnModel().getColumn(2).setCellRenderer(AppTheme.rendererCentro());
        tablaCarrito.getColumnModel().getColumn(3).setCellRenderer(AppTheme.rendererMoneda());
        tablaCarrito.getColumnModel().getColumn(4).setCellRenderer(AppTheme.rendererCentro());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setBackground(AppTheme.FONDO);
        JButton btnMas = UiFactory.botonClaro("+");
        JButton btnMenos = UiFactory.botonClaro("-");
        JButton btnQuitar = UiFactory.botonSecundario("Quitar");
        JButton btnVaciar = UiFactory.botonPeligro("Vaciar");
        JButton btnRegresar = UiFactory.botonClaro("Regresar al menu");
        btnMas.addActionListener(e -> aumentarSeleccion());
        btnMenos.addActionListener(e -> disminuirSeleccion());
        btnQuitar.addActionListener(e -> quitarSeleccion());
        btnVaciar.addActionListener(e -> vaciarCarrito());
        btnRegresar.addActionListener(e -> cerrarVentana());
        acciones.add(btnMas);
        acciones.add(btnMenos);
        acciones.add(btnQuitar);
        acciones.add(btnVaciar);
        acciones.add(btnRegresar);

        lblEstado = new JLabel("F2 buscar producto | Enter agregar | F12 cobrar | Esc regresar");
        lblEstado.setFont(AppTheme.fuenteNegrita(12));
        lblEstado.setForeground(AppTheme.TEXTO_SUAVE);

        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setBackground(AppTheme.FONDO);
        sur.add(acciones, BorderLayout.NORTH);
        sur.add(lblEstado, BorderLayout.SOUTH);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setPreferredSize(new Dimension(245, 150));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        txtBuscarCliente = UiFactory.campoTexto();
        modeloClientes = new DefaultListModel<>();
        listaClientes = new JList<>(modeloClientes);
        listaClientes.setFont(AppTheme.fuenteNormal(13));
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(UiFactory.etiqueta("Cliente"), BorderLayout.NORTH);
        panel.add(txtBuscarCliente, BorderLayout.CENTER);
        panel.add(new JScrollPane(listaClientes), BorderLayout.SOUTH);

        txtBuscarCliente.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarClientes(); }
            public void removeUpdate(DocumentEvent e) { actualizarClientes(); }
            public void changedUpdate(DocumentEvent e) { actualizarClientes(); }
        });

        return panel;
    }

    private JPanel crearPanelCobro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        lblTotal = new JLabel(AppTheme.moneda(0), SwingConstants.RIGHT);
        lblTotal.setFont(AppTheme.fuenteNegrita(34));
        lblTotal.setForeground(AppTheme.VERDE);
        lblArticulos = new JLabel("0 articulos", SwingConstants.RIGHT);
        lblArticulos.setFont(AppTheme.fuenteNegrita(13));
        lblArticulos.setForeground(AppTheme.TEXTO_SUAVE);
        txtRecibido = UiFactory.campoTexto();
        lblCambio = new JLabel("Cambio: " + AppTheme.moneda(0), SwingConstants.RIGHT);
        lblCambio.setFont(AppTheme.fuenteNegrita(15));
        lblCambio.setForeground(AppTheme.ROJO);
        JButton btnCobrar = UiFactory.botonExito("F12 Cobrar");
        btnCobrar.addActionListener(e -> finalizarVenta());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(UiFactory.etiqueta("Total a pagar"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(lblTotal, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(lblArticulos, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(UiFactory.etiqueta("Efectivo recibido"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(txtRecibido, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(lblCambio, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnCobrar, gbc);

        txtRecibido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarCambio(); }
            public void removeUpdate(DocumentEvent e) { actualizarCambio(); }
            public void changedUpdate(DocumentEvent e) { actualizarCambio(); }
        });

        return panel;
    }

    private void registrarAtajos() {
        InputMap input = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actions = getRootPane().getActionMap();

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "buscarProducto");
        actions.put("buscarProducto", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                txtBuscarProducto.requestFocus();
                txtBuscarProducto.selectAll();
            }
        });

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "agregarProducto");
        actions.put("agregarProducto", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                agregarSeleccionado();
            }
        });

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "quitarProducto");
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "quitarProducto");
        actions.put("quitarProducto", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                quitarSeleccion();
            }
        });

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "cobrar");
        actions.put("cobrar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                finalizarVenta();
            }
        });

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        actions.put("cerrar", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cerrarVentana();
            }
        });
    }

    private void actualizarProductos() {
        modeloProductos.setRowCount(0);
        productosFiltrados.clear();
        String busqueda = txtBuscarProducto == null ? "" : txtBuscarProducto.getText().trim().toLowerCase();

        for (Producto producto : Main.inventario) {
            if (busqueda.isEmpty()
                    || producto.getNombre().toLowerCase().contains(busqueda)
                    || String.valueOf(producto.getPrecio()).contains(busqueda)
                    || String.valueOf(producto.getStock()).contains(busqueda)) {
                productosFiltrados.add(producto);
                modeloProductos.addRow(new Object[]{producto.getNombre(), producto.getPrecio(), producto.getStock()});
            }
        }
        if (!productosFiltrados.isEmpty()) {
            tablaProductos.setRowSelectionInterval(0, 0);
        }
    }

    private void actualizarClientes() {
        modeloClientes.clear();
        clientesFiltrados.clear();
        String busqueda = txtBuscarCliente == null ? "" : txtBuscarCliente.getText().trim().toLowerCase();

        for (Cliente cliente : Main.listaClientes) {
            if (busqueda.isEmpty()
                    || cliente.getId().toLowerCase().contains(busqueda)
                    || cliente.getNombre().toLowerCase().contains(busqueda)
                    || cliente.getTelefono().toLowerCase().contains(busqueda)) {
                clientesFiltrados.add(cliente);
                modeloClientes.addElement(cliente);
            }
        }
        if (!clientesFiltrados.isEmpty()) {
            listaClientes.setSelectedIndex(0);
        }
    }

    // Agrega productos al carrito validando cantidad y existencias reales.
    private void agregarSeleccionado() {
        Producto producto = obtenerProductoSeleccionado();
        if (producto == null) {
            lblEstado.setText("Selecciona un producto disponible.");
            return;
        }

        int cantidad = ((Integer) spinnerCantidad.getValue()).intValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
            return;
        }

        int cantidadPendiente = cantidadEnCarrito(producto);
        if (cantidadPendiente + cantidad > producto.getStock()) {
            JOptionPane.showMessageDialog(this, "No hay suficientes existencias.");
            return;
        }

        DetalleVenta detalle = buscarDetalle(producto);
        if (detalle == null) {
            carrito.add(new DetalleVenta(producto, cantidad));
        } else {
            detalle.cantidad += cantidad;
        }
        actualizarCarrito();
        lblEstado.setText("Producto agregado: " + producto.getNombre());
    }

    private Producto obtenerProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0 && fila < productosFiltrados.size()) {
            return productosFiltrados.get(fila);
        }
        return null;
    }

    private DetalleVenta obtenerDetalleSeleccionado() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila >= 0 && fila < carrito.size()) {
            return carrito.get(fila);
        }
        return null;
    }

    private void aumentarSeleccion() {
        DetalleVenta detalle = obtenerDetalleSeleccionado();
        if (detalle == null) {
            return;
        }
        if (detalle.cantidad + 1 > detalle.producto.getStock()) {
            JOptionPane.showMessageDialog(this, "No hay suficientes existencias.");
            return;
        }
        detalle.cantidad++;
        actualizarCarrito();
    }

    private void disminuirSeleccion() {
        DetalleVenta detalle = obtenerDetalleSeleccionado();
        if (detalle == null) {
            return;
        }
        detalle.cantidad--;
        if (detalle.cantidad <= 0) {
            carrito.remove(detalle);
        }
        actualizarCarrito();
    }

    private void quitarSeleccion() {
        DetalleVenta detalle = obtenerDetalleSeleccionado();
        if (detalle != null) {
            carrito.remove(detalle);
            actualizarCarrito();
        }
    }

    private void vaciarCarrito() {
        if (carrito.isEmpty()) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas vaciar el carrito?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            carrito.clear();
            actualizarCarrito();
        }
    }

    private void actualizarCarrito() {
        modeloCarrito.setRowCount(0);
        for (DetalleVenta detalle : carrito) {
            modeloCarrito.addRow(new Object[]{
                    detalle.producto.getNombre(),
                    detalle.producto.getPrecio(),
                    detalle.cantidad,
                    detalle.getSubtotal(),
                    detalle.producto.getStock()
            });
        }
        lblTotal.setText(AppTheme.moneda(calcularTotal()));
        lblArticulos.setText(contarArticulos() + " articulos");
        actualizarCambio();
    }

    private void actualizarCambio() {
        double total = calcularTotal();
        String recibidoTexto = txtRecibido.getText().trim();
        if (recibidoTexto.isEmpty()) {
            lblCambio.setText("Cambio: " + AppTheme.moneda(0));
            lblCambio.setForeground(AppTheme.ROJO);
            return;
        }

        try {
            double recibido = Double.parseDouble(recibidoTexto);
            double cambio = recibido - total;
            lblCambio.setText("Cambio: " + AppTheme.moneda(Math.max(0, cambio)));
            lblCambio.setForeground(cambio >= 0 ? AppTheme.VERDE : AppTheme.PELIGRO);
        } catch (NumberFormatException e) {
            lblCambio.setText("Efectivo invalido");
            lblCambio.setForeground(AppTheme.PELIGRO);
        }
    }

    // Termina la venta, descuenta inventario y la manda al corte de caja.
    private void finalizarVenta() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito esta vacio.");
            return;
        }

        double total = calcularTotal();
        double recibido;
        try {
            recibido = Double.parseDouble(txtRecibido.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa el efectivo recibido.");
            txtRecibido.requestFocus();
            return;
        }

        if (recibido < total) {
            JOptionPane.showMessageDialog(this, "El efectivo recibido no cubre el total.");
            txtRecibido.requestFocus();
            return;
        }

        for (DetalleVenta detalle : carrito) {
            detalle.producto.reducirStock(detalle.cantidad);
        }

        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        int idVenta = Main.ventasDelDia.size() + 1;
        Main.ventasDelDia.add(new Venta(idVenta, hora, obtenerNombreCliente(), total));

        JOptionPane.showMessageDialog(this,
                "Venta realizada por " + AppTheme.moneda(total)
                        + "\nCambio: " + AppTheme.moneda(recibido - total));
        dispose();
    }

    private String obtenerNombreCliente() {
        Cliente cliente = listaClientes.getSelectedValue();
        return cliente == null ? "Publico General" : cliente.getNombre();
    }

    private int cantidadEnCarrito(Producto producto) {
        int total = 0;
        for (DetalleVenta detalle : carrito) {
            if (detalle.producto == producto) {
                total += detalle.cantidad;
            }
        }
        return total;
    }

    private DetalleVenta buscarDetalle(Producto producto) {
        for (DetalleVenta detalle : carrito) {
            if (detalle.producto == producto) {
                return detalle;
            }
        }
        return null;
    }

    private double calcularTotal() {
        double total = 0;
        for (DetalleVenta detalle : carrito) {
            total += detalle.getSubtotal();
        }
        return total;
    }

    private int contarArticulos() {
        int total = 0;
        for (DetalleVenta detalle : carrito) {
            total += detalle.cantidad;
        }
        return total;
    }

    private void cerrarVentana() {
        if (!carrito.isEmpty()) {
            int respuesta = JOptionPane.showConfirmDialog(this, "Hay productos sin cobrar. Deseas salir?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
    }

    // Clase interna para recordar que producto y cantidad se vendieron.
    private static class DetalleVenta {
        Producto producto;
        int cantidad;

        DetalleVenta(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        double getSubtotal() {
            return producto.getPrecio() * cantidad;
        }
    }
}
