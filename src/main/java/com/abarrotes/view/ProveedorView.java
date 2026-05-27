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
import com.abarrotes.model.Proveedor;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es la "Agenda de Suministros".
 * Sirve para guardar los datos de las empresas y vendedores que traen la mercancia.
 */

public class ProveedorView extends JDialog {
    // Estas son las herramientas de la ventana: el modelo de datos y la tabla visual.
    private NonEditableTableModel modelo;
    private JTable tabla;
    private JTextField txtEmpresa, txtNombre, txtTel, txtBuscar;
    private JLabel lblRegistros, lblModo;
    private JButton btnModificar, btnBorrar;
    private ArrayList<Proveedor> listaFiltrada = new ArrayList<>();

    public ProveedorView(JFrame parent) {
        super(parent, "Proveedores - Abarrotes", true);
        setSize(1040, 680);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Proveedores", ""), BorderLayout.NORTH);
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

        modelo = new NonEditableTableModel(new Object[]{"Empresa", "Vendedor", "Telefono"}, 0);
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
        panel.setPreferredSize(new Dimension(325, 0));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        lblModo = UiFactory.tituloPanel("Nuevo proveedor");
        txtEmpresa = UiFactory.campoTexto();
        txtNombre = UiFactory.campoTexto();
        txtTel = UiFactory.campoTexto();

        JButton btnAgregar = UiFactory.botonPrimario("Agregar");
        btnModificar = UiFactory.botonSecundario("Guardar cambios");
        JButton btnLimpiar = UiFactory.botonClaro("Limpiar");
        btnBorrar = UiFactory.botonPeligro("Eliminar");

        btnAgregar.addActionListener(e -> agregarProveedor());
        btnModificar.addActionListener(e -> modificarProveedor());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBorrar.addActionListener(e -> borrarProveedor());

        JPanel botones = new JPanel(new GridLayout(4, 1, 0, 8));
        botones.setBackground(AppTheme.SUPERFICIE);
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnLimpiar);
        botones.add(btnBorrar);

        JLabel lblEmpresa = UiFactory.etiqueta("Empresa");
        JLabel lblVendedor = UiFactory.etiqueta("Vendedor");
        JLabel lblTelefono = UiFactory.etiqueta("Telefono");
        JLabel separador = new JLabel();

        GridBagConstraints gbcLblModo = new GridBagConstraints();
        gbcLblModo.gridx = 0;
        gbcLblModo.gridy = 0;
        gbcLblModo.fill = GridBagConstraints.HORIZONTAL;
        gbcLblModo.weightx = 1;
        gbcLblModo.insets = new Insets(0, 0, 18, 0);
        panel.add(lblModo, gbcLblModo);

        GridBagConstraints gbcLblEmpresa = new GridBagConstraints();
        gbcLblEmpresa.gridx = 0;
        gbcLblEmpresa.gridy = 1;
        gbcLblEmpresa.fill = GridBagConstraints.HORIZONTAL;
        gbcLblEmpresa.weightx = 1;
        gbcLblEmpresa.insets = new Insets(0, 0, 6, 0);
        panel.add(lblEmpresa, gbcLblEmpresa);

        GridBagConstraints gbcTxtEmpresa = new GridBagConstraints();
        gbcTxtEmpresa.gridx = 0;
        gbcTxtEmpresa.gridy = 2;
        gbcTxtEmpresa.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtEmpresa.weightx = 1;
        gbcTxtEmpresa.insets = new Insets(0, 0, 14, 0);
        panel.add(txtEmpresa, gbcTxtEmpresa);

        GridBagConstraints gbcLblVendedor = new GridBagConstraints();
        gbcLblVendedor.gridx = 0;
        gbcLblVendedor.gridy = 3;
        gbcLblVendedor.fill = GridBagConstraints.HORIZONTAL;
        gbcLblVendedor.weightx = 1;
        gbcLblVendedor.insets = new Insets(0, 0, 6, 0);
        panel.add(lblVendedor, gbcLblVendedor);

        GridBagConstraints gbcTxtNombre = new GridBagConstraints();
        gbcTxtNombre.gridx = 0;
        gbcTxtNombre.gridy = 4;
        gbcTxtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtNombre.weightx = 1;
        gbcTxtNombre.insets = new Insets(0, 0, 14, 0);
        panel.add(txtNombre, gbcTxtNombre);

        GridBagConstraints gbcLblTelefono = new GridBagConstraints();
        gbcLblTelefono.gridx = 0;
        gbcLblTelefono.gridy = 5;
        gbcLblTelefono.fill = GridBagConstraints.HORIZONTAL;
        gbcLblTelefono.weightx = 1;
        gbcLblTelefono.insets = new Insets(0, 0, 6, 0);
        panel.add(lblTelefono, gbcLblTelefono);

