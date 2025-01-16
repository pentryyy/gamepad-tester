package org.example.IXInputDevice;

import com.github.strikerx3.jxinput.XInputComponents;

public interface IXInputDevice {
    XInputComponents getComponents();
    Boolean          poll();
    Boolean          isConnected(); 
    void             setVibration(final int leftMotor, final int rightMotor);
}
