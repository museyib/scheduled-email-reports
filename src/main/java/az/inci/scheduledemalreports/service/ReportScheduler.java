package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.*;
import az.inci.scheduledemalreports.repository.RecipientRepository;
import az.inci.scheduledemalreports.service.builder.*;
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
    private final PurchaseReportBuilder purchaseReportBuilder;
    private final PurchaseReportService purchaseReportService;
    private final RestoredPricesReportBuilder restoredPricesReportBuilder;
    private final RestoredPricesReportService restoredPricesReportService;
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
                new InternetAddress("israil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az"),
                new InternetAddress("elnur.qasimov@inci.az"),
                new InternetAddress("ramil.quliyev@inci.az"),
                new InternetAddress("seltenet.bagirova@inci.az")
        };
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        List<DeletedLinesFromPickingData> reportData = deletedLinesFromPickingService.getReportData();
        if (!reportData.isEmpty())
            mailService.sendEmail(deletedLinesReportBuilder.build(reportData), title, recipients);
    }

    @Scheduled(cron = "0 20 21 * * *")
    public void sendReport3() {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        for (Recipient recipient : recipientList) {
            List<DeletedLinesFromPickingData> reportData = deletedLinesFromPickingService.getReportDataForSbe(recipient.getManagerCode());
            if (!reportData.isEmpty())
                mailService.sendEmail(deletedLinesReportBuilder.build(reportData), title, recipients);
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendReport4() {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində POS-dan qaytarılmış mallar";
        for (Recipient recipient : recipientList) {
            List<ReturnsFromPOSData> reportData = returnsFromPosReportService.getReportData(recipient.getManagerCode());
            if (!reportData.isEmpty())
                mailService.sendEmail(returnsFromPOSReportBuilder.build(reportData), title, recipients);
        }
    }

//    @Scheduled(cron = "0 30 21 * * *")
    public void sendReport5() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("mikayil.yusifov@inci.az"),
                new InternetAddress("israil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az"),
                new InternetAddress("elnur.qasimov@inci.az")
        };
        String title = "Gün ərzində alınmış mallar";
        List<PurchaseReportData> reportData = purchaseReportService.getReportData();
        if (!reportData.isEmpty())
            mailService.sendEmail(purchaseReportBuilder.build(reportData), title, recipients);
    }

    @Scheduled(cron = "0 10 7 * * *")
    public void sendReport6() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("ilkin.rufullayev@inci.az"),
                new InternetAddress("elnur.qasimov@inci.az")
        };
        String title = "Vaxtı bitmiş endirimli qiymətlər";
        List<RestoredPriceData> reportData = restoredPricesReportService.getReportData();
        if (!reportData.isEmpty())
            mailService.sendEmail(restoredPricesReportBuilder.build(reportData), title, recipients);
    }
}