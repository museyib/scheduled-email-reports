package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class PurchaseTotalReportData  implements ReportData {
    private double amount;
    private double amountWithOldPrice;
    private double amountDifference;
}
