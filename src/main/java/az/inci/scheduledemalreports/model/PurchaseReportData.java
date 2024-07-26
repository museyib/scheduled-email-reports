package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class PurchaseReportData  implements ReportData {
    private String invCode;
    private String invName;
    private String invBrandCode;
    private double qty;
    private double price;
    private double amount;
    private double oldPrice;
    private double amountWithOldPrice;
    private double amountDifference;
    private String oldTrxDate;
    private String oldName;
}
