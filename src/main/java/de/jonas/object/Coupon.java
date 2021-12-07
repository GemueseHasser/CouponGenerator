package de.jonas.object;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.FileOutputStream;

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
    @SneakyThrows
    public void generate() {
        final Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(getCustomFile()));

        document.open();

        final Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.UNDERLINE, BaseColor.BLACK);
        final Paragraph paragraph = new Paragraph("Gutscheine - by Jonas", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph);
        document.close();
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

        return chosen;
    }

}