        GridBagConstraints gbcTxtTel = new GridBagConstraints();
        gbcTxtTel.gridx = 0;
        gbcTxtTel.gridy = 6;
        gbcTxtTel.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtTel.weightx = 1;
        gbcTxtTel.insets = new Insets(0, 0, 14, 0);
        panel.add(txtTel, gbcTxtTel);

        GridBagConstraints gbcSeparador = new GridBagConstraints();
        gbcSeparador.gridx = 0;
        gbcSeparador.gridy = 7;
        gbcSeparador.fill = GridBagConstraints.HORIZONTAL;
        gbcSeparador.weightx = 1;
        gbcSeparador.weighty = 1;
        panel.add(separador, gbcSeparador);

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 8;
        gbcBotones.fill = GridBagConstraints.HORIZONTAL;
        gbcBotones.weightx = 1;
        gbcBotones.insets = new Insets(12, 0, 0, 0);
        panel.add(botones, gbcBotones);

        return panel;
    }

    // Agrega un proveedor validando datos importantes.
    private void agregarProveedor() {
        String empresa = txtEmpresa.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tel = txtTel.getText().trim();

        if (empresa.isEmpty() || nombre.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }
        if (buscarProveedorPorEmpresa(empresa) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un proveedor con esa empresa");
            return;
        }

        Main.listaProveedores.add(new Proveedor(nombre, empresa, tel));
        actualizarTabla();
        limpiarCampos();
    }

    // Modifica el proveedor seleccionado.
    private void modificarProveedor() {
        Proveedor proveedor = obtenerProveedorSeleccionado();
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor de la tabla");
            return;
        }

        String empresa = txtEmpresa.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tel = txtTel.getText().trim();

        if (empresa.isEmpty() || nombre.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos");
            return;
        }

        Proveedor repetido = buscarProveedorPorEmpresa(empresa);
        if (repetido != null && repetido != proveedor) {
            JOptionPane.showMessageDialog(this, "Ya existe otro proveedor con esa empresa");
            return;
        }

        proveedor.setEmpresa(empresa);
        proveedor.setNombre(nombre);
        proveedor.setContacto(tel);
        actualizarTabla();
        limpiarCampos();
    }

    // Borra el proveedor seleccionado con confirmacion.
    private void borrarProveedor() {
        Proveedor proveedor = obtenerProveedorSeleccionado();
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor de la tabla");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "Deseas borrar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            Main.listaProveedores.remove(proveedor);
            actualizarTabla();
            limpiarCampos();
        }
    }

    /**
     * Este metodo borra lo que se ve en la tabla y lo vuelve a llenar con la lista
     * actualizada que tiene el programa en su memoria principal.
     */
    private void actualizarTabla() {
        modelo.setRowCount(0);
        listaFiltrada.clear();
        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim().toLowerCase();

        for (Proveedor p : Main.listaProveedores) {
            if (busqueda.isEmpty()
                    || p.getEmpresa().toLowerCase().contains(busqueda)
                    || p.getNombre().toLowerCase().contains(busqueda)
                    || p.getContacto().toLowerCase().contains(busqueda)) {
                listaFiltrada.add(p);
                modelo.addRow(new Object[]{p.getEmpresa(), p.getNombre(), p.getContacto()});
            }
        }
        lblRegistros.setText(listaFiltrada.size() + " registros");
        actualizarEstadoBotones();
    }

    private void cargarSeleccion() {
        Proveedor proveedor = obtenerProveedorSeleccionado();
        if (proveedor != null) {
            txtEmpresa.setText(proveedor.getEmpresa());
            txtNombre.setText(proveedor.getNombre());
            txtTel.setText(proveedor.getContacto());
            lblModo.setText("Editar proveedor");
        }
        actualizarEstadoBotones();
    }

    private Proveedor obtenerProveedorSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0 && fila < listaFiltrada.size()) {
            return listaFiltrada.get(fila);
        }
        return null;
    }

    private Proveedor buscarProveedorPorEmpresa(String empresa) {
        for (Proveedor p : Main.listaProveedores) {
            if (p.getEmpresa().equalsIgnoreCase(empresa)) {
                return p;
            }
        }
        return null;
    }

    private void limpiarCampos() {
        tabla.clearSelection();
        txtEmpresa.setText("");
        txtNombre.setText("");
        txtTel.setText("");
        lblModo.setText("Nuevo proveedor");
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
