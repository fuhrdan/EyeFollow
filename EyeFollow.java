import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class EyeFollow extends JFrame {
    private int mouseX = 0, mouseY = 0;
    private boolean isBlinking = false;

    public EyeFollow() {
        // Set up the transparent window
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Track mouse movements
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) event;
                mouseX = me.getXOnScreen();
                mouseY = me.getYOnScreen();
                repaint();
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK);

        // Timer for random blinking
        Timer blinkTimer = new Timer(true);
        blinkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isBlinking = true;
                repaint();
                try {
                    Thread.sleep(200); // Blink duration
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isBlinking = false;
                repaint();
            }
        }, 2000, 4000); // Blink every 2-4 seconds randomly

        // Exit on click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get window center
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Eye dimensions
        int eyeRadius = 70;
        int pupilRadius = 20;
        int eyeSpacing = 100;

        // Draw eyes
        g2d.setColor(Color.WHITE);
        if (!isBlinking) {
            g2d.fillOval(centerX - eyeSpacing - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2); // Left eye
            g2d.fillOval(centerX + eyeSpacing - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2); // Right eye

            // Calculate pupil position based on mouse location
            int leftEyeX = centerX - eyeSpacing;
            int rightEyeX = centerX + eyeSpacing;

            int leftPupilX = calculatePupilPosition(leftEyeX, mouseX, eyeRadius);
            int leftPupilY = calculatePupilPosition(centerY, mouseY, eyeRadius);
            int rightPupilX = calculatePupilPosition(rightEyeX, mouseX, eyeRadius);
            int rightPupilY = calculatePupilPosition(centerY, mouseY, eyeRadius);

            // Draw pupils
            g2d.setColor(Color.BLACK);
            g2d.fillOval(leftPupilX - pupilRadius, leftPupilY - pupilRadius, pupilRadius * 2, pupilRadius * 2);
            g2d.fillOval(rightPupilX - pupilRadius, rightPupilY - pupilRadius, pupilRadius * 2, pupilRadius * 2);
        } else {
            // Draw blinking eyes
            g2d.setColor(Color.BLACK);
            g2d.fillRect(centerX - eyeSpacing - eyeRadius, centerY - 10, eyeRadius * 2, 20); // Left eye blink
            g2d.fillRect(centerX + eyeSpacing - eyeRadius, centerY - 10, eyeRadius * 2, 20); // Right eye blink
        }
    }

    private int calculatePupilPosition(int eyeCenter, int mouseCoord, int eyeRadius) {
        int offset = Math.min(eyeRadius - 20, Math.max(-eyeRadius + 20, mouseCoord - eyeCenter));
        return eyeCenter + offset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EyeFollow eyeFollow = new EyeFollow();
            eyeFollow.setVisible(true);
        });
    }
}
