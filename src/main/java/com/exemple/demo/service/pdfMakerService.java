package com.exemple.demo.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.exemple.demo.entities.Assuree;
import com.exemple.demo.entities.Simulation;
import com.exemple.demo.entities.Subscription;
import com.exemple.demo.entities.Vehicule;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class pdfMakerService {

    public byte[] generatePdfSouscription(Subscription subscription) throws Exception {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font fontTitle = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, Font.BOLD);
            // fontTitle.setSize(28);
            Paragraph paraTitle = new Paragraph("Attestation de Souscription".toUpperCase(), fontTitle);
            paraTitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paraTitle);
            document.add(new Paragraph("\n\n"));
            addTexte(document, "Date de souscription:", subscription.getDateCreated());
            addTexte(document, "Réference:",
                    subscription.getQuoteReference() != null ? subscription.getQuoteReference() : "Neant");
            addTexte(document, "Montant du Prime:", subscription.getDetails().get("Montant Prime").toString() + "CFA");
            document.add(new Paragraph("\n\n"));
            addClient(document, subscription.getAssuree());
            document.add(new Paragraph("\n\n"));
            addVehicule(document, subscription.getVehicule());
            document.add(new Paragraph("\n\n"));
            // addClient(document, subscription.getAssuree());
            // document.add(new Paragraph("\n\n"));
            addProduit(document, subscription);
            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void addTexte(Document document, String header, String value) {
        document.add(
                new Paragraph(header + "\t" + value));
    }

    private static void addClient(Document document, Assuree assuree) {
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Informations du Client", sectionFont));
        addTexte(document, "Nom:", assuree.getNom());
        addTexte(document, "Cin:", assuree.getCin());
        addTexte(document, "Adresse:", assuree.getAdresse());
        addTexte(document, "Téléphone:", assuree.getTelephone());

        document.add(new Paragraph("\n"));
    }

    // information du vehicule
    private static void addVehicule(Document document, Vehicule vehicule) {
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Informations du Véhicule", sectionFont));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell(createCell("Modèle :"));
        table.addCell(createCell(vehicule.getModele()));
        table.addCell(createCell("Puissance Civile :"));
        table.addCell(createCell(Integer.toString(vehicule.getPuissance())));
        table.addCell(createCell("Valeur vénale :"));
        table.addCell(createCell(Double.toString(vehicule.getValeurVenale())));
        table.addCell(createCell("Date Mise circulation :"));
        table.addCell(createCell(vehicule.getDateMiseCirculation()));
        table.addCell(createCell("Couleur :"));
        table.addCell(createCell(vehicule.getColor()));
        table.addCell(createCell("Nombres de siège :"));
        table.addCell(createCell(Integer.toString(vehicule.getNombreSieges())));
        table.addCell(createCell("Nombres de porte :"));
        table.addCell(createCell(Integer.toString(vehicule.getNombresPortes())));
        // table.addCell(createCell("CIN :"));
        // table.addCell(createCell(assuree.getCin()));

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    // information du vehicule
    private static void addProduit(Document document, Subscription subscription) {
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Informations produit", sectionFont));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell(createCell("Nom du produit :"));
        table.addCell(createCell(subscription.getProduitAssure().getNomProduit()));
        table.addCell(createCell("Montant du prime :"));
        table.addCell(createCell(subscription.getDetails().get("Montant Prime").toString()));

        // table.addCell(createCell("CIN :"));
        // table.addCell(createCell(assuree.getCin()));

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    // Méthode utilitaire pour créer une cellule normal
    private static PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setPadding(5);
        return cell;
    }

}
