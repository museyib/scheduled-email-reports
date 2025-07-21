package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class InternalPurchaseReportData implements ReportData {
    private String invCode;
    private String invName;
    private String invBrandCode;
    private double qty;
    private double price;
    private double amount;
    private String bpCode;
    private String bpName;
    private String bpPhone;
    private String sbeCode;
    private String sbeName;
    private String trxNo;
    private String description;
}
