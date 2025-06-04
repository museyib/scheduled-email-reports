package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.SaleLimitExceededLine;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleLimitExceededReportService extends AbstractService {
    public List<SaleLimitExceededLine> getReportData() {
        List<SaleLimitExceededLine> reportData = new ArrayList<>();

        String invList1 = "a001098";
        String invList2 = "a031878 a001058 a001124 a003931 a001102";
        Query query = entityManager.createNativeQuery("""
                     SELECT
                         IT.TRX_NO,
                         CAST(IT.TRX_DATE as DATE) AS TRX_DATE,
                         IT.INV_CODE,
                         IT.INV_NAME,
                         IT.QTY,
                         IT.BP_CODE,
                         BT.BP_NAME,
                         IT.SBE_CODE,
                         BT.SBE_NAME
                     FROM IVC_TRX IT
                         JOIN BP_TRX BT ON IT.TRX_NO = BT.TRX_NO
                     WHERE IT.TRX_DATE = CAST(GETDATE() AS DATE)
                       AND BT.TRX_TYPE_ID = 17
                       AND ((INV_CODE IN (
                         SELECT Item FROM dbo.Split('""" + invList1 + """
                            ', ' ')) AND QTY >= 50) OR (INV_CODE IN (
                         SELECT Item FROM dbo.Split('""" + invList2 + """
                            ', ' ')) AND QTY >= 100))
                    ORDER BY IT.TRX_NO DESC""");
        List<Object[]> resultList = query.getResultList();

        for (Object[] result : resultList) {
            SaleLimitExceededLine line = new SaleLimitExceededLine();
            line.setTrxNo((String) result[0]);
            line.setTrxDate(String.valueOf(result[1]));
            line.setInvCode((String) result[2]);
            line.setInvName((String) result[3]);
            line.setQty((int) Double.parseDouble(String.valueOf(result[4])));
            line.setBpCode((String) result[5]);
            line.setBpName((String) result[6]);
            line.setSbeCode((String) result[7]);
            line.setSbeName((String) result[8]);
            reportData.add(line);
        }
        return reportData;
    }
}
