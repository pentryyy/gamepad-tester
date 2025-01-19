package org.example;

import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import org.example.components.CircleWithLetter;
import org.example.components.CustomImage;
import org.example.components.CustomSlider;
import org.example.components.CustomWindow;
import org.example.components.RectangleWithLetter;
import org.example.components.ShapeFromImage;

public class App {
    private static AtomicBoolean isPanelShowAllowed  = new AtomicBoolean(false);

    public static void main(String[] args) {
        
        // Основная панель программы
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        CustomImage customImage = new CustomImage("gamepad_photo.jpeg");

        JLabel imageLabel = new JLabel(customImage.resize(
            (int) (customImage.getWidth() * 0.65), 
            (int) (customImage.getHeight() * 0.65)));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        CircleWithLetter buttonA = new CircleWithLetter("A", 18, 40,  515, 215);
        CircleWithLetter buttonB = new CircleWithLetter("B", 18, 40,  550, 181);
        CircleWithLetter buttonX = new CircleWithLetter("X", 18, 40,  480, 181);
        CircleWithLetter buttonY = new CircleWithLetter("Y", 18, 40,  515, 148);

        CircleWithLetter buttonLS = new CircleWithLetter("LS", 18, 60,  234, 171, 3);
        CircleWithLetter buttonRS = new CircleWithLetter("RS", 18, 60,  441, 250, 3);

        ShapeFromImage buttonBack = new ShapeFromImage("xbox-back.png", 28, 28,  349, 186);
        ShapeFromImage buttonStart = new ShapeFromImage("xbox-start.png", 26, 26,  426, 186);

        ShapeFromImage buttonGuide   = new ShapeFromImage("xbox-logo.png", 41, 41,  380, 129, Color.WHITE);
        ShapeFromImage buttonUnknown = new ShapeFromImage("share-button.png", 34, 20,  384, 219);

        RectangleWithLetter buttonArrowUp    = new RectangleWithLetter("↑", 18, 25, 29, 6, 0, 318, 247);
        RectangleWithLetter buttonArrowDown  = new RectangleWithLetter("↑", 18, 25, 29, 6, 180, 318, 300);
        RectangleWithLetter buttonArrowLeft  = new RectangleWithLetter("↑", 18, 25, 29, 6, 270, 291, 274);
        RectangleWithLetter buttonArrowRight = new RectangleWithLetter("↑", 18, 25, 29, 6, 90, 346, 274);

        ShapeFromImage buttonLeftBumper  = new ShapeFromImage("xbox-lb.png", 50, 50,  250, 60);
        ShapeFromImage buttonRightBumper = new ShapeFromImage("xbox-rb.png", 50, 50,  500, 60);

        ShapeFromImage buttonLeftTrigger  = new ShapeFromImage("xbox-lt.png", 50, 50,  250, 20);
        ShapeFromImage buttonRightTrigger = new ShapeFromImage("xbox-rt.png", 50, 50,  500, 20);

        GamepadInput gamepadInput = new GamepadInput();

        CustomSlider customSlider = new CustomSlider(0, 65535, 32768);
        customSlider.setBounds(300, 440, 200, 20);

        JLabel exitInfo = new JLabel("Выход на LS + RS (Удерживать)", SwingConstants.CENTER);
        exitInfo.setBounds(300, 10, 200, 20);

        JLabel exitCountdownInfo = new JLabel("", SwingConstants.CENTER);
        exitCountdownInfo.setBounds(300, 30, 200, 20);

        JLabel vibrationInfo = new JLabel("Вибрация на LB + RB (Сила вибрации " + customSlider.getValue() + " )", SwingConstants.CENTER);
        vibrationInfo.setBounds(260, 420, 280, 20);

        customSlider.getSlider().addChangeListener(e -> {
            vibrationInfo.setText("Вибрация на LB + RB (Сила вибрации " + customSlider.getValue() + " )");
        });

        // Панель в случае ошибки
        JPanel errorPanel = new JPanel();
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setLayout(new BorderLayout());

        JLabel errorInfo = new JLabel("Произошла ошибка!", SwingConstants.CENTER);
        errorInfo.setBounds(260, 420, 280, 20);

        Thread gamepadThread = new Thread(() -> {
            try {
                while (true) {
                    gamepadInput.handleXInput();

                    // System.out.println(gamepadInput.getIsErrorAppeared());
                    isPanelShowAllowed.set(gamepadInput.getIsErrorAppeared());
                    if (gamepadInput.getIsErrorAppeared()) {
                        errorInfo.setText("Произошла ошибка " + gamepadInput.getErrorMessage());
                        continue;
                    }
                    
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

                    buttonLeftTrigger.setTransparencyFromIntensity(gamepadInput.getLeftTriggerRawData());
                    buttonRightTrigger.setTransparencyFromIntensity(gamepadInput.getRightTriggerRawData());

                    gamepadInput.setVibrationStrength(customSlider.getValue());

                    gamepadInput.exitFromApp();

                    if (gamepadInput.getIsTimerRunning()) {
                        exitCountdownInfo.setText("Выход через " + (int) (gamepadInput.getCountdown() + 1) + " (секунд)");
                    } else {
                        exitCountdownInfo.setText("");
                    }
                }
            } catch (XInputNotLoadedException e) {}
        });

        gamepadThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(gamepadThread::interrupt));

        errorPanel.add(errorInfo);

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

        contentPanel.add(buttonLeftTrigger);
        contentPanel.add(buttonRightTrigger);

        contentPanel.add(exitInfo);
        contentPanel.add(exitCountdownInfo);
        contentPanel.add(vibrationInfo);
        contentPanel.add(customSlider.getSlider());
        contentPanel.add(imageLabel, BorderLayout.CENTER);
        
        // Выполняем создание и отображение пользовательского окна
        SwingUtilities.invokeLater(() -> {
            CustomWindow window = new CustomWindow(800, 600);
            
            if (isPanelShowAllowed.get()) {
                window.addContentPanel(errorPanel);
            } else {
                window.addContentPanel(contentPanel);
            }
           
            window.show();
        });
    }
}
