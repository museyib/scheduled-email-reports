package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.InternalPurchaseReportData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalPurchaseReportService extends AbstractService {
    public List<InternalPurchaseReportData> getReportData() {
        List<InternalPurchaseReportData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                DECLARE @date DATE= GETDATE();
                SELECT IM.INV_CODE,
                       IM.INV_NAME,
                       IM.INV_BRAND_CODE,
                       QTY,
                       UNIT_PRICE_OPR,
                       AMOUNT_OPR,
                       IT.BP_CODE,
                       BM.BP_NAME,
                       IT.TRX_NO
                FROM IVC_TRX IT
                     JOIN INV_MASTER IM ON IT.INV_CODE = IM.INV_CODE
                     JOIN BP_MASTER BM ON IT.BP_CODE = BM.BP_CODE
                     JOIN dbo.FN_GET_SBE_HIERARCHY_FROM_LIST('100') SL ON IT.SBE_CODE = SL.SBE_CODE
                WHERE TRX_DATE = @date
                      AND TRX_TYPE_ID = 15
                ORDER BY IT.SBE_CODE, IT.TRX_NO;""");

        List<Object[]> list = query.getResultList();
        for (Object[] row : list) {
            InternalPurchaseReportData data = new InternalPurchaseReportData();
            data.setInvCode((String) row[0]);
            data.setInvName((String) row[1]);
            data.setInvBrandCode((String) row[2]);
            data.setQty(Double.parseDouble(String.valueOf(row[3])));
            data.setPrice(Double.parseDouble(String.valueOf(row[4])));
            data.setAmount(Double.parseDouble(String.valueOf(row[5])));
            data.setBpCode((String) row[6]);
            data.setBpName((String) row[7]);
            data.setTrxNo((String) row[8]);
            reportData.add(data);
        }
        return reportData;
    }
}
