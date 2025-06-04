package az.inci.scheduledemalreports.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleLimitExceededLine implements ReportData {
    private String trxNo;
    private String trxDate;
    private String invCode;
    private String invName;
    private int qty;
    private String bpCode;
    private String bpName;
    private String sbeCode;
    private String sbeName;
}
