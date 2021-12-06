package de.jonas.object.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Mithilfe des {@link Draw}, werden alle nötigen Grafiken auf das jeweilige Fenster gezeichnet, damit die visuelle
 * grundlage für einen {@link de.jonas.CouponGenerator} geschaffen wird.
 */
@NotNull
@RequiredArgsConstructor
public final class Draw extends JLabel {

    //<editor-fold desc="CONSTANTS">
    /** Die Schriftgröße der standard Schriftart. */
    public static final int DEFAULT_FONT_SIZE = 20;
    /** Die Schriftart, die für alle normalen Schriften genutzt wird. */
    @NotNull
    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, DEFAULT_FONT_SIZE);
    /** Der Abstand zu den äußeren Rändern des Fensters, den das innere Rechteck haben soll. */
    private static final int INNER_RECT_MARGIN = 100;
    /** Die Schriftart, mit der die Überschrift geschrieben wird. */
    @NotNull
    private static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 30);
    /** Die Y-Koordinate der Überschrift. */
    private static final int HEADING_Y = 40;
    /** Die X-Koordinate, ab wo die Attribute des Gutscheins beginnen. */
    private static final int ATTRIBUTES_BEGIN_X = 110;
    /** Die X-Koordinate, ab wo die Attribute des Gutscheins beginnen. */
    private static final int ATTRIBUTES_BEGIN_Y = 120;
    /** Der Multiplikator des Zeilenabstandes. */
    private static final double LINE_SPACING_MULTIPLIER = 1.5;
    /** Alle Attribute. */
    @NotNull
    private static final String @NotNull [] ATTRIBUTES = {
        "Empfänger:",
        "Anlass:",
        "Ersteller:",
        "",
        "Größe:",
        "Anzahl",
    };
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Der Titel des Fensters, auf welches diese Klasse alle nötigen Grafiken zeichnet. */
    @NotNull
    private final String title;
    /** Die X-Koordinate, ab der alle Objekte anfangen platziert zu werden. */
    @Getter
    @Range(from = 0, to = Integer.MAX_VALUE)
    private final int objectX = this.getFontMetrics(DEFAULT_FONT).stringWidth(
        Arrays
            .stream(ATTRIBUTES)
            .max(Comparator.comparing(String::length))
            .orElseThrow(NullPointerException::new)
    ) + ATTRIBUTES_BEGIN_X + 50;
    //</editor-fold>

    //<editor-fold desc="implementation">
    @Override
    @SuppressWarnings("checkstyle:MagicNumber")
    protected void paintComponent(@NotNull final Graphics g) {
        super.paintComponent(g);

        // draw background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(
            0,
            0,
            super.getWidth(),
            super.getHeight()
        );

        // draw heading
        g.setColor(Color.BLACK);
        g.setFont(HEADING_FONT);

        g.drawString(title, (super.getWidth() / 2) - (g.getFontMetrics().stringWidth(title) / 2) - 15, HEADING_Y);

        // draw inner rect
        g.setColor(Color.DARK_GRAY);
        g.fillRect(
            INNER_RECT_MARGIN - 10,
            INNER_RECT_MARGIN - 10,
            super.getWidth() - (2 * INNER_RECT_MARGIN),
            super.getHeight() - (2 * INNER_RECT_MARGIN)
        );

        // draw attributes
        g.setColor(Color.WHITE);
        g.setFont(DEFAULT_FONT);

        for (int i = 0; i < ATTRIBUTES.length; i++) {
            g.drawString(ATTRIBUTES[i], ATTRIBUTES_BEGIN_X, getAttributeY(i));
        }

        // draw 'x' for size
        g.drawString("x", this.objectX + 70, getAttributeY(4) - 5);
    }
    //</editor-fold>

    //<editor-fold desc="utility">

    /**
     * Berechnet die Y-Koordinate für ein bestimmtes Attribut bzw. für eine bestimmte Zeile, in der das Attribut stehen
     * soll.
     *
     * @param line Die Zeile, in der das Attribut stehen soll.
     *
     * @return Die Y-Koordinate für ein bestimmtes Attribut bzw. für eine bestimmte Zeile, in der das Attribut stehen
     *     soll.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    public static int getAttributeY(@Range(from = 0, to = Integer.MAX_VALUE) final int line) {
        return (int) (ATTRIBUTES_BEGIN_Y + (line * (DEFAULT_FONT_SIZE * LINE_SPACING_MULTIPLIER)));
    }

    /**
     * Berechnet die Y-Koordinate für ein bestimmtes Objekt auf der Basis einer bestimmten Y-Koordinate eines
     * Attributs.
     *
     * @param line Die Zeile, in der das Objekt platziert werden soll.
     *
     * @return Die Y-Koordinate für ein bestimmtes Objekt auf der Basis einer bestimmten Y-Koordinate eines Attributs.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    public static int getObjectY(@Range(from = 0, to = Integer.MAX_VALUE) final int line) {
        return getAttributeY(line) - DEFAULT_FONT_SIZE;
    }
    //</editor-fold>
}
