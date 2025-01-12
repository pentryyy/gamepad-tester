package org.example;

import java.util.Timer;
import java.util.TimerTask;

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

    private int     vibrationStrength = 32767;

    private Boolean isTimerRunning    = false;
    private Timer   timer;

    private String  errorMessage;

    private Boolean isButtonPressedA, isButtonPressedB, 
                    isButtonPressedX, isButtonPressedY;
    
    private Boolean isButtonPressedArrowUp, isButtonPressedArrowDown, 
                    isButtonPressedArrowLeft, isButtonPressedArrowRight;

    private Boolean isButtonPressedLeftBumper, isButtonPressedRightBumper;
    private Boolean isButtonPressedLS, isButtonPressedRS;
    private Boolean isButtonPressedBack, isButtonPressedStart;
    private Boolean isButtonPressedGuide, isButtonPressedUnknown;
    private float   leftStickAxisX, leftStickAxisY;
    private float   rightStickAxisX, rightStickAxisY;
    private int     leftTriggerRawData, rightTriggerRawData;
    
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.exit(0);
            }
        }, 5000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    // Выход через 5 секунд при удержании LS + RS
    public void exitFromApp() {
        if (isButtonPressedLS && isButtonPressedRS) {
            if (!isTimerRunning) {
                startTimer();
                isTimerRunning = true;
            }
        } else {
            isTimerRunning = false;
            stopTimer();
        }
    }

    public int getVibrationStrength() {
        return vibrationStrength;
    }

    public void setVibrationStrength(int vibrationStrength) {
        this.vibrationStrength = vibrationStrength;
    }
    
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

                leftTriggerRawData  = axes.getRaw(XInputAxis.LEFT_TRIGGER);
                rightTriggerRawData = axes.getRaw(XInputAxis.RIGHT_TRIGGER);

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

                isButtonPressedLeftBumper  = buttons.lShoulder ? true : false;
                isButtonPressedRightBumper = buttons.rShoulder ? true : false;

                // Вибрация по LB + RB
                if (buttons.lShoulder && buttons.rShoulder) {
                    int leftMotor  = vibrationStrength;
                    int rightMotor = vibrationStrength;

                    device.setVibration(leftMotor, rightMotor);
                } else {
                    device.setVibration(0, 0);
                }
            }
        } else {
            errorMessage = "Контроллер не подключен";
        }
    }
}
