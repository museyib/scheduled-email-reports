package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class MailRequest
{
    private String title;
    private String content;
    private String recipients;
}
