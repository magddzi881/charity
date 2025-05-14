package pl.sii.charity.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.sii.charity.dto.CreateFundraisingEventDTO;
import pl.sii.charity.dto.FinancialReportDTO;
import pl.sii.charity.entity.FundraisingEvent;
import pl.sii.charity.error.EventNotFoundException;
import pl.sii.charity.repository.FundraisingEventRepository;

import java.io.*;
import java.util.List;

@Service
public class FundraisingEventService {

    private final FundraisingEventRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(FundraisingEventService.class);

    public FundraisingEventService(FundraisingEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new fundraising event based on the provided DTO
     */
    public void createEvent(CreateFundraisingEventDTO dto) {
        FundraisingEvent event = new FundraisingEvent();
        event.setName(dto.name);
        event.setCurrency(dto.currency);
        repository.save(event);
    }

    /**
     * Retrieves all fundraising events and generates a financial report
     * Also saves the report to a text file
     */
    public List<FinancialReportDTO> getFinancialReport() {
        List<FinancialReportDTO> report = repository.findAll().stream()
                .map(e -> new FinancialReportDTO(e.getName(), e.getAmount(), e.getCurrency()))
                .toList();

        saveReportToTxt(report);
        saveReportToPdf(report);
        return report;
    }

    /**
     * Retrieves a fundraising event by its ID
     */
    public FundraisingEvent getEvent(Long eventId) {
        return repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    /**
     * Saves the provided fundraising event to the repository
     */
    public void save(FundraisingEvent event) {
        repository.save(event);
    }

    /**
     * Saves the financial report to a text file
     */
    private void saveReportToTxt(List<FinancialReportDTO> report) {
        String fileName = "financial_report.txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write("Fundraising Event\tAmount\tCurrency\n");

            for (FinancialReportDTO entry : report) {
                String amount = (entry.getAmount() == null) ? "-" : String.format("%.2f", entry.getAmount());
                writer.write(String.format("%s\t%s\t%s\n", entry.getEventName(), amount, entry.getCurrency()));
            }

            System.out.println("Financial report saved to " + fileName);
        } catch (IOException e) {
            logger.error("Failed to save financial report ", e);
        }
    }

    /**
     * Saves the financial report to a pdf file
     */
    private void saveReportToPdf(List<FinancialReportDTO> report) {
        String fileName = "financial_report.pdf";

        try (Document document = new Document()) {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font cellFont = new Font(Font.HELVETICA, 12);

            Paragraph title = new Paragraph("Financial Report", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.addCell(new PdfPCell(new Phrase("Fundraising Event", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Amount", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Currency", tableHeaderFont)));

            for (FinancialReportDTO entry : report) {
                table.addCell(new PdfPCell(new Phrase(entry.getEventName(), cellFont)));
                String amount = (entry.getAmount() == null) ? "" : String.format("%.2f", entry.getAmount());
                table.addCell(new PdfPCell(new Phrase(amount, cellFont)));
                table.addCell(new PdfPCell(new Phrase(entry.getCurrency().name(), cellFont)));
            }
            document.add(table);

            System.out.println("PDF report saved to " + fileName);
        } catch (Exception e) {
            logger.error("Failed to save PDF financial report", e);
        }
    }
}
