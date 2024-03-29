package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class InvSaleStockReportData implements ReportData
{
    private String invCode;
    private String invName;
    private double saleQty;
    private double whsQty;
}
