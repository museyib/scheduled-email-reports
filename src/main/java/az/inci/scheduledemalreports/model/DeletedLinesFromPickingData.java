package az.inci.scheduledemalreports.model;

import lombok.Data;

@Data
public class DeletedLinesFromPickingData implements ReportData
{
    private String trxNo;
    private String invCode;
    private String invName;
    private String invBrand;
    private double deletedQty;
    private double deletedAmount;
    private String whsCode;
    private double whsQty;
    private String bpCode;
    private String bpName;
    private String sbeCode;
    private String sbeName;
    private String target;
    private String pickUser;
    private String packUser;
    private String notPickedReason;

    @Data
    public static class ExtraDataDetails
    {
        private int itemCount;
        private double totalQty;
        private double totalAmount;

        public void incrementCount()
        {
            itemCount++;
        }
    }
}
