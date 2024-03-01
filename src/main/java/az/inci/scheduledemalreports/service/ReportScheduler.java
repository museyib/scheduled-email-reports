package az.inci.scheduledemalreports.service;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportScheduler
{
    private final ReportEmailService emailService;
    private final SaleStockReportBuilder saleStockReportBuilder;
    private final SaleStockReportService saleStockReportService;
    private final DeletedLinesReportBuilder deletedLinesReportBuilder;
    private final DeletedLinesFromPickingService deletedLinesFromPickingService;

    @Scheduled(cron = "0 0 21 * * *")
    public void sendReport() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("mikayil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az")
        };
        String title = "Günlük satış hesabatı";
        emailService.sendEmail(saleStockReportBuilder.build(saleStockReportService.getReportData()), title, recipients);
    }

    @Scheduled(cron = "0 10 21 * * *")
    public void sendReport2() throws AddressException
    {
        Address[] recipients = {
                new InternetAddress("mikayil.yusifov@inci.az"),
                new InternetAddress("isa.abbasov@inci.az"),
                new InternetAddress("elnur.qasimov@inci.az")
        };
        String title = "Gün ərzində yığımda azaldılmış/silinmiş mallar";
        emailService.sendEmail(deletedLinesReportBuilder.build(deletedLinesFromPickingService.getReportData()), title, recipients);
    }
}