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

    private String  errorMessage;
    private Boolean isButtonPressedA, isButtonPressedB, isButtonPressedX, isButtonPressedY;
    private Boolean isButtonPressedLS, isButtonPressedRS;
    private Boolean isButtonPressedBack, isButtonPressedStart;
    private Boolean isButtonPressedGuide, isButtonPressedUnknown;
    private Boolean isButtonPressedArrowUp, isButtonPressedArrowDown, isButtonPressedArrowLeft, isButtonPressedArrowRight;
    private float   leftStickAxisX, leftStickAxisY;
    private float   rightStickAxisX, rightStickAxisY;
    
    public boolean isXInputAvailable() {
        if (XInputDevice.isAvailable() || XInputDevice14.isAvailable()) {
            return true;
        }

        errorMessage = "Не поддерживаются необходимые протоколы XInput";
        return false;
    }

    public void handleXInput() throws XInputNotLoadedException {
        XInputDevice device = XInputDevice.getDeviceFor(0);
    
        if (device.isConnected()) {

            XInputComponents components = device.getComponents();
            XInputButtons    buttons    = components.getButtons();
            XInputAxes       axes       = components.getAxes();

            if (device.poll()) {
                StringBuilder status = new StringBuilder();

                int ltRaw = axes.getRaw(XInputAxis.LEFT_TRIGGER);
                int rtRaw = axes.getRaw(XInputAxis.RIGHT_TRIGGER);

                leftStickAxisX = axes.get(XInputAxis.LEFT_THUMBSTICK_X);
                leftStickAxisY = axes.get(XInputAxis.LEFT_THUMBSTICK_Y);
                
                rightStickAxisX = axes.get(XInputAxis.RIGHT_THUMBSTICK_X);
                rightStickAxisY = axes.get(XInputAxis.RIGHT_THUMBSTICK_Y);

                isButtonPressedA = buttons.a ? true : false;
                isButtonPressedB = buttons.b ? true : false;
                isButtonPressedX = buttons.x ? true : false;
                isButtonPressedY = buttons.y ? true : false;

                isButtonPressedLS = buttons.lThumb ? true : false;
                isButtonPressedRS = buttons.rThumb ? true : false;

                isButtonPressedBack  = buttons.back ? true : false;
                isButtonPressedStart = buttons.start ? true : false;

                isButtonPressedGuide   = buttons.guide ? true : false;
                isButtonPressedUnknown = buttons.unknown ? true : false;

                isButtonPressedArrowUp    = buttons.up ? true : false;
                isButtonPressedArrowDown  = buttons.down ? true : false;
                isButtonPressedArrowLeft  = buttons.left ? true : false;
                isButtonPressedArrowRight = buttons.right ? true : false;

                status.append("Кнопка unknown - " + isButtonPressedUnknown + " ");

                if (buttons.lShoulder) {
                    status.append("Кнопка LB нажата ");
                }

                if (buttons.rShoulder) {
                    status.append("Кнопка RB нажата ");
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
                        .append(String.format("Левый стик: X=%.6f Y=%.6f", leftStickAxisX, leftStickAxisY))
                        .append("\n")
                        .append(String.format("Правый стик: X=%.6f Y=%.6f", rightStickAxisX, rightStickAxisY))
                        .append("\n");
                
                System.out.print(status);
            }
        } else {
            System.out.println("Контроллер не подключен");
        }
    }
}
