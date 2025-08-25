package az.inci.scheduledemailreports.model;

import lombok.Data;

import java.util.List;

@Data
public class InvSaleStockReportGroup implements ReportData
{
    private String groupName;
    private String codes;
    private List<InvSaleStockReportData> reportData;
}
