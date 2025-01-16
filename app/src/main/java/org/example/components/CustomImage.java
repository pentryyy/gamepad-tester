package org.example.components;

import javax.swing.ImageIcon;

import lombok.Getter;

import java.awt.Image;

@Getter
public class CustomImage {

    private ImageIcon imageIcon;
    private String    errorMessage;
    private Boolean   isErrorAppeared = false;

    public CustomImage(String imagePath) {
        try {
            var resource = CustomImage.class.getClassLoader().getResource(imagePath);
            if (resource == null) {
                isErrorAppeared = true;
                errorMessage = "Изображение не найдено: " + imagePath;
                return;
            }
            this.imageIcon = new ImageIcon(resource);
        } catch (Exception e) {
            isErrorAppeared = true;
            errorMessage = "Ошибка при загрузке изображения: " + e.getMessage();
        }
    }

    public ImageIcon resize(int width, int height) {
        if (imageIcon == null) {
            isErrorAppeared = true;
            errorMessage = "Невозможно изменить размер: изображение не загружено";
        }
        Image scaledImage = imageIcon
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public int getWidth() {
        return imageIcon.getIconWidth();
    }

    public int getHeight() {
        return imageIcon.getIconHeight();
    }
}
