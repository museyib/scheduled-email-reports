package az.inci.scheduledemalreports.service;

import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportEmailService
{
    private final JavaMailSender mailSender;

    public ReportEmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void  sendEmail(String content)
    {
        MimeMessagePreparator messagePreparator;
        try
        {
            Address[] recipients = {
                    new InternetAddress("mikayil.yusifov@inci.az"),
                    new InternetAddress("isa.abbasov@inci.az")
            };
            Address[] recipientsCC = {
                    new InternetAddress("museyib.alekber@inci.az"),
                    new InternetAddress("museyib.cr@gmail.com"),
//                    new InternetAddress("elnur.qasimov@inci.az")
            };
            String recipient = "museyib.alekber@inci.az";

            messagePreparator = (message) -> {
                message.setRecipients(MimeMessage.RecipientType.TO, recipients);
                message.setRecipients(MimeMessage.RecipientType.CC, recipientsCC);
                message.setSubject("Günlük satış hesabatı", "UTF-8");
                message.setFrom("Inci Report Center <report@inci.az>");
                message.setSender(new InternetAddress("report@inci.az"));
                message.setContent(content, "text/html; charset=utf-8");
            };
            mailSender.send(messagePreparator);
        }
        catch(Exception e)
        {
            log.error(e.toString());
            sendEmail(content);
        }
    }
}
