import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorPanel extends JPanel {

    // The app implements this to "hear"(=react) the button clicks
    public interface Listener {
        void onBack();
        void onSave(BufferedImage current);
        void onInvert();
        void onScramble();
        void onRotate();
        void onShift();
        void onNoise();
    }

    public void setImage(BufferedImage img) {
        preview.setIcon(new ImageIcon(img));
        preview.setText(null);
        revalidate();
        repaint();
    }

    public BufferedImage getCurrentImage() {
        Icon icon = preview.getIcon();
        if (!(icon instanceof ImageIcon)) {
            return null;
        }

        Image img = ((ImageIcon) icon).getImage();
        return (img instanceof BufferedImage) ? (BufferedImage) img : null;
    }

    private final JLabel preview = new JLabel("Image Preview", SwingConstants.CENTER);

    public EditorPanel(Listener listener) {
        setLayout(new BorderLayout());
        preview.setPreferredSize(new Dimension(900, 600));
        add(preview, BorderLayout.CENTER);

        // Vertical button bar
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS)); // We set it on the Y-axis so that every
                                                               // button is under one another
        side.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton invertBtn = new JButton("Invert");
        JButton scrambleBtn = new JButton("Scramble");
        JButton rotateBtn = new JButton("Rotate");
        JButton shiftBtn = new JButton("Channel Shift");
        JButton noiseBtn = new JButton("Noise");
        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");

        for (JButton button: new JButton[] {invertBtn, scrambleBtn, rotateBtn, shiftBtn, noiseBtn, saveBtn, backBtn}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            side.add(button);
            side.add(Box.createVerticalStrut(10));
        }

        add(side, BorderLayout.EAST);

        invertBtn.addActionListener(event -> listener.onInvert());
        scrambleBtn.addActionListener(event -> listener.onScramble());
        rotateBtn.addActionListener(event -> listener.onRotate());
        shiftBtn.addActionListener(event -> listener.onShift());
        noiseBtn.addActionListener(event -> listener.onNoise());
        saveBtn.addActionListener(event -> listener.onSave(getCurrentImage()));
        backBtn.addActionListener(event -> listener.onBack());

    }
}
