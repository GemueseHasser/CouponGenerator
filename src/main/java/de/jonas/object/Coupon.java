package de.jonas.object;

import lombok.RequiredArgsConstructor;

/**
 * Ein {@link Coupon} stellt eine Ansammlung an Gutscheinen dar, welche man zu einem PDF-Dokument migrieren kann, bzw .
 * woraus man ein PDF-Dokument generieren kann.
 */
@RequiredArgsConstructor
public final class Coupon {

    //<editor-fold desc="LOCAL FIELDS">
    /** Der Empfänger der Gutscheine. */
    private final String recipient;
    /** Der Anlass für die Gutscheine. */
    private final String reason;
    /** Der Ersteller der Gutscheine. */
    private final String creator;
    /** Die Breite eines Gutscheins. */
    private final int width;
    /** Die Höhe eines Gutscheins. */
    private final int height;
    /** Die Anzahl an Gutscheinen. */
    private final int amount;
    //</editor-fold>


    /**
     * Generiert dieses Gutschein, sodass man ihn abspeichern kann, in Form eines PDF-Dokuments.
     */
    public void generate() {
    }

}
