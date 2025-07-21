package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.InternalPurchaseReportData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternalPurchaseReportService extends AbstractService {
    public List<InternalPurchaseReportData> getReportData() {
        List<InternalPurchaseReportData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                DECLARE @date DATE= GETDATE();
                SELECT IM.INV_CODE,
                       IM.INV_NAME,
                       IM.INV_BRAND_CODE,
                       QTY,
                       UNIT_PRICE_OPR,
                       IT.AMOUNT_OPR,
                       IT.BP_CODE,
                       BM.BP_NAME,
                       ISNULL(BM.PHONE1, '') + ISNULL('; ' + BM.PHONE2, '') + ISNULL('; ' + BM.PHONE3, '') + ISNULL('; ' + BM.PHONE4, ''),
                       IT.SBE_CODE,
                       SL.SBE_NAME,
                       IT.TRX_NO,
                       AD.DOC_DESC
                FROM IVC_TRX IT
                    JOIN INV_MASTER IM ON IT.INV_CODE = IM.INV_CODE
                    JOIN BP_MASTER BM ON IT.BP_CODE = BM.BP_CODE
                    JOIN ACC_DOC AD ON IT.TRX_NO = AD.TRX_NO
                    JOIN dbo.FN_GET_SBE_HIERARCHY_FROM_LIST('120') SL ON IT.SBE_CODE = SL.SBE_CODE
                WHERE IT.TRX_DATE = @date
                      AND IT.TRX_TYPE_ID = 15
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
            data.setBpPhone((String) row[8]);
            data.setSbeCode((String) row[9]);
            data.setSbeName((String) row[10]);
            data.setTrxNo((String) row[11]);
            data.setDescription((String) row[12]);
            reportData.add(data);
        }
        return reportData;
    }
}
