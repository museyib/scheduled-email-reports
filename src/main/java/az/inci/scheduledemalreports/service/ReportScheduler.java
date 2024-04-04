package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.Recipient;
import az.inci.scheduledemalreports.repository.RecipientRepository;
import az.inci.scheduledemalreports.service.builder.DeletedLinesReportBuilder;
import az.inci.scheduledemalreports.service.builder.ReturnsFromPOSReportBuilder;
import az.inci.scheduledemalreports.service.builder.SaleStockReportBuilder;
import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportScheduler
{
    private final MailService mailService;
    private final SaleStockReportBuilder saleStockReportBuilder;
    private final SaleStockReportService saleStockReportService;
    private final DeletedLinesReportBuilder deletedLinesReportBuilder;
    private final DeletedLinesFromPickingService deletedLinesFromPickingService;
    private final ReturnsFromPOSReportBuilder returnsFromPOSReportBuilder;
    private final ReturnsFromPOSReportService returnsFromPosReportService;
    private final RecipientRepository recipientRepository;

    @Scheduled(cron = "0 0 21 * * *")
    public void sendReport() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("mikayil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az")
        };
        String title = "Günlük satış hesabatı";
        mailService.sendEmail(saleStockReportBuilder.build(saleStockReportService.getReportData()), title, recipients);
    }

    @Scheduled(cron = "0 10 21 * * *")
    public void sendReport2() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("mikayil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az"),
                new InternetAddress("elnur.qasimov@inci.az"),
                new InternetAddress("ramil.quliyev@inci.az"),
                new InternetAddress("seltenet.bagirova@inci.az")
        };
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        mailService.sendEmail(deletedLinesReportBuilder.build(deletedLinesFromPickingService.getReportData()), title, recipients);
    }

    @Scheduled(cron = "0 20 21 * * *")
    public void sendReport3() throws AddressException
    {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        for (Recipient recipient : recipientList) {
            recipients[0] = new InternetAddress(recipient.getEmail());
            mailService.sendEmail(deletedLinesReportBuilder.build(deletedLinesFromPickingService.getReportDataForSbe(recipient.getManagerCode())), title, recipients);
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendReport4() throws AddressException
    {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində POS-dan qaytarılmış mallar";
        for (Recipient recipient : recipientList) {
            recipients[0] = new InternetAddress(recipient.getEmail());
            mailService.sendEmail(returnsFromPOSReportBuilder.build(returnsFromPosReportService.getReportData(recipient.getManagerCode())), title, recipients);
        }
    }
}