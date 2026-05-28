package com.abarrotes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.abarrotes.app.Main;
import com.abarrotes.model.Venta;
import com.abarrotes.ticket.TicketPdfService;
import com.abarrotes.view.ui.AppTheme;
import com.abarrotes.view.ui.IconoApp;
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
        super(parent, "Corte de Caja - Abarrotes", true);
        IconoApp.aplicar(this);
        setSize(1040, 680);
        setMinimumSize(new Dimension(960, 620));
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

        JButton btnImprimir = UiFactory.botonPrimario("Imprimir corte PDF");
        JButton btnVolver = UiFactory.botonClaro("Regresar al menu");
        btnImprimir.addActionListener(e -> imprimirCortePdf());
        btnVolver.addActionListener(e -> dispose());

        JPanel acciones = new JPanel(new GridLayout(1, 2, 10, 0));
        acciones.setBackground(AppTheme.FONDO);
        acciones.add(btnImprimir);
        acciones.add(btnVolver);

        panel.add(resumen, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
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
        valor.setForeground(AppTheme.TEXTO);

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

    private void imprimirCortePdf() {
        try {
            File archivo = TicketPdfService.imprimirCorte(Main.ventasDelDia);
            if (TicketPdfService.abrirPdf(archivo)) {
                JOptionPane.showMessageDialog(this, "Corte abierto correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Corte guardado en:\n" + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo crear el comprobante de corte.");
        }
    }
}
