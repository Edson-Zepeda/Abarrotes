package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.abarrotes.app.Main;
import com.abarrotes.model.Compra;
import com.abarrotes.model.Producto;
import com.abarrotes.model.Proveedor;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana registra las compras hechas a proveedores.
 * Al guardar una compra se aumentan las existencias del producto elegido.
 */

public class CompraView extends JDialog {
    private NonEditableTableModel modeloProductos;
    private NonEditableTableModel modeloCompras;
    private JTable tablaProductos;
    private JTable tablaCompras;
    private JTextField txtBuscarProducto, txtBuscarProveedor, txtCosto;
    private JSpinner spinnerCantidad;
    private JLabel lblTotalCompra, lblProductoSeleccionado;
    private DefaultListModel<Proveedor> modeloProveedores;
    private JList<Proveedor> listaProveedores;
    private ArrayList<Producto> productosFiltrados = new ArrayList<>();
    private ArrayList<Proveedor> proveedoresFiltrados = new ArrayList<>();

    public CompraView(JFrame parent) {
        super(parent, "Compras - Abarrotes", true);
        setSize(1080, 660);
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Compras", ""), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);

        actualizarProductos();
        actualizarProveedores();
        actualizarTablaCompras();
        actualizarResumen();
    }

    private JPanel crearContenido() {
        JPanel panel = UiFactory.panelBase();

        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 12, 0));
        cuerpo.setBackground(AppTheme.FONDO);
        cuerpo.add(crearPanelSeleccion());
        cuerpo.add(crearPanelRegistro());

        JButton btnCerrar = UiFactory.botonClaro("Regresar al menu");
        btnCerrar.addActionListener(e -> dispose());

        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(AppTheme.FONDO);

        JPanel busqueda = new JPanel(new BorderLayout(0, 6));
        busqueda.setBackground(AppTheme.FONDO);
        busqueda.add(UiFactory.tituloPanel("Producto a comprar"), BorderLayout.NORTH);
        txtBuscarProducto = UiFactory.campoTexto();
        busqueda.add(txtBuscarProducto, BorderLayout.CENTER);

        modeloProductos = new NonEditableTableModel(new Object[]{"Producto", "Precio venta", "Existencias"}, 0);
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tablaProductos);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(AppTheme.rendererMoneda());
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(AppTheme.rendererCentro());

        panel.add(busqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarProductos(); }
            public void removeUpdate(DocumentEvent e) { actualizarProductos(); }
            public void changedUpdate(DocumentEvent e) { actualizarProductos(); }
        });
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarResumen();
            }
        });

        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(AppTheme.FONDO);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(AppTheme.SUPERFICIE);
        formulario.setBorder(AppTheme.bordeTarjeta());

        txtBuscarProveedor = UiFactory.campoTexto();
        modeloProveedores = new DefaultListModel<>();
        listaProveedores = new JList<>(modeloProveedores);
        listaProveedores.setFont(AppTheme.fuenteNormal(13));
        listaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spinnerCantidad.setPreferredSize(new Dimension(90, 36));
        txtCosto = UiFactory.campoTexto();
        lblProductoSeleccionado = new JLabel("Selecciona un producto");
        lblProductoSeleccionado.setFont(AppTheme.fuenteNegrita(13));
        lblProductoSeleccionado.setForeground(AppTheme.TEXTO);
        lblTotalCompra = new JLabel(AppTheme.moneda(0));
        lblTotalCompra.setFont(AppTheme.fuenteNegrita(24));
        lblTotalCompra.setForeground(AppTheme.VERDE);

        JButton btnRegistrar = UiFactory.botonPrimario("Registrar compra");
        btnRegistrar.addActionListener(e -> registrarCompra());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        formulario.add(UiFactory.tituloPanel("Datos de compra"), gbc);
        agregarComponente(formulario, gbc, "Producto elegido", lblProductoSeleccionado);
        agregarComponente(formulario, gbc, "Buscar proveedor", txtBuscarProveedor);
        agregarComponente(formulario, gbc, "Proveedor", new JScrollPane(listaProveedores));
        agregarComponente(formulario, gbc, "Cantidad comprada", spinnerCantidad);
        agregarComponente(formulario, gbc, "Costo de compra", txtCosto);
        agregarComponente(formulario, gbc, "Total estimado", lblTotalCompra);
        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 0, 0);
        formulario.add(btnRegistrar, gbc);

        modeloCompras = new NonEditableTableModel(new Object[]{"Numero de Compra", "Hora", "Proveedor", "Producto", "Cantidad", "Total"}, 0);
        tablaCompras = new JTable(modeloCompras);
        AppTheme.configurarTabla(tablaCompras);
        tablaCompras.getColumnModel().getColumn(5).setCellRenderer(AppTheme.rendererMoneda());

        JPanel historial = new JPanel(new BorderLayout(0, 8));
        historial.setBackground(AppTheme.FONDO);
        historial.add(UiFactory.tituloPanel("Compras registradas"), BorderLayout.NORTH);
        historial.add(new JScrollPane(tablaCompras), BorderLayout.CENTER);

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(historial, BorderLayout.CENTER);

        txtBuscarProveedor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarProveedores(); }
            public void removeUpdate(DocumentEvent e) { actualizarProveedores(); }
            public void changedUpdate(DocumentEvent e) { actualizarProveedores(); }
        });
        txtCosto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarResumen(); }
            public void removeUpdate(DocumentEvent e) { actualizarResumen(); }
            public void changedUpdate(DocumentEvent e) { actualizarResumen(); }
        });
        spinnerCantidad.addChangeListener(e -> actualizarResumen());

        return panel;
    }

    private void agregarComponente(JPanel panel, GridBagConstraints gbc, String etiqueta, java.awt.Component componente) {
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(UiFactory.etiqueta(etiqueta), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(componente, gbc);
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

    private void actualizarProveedores() {
        modeloProveedores.clear();
        proveedoresFiltrados.clear();
        String busqueda = txtBuscarProveedor == null ? "" : txtBuscarProveedor.getText().trim().toLowerCase();

        for (Proveedor proveedor : Main.listaProveedores) {
            if (busqueda.isEmpty()
                    || proveedor.getEmpresa().toLowerCase().contains(busqueda)
                    || proveedor.getNombre().toLowerCase().contains(busqueda)
                    || proveedor.getContacto().toLowerCase().contains(busqueda)) {
                proveedoresFiltrados.add(proveedor);
                modeloProveedores.addElement(proveedor);
            }
        }
        if (!proveedoresFiltrados.isEmpty()) {
            listaProveedores.setSelectedIndex(0);
        }
    }

    private void actualizarTablaCompras() {
        modeloCompras.setRowCount(0);
        for (Compra compra : Main.comprasDelDia) {
            modeloCompras.addRow(new Object[]{
                    "Compra " + compra.getId(),
                    compra.getHora(),
                    compra.getProveedor(),
                    compra.getProducto(),
                    compra.getCantidad(),
                    compra.getTotal()
            });
        }
    }

    private void actualizarResumen() {
        Producto producto = obtenerProductoSeleccionado();
        lblProductoSeleccionado.setText(producto == null ? "Selecciona un producto" : producto.getNombre());
        double costo = leerCosto(false);
        int cantidad = spinnerCantidad == null ? 0 : ((Integer) spinnerCantidad.getValue()).intValue();
        lblTotalCompra.setText(AppTheme.moneda(cantidad * costo));
    }

    private void registrarCompra() {
        Producto producto = obtenerProductoSeleccionado();
        Proveedor proveedor = listaProveedores.getSelectedValue();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor.");
            return;
        }

        int cantidad = ((Integer) spinnerCantidad.getValue()).intValue();
        double costo = leerCosto(true);
        if (costo < 0) {
            return;
        }

        producto.setStock(producto.getStock() + cantidad);
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        int idCompra = Main.comprasDelDia.size() + 1;
        Main.comprasDelDia.add(new Compra(idCompra, hora, proveedor.getEmpresa(), producto.getNombre(), cantidad, costo));

        actualizarProductos();
        actualizarTablaCompras();
        actualizarResumen();
        JOptionPane.showMessageDialog(this, "Compra registrada. Existencias actualizadas.");
    }

    private Producto obtenerProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0 && fila < productosFiltrados.size()) {
            return productosFiltrados.get(fila);
        }
        return null;
    }

    private double leerCosto(boolean mostrarError) {
        if (txtCosto == null || txtCosto.getText().trim().isEmpty()) {
            return 0;
        }
        try {
            double costo = Double.parseDouble(txtCosto.getText().trim());
            if (costo < 0) {
                if (mostrarError) {
                    JOptionPane.showMessageDialog(this, "El costo no puede ser negativo.");
                }
                return -1;
            }
            return costo;
        } catch (NumberFormatException e) {
            if (mostrarError) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un numero valido.");
            }
            return -1;
        }
    }
}
