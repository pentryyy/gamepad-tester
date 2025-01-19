package org.example.components;

import javax.swing.ImageIcon;

import lombok.Getter;

import java.awt.Image;

@Getter
public class CustomImage {

    private ImageIcon imageIcon;

    public CustomImage(String imagePath) {
        try {
            var resource = CustomImage.class.getClassLoader().getResource(imagePath);
            if (resource == null) {
                return;
            }
            this.imageIcon = new ImageIcon(resource);
        } catch (Exception e) {}
    }

    public ImageIcon resize(int width, int height) {
        if (imageIcon != null) {
            Image scaledImage = imageIcon
                    .getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            return null;
        }
    }

    public int getWidth() {
        return imageIcon.getIconWidth();
    }

    public int getHeight() {
        return imageIcon.getIconHeight();
    }
}
