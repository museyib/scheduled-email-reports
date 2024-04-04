package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class ReturnsFromPOSData implements ReportData {
    private String invCode;
    private String invName;
    private double qty;
    private String trxDate;
    private String store;
}
