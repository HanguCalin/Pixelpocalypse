import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    private JFrame frame;
    private JPanel panel; // Using a CardLayout for the panels to emulate "scenes". The 2 scenes
                          // that are present here are the main page and the editor page.
    private CardLayout cards;

    private static String CardMenu = "menu";
    private static String CardEditor = "editor";

    private BufferedImage original;   // as loaded
    private BufferedImage working;    // current editable copy
    private JLabel preview;           // lives in editor card

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
        MenuPanel menu = new MenuPanel((image, file) -> {
            original = image;
            working = deepCopy(image);

            if (preview != null) {
                preview.setIcon(new ImageIcon(working));
                preview.setText(null);
            }

            cards.show(panel, CardEditor);
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        panel.add(menu, CardEditor);
        panel.add(buildCardEditor(), CardEditor);

        // Showing the menu
        cards.show(panel, CardMenu);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centering the window
        frame.setVisible(true);
    }

    private JPanel buildCardEditor() {
        JPanel p = new JPanel(new BorderLayout());

        JLabel preview = new JLabel("Image Preview", SwingConstants.CENTER);
        preview.setPreferredSize(new Dimension(900, 600));
        p.add(preview, BorderLayout.CENTER);

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

        for (JButton button: new JButton[]{invertBtn, scrambleBtn, rotateBtn, shiftBtn, noiseBtn, saveBtn, backBtn}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            side.add(button);
            side.add(Box.createVerticalStrut(10));
        }

        backBtn.addActionListener(event -> cards.show(panel, CardMenu));

        p.add(side, BorderLayout.EAST);
        return p;
    }
}
