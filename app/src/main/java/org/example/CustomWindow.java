package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

public class CustomWindow {

    private JFrame frame;
    private Point point;

    public CustomWindow() {
        initializeUI();
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
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // ----------------------------Контент внутри окна-------------------------
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(
            getClass().
            getClassLoader().
            getResource("gamepad_photo.jpg"));

        ImageIcon scaledIcon = new ImageIcon(
            imageIcon.getImage()
                     .getScaledInstance(
                        (int) (imageIcon.getIconWidth() * 0.65), 
                        (int) (imageIcon.getIconHeight() * 0.65), 
                        Image.SCALE_SMOOTH));

        JLabel imageLabel = new JLabel(scaledIcon);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        CircleWithLetter buttonA = new CircleWithLetter("A", 24, 40,  515, 215, Color.WHITE);
        CircleWithLetter buttonB = new CircleWithLetter("B", 24, 40,  550, 181, Color.WHITE);
        CircleWithLetter buttonX = new CircleWithLetter("X", 24, 40,  480, 181, Color.WHITE);
        CircleWithLetter buttonY = new CircleWithLetter("Y", 24, 40,  515, 148, Color.WHITE);

        CircleWithLetter buttonBack = new CircleWithLetter("back", 10, 26,  349, 187, Color.WHITE);
        CircleWithLetter buttonStart = new CircleWithLetter("start", 10, 26,  426, 187, Color.WHITE);

        JLabel info = new JLabel("Выход на LS + RS", SwingConstants.CENTER);
        info.setBounds(330, 10, 140, 30);

        GamepadInput gamepadInput = new GamepadInput();

        Thread gamepadThread = new Thread(() -> {
            try {
                while (true) {
                    gamepadInput.handleXInput();

                    buttonA.showButton(gamepadInput.getIsButtonPressedA());
                    buttonB.showButton(gamepadInput.getIsButtonPressedB());
                    buttonX.showButton(gamepadInput.getIsButtonPressedX());
                    buttonY.showButton(gamepadInput.getIsButtonPressedY());

                    buttonBack.showButton(gamepadInput.getIsButtonPressedBack());
                    buttonStart.showButton(gamepadInput.getIsButtonPressedStart());

                    if (gamepadInput.getIsButtonPressedLS() && gamepadInput.getIsButtonPressedRS()) {
                        System.exit(0);
                    }

                    Thread.sleep(0);
                }
            } catch (XInputNotLoadedException e) {
                System.err.println("Ошибка загрузки XInput: " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток был прерван.");
            }
        });

        gamepadThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(gamepadThread::interrupt));

        contentPanel.add(buttonA);
        contentPanel.add(buttonB);
        contentPanel.add(buttonX);
        contentPanel.add(buttonY);

        contentPanel.add(buttonBack);
        contentPanel.add(buttonStart);

        contentPanel.add(info);
        contentPanel.add(imageLabel, BorderLayout.CENTER);
        frame.add(contentPanel);
        // ----------------------------Контент внутри окна-------------------------

        // Добавляем кнопки управления окном
        JButton closeButton    = new JButton();
        JButton minimizeButton = new JButton();
        JButton maximizeButton = new JButton();

        styleButton(closeButton, Color.RED, "icon-close.png", 16);
        styleButton(minimizeButton, Color.LIGHT_GRAY, "icon-minimize.png", 16);
        styleButton(maximizeButton, Color.LIGHT_GRAY, "icon-maximize.png", 16);

        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        maximizeButton.addActionListener(e -> {
            if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(minimizeButton);
        buttonPanel.add(maximizeButton);
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
        frame.add(contentPanel, BorderLayout.CENTER);
    }

    private void styleButton(
        JButton button, 
        Color hoverColor, 
        String iconName, 
        int iconSize) {
        
        ImageIcon icon = new ImageIcon(
            getClass().
            getClassLoader().
            getResource(iconName));
        
        button.setIcon(new ImageIcon(
            icon.getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
        
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

    public void show() {
        frame.setVisible(true);
    }
}