package org.example.components;

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

import org.example.GamepadInput;

import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

public class CustomWindow {

    private JFrame frame;
    private Point  point;

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

        CircleWithLetter buttonA = new CircleWithLetter("A", 18, 40,  515, 215);
        CircleWithLetter buttonB = new CircleWithLetter("B", 18, 40,  550, 181);
        CircleWithLetter buttonX = new CircleWithLetter("X", 18, 40,  480, 181);
        CircleWithLetter buttonY = new CircleWithLetter("Y", 18, 40,  515, 148);

        CircleWithLetter buttonLS = new CircleWithLetter("LS", 18, 60,  234, 171, 3);
        CircleWithLetter buttonRS = new CircleWithLetter("RS", 18, 60,  441, 250, 3);

        CircleWithLetter buttonBack = new CircleWithLetter("back", 10, 26,  349, 187);
        CircleWithLetter buttonStart = new CircleWithLetter("start", 10, 26,  426, 187);

        ShapeFromImage buttonGuide   = new ShapeFromImage("xbox-logo.png", 41, 41,  380, 129, Color.WHITE);
        ShapeFromImage buttonUnknown = new ShapeFromImage("share-button.png", 34, 20,  384, 219, new Color(255, 255, 255, 0));

        RectangleWithLetter buttonArrowUp    = new RectangleWithLetter("↑", 18, 25, 29, 6, 0, 318, 247);
        RectangleWithLetter buttonArrowDown  = new RectangleWithLetter("↑", 18, 25, 29, 6, 180, 318, 300);
        RectangleWithLetter buttonArrowLeft  = new RectangleWithLetter("↑", 18, 25, 29, 6, 270, 291, 274);
        RectangleWithLetter buttonArrowRight = new RectangleWithLetter("↑", 18, 25, 29, 6, 90, 346, 274);

        ShapeFromImage buttonLeftBumper  = new ShapeFromImage("xbox-lb.png", 50, 50,  250, 60, new Color(255, 255, 255, 0));
        ShapeFromImage buttonRightBumper = new ShapeFromImage("xbox-rb.png", 50, 50,  500, 60, new Color(255, 255, 255, 0));

        GamepadInput gamepadInput = new GamepadInput();

        CustomSlider customSlider = new CustomSlider(0, 65535, gamepadInput.getVibrationStrength());
        customSlider.setBounds(300, 50, 200, 20);

        JLabel infoString1 = new JLabel("Выход на LS + RS (Удерживать 5 секунд)", SwingConstants.CENTER);
        infoString1.setBounds(280, 10, 240, 20);

        JLabel infoString2 = new JLabel("Вибрация на LB + RB (Сила вибрации " + customSlider.getValue() + " )", SwingConstants.CENTER);
        infoString2.setBounds(260, 30, 280, 20);

        customSlider.getSlider().addChangeListener(e -> {
            infoString2.setText("Вибрация на LB + RB (Сила вибрации " + customSlider.getValue() + " )");
        });

        Thread gamepadThread = new Thread(() -> {
            try {
                while (true) {
                    gamepadInput.handleXInput();
                    
                    float leftStickAxisX = gamepadInput.getLeftStickAxisX();
                    float leftStickAxisY = gamepadInput.getLeftStickAxisY();

                    float rightStickAxisX = gamepadInput.getRightStickAxisX();
                    float rightStickAxisY = gamepadInput.getRightStickAxisY();

                    buttonA.showButton(gamepadInput.getIsButtonPressedA());
                    buttonB.showButton(gamepadInput.getIsButtonPressedB());
                    buttonX.showButton(gamepadInput.getIsButtonPressedX());
                    buttonY.showButton(gamepadInput.getIsButtonPressedY());

                    buttonLS.setOffsetsXY(
                        buttonLS.getOffsetX() + (int) (leftStickAxisX * 10),
                        buttonLS.getOffsetY() - (int) (leftStickAxisY * 10));
                    if (gamepadInput.getIsButtonPressedLS()) {
                        buttonLS.setLetter("LS");
                        buttonLS.setColorByDefault();
                    } else if(leftStickAxisX != 0 || leftStickAxisY != 0) {
                        float colorIntensive = Math.abs(leftStickAxisX) > Math.abs(leftStickAxisY) ? leftStickAxisX : leftStickAxisY;
                        
                        buttonLS.setLetter("");
                        buttonLS.setColorFromIntensity(colorIntensive);
                        buttonLS.showButton(true);
                    }
                    
                    buttonRS.setOffsetsXY(
                        buttonRS.getOffsetX() + (int) (rightStickAxisX * 10),
                        buttonRS.getOffsetY() - (int) (rightStickAxisY * 10));
                    if (gamepadInput.getIsButtonPressedRS()) {
                        buttonRS.setLetter("RS");
                        buttonRS.setColorByDefault();
                    } else if(rightStickAxisX != 0 || rightStickAxisY != 0) {
                        float colorIntensive = Math.abs(rightStickAxisX) > Math.abs(rightStickAxisY) ? rightStickAxisX : rightStickAxisY;
                        
                        buttonRS.setLetter("");
                        buttonRS.setColorFromIntensity(colorIntensive);
                        buttonRS.showButton(true);
                    }

                    buttonBack.showButton(gamepadInput.getIsButtonPressedBack());
                    buttonStart.showButton(gamepadInput.getIsButtonPressedStart());

                    buttonGuide.showButton(gamepadInput.getIsButtonPressedGuide());
                    buttonUnknown.showButton(gamepadInput.getIsButtonPressedUnknown());

                    buttonArrowUp.showButton(gamepadInput.getIsButtonPressedArrowUp());
                    buttonArrowDown.showButton(gamepadInput.getIsButtonPressedArrowDown());
                    buttonArrowLeft.showButton(gamepadInput.getIsButtonPressedArrowLeft());
                    buttonArrowRight.showButton(gamepadInput.getIsButtonPressedArrowRight());

                    buttonLeftBumper.showButton(gamepadInput.getIsButtonPressedLeftBumper());
                    buttonRightBumper.showButton(gamepadInput.getIsButtonPressedRightBumper());

                    gamepadInput.setVibrationStrength(customSlider.getValue());

                    gamepadInput.exitFromApp();
                }
            } catch (XInputNotLoadedException e) {
                System.err.println("Ошибка загрузки XInput: " + e.getMessage());
                e.printStackTrace();
            }
        });

        gamepadThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(gamepadThread::interrupt));

        contentPanel.add(buttonA);
        contentPanel.add(buttonB);
        contentPanel.add(buttonX);
        contentPanel.add(buttonY);

        contentPanel.add(buttonLS);
        contentPanel.add(buttonRS);

        contentPanel.add(buttonBack);
        contentPanel.add(buttonStart);

        contentPanel.add(buttonGuide);
        contentPanel.add(buttonUnknown);

        contentPanel.add(buttonArrowUp);
        contentPanel.add(buttonArrowDown);

        contentPanel.add(buttonArrowLeft);
        contentPanel.add(buttonArrowRight);

        contentPanel.add(buttonLeftBumper);
        contentPanel.add(buttonRightBumper);

        contentPanel.add(infoString1);
        contentPanel.add(infoString2);
        contentPanel.add(customSlider.getSlider());
        contentPanel.add(imageLabel, BorderLayout.CENTER);
        frame.add(contentPanel);
        // ----------------------------Контент внутри окна-------------------------

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
