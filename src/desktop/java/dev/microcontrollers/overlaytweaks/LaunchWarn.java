package dev.microcontrollers.overlaytweaks;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URI;

/**
 * Taken from Sodium
 * https://github.com/CaffeineMC/sodium-fabric/blob/55503937143a72a6c06676ed63f6de7a048e72ee/src/desktop/java/net/caffeinemc/mods/sodium/desktop/LaunchWarn.java
 * which is taken from Iris
 * https://github.com/IrisShaders/Iris/blob/6c880cd377d97ffd5de648ba4dfac7ea88897b4f/src/main/java/net/coderbot/iris/LaunchWarn.java
 * and modified to fit Overlay Tweaks. See Iris' license for more information.
 */
public class LaunchWarn {
    public static void main(String[] args) {
        String message = "Overlay Tweaks is a Fabric mod and needs to be put in your fabric mods folder.\nIf you are unsure how to install Fabric, I have a written guide on how to install Fabric through Prism Launcher.\n\nWould you like to read this guide?";
        String fallback = "Overlay Tweaks is a Fabric mod and needs to be put in your fabric mods folder.\nIf you are unsure how to install Fabric, I have a written guide on how to install Fabric through Prism Launcher here: https://alternatives.microcontrollers.dev/latest/migrating/#installing-fabric";
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println(fallback);
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignored) {
                // Ignored
            }

            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                int option = JOptionPane.showOptionDialog(null, message, "Overlay Tweaks", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "Open Guide", "Cancel" }, JOptionPane.YES_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().browse(URI.create("https://alternatives.microcontrollers.dev/latest/migrating/#installing-fabric"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Fallback for Linux, etc users with no "default" browser
                JOptionPane.showMessageDialog(null, fallback);
            }
        }

        System.exit(0);
    }
}