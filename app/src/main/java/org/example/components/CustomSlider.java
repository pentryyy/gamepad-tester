package org.example.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class CustomSlider {

    private JSlider slider;

    CustomSlider(int minimalValue, int maximumValue, int valueByDefault) {
        slider = new JSlider(minimalValue, maximumValue, valueByDefault);

        slider.setOrientation(SwingConstants.HORIZONTAL);
        slider.setOpaque(false);
        slider.setFocusable(false);
        slider.setUI(new BasicSliderUI(slider) {
            @Override
            public void paintThumb(Graphics g) {
                g.setColor(Color.BLACK);
                int thumbRadius = 6;
                int centerX = thumbRect.x + thumbRect.width / 2;
                int centerY = thumbRect.y + thumbRect.height / 2;
                g.fillOval(
                        centerX - thumbRadius,
                        centerY - thumbRadius,
                        thumbRadius * 2,
                        thumbRadius * 2);
            }

            @Override
            public void paintTrack(Graphics g) {
                g.setColor(Color.LIGHT_GRAY);
                Rectangle trackBounds = trackRect;
                int cy = trackBounds.y + (trackBounds.height / 2) - 2;
                g.fillRect(trackBounds.x, cy, trackBounds.width, 4);

                g.setColor(Color.BLACK);
                int progressX = thumbRect.x + thumbRect.width / 2;
                g.fillRect(trackBounds.x, cy, progressX - trackBounds.x, 4);
            }
        });
    }

    public JSlider getSlider() {
        return slider;
    }

    public int getValue() {
        return slider.getValue();
    }

    public void setBounds (int offsetX, int offsetY, int width, int height) {
        slider.setBounds(offsetX, offsetY, width, height);

    }
}

