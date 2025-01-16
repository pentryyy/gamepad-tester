package org.example.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class CustomWindow {

    private JFrame frame;
    private Point  point;
    private int    width, height;

    public CustomWindow(int width, int height) {
        this.width  = width;
        this.height = height;

        initializeUI();
        frame.setVisible(true);
    }

    public void addContentPanel(JPanel jPanel) {
        frame.add(jPanel);
    }

    private void initializeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        // Добавляем кнопки управления окном
        JButton closeButton    = new JButton();
        JButton minimizeButton = new JButton();

        styleButton(closeButton, Color.RED, "icon-close.png", 16);
        styleButton(minimizeButton, Color.LIGHT_GRAY, "icon-minimize.png", 16);

        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(minimizeButton);
        buttonPanel.add(closeButton);

        // Создаем панель заголовка
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        // Перетаскивание окна
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point = e.getPoint();
            }
        });

        titlePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - point.x;
                int y = e.getYOnScreen() - point.y;
                frame.setLocation(x, y);
            }
        });

        // Устанавливаем основной макет окна
        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
    }

    private void styleButton(
        JButton button, 
        Color hoverColor, 
        String iconName, 
        int iconSize) {
        
        CustomImage customImage = new CustomImage(iconName);

        button.setIcon(customImage.resize(iconSize, iconSize));
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setBackground(Color.WHITE);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }
}
