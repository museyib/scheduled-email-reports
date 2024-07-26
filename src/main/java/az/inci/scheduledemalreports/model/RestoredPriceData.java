package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class RestoredPriceData implements ReportData {
    private String invCode;
    private String invName;
    private String priceCode;
    private double oldPrice;
    private double newPrice;
}
