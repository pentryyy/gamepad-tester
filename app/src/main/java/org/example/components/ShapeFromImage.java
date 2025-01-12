package org.example.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ShapeFromImage extends JPanel {

    private int    transparency = 255; // По умолчанию фигура непрозрачна
    private String filePath;           // Путь к файлу картики
    private int    width, height;      // Ширина и высота изображения

    public ShapeFromImage(
        String filePath, int width, int height, 
        int offsetX, int offsetY) {
        
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.setBounds(offsetX, offsetY, width, height);
        this.setOpaque(false);
    }

    public ShapeFromImage(
        String filePath, int width, int height, 
        int offsetX, int offsetY, Color color) {
        
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.setBounds(offsetX, offsetY, width, height);
        this.setBackground(color);
    }

    public void showButton(Boolean state) { 
        this.setVisible(state);
    }

    public void setTransparencyFromIntensity(int transparencyIntensive) {
        // Убедимся, что значение в диапазоне [0, 255]
        transparency = Math.max(0, Math.min(255, transparencyIntensive));
        repaint(); // Обновляем компонент после изменения прозрачности
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            URL resourceUrl = getClass()
                    .getClassLoader()
                    .getResource(filePath);

            if (resourceUrl == null) {
                throw new IllegalArgumentException("Файл не найден: " + filePath);
            }

            Image image = ImageIO.read(resourceUrl);
            Image resizedImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            int x = (getWidth() - width) / 2;   // Центрируем по ширине
            int y = (getHeight() - height) / 2; // Центрируем по высоте

            // Устанавливаем прозрачность
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency / 255f));

            g2d.drawImage(resizedImg, x, y, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
