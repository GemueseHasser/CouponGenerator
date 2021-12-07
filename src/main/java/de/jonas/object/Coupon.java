package de.jonas.object;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Ein {@link Coupon} stellt eine Ansammlung an Gutscheinen dar, welche man zu einem PDF-Dokument migrieren kann, bzw.
 * woraus man ein PDF-Dokument generieren kann. Dieses PDF-Dokument kann dann an einer beliebig wählbaren Position
 * gespeichert werden. Alle Eigenschaften des Gutscheins sind völlig variabel konfigurierbar, jedoch darf die Breite und
 * die Höhe eines Gutscheins nicht die Breite und Höhe eines Blattes überschreiten.
 */
public final class Coupon {

    //<editor-fold desc="CONSTANTS">
    /** Die maximale Breite eines Gutscheins. */
    public static final int MAX_WIDTH = 400;
    /** Die maximale Höhe eines Gutscheins. */
    public static final int MAX_HEIGHT = 700;
    /** Der Abstand zu der oberen Kante des Dokuments, ab dem die Gutscheine beginnen. */
    private static final int SPACING_BEFORE_COUPONS = 50;
    /** Die Schriftart für die Überschrift eines jeden Gutscheins. */
    private static final Font COUPON_HEADING_FONT = FontFactory.getFont(
        FontFactory.COURIER_BOLD,
        20,
        Font.UNDERLINE,
        BaseColor.BLACK
    );
    /** Die standard Schriftart für einen jeden Gutschein. */
    private static final Font COUPON_DEFAULT_FONT = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
    /** Die Schriftart für den Grund des Gutscheins. */
    private static final Font COUPON_REASON_FONT = FontFactory.getFont(
        FontFactory.TIMES_BOLDITALIC,
        17,
        BaseColor.BLACK
    );
    /** Die Schriftart für den Ersteller des Gutscheins. */
    private static final Font COUPON_CREATOR_FONT = FontFactory.getFont(
        FontFactory.TIMES_ITALIC,
        15,
        BaseColor.DARK_GRAY
    );
    //</editor-fold>


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
    /** Die Skalierung des Gutscheins. */
    private final int scaling;
    //</editor-fold>


