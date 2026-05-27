package com.abarrotes.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Fabrica pequena de componentes.
 * Sirve para que botones, campos y encabezados mantengan el mismo estilo.
 */

public class UiFactory {
    private UiFactory() {
    }

    public static JPanel panelBase() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(AppTheme.bordeVacio(18, 18, 18, 18));
        AppTheme.aplicarFondo(panel);
        return panel;
    }

    public static JPanel encabezado(String titulo, String subtitulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BOTON_FONDO);
        panel.setBorder(AppTheme.bordeVacio(14, 18, 0, 18));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(AppTheme.fuenteNegrita(22));
        lblTitulo.setForeground(AppTheme.TEXTO);

        JPanel textos = new JPanel(new BorderLayout(0, 3));
        textos.setOpaque(false);
        textos.add(lblTitulo, BorderLayout.CENTER);
        if (subtitulo != null && !subtitulo.trim().isEmpty()) {
            JLabel lblSubtitulo = new JLabel(subtitulo);
            lblSubtitulo.setFont(AppTheme.fuenteNormal(12));
            lblSubtitulo.setForeground(AppTheme.TEXTO_SUAVE);
            textos.add(lblSubtitulo, BorderLayout.SOUTH);
        }

        JPanel franja = new JPanel();
        franja.setBackground(AppTheme.AMARILLO);
        franja.setPreferredSize(new Dimension(0, 5));

        panel.add(textos, BorderLayout.CENTER);
        panel.add(franja, BorderLayout.SOUTH);
        return panel;
    }

    public static JLabel etiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(AppTheme.fuenteNegrita(12));
        label.setForeground(AppTheme.TEXTO_SUAVE);
        return label;
    }

    public static JLabel tituloPanel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(AppTheme.fuenteNegrita(17));
        label.setForeground(AppTheme.TEXTO);
        return label;
    }

    public static JTextField campoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(AppTheme.fuenteNormal(14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDE),
                AppTheme.bordeVacio(8, 10, 8, 10)
        ));
        return campo;
    }

    public static JPasswordField campoPassword() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(AppTheme.fuenteNormal(14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDE),
                AppTheme.bordeVacio(8, 10, 8, 10)
        ));
        return campo;
    }

    public static JButton botonPrimario(String texto) {
        return boton(texto, AppTheme.BOTON_FONDO, AppTheme.TEXTO);
    }

    public static JButton botonExito(String texto) {
        return boton(texto, AppTheme.BOTON_FONDO, AppTheme.TEXTO);
    }

    public static JButton botonSecundario(String texto) {
        return boton(texto, AppTheme.BOTON_FONDO, AppTheme.TEXTO);
    }

    public static JButton botonPeligro(String texto) {
        return boton(texto, AppTheme.BOTON_FONDO, AppTheme.TEXTO);
    }

    public static JButton botonClaro(String texto) {
        return boton(texto, AppTheme.BOTON_FONDO, AppTheme.TEXTO);
    }

    public static JButton boton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(AppTheme.fuenteNegrita(12));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BOTON_BORDE),
                AppTheme.bordeVacio(9, 14, 9, 14)
        ));
        return boton;
    }

    public static JPanel tarjetaResumen(String titulo, String valor, Color colorValor) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(AppTheme.SUPERFICIE);
        panel.setBorder(AppTheme.bordeTarjeta());

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(AppTheme.fuenteNegrita(12));
        lblTitulo.setForeground(AppTheme.TEXTO_SUAVE);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(AppTheme.fuenteNegrita(24));
        lblValor.setForeground(AppTheme.TEXTO);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel barraBusqueda(JTextField txtBuscar, JButton btnLimpiar, JLabel lblRegistros) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(AppTheme.FONDO);
        panel.add(etiqueta("Buscar"));
        txtBuscar.setPreferredSize(new Dimension(260, 36));
        panel.add(txtBuscar);
        panel.add(btnLimpiar);
        lblRegistros.setHorizontalAlignment(SwingConstants.LEFT);
        lblRegistros.setFont(AppTheme.fuenteNegrita(12));
        lblRegistros.setForeground(AppTheme.TEXTO_SUAVE);
        panel.add(lblRegistros);
        return panel;
    }
}
