package model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class SplashScreen extends JFrame {

    public SplashScreen(Runnable onFinish) {
        // إعداد النافذة
        setUndecorated(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // زوايا ناعمة
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        // خلفية بيضاء
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(Color.WHITE);
        add(background, BorderLayout.CENTER);

        // تحميل الصورة وتكبيرها بجودة عالية جداً
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("17.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = getHighQualityScaledImage(originalImage, 480, 260);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(logoLabel, BorderLayout.CENTER);

        // زر "Enter" كبير وأنيق
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        enterButton.setBackground(new Color(10, 70, 130));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enterButton.setPreferredSize(new Dimension(300, 50));
        enterButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        enterButton.setHorizontalAlignment(SwingConstants.CENTER);
        enterButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // لوحة للزر في المنتصف
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(enterButton);
        background.add(buttonPanel, BorderLayout.SOUTH);

        // حدث الضغط على الزر
        enterButton.addActionListener(e -> {
            dispose();
            onFinish.run();
        });

        getRootPane().setDefaultButton(enterButton);
        setVisible(true);
    }

    // دالة لتحجيم الصورة بأعلى جودة ممكنة
    private Image getHighQualityScaledImage(Image srcImg, int width, int height) {
        int imageType = BufferedImage.TYPE_INT_ARGB;
        BufferedImage resizedImg = new BufferedImage(width, height, imageType);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();

        return resizedImg;
    }
}
