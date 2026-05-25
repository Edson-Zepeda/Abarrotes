package com.abarrotes.view;

import java.awt.BorderLayout;
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
import com.abarrotes.app.Main;
import com.abarrotes.model.Cliente;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Directorio de Clientes".
 * Sirve para anotar quienes nos compran, modificar sus datos o borrarlos.
 */

public class ClienteView extends JDialog {
    // Estas son las piezas necesarias: la tabla, el modelo de datos y los cuadritos de texto.
    private NonEditableTableModel modelo;
    private JTable tabla;
    private JTextField txtId, txtNombre, txtTel, txtBuscar;
    private JLabel lblRegistros, lblModo;
    private JButton btnModificar, btnBorrar;
    private ArrayList<Cliente> listaFiltrada = new ArrayList<>();

    public ClienteView(JFrame parent) {
        super(parent, "Clientes - Abarrotes POS", true);
        setSize(980, 620);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Clientes", "Directorio para ventas y seguimiento"), BorderLayout.NORTH);
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

        modelo = new NonEditableTableModel(new Object[]{"Numero de Cliente", "Nombre", "Telefono"}, 0);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        AppTheme.configurarTabla(tabla);

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

        lblModo = UiFactory.tituloPanel("Nuevo cliente");
        txtId = UiFactory.campoTexto();
        txtNombre = UiFactory.campoTexto();
        txtTel = UiFactory.campoTexto();

        JButton btnAgregar = UiFactory.botonPrimario("Agregar");
        btnModificar = UiFactory.botonSecundario("Guardar cambios");
        JButton btnLimpiar = UiFactory.botonClaro("Limpiar");
        btnBorrar = UiFactory.botonPeligro("Eliminar");

        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBorrar.addActionListener(e -> borrarCliente());

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
        agregarCampo(panel, gbc, "Numero de Cliente", txtId);
        agregarCampo(panel, gbc, "Nombre", txtNombre);
        agregarCampo(panel, gbc, "Telefono", txtTel);
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

    // Agrega clientes validando que no falten datos ni se repita el numero.
    private void agregarCliente() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTel.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }
        if (buscarClientePorId(id) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese numero");
            return;
        }

        Main.listaClientes.add(new Cliente(id, nombre, telefono));
        actualizarTabla();
        limpiarCampos();
    }

    // Modifica el cliente seleccionado en la tabla.
    private void modificarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla");
            return;
        }

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTel.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }

        Cliente repetido = buscarClientePorId(id);
        if (repetido != null && repetido != cliente) {
            JOptionPane.showMessageDialog(this, "Ya existe otro cliente con ese numero");
            return;
        }

        cliente.setId(id);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        actualizarTabla();
        limpiarCampos();
    }

    // Borra el cliente seleccionado con confirmacion.
    private void borrarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas borrar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            Main.listaClientes.remove(cliente);
            actualizarTabla();
            limpiarCampos();
        }
    }

    /**
     * Este metodo borra lo que hay en la tabla y vuelve a leer la lista de clientes
     * para que siempre muestre la informacion mas reciente.
     */
    private void actualizarTabla() {
        modelo.setRowCount(0);
        listaFiltrada.clear();
        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();

        for (Cliente c : Main.listaClientes) {
            if (busqueda.isEmpty()
                    || c.getId().toLowerCase().contains(busqueda)
                    || c.getNombre().toLowerCase().contains(busqueda)
                    || c.getTelefono().toLowerCase().contains(busqueda)) {
                listaFiltrada.add(c);
                modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono()});
            }
        }
        lblRegistros.setText(listaFiltrada.size() + " registros");
        actualizarEstadoBotones();
    }

    // Copia a los campos los datos del renglon elegido.
    private void cargarSeleccion() {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente != null) {
            txtId.setText(cliente.getId());
            txtNombre.setText(cliente.getNombre());
            txtTel.setText(cliente.getTelefono());
            lblModo.setText("Editar cliente");
        }
        actualizarEstadoBotones();
    }

    private Cliente obtenerClienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && fila < listaFiltrada.size()) {
            return listaFiltrada.get(fila);
        }
        return null;
    }

    private Cliente buscarClientePorId(String id) {
        for (Cliente c : Main.listaClientes) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    private void limpiarCampos() {
        tabla.clearSelection();
        txtId.setText("");
        txtNombre.setText("");
        txtTel.setText("");
        lblModo.setText("Nuevo cliente");
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
}
