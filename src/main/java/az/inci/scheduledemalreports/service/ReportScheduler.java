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
import java.util.Map;

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
    private final POSAuthSMSReportBuilder posAuthSMSReportBuilder;
    private final POSAuthSMSReportService posAuthSMSReportService;
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
    public void sendReport3() throws AddressException {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        for (Recipient recipient : recipientList) {
            recipients[0] = new InternetAddress(recipient.getEmail());
            List<DeletedLinesFromPickingData> reportData = deletedLinesFromPickingService.getReportDataForSbe(recipient.getManagerCode());
            if (!reportData.isEmpty())
                mailService.sendEmail(deletedLinesReportBuilder.build(reportData), title, recipients);
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendReport4() throws AddressException {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində POS-dan qaytarılmış mallar";
        for (Recipient recipient : recipientList) {
            recipients[0] = new InternetAddress(recipient.getEmail());
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
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[recipientList.size() + 1];
        String title = "Vaxtı bitmiş endirimli qiymətlər";
        recipients[0] = new InternetAddress("elnur.qasimov@inci.az");
        for (int i = 0; i < recipientList.size(); i++) {
            Recipient recipient = recipientList.get(i);
            recipients[i + 1] = new InternetAddress(recipient.getEmail());
        }
        List<RestoredPriceData> reportData = restoredPricesReportService.getReportData();
        if (!reportData.isEmpty())
            mailService.sendEmail(restoredPricesReportBuilder.build(reportData), title, recipients);
    }

    @Scheduled(cron = "0 20 7 * * *")
    public void sendReport7() throws AddressException {
        List<Recipient> recipientList = recipientRepository.findAll();
        Address[] recipients = new Address[1];
        String title = "Gün ərzində POS səlahiyyət kodu üçün göndərilmiş SMS sorğuları";
        for (Recipient recipient : recipientList) {
            recipients[0] = new InternetAddress(recipient.getEmail());
            List<SMSReportData> reportData = posAuthSMSReportService.getReportData(recipient.getManagerCode());
            if (!reportData.isEmpty())
                mailService.sendEmail(posAuthSMSReportBuilder.build(reportData), title, recipients);
        }
    }
}