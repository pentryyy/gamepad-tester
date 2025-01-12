package org.example.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import lombok.Getter;

@Getter
public class RectangleWithLetter extends JPanel {
    
    private Color  fillColor = Color.BLACK; // Цвет заливки фигуры по умолчанию
    private String letter;                  // Буква для отображения
    private int    fontSize;                // Размер шрифта
    private int    width, height;           // Ширина и высот прямоугольника
    private int    degreeOfRounding;        // Степень закругления краев
    private int    degreeOfInclination;     // Угол наклона фигуры
    private int    offsetX, offsetY;        // Отступы по осям

    public RectangleWithLetter(
        String letter, int fontSize, int width, int height, int degreeOfRounding,
        int degreeOfInclination, int offsetX, int offsetY) {
        
        this.letter              = letter;
        this.fontSize            = fontSize;
        this.width               = width;
        this.height              = height;
        this.degreeOfRounding    = degreeOfRounding;
        this.degreeOfInclination = degreeOfInclination;
        this.offsetX             = offsetX;
        this.offsetY             = offsetY;
        
        this.setBounds(offsetX, offsetY, width, height);
        this.setOpaque(false);
    }

    public void showButton(Boolean state) { 
        this.setVisible(state);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(getWidth() / 2.0, getHeight() / 2.0);   // Перемещаем центр вращения в центр компонента
        g2d.rotate(Math.toRadians(degreeOfInclination));      // Поворачиваем на заданный угол
        g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0); // Возвращаем обратно
        g2d.setColor(fillColor);

        // Создаем путь для формы с закруглением двух верхних углов
        GeneralPath path = new GeneralPath();

        path.moveTo(0, degreeOfRounding);                // Начало снизу слева
        path.quadTo(0, 0, degreeOfRounding, 0);   // Левый верхний угол (скругленный)
        path.lineTo(width - degreeOfRounding, 0);        // Верхняя сторона (до правого верхнего угла)
        path.quadTo(width, 0, width, degreeOfRounding); // Правый верхний угол (скругленный)
        path.lineTo(width, height);                        // Правая сторона вниз
        path.lineTo(0, height);                          // Нижняя сторона влево
        path.closePath();                                  // Замыкаем путь

        g2d.fill(path);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        FontMetrics fm = g2d.getFontMetrics();
        int xText = (width - fm.stringWidth(letter)) / 2;            // Центрируем по ширине
        int yText = (height + fm.getAscent() - fm.getDescent()) / 2; // Центрируем по высоте

        g2d.drawString(letter, xText, yText);
        g2d.setTransform(new AffineTransform());
    }
}
