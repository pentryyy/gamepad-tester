package org.example;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class CircleWithLetter extends JPanel {
    private String letter;   // Буква для отображения
    private int    fontSize; // Размер шрифта
    private int    diameter; // Диаметр буквы

    public CircleWithLetter(
        String letter, int fontSize, int diameter, 
        int offsetX, int offsetY, Color color) {
        
        this.letter   = letter;
        this.fontSize = fontSize;
        this.diameter = diameter;
        this.setBounds(offsetX, offsetY, diameter, diameter);
        this.setBackground(color);
    }

    public void showButton(Boolean state) { 
        this.setVisible(state);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(0, 0, diameter, diameter);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        FontMetrics fm = g2d.getFontMetrics();
        int xText = (diameter - fm.stringWidth(letter)) / 2; // Центрируем по ширине
        int yText = (diameter + fm.getAscent()) / 2;         // Центрируем по высоте

        g2d.drawString(letter, xText, yText);
    }
}
