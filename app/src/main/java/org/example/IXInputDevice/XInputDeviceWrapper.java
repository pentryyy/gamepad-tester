package org.example.IXInputDevice;

import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice;

public class XInputDeviceWrapper implements IXInputDevice {
    private final XInputDevice device;

    public XInputDeviceWrapper(XInputDevice device) {
        this.device = device;
    }

    @Override
    public XInputComponents getComponents() {
        return device.getComponents();
    }

    @Override
    public Boolean poll() {
        return device.poll();
    }

    @Override
    public Boolean isConnected() {
        return device.isConnected();
    }


    @Override
    public void setVibration(final int leftMotor, final int rightMotor) {
        device.setVibration(leftMotor, rightMotor);
    }
}
