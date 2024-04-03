package az.inci.scheduledemalreports.controller;

import az.inci.scheduledemalreports.model.AuthRequest;
import az.inci.scheduledemalreports.model.MailRequest;
import az.inci.scheduledemalreports.model.Response;
import az.inci.scheduledemalreports.service.MailService;
import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailController
{
    private final MailService mailService;

    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @Value("${api.secret-key}")
    private String secretKey;

    @PostMapping(value = "/send-mail", consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> sendMail(@RequestBody MailRequest request)
    {
        try
        {
            AuthRequest authRequest =  AuthRequest.builder()
                    .username(username)
                    .password(password)
                    .secretKey(secretKey)
                    .build();
            if (!authRequest.equals(request.getAuthRequest()))
            {
                log.error("Not authorized!");
                return ResponseEntity.ok(Response.getUserErrorResponse("Not authorized!"));
            }

            String[] addresses = request.getRecipients().split(" ");
            Address[] recipients = new Address[addresses.length];
            for(int i = 0; i < recipients.length; i++)
            {
                recipients[i] = new InternetAddress(addresses[i]);
            }

            mailService.sendEmail(request.getContent(), request.getTitle(), recipients);

            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
