package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import com.abarrotes.model.Producto;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Almacen Digital".
 * Aqui es donde el equipo registra que productos vende la tienda y cuanta mercancia queda.
 */

public class InventarioView extends JDialog {
    // Estas son las herramientas de la ventana: la tabla para ver todo y los cuadros para escribir.
    private NonEditableTableModel modelo;
    private JTable tabla;
    private JTextField txtNombre, txtPrecio, txtStock, txtBuscar;
    private JLabel lblRegistros, lblModo;
    private JButton btnModificar, btnBorrar;
    private ArrayList<Producto> listaProductos;
    private ArrayList<Producto> listaFiltrada = new ArrayList<>();

    public InventarioView(JFrame parent, ArrayList<Producto> listaProductos) {
        super(parent, "Inventario - Abarrotes", true);
        this.listaProductos = listaProductos;
        setSize(980, 620);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Inventario", ""), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);

        actualizarTabla();
        actualizarEstadoBotones();
    }

    private JPanel crearContenido() {
        JPanel panel = UiFactory.panelBase();

        txtBuscar = UiFactory.campoTexto();
        JButton btnLimpiarBusqueda = UiFactory.botonClaro("Limpiar");
        lblRegistros = new JLabel();
        JPanel barraBusqueda = UiFactory.barraBusqueda(txtBuscar, btnLimpiarBusqueda, lblRegistros);

        modelo = new NonEditableTableModel(new Object[]{"Producto", "Precio", "Existencias"}, 0);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tabla);
        tabla.getColumnModel().getColumn(1).setCellRenderer(AppTheme.rendererMoneda());
        tabla.getColumnModel().getColumn(2).setCellRenderer(new StockRenderer());

        JPanel cuerpo = new JPanel(new BorderLayout(12, 0));
        cuerpo.setBackground(AppTheme.FONDO);
        cuerpo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        cuerpo.add(crearFormulario(), BorderLayout.EAST);

        JButton btnCerrar = UiFactory.botonClaro("Regresar al menu");
        btnCerrar.addActionListener(e -> dispose());

        panel.add(barraBusqueda, BorderLayout.NORTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTabla(); }
            public void removeUpdate(DocumentEvent e) { actualizarTabla(); }
            public void changedUpdate(DocumentEvent e) { actualizarTabla(); }
        });
        btnLimpiarBusqueda.addActionListener(e -> txtBuscar.setText(""));
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(305, 0));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        lblModo = UiFactory.tituloPanel("Nuevo producto");
        txtNombre = UiFactory.campoTexto();
        txtPrecio = UiFactory.campoTexto();
        txtStock = UiFactory.campoTexto();

        JButton btnAgregar = UiFactory.botonPrimario("Agregar");
        btnModificar = UiFactory.botonSecundario("Guardar cambios");
        JButton btnLimpiar = UiFactory.botonClaro("Limpiar");
        btnBorrar = UiFactory.botonPeligro("Eliminar");

        btnAgregar.addActionListener(e -> agregarProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBorrar.addActionListener(e -> borrarProducto());

        JPanel botones = new JPanel(new GridLayout(4, 1, 0, 8));
        botones.setBackground(AppTheme.SUPERFICIE);
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnLimpiar);
        botones.add(btnBorrar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(lblModo, gbc);
        agregarCampo(panel, gbc, "Producto", txtNombre);
        agregarCampo(panel, gbc, "Precio", txtPrecio);
        agregarCampo(panel, gbc, "Existencias", txtStock);
        gbc.gridy++;
        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(12, 0, 0, 0);
        panel.add(botones, gbc);

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JTextField campo) {
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(UiFactory.etiqueta(etiqueta), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);
        panel.add(campo, gbc);
    }

    // Agrega un producto nuevo validando numeros y duplicados.
    private void agregarProducto() {
        Producto producto = leerProductoFormulario();
        if (producto == null) {
            return;
        }
        if (buscarProductoPorNombre(producto.getNombre()) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un producto con ese nombre");
            return;
        }

        listaProductos.add(producto);
        actualizarTabla();
        seleccionarProducto(producto);
        limpiarCampos();
    }

    // Modifica el producto seleccionado.
    private void modificarProducto() {
        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla");
            return;
        }

        Producto datos = leerProductoFormulario();
        if (datos == null) {
            return;
        }

        Producto repetido = buscarProductoPorNombre(datos.getNombre());
        if (repetido != null && repetido != seleccionado) {
            JOptionPane.showMessageDialog(this, "Ya existe otro producto con ese nombre");
            return;
        }

        seleccionado.setNombre(datos.getNombre());
        seleccionado.setPrecio(datos.getPrecio());
        seleccionado.setStock(datos.getStock());
        actualizarTabla();
        limpiarCampos();
    }

    // Borra el producto seleccionado con confirmacion.
    private void borrarProducto() {
        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas borrar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            listaProductos.remove(seleccionado);
            actualizarTabla();
            limpiarCampos();
        }
    }

    // Lee y valida los datos del formulario.
    private Producto leerProductoFormulario() {
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return null;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);
            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Precio y existencias no pueden ser negativos");
                return null;
            }
            return new Producto(nombre, precio, stock);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y existencias deben ser numeros validos");
            return null;
        }
    }

    // Metodo para llenar la tabla con filtro de busqueda.
    private void actualizarTabla() {
        modelo.setRowCount(0);
        listaFiltrada.clear();
        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();

        if (listaProductos != null) {
            for (Producto p : listaProductos) {
                if (busqueda.isEmpty()
                        || p.getNombre().toLowerCase().contains(busqueda)
                        || String.valueOf(p.getPrecio()).contains(busqueda)
                        || String.valueOf(p.getStock()).contains(busqueda)) {
                    listaFiltrada.add(p);
                    modelo.addRow(new Object[]{p.getNombre(), p.getPrecio(), p.getStock()});
                }
            }
        }
        lblRegistros.setText(listaFiltrada.size() + " registros");
        actualizarEstadoBotones();
    }

    private void cargarSeleccion() {
        Producto producto = obtenerProductoSeleccionado();
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtStock.setText(String.valueOf(producto.getStock()));
            lblModo.setText("Editar producto");
        }
        actualizarEstadoBotones();
    }

    private Producto obtenerProductoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && fila < listaFiltrada.size()) {
            return listaFiltrada.get(fila);
        }
        return null;
    }

    private Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : listaProductos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    private void seleccionarProducto(Producto producto) {
        int index = listaFiltrada.indexOf(producto);
        if (index >= 0) {
            tabla.setRowSelectionInterval(index, index);
        }
    }

    private void limpiarCampos() {
        tabla.clearSelection();
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        lblModo.setText("Nuevo producto");
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = tabla != null && tabla.getSelectedRow() >= 0;
        if (btnModificar != null) {
            btnModificar.setEnabled(haySeleccion);
        }
        if (btnBorrar != null) {
            btnBorrar.setEnabled(haySeleccion);
        }
    }

    // Colorea existencias bajas para que el cajero las detecte rapido.
    private class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            if (!isSelected) {
                setForeground(AppTheme.TEXTO);
                setBackground(row % 2 == 0 ? AppTheme.SUPERFICIE : AppTheme.GRIS_TABLA);
                if (value instanceof Integer && ((Integer) value) <= 10) {
                    setForeground(AppTheme.PELIGRO);
                    setBackground(new Color(255, 238, 235));
                }
            }
            return this;
        }
    }
}
