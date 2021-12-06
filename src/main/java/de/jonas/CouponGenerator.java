package de.jonas;

import de.jonas.gui.Gui;
import org.jetbrains.annotations.NotNull;

/**
 * Die Haupt- und Main-Klasse. Von hier aus wird die gesamte Anwendung initialisiert und gestartet.
 */
@NotNull
public final class CouponGenerator {

    //<editor-fold desc="main">

    /**
     * Die Main-Methode der Anwendung. Diese Methode wird von der JRE als aller erstes aufgerufen - vor allen anderen
     * Methoden - und mithilfe dieser Methode wird die gesamte Anwendung gestartet.
     *
     * @param args Die Argumente, die von der JRE übergeben werden.
     */
    public static void main(@NotNull final String @NotNull [] args) {
        final Gui gui = new Gui();
        gui.open();
    }
    //</editor-fold>

}
