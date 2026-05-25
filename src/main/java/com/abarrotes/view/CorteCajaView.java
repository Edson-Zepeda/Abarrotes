package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.abarrotes.app.Main;
import com.abarrotes.model.Venta;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.NonEditableTableModel;
import com.abarrotes.view.ui.UiFactory;

/**
 * Esta ventana es el "Reporte Final".
 * Su unica funcion es sumar todas las ventas que se hicieron y mostrarlas ordenadas.
 */

public class CorteCajaView extends JDialog {
    private NonEditableTableModel modelo;
    private JLabel lblTotalVendido, lblNumVentas, lblPromedio, lblVacio;

    public CorteCajaView(JFrame parent) {
        super(parent, "Corte de Caja - Abarrotes POS", true);
        setSize(980, 620);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(UiFactory.encabezado("Corte de caja", ""), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);

        calcularCorte();
    }

    private JPanel crearContenido() {
        JPanel panel = UiFactory.panelBase();

        JPanel resumen = new JPanel(new GridLayout(1, 3, 14, 0));
        resumen.setBackground(AppTheme.FONDO);
        lblTotalVendido = new JLabel(AppTheme.moneda(0));
        lblNumVentas = new JLabel("0");
        lblPromedio = new JLabel(AppTheme.moneda(0));
        resumen.add(crearTarjeta("Efectivo total", lblTotalVendido, AppTheme.VERDE));
        resumen.add(crearTarjeta("Operaciones", lblNumVentas, AppTheme.ROJO));
        resumen.add(crearTarjeta("Ticket promedio", lblPromedio, AppTheme.TEXTO));

        modelo = new NonEditableTableModel(new Object[]{"Numero de Venta", "Hora", "Cliente", "Total"}, 0);
        JTable tabla = new JTable(modelo);
        AppTheme.configurarTabla(tabla);
        tabla.getColumnModel().getColumn(3).setCellRenderer(AppTheme.rendererMoneda());

        lblVacio = new JLabel("Sin ventas registradas en este corte.");
        lblVacio.setFont(AppTheme.fuenteNegrita(13));
        lblVacio.setForeground(AppTheme.TEXTO_SUAVE);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(AppTheme.FONDO);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        centro.add(lblVacio, BorderLayout.SOUTH);

        JButton btnVolver = UiFactory.botonClaro("Regresar al menu");
        btnVolver.addActionListener(e -> dispose());

        panel.add(resumen, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(btnVolver, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTarjeta(String titulo, JLabel valor, java.awt.Color colorValor) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(AppTheme.fuenteNegrita(12));
        lblTitulo.setForeground(AppTheme.TEXTO_SUAVE);
        valor.setFont(AppTheme.fuenteNegrita(28));
        valor.setForeground(colorValor);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Esta es la funcion "calculadora".
     * Va a la lista general del programa (Main.ventasDelDia), suma venta por venta
     * y los anota en la tabla que ve el usuario.
     */

    private void calcularCorte() {
        double sumaTotal = 0;
        modelo.setRowCount(0);

        // Recorremos la lista de Main donde guardamos las ventas completas.
        for (Venta venta : Main.ventasDelDia) {
            sumaTotal += venta.getTotal();
            modelo.addRow(new Object[]{"Venta " + venta.getId(), venta.getHora(), venta.getCliente(), venta.getTotal()});
        }

        int operaciones = Main.ventasDelDia.size();
        lblTotalVendido.setText(AppTheme.moneda(sumaTotal));
        lblNumVentas.setText(String.valueOf(operaciones));
        lblPromedio.setText(AppTheme.moneda(operaciones == 0 ? 0 : sumaTotal / operaciones));
        lblVacio.setVisible(operaciones == 0);
    }
}
