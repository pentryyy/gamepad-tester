package org.example;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomWindow window = new CustomWindow();
            window.show();
        });
    }
}
