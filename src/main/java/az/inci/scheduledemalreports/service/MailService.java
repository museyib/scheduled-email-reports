package az.inci.scheduledemalreports.service;

import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class MailService
{
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void  sendEmail(String content, String title, Address[] recipients)
    {
        MimeMessagePreparator messagePreparator;
        try
        {
            Address[] recipientsCC = {
                    new InternetAddress("museyib.alekber@inci.az")
            };
            String recipient = "elekbermuseyib@gmail.com";

            messagePreparator = (message) -> {
                message.setRecipients(MimeMessage.RecipientType.TO, recipients);
                message.setRecipients(MimeMessage.RecipientType.CC, recipientsCC);
                message.setSubject(title, "UTF-8");
                message.setFrom("Inci Report Center <report@inci.az>");
                message.setSender(new InternetAddress("report@inci.az"));
                message.setContent(content, "text/html; charset=utf-8");
            };
            mailSender.send(messagePreparator);
            System.out.println("Email sent successfully: " + Arrays.toString(recipients));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            sendEmail(content, title, recipients);
        }
    }
}
