package org.example.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import lombok.Getter;

@Getter
public class CircleWithLetter extends JPanel {
    
    private Color  fillColor = Color.BLACK; // Цвет заливки фигуры по умолчанию
    private String letter;                  // Буква для отображения
    private int    fontSize;                // Размер шрифта
    private int    diameter;                // Диаметр буквы
    private int    lineThickness;           // Толщина линии обводки
    private int    offsetX, offsetY;        // Отступы по осям

    public CircleWithLetter(
        String letter, int fontSize, int diameter, 
        int offsetX, int offsetY, int lineThickness) {
        
        this.letter        = letter;
        this.fontSize      = fontSize;
        this.diameter      = diameter;
        this.offsetX       = offsetX;
        this.offsetY       = offsetY;
        this.lineThickness = lineThickness;
        this.setBounds(offsetX, offsetY, diameter, diameter);
        this.setOpaque(false);
    }

    public CircleWithLetter(
        String letter, int fontSize, int diameter, 
        int offsetX, int offsetY) {
        
        this.letter        = letter;
        this.fontSize      = fontSize;
        this.diameter      = diameter;
        this.offsetX       = offsetX;
        this.offsetY       = offsetY;
        this.lineThickness = 0;
        this.setBounds(offsetX, offsetY, diameter, diameter);
        this.setOpaque(false);
    }

    public void setColorFromIntensity(float colorIntensive) {
        // Убедимся, что интенсивность в диапазоне [0, 1]
        colorIntensive = Math.max(0, Math.min(1, Math.abs(colorIntensive)));
        
        // Интенсивность цвета (0 = белый, 1 = черный)
        int intensityValue = Math.round((1 - colorIntensive) * 255);

        fillColor = new Color(intensityValue, intensityValue, intensityValue);
        repaint();
    }

    public void showButton(Boolean state) { 
        this.setVisible(state);
    }

    public void setOffsetsXY(int offsetX, int offsetY) {
        this.setBounds(offsetX, offsetY, diameter, diameter);
    }

    public void setLetter(String letter) {
        this.letter = letter;
        repaint();
    }

    public void setColorByDefault() {
        fillColor = Color.BLACK;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(fillColor);
        g2d.fillOval(0, 0, diameter, diameter);

        if (lineThickness > 0) {
            int offset = lineThickness / 2; // Сдвиг для корректной обводки внутри границ

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(lineThickness));
            g2d.drawOval(offset, offset, diameter - lineThickness, diameter - lineThickness);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        FontMetrics fm = g2d.getFontMetrics();
        int xText = (diameter - fm.stringWidth(letter)) / 2; // Центрируем по ширине
        int yText = (diameter + fm.getAscent()) / 2;         // Центрируем по высоте

        g2d.drawString(letter, xText, yText);
    }
}
