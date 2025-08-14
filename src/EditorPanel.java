import javax.swing.*;
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

    private final JLabel preview = new JLabel("Image Preview", SwingConstants.CENTER);
}
