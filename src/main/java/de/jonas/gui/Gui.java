package de.jonas.gui;

import de.jonas.object.Coupon;
import de.jonas.object.gui.Draw;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Color;
import java.util.Arrays;

/**
 * <p>Ein {@link Gui} stellt ein Fenster dar, welches dem Nutzer die Grundlage bietet, um die Gutscheine zu
 * generieren. Mit dieser grafischen Oberfläche wählt der Nutzer alle Attribute für die Gutscheine aus.</p>
 * <p>Die visuelle Grundlage wird durch ein {@link Draw} geschaffen, mit wessen Hilfe alle nötigen Grafiken auf
 * dieses Fenster gezeichnet werden.</p>
 */
@NotNull
public final class Gui extends JFrame {

    //<editor-fold desc="CONSTANTS">
    /** Der Titel des Fensters. */
    @NotNull
    private static final String TITLE = "Gutschein-Generator";
    /** Die Breite des Fensters. */
    private static final int WIDTH = 600;
    /** Die Höhe des Fensters. */
    private static final int HEIGHT = 450;
    /** Die standardmäßige Breite eines Text-Feldes, um das jeweilige Attribut einzutragen. */
    private static final int TEXT_FIELD_WIDTH = 100;
    /** Die Breite des Buttons, womit man die Gutscheine generieren kann. */
    private static final int GENERATE_BUTTON_WIDTH = 200;
    /** Die Höhe des Buttons, womit man die Gutscheine generieren kann. */
    private static final int GENERATE_BUTTON_HEIGHT = 40;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * <p>Erzeugt eine neue und vollständig unabhängige Instanz eines {@link Gui}. Ein {@link Gui} stellt ein Fenster
     * dar, welches dem Nutzer die Grundlage bietet, um die Gutscheine zu generieren. Mit dieser grafischen Oberfläche
     * wählt der Nutzer alle Attribute für die Gutscheine aus.</p>
     * <p>Die visuelle Grundlage wird durch ein {@link Draw} geschaffen, mit wessen Hilfe alle nötigen Grafiken auf
     * dieses Fenster gezeichnet werden.</p>
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public Gui() {
        super.setTitle(TITLE);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setBounds(0, 0, WIDTH, HEIGHT);
        super.setLocationRelativeTo(null);
        super.setResizable(false);
        super.setLayout(null);

        final Draw draw = new Draw(TITLE);
        draw.setBounds(0, 0, WIDTH, HEIGHT);
        draw.setVisible(true);

        final int x = draw.getObjectX();
        final int size = Draw.DEFAULT_FONT_SIZE;

        final JTextField[] fields = new JTextField[7];

        for (int i = 0; i < fields.length; i++) {
            if (i == 3) continue;

            fields[i] = new JTextField();

            if (i == 4 || i == 5) {
                fields[i].setBounds((i == 5) ? x + 100 : x, Draw.getObjectY(4), TEXT_FIELD_WIDTH / 2, size);
                super.add(fields[i]);
                continue;
            }

            fields[i].setBounds(x, Draw.getObjectY((i > 4) ? i - 1 : i), TEXT_FIELD_WIDTH, size);
            super.add(fields[i]);
        }

        final JButton generate = new JButton("PDF generieren");
        generate.setBounds(
            (WIDTH / 2) - (GENERATE_BUTTON_WIDTH / 2) - 10,
            Draw.getObjectY(fields.length - 1) + 7,
            GENERATE_BUTTON_WIDTH,
            GENERATE_BUTTON_HEIGHT
        );
        generate.setFocusable(false);
        generate.setFont(Draw.DEFAULT_FONT);
        generate.setOpaque(true);
        generate.setBackground(Color.BLACK);
        generate.setForeground(Color.WHITE);
        generate.addActionListener(actionEvent -> {
            // check if all fields are correct
            if (Arrays.stream(fields).anyMatch(field -> field != null && field.getText().trim().equalsIgnoreCase(""))) {
                showError("Bitte fülle alle Felder korrekt aus!");
                return;
            }

            for (int i = 4; i < fields.length; i++) {
                if (!fields[i].getText().matches("[0-9]+")) {
                    showError("Bitte fülle alle Felder korrekt aus!");
                    return;
                }
            }

            final int width = Integer.parseInt(fields[4].getText());
            final int height = Integer.parseInt(fields[5].getText());

            if (width > Coupon.MAX_WIDTH || height > Coupon.MAX_HEIGHT) {
                showError("maximal erlaubte Maße: " + Coupon.MAX_WIDTH + " x " + Coupon.MAX_HEIGHT);
                return;
            }

            // create coupon
            final Coupon coupon = new Coupon(
                fields[0].getText(),
                fields[1].getText(),
                fields[2].getText(),
                width,
                height,
                Integer.parseInt(fields[6].getText())
            );

            // generate coupons
            coupon.generate();
        });

        super.add(generate);
        super.add(draw);
    }
    //</editor-fold>


    /**
     * Öffnet dieses Fenster.
     */
    public void open() {
        super.setVisible(true);
    }

    /**
     * Zeigt dem Nutzer einen Error an und fordert ihn auf alle Felder korrekt auszufüllen.
     */
    private void showError(@NotNull final String error) {
        JOptionPane.showConfirmDialog(
            null,
            error,
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

}
