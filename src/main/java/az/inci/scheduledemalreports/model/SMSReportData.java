package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class SMSReportData implements ReportData {
    private String recipient;
    private String message;
    private String sendTime;
    private String sbeCode;
    private String sbeName;
}
