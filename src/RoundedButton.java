import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {


    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final int BORDER_THICKNESS = 2;
    private static final int BORDER_RADIUS = 10;

    public RoundedButton(String text) {
        super(text);
        setForeground(TEXT_COLOR);
        setFocusPainted(true);
        Font font = getFont(); // Pobranie aktualnej czcionki
        Font biggerFont = font.deriveFont(font.getSize() + 4f); // Tworzenie nowej czcionki z większym rozmiarem
        setFont(biggerFont);
        setSize(800, 400);
    }

}
