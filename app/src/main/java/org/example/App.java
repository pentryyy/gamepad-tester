package org.example;

import javax.swing.SwingUtilities;

import org.example.components.CustomWindow;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomWindow window = new CustomWindow();
            window.show();
        });
    }
}