    /**
     * Erzeugt eine neue und vollständig unabhängige Instanz eines {@link Coupon Gutscheins}. Ein {@link Coupon} stellt
     * eine Ansammlung an Gutscheinen dar, welche man zu einem PDF-Dokument migrieren kann, bzw. woraus man ein
     * PDF-Dokument generieren kann. Dieses PDF-Dokument kann dann an einer beliebig wählbaren Position gespeichert
     * werden. Alle Eigenschaften des Gutscheins sind völlig variabel konfigurierbar, jedoch darf die Breite und die
     * Höhe eines Gutscheins nicht die Breite und Höhe eines Blattes überschreiten.
     *
     * @param recipient Der Empfänger der Gutscheine.
     * @param reason    Der Anlass für die Gutscheine.
     * @param creator   Der Ersteller der Gutscheine.
     * @param width     Die Breite eines Gutscheins.
     * @param height    Die Höhe eines Gutscheins.
     * @param amount    Die Anzahl an Gutscheinen.
     * @param scaling   Die Skalierung des Gutscheins.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public Coupon(
        @NotNull final String recipient,
        @NotNull final String reason,
        @NotNull final String creator,
        @Range(from = 0, to = 400) final int width,
        @Range(from = 0, to = 700) final int height,
        @Range(from = 0, to = Integer.MAX_VALUE) final int amount,
        @Range(from = 0, to = Integer.MAX_VALUE) final int scaling
    ) {
        this.recipient = recipient;
        this.reason = reason;
        this.creator = creator;
        this.width = width;
        this.height = height;
        this.amount = amount;
        this.scaling = scaling;

        // set scaling
        final float finallyScaling = (float) ((scaling == 1) ? 1 : (1 + (0.1 * scaling)));

        COUPON_HEADING_FONT.setSize(COUPON_HEADING_FONT.getSize() * finallyScaling);
        COUPON_DEFAULT_FONT.setSize(COUPON_DEFAULT_FONT.getSize() * finallyScaling);
        COUPON_REASON_FONT.setSize(COUPON_REASON_FONT.getSize() * finallyScaling);
        COUPON_CREATOR_FONT.setSize(COUPON_CREATOR_FONT.getSize() * finallyScaling);
    }


    /**
     * Generiert dieses Gutschein, sodass man ihn abspeichern kann, in Form eines PDF-Dokuments.
     */
    @SneakyThrows
    public void generate() {
        // create pdf document
        final Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getCustomFile()));

        // open pdf document for editing
        document.open();

        // write heading
        final Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.UNDERLINE, BaseColor.BLACK);
        final Paragraph paragraph = new Paragraph("Gutscheine - by Jonas", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        // create coupons
        final int columns = (int) (document.getPageSize().getWidth() / this.width);
        final PdfPTable coupons = new PdfPTable(columns);
        coupons.setSpacingBefore(SPACING_BEFORE_COUPONS);

        // add all coupons to table
        for (int i = 1; i < this.amount + 1; i++) {
            coupons.addCell(getCouponCell());
        }

        // complete last table row
        coupons.completeRow();

        // add objects
        document.add(paragraph);
        document.add(coupons);

        // close pdf document
        document.close();
    }

    /**
     * Der Gutschein wird in Form einer {@link PdfPCell} erstellt, welcher dann {@code amount} mal auf dem Dokument
     * erscheint.
     *
     * @return Der Gutschein in Form einer {@link PdfPCell}, welcher {@code amount} mal auf dem Dokument erscheint.
     */
    private PdfPCell getCouponCell() {
        final Paragraph text = new Paragraph();

        text.setFont(COUPON_HEADING_FONT);
        text.add("Gutschein");

        text.setFont(COUPON_DEFAULT_FONT);
        text.add("\n\n");

        for (int i = 0; i < this.scaling; i++) {
            text.add("\n");
        }

        text.add("für " + this.recipient);

        text.add("\n\n");

        for (int i = 0; i < this.scaling; i++) {
            text.add("\n");
        }

        text.add("für ");
        text.setFont(COUPON_REASON_FONT);
        text.add(this.reason);

        text.add("\n\n\n\n");

        for (int i = 0; i < this.scaling; i++) {
            text.add("\n");
        }

        text.setFont(COUPON_DEFAULT_FONT);
        text.add("von ");
        text.setFont(COUPON_CREATOR_FONT);
        text.add(this.creator);

        final PdfPCell cell = new PdfPCell(text);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(this.height);

        return cell;
    }

    /**
     * Öffnet dem Nutzer ein Menü, worin er einen bestimmten Speicherort auswählen kann, unter dem er das generierte
     * PDF-Dokument speichern möchte. Dieser gewählte Pfad, wird dann als {@link File} zurückgegeben.
     *
     * @return Der ausgewählte Speicherort des Nutzers für das generierte PDF-Dokument.
     */
    @NotNull
    private File getCustomFile() {
        // create file chooser based on home directory
        final JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setDialogTitle("Speichern unter...");

        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Portable Document Format (PDF)",
            "pdf"
        );

        // customize file filters
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.addChoosableFileFilter(filter);

        // choose file
        final int result = chooser.showSaveDialog(null);

        final File defaultFile = FileSystemView.getFileSystemView().createFileObject(
            FileSystemView.getFileSystemView().getHomeDirectory(),
            "generated.pdf"
        );

        // if chosen file is invalid return home directory
        if (result != JFileChooser.APPROVE_OPTION) return defaultFile;

        // get chosen file
        final File chosen = new File(chooser.getSelectedFile().getAbsolutePath() + ".pdf");

        // if chosen file format is invalid return home directory
        if (!filter.accept(chosen)) return defaultFile;

        // return chosen file
        return chosen;
    }

}
