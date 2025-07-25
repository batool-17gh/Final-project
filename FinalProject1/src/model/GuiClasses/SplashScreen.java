package model.GuiClasses;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class SplashScreen extends JFrame {
    // Constructor receives a Runnable to run after closing the splash screen
    public SplashScreen(Runnable onFinish) {
        // Remove window decorations (title bar, borders)
        setUndecorated(true);

        // Set size and center the window on screen
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Use BorderLayout for main layout
        setLayout(new BorderLayout());

        // Set rounded corners for the window (smooth edges)
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        // Create main panel with white background
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(Color.WHITE);
        add(background, BorderLayout.CENTER);

        // Load image from resources
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/model/img/17.png"));
        Image originalImage = originalIcon.getImage();

        // Scale image to target size with high quality
        Image scaledImage = getHighQualityScaledImage(originalImage, 480, 260);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Create JLabel to display the scaled image, centered horizontally
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(logoLabel, BorderLayout.CENTER);

        // Create a big, styled "Enter" button
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        enterButton.setBackground(new Color(10, 70, 130)); // dark blue color
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false); // remove focus border on click
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // hand cursor on hover
        enterButton.setPreferredSize(new Dimension(300, 50));
        enterButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        enterButton.setHorizontalAlignment(SwingConstants.CENTER);
        enterButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Create panel to hold the button and center it horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(enterButton);
        background.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to the Enter button
        // When clicked, close this splash screen and run the provided onFinish Runnable
        enterButton.addActionListener(e -> {
            dispose();
            onFinish.run();
        });

        // Set the Enter button as the default button (activated on Enter key press)
        getRootPane().setDefaultButton(enterButton);

        // Show the splash screen window
        setVisible(true);
    }

    // Utility method to scale an image with high quality rendering hints
    private Image getHighQualityScaledImage(Image srcImg, int targetWidth, int targetHeight) {
        int imageType = BufferedImage.TYPE_INT_ARGB; // supports transparency
        BufferedImage resizedImg = new BufferedImage(targetWidth, targetHeight, imageType);
        Graphics2D g2 = resizedImg.createGraphics();

        // Set rendering hints for best quality scaling
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Draw the source image scaled to target width and height
        g2.drawImage(srcImg, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return resizedImg;
    }

}
