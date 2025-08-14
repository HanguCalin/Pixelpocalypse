import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private JFrame frame;
    private JPanel panel; // Using a CardLayout for the panels to emulate "scenes". The 2 scenes
                          // that are present here are the main page and the editor page.
    private CardLayout cards;

    private static String CardMenu = "menu";
    private static String CardEditor = "editor";

    private BufferedImage original; // As loaded
    private BufferedImage working; // Current editable copy
    private JLabel preview; // Lives in editor card

    private EditorPanel editor;

    public static void main(String[] args) {
        // This schedules your UI setup to run on the EDT
        // If it's not done it will affect the performance of the program depending on its size

        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private static BufferedImage deepCopy(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return dst;
    }

    private void start() {
        frame = new JFrame("Pixelpocalypse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new CardLayout();
        panel = new JPanel(cards);
        frame.setContentPane(panel);

        // Adding the 2 "scenes" (temporary placeholders so the program can run)
         editor = new EditorPanel(new EditorPanel.Listener() {
            @Override
            public void onBack() {
                original = null;
                working = null;
                cards.show(panel, CardMenu);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }

            @Override
            public void onSave(BufferedImage current) {
                savePng(current);
            }

            @Override
            public void onInvert() {
                if (working == null) {
                    return;
                }

                invertImage(working);
                editor.setImage(working);
            }

            @Override
            public void onScramble() {

            }

            @Override
            public void onRotate() {

            }

            @Override
            public void onShift() {

            }

            @Override
            public void onNoise() {

            }
        });
        panel.add(editor, CardEditor);

        MenuPanel menu = new MenuPanel((image, file) -> {
            original = image;
            working = deepCopy(image);
            editor.setImage(working);
            cards.show(panel, CardEditor);
            frame.pack();
            frame.setLocationRelativeTo(null);
        });
        panel.add(menu, CardMenu);


        // Showing the menu
        cards.show(panel, CardMenu);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centering the window
        frame.setVisible(true);
    }

    private void savePng(BufferedImage img) {
        if (img == null) {
            JOptionPane.showMessageDialog(frame, "There's nothing to save yet.",
                    "Save", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser saver = new JFileChooser();
        saver.setDialogTitle("Save glitched image");
        saver.setSelectedFile(new File("glitched.png")); // Default name

        int choice = saver.showSaveDialog(frame);
        if (choice != JFileChooser.APPROVE_OPTION) return;

        File out = saver.getSelectedFile();
        String path = out.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".png")) {
            out = new File(path + ".png"); // Forcing PNG extension
        }

        try {
            ImageIO.write(img, "png", out);
            JOptionPane.showMessageDialog(frame, "Saved to:\n" + out.getAbsolutePath(),
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Save failed:\n" + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void invertImage(BufferedImage img) {
        if (img == null) {
            return;
        }

        int width = img.getWidth(); int height = img.getHeight();
        for (int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                int rgb = img.getRGB(i, j); // ARGB = 0xAARRGGBB (32 bits)

                int alpha = (rgb >> 24) & 0xFF;
                int red = 255 - ((rgb >> 16) & 0xFF);
                int green  = 255 - ((rgb >> 8) & 0xFF);
                int blue = 255 - (rgb & 0xFF);

                int new_rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                img.setRGB(i, j, new_rgb);
            }
        }
    }
}
