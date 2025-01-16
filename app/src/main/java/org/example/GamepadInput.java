package org.example;

import java.util.Timer;
import java.util.TimerTask;

import org.example.IXInputDevice.IXInputDevice;
import org.example.IXInputDevice.XInputDevice14Wrapper;
import org.example.IXInputDevice.XInputDeviceWrapper;

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

    private int vibrationStrength = 32767;
    
    private int     countdown;
    private Timer   timer;
    private String  errorMessage;
    private Boolean isErrorAppeared;
    private Boolean isTimerRunning;

    private Boolean isButtonPressedA, isButtonPressedB, 
                    isButtonPressedX, isButtonPressedY;
    
    private Boolean isButtonPressedArrowUp,   isButtonPressedArrowDown, 
                    isButtonPressedArrowLeft, isButtonPressedArrowRight;

    private Boolean isButtonPressedBack,  isButtonPressedStart,
                    isButtonPressedGuide, isButtonPressedUnknown;

    private Boolean isButtonPressedLeftBumper, isButtonPressedRightBumper;
    private Boolean isButtonPressedLS,         isButtonPressedRS;
    
    private float   leftStickAxisX,     leftStickAxisY;
    private float   rightStickAxisX,    rightStickAxisY;
    private int     leftTriggerRawData, rightTriggerRawData;
    
    private void startTimer() {
        countdown = 5;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (countdown > 0) {
                    countdown--;
                } else {
                    System.exit(0);
                }
            }
        }, 0, 1000); // Интервал обновления - 1 секунда
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

    public void setVibrationStrength(int vibrationStrength) {
        this.vibrationStrength = vibrationStrength;
    }

    private Boolean checkControllerConnection(IXInputDevice device) {
        if (!device.isConnected()) {
            return false;
        }
        
        return true;
    }

    private IXInputDevice selectDevice() throws XInputNotLoadedException {
        if (XInputDevice.isAvailable()) {
            return new XInputDeviceWrapper(XInputDevice.getDeviceFor(0));
        }
        if (XInputDevice14.isAvailable()) {
            return new XInputDevice14Wrapper(XInputDevice14.getDeviceFor(0));
        }
        return null;        
    }

    public void handleXInput() throws XInputNotLoadedException {
        IXInputDevice device = selectDevice();

        if (device == null) {
            isErrorAppeared = true;
            errorMessage    = "Не поддерживаются необходимые протоколы XInput";
            return;
        }

        if (!checkControllerConnection(device)) {
            isErrorAppeared = true;
            errorMessage    = "Контроллер не подключен";
            return;
        }

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
            if (isButtonPressedLeftBumper && isButtonPressedRightBumper) {
                int leftMotor  = vibrationStrength;
                int rightMotor = vibrationStrength;

                device.setVibration(leftMotor, rightMotor);
            } else {
                device.setVibration(0, 0);
            }

            isErrorAppeared = false;
        }
    }
}
