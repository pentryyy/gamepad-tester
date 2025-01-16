package org.example.IXInputDevice;

import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice14;

public class XInputDevice14Wrapper implements IXInputDevice {
    private final XInputDevice14 device;

    public XInputDevice14Wrapper(XInputDevice14 device) {
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
