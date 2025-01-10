package org.example;

import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputButtons;
import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.XInputDevice14;
import com.github.strikerx3.jxinput.enums.XInputAxis;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import lombok.Getter;

@Getter
public class GamepadInput {

    private Boolean isButtonPressedA, isButtonPressedB, isButtonPressedX, isButtonPressedY;
    private Boolean isButtonPressedLS, isButtonPressedRS;
    private Boolean isButtonPressedBack, isButtonPressedStart;
    
    public boolean isXInputAvailable() {
        if (XInputDevice.isAvailable() || XInputDevice14.isAvailable()) {
            return true;
        }
        return false;
    }

    public void handleXInput() throws XInputNotLoadedException {
        // if (!isXInputAvailable()) {
        //     System.out.println("Не поддерживаются необходимые протоколы XInput");
        //     return;
        // }

        XInputDevice device = XInputDevice.getDeviceFor(0);
    
        if (device.isConnected()) {

            XInputComponents components = device.getComponents();
            XInputButtons    buttons    = components.getButtons();
            XInputAxes       axes       = components.getAxes();

            if (device.poll()) {
                StringBuilder status = new StringBuilder();

                int ltRaw = axes.getRaw(XInputAxis.LEFT_TRIGGER);
                int rtRaw = axes.getRaw(XInputAxis.RIGHT_TRIGGER);

                float lx = axes.get(XInputAxis.LEFT_THUMBSTICK_X);
                float ly = axes.get(XInputAxis.LEFT_THUMBSTICK_Y);
                float rx = axes.get(XInputAxis.RIGHT_THUMBSTICK_X);
                float ry = axes.get(XInputAxis.RIGHT_THUMBSTICK_Y);

                isButtonPressedA = buttons.a ? true : false;
                isButtonPressedB = buttons.b ? true : false;
                isButtonPressedX = buttons.x ? true : false;
                isButtonPressedY = buttons.y ? true : false;

                isButtonPressedLS = buttons.lThumb ? true : false;
                isButtonPressedRS = buttons.rThumb ? true : false;

                isButtonPressedBack  = buttons.back ? true : false;
                isButtonPressedStart = buttons.start ? true : false;

                if (buttons.lShoulder) {
                    status.append("Кнопка LB нажата ");
                }

                if (buttons.rShoulder) {
                    status.append("Кнопка RB нажата ");
                }

                if (buttons.up) {
                    status.append("Стрелка вверх нажата ");
                }

                if (buttons.down) {
                    status.append("Стрелка вниз нажата ");
                }

                if (buttons.left) {
                    status.append("Стрелка влево нажата ");
                }

                if (buttons.right) {
                    status.append("Стрелка вправо нажата ");
                }

                if (XInputDevice.isGuideButtonSupported()) {
                    if (buttons.guide) {
                        status.append("Кнопка Guide нажата ");
                    }
                }

                // Вибрация по LB + RB
                if (buttons.lShoulder && buttons.rShoulder) {
                    int leftMotor  = 256;
                    int rightMotor = 256;

                    device.setVibration(leftMotor, rightMotor);
                } else {
                    device.setVibration(0, 0);
                }
                
                status.append("\n")
                        .append(String.format("LT: %d  RT: %d", ltRaw, rtRaw))
                        .append("\n")
                        .append(String.format("Левый стик: X=%.6f Y=%.6f", lx, ly))
                        .append("\n")
                        .append(String.format("Правый стик: X=%.6f Y=%.6f", rx, ry))
                        .append("\n");
                
                System.out.print(status);
            }
        } else {
            System.out.println("Контроллер не подключен");
        }
    }
}
