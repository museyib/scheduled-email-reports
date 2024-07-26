package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.PurchaseReportData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseReportService extends AbstractService {

    public List<PurchaseReportData> getReportData()
    {
        List<PurchaseReportData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                DECLARE @date DATE = GETDATE()
                SELECT INV_CODE,
                  INV_NAME,
                  INV_BRAND_CODE,
                  QTY,
                  PRICE,
                  AMOUNT,
                  OLD_PRICE,
                  AMOUNT_WITH_OLD_PRICE,
                  AMOUNT_DIFFERENCE,
                  OLD_DATE,
                  OLD_NAME
              FROM (
                  SELECT IM.INV_CODE,
                      IM.INV_NAME,
                      IM.INV_BRAND_CODE,
                      IT_1.QTY,
                      IT_1.UNIT_PRICE_OPR AS PRICE,
                      IT_1.AMOUNT_OPR AS AMOUNT,
                      IT_2.UNIT_PRICE_OPR AS OLD_PRICE,
                      IT_1.QTY * IT_2.UNIT_PRICE_OPR AS AMOUNT_WITH_OLD_PRICE,
                      IT_1.AMOUNT_OPR - IT_1.QTY * IT_2.UNIT_PRICE_OPR AS AMOUNT_DIFFERENCE,
                      dbo.fnFormatDate(IT_2.TRX_DATE, 'dd-mm-yyyy') AS OLD_DATE,
                      IT_2.INV_NAME AS OLD_NAME
                  FROM (
                      SELECT INV_CODE,
                          SUM(QTY) AS QTY,
                          ROUND(AVG(UNIT_PRICE_OPR), 2) AS UNIT_PRICE_OPR,
                          ROUND(SUM(AMOUNT_OPR), 2) AS AMOUNT_OPR
                      FROM IVC_TRX
                      WHERE TRX_DATE = @date
                          AND TRX_TYPE_ID = 15
                          AND SBE_CODE = '120'
                      GROUP BY INV_CODE
                      ) IT_1
                  JOIN (
                      SELECT IT.INV_CODE,
                          IT_L.TRX_DATE,
                          IT.INV_NAME,
                          SUM(QTY) AS QTY,
                          ROUND(AVG(UNIT_PRICE_OPR), 2) AS UNIT_PRICE_OPR
                      FROM (
                          SELECT DISTINCT MAX(TRX_DATE) OVER (PARTITION BY INV_CODE) TRX_DATE,
                              INV_CODE,
                              INV_NAME
                          FROM IVC_TRX
                          WHERE TRX_DATE < @date
                              AND TRX_TYPE_ID = 15
                              AND SBE_CODE = '120'
                          ) IT_L
                      JOIN IVC_TRX IT
                          ON IT_L.TRX_DATE = IT.TRX_DATE
                              AND IT_L.INV_CODE = IT.INV_CODE
                              AND IT.TRX_TYPE_ID = 15
                              AND IT.SBE_CODE = '120'
                      GROUP BY IT.INV_CODE,
                          IT_L.TRX_DATE,
                          IT.INV_NAME
                      ) IT_2
                      ON IT_1.INV_CODE = IT_2.INV_CODE
                  JOIN INV_MASTER IM
                      ON IT_1.INV_CODE = IM.INV_CODE
                  ) T
              ORDER BY AMOUNT_DIFFERENCE DESC""");

        List<Object[]> list = query.getResultList();
        for (Object[] row : list) {
            PurchaseReportData data = new PurchaseReportData();
            data.setInvCode((String) row[0]);
            data.setInvName((String) row[1]);
            data.setInvBrandCode((String) row[2]);
            data.setQty(Double.parseDouble(String.valueOf(row[3])));
            data.setPrice(Double.parseDouble(String.valueOf(row[4])));
            data.setAmount(Double.parseDouble(String.valueOf(row[5])));
            data.setOldPrice(Double.parseDouble(String.valueOf(row[6])));
            data.setAmountWithOldPrice(Double.parseDouble(String.valueOf(row[7])));
            data.setAmountDifference(Double.parseDouble(String.valueOf(row[8])));
            data.setOldTrxDate(String.valueOf(row[9]));
            data.setOldName((String) row[10]);
            reportData.add(data);
        }
        return reportData;
    }
}
