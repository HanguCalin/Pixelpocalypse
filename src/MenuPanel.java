import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {

    // The app implements this to "hear"(=react) about the selected image
    public interface Listener {
        void onImageChosen(BufferedImage image, File file);
    }

    public MenuPanel(Listener listener) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints(); // These are the "placement specs"

        constraints.gridx = 0; constraints.gridy = 0; // Column and row indexes
        constraints.insets = new Insets(10, 10, 10, 10); // External margins around the component

        JLabel title = new JLabel("Pixelpocalypse");
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        add(title, constraints);

        constraints.gridy++;
        JButton selectBtn = new JButton("Select Photo");
        add(selectBtn, constraints);

        // “When the action happens, take the event 'event' and {...}
        selectBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose image to pixelpocalypse.");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));

            // Center the dialog on this panel
            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    JOptionPane.showMessageDialog(this, "Unsupported or unreadable image.",
                                                    "Open Error", JOptionPane.ERROR_MESSAGE);
                }
                listener.onImageChosen(image, file);
                // Notifying the app that a valid image was chosen
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to open image:\n" + e.getMessage(),
                                            "Open Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
