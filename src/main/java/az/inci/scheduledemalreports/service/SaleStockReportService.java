package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.InvSaleStockReportData;
import az.inci.scheduledemalreports.model.InvSaleStockReportGroup;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleStockReportService extends AbstractService
{

    public List<InvSaleStockReportGroup> getReportData()
    {
        List<InvSaleStockReportGroup> groupList = new ArrayList<>();

        InvSaleStockReportGroup reportGroup1 = new InvSaleStockReportGroup();
        reportGroup1.setGroupName("Kağızlar");
        reportGroup1.setCodes("a001102,a001115,a001124,a001347,a003931,a001098,a001381,a001104");
        reportGroup1.setReportData(new ArrayList<>());
        groupList.add(reportGroup1);

        InvSaleStockReportGroup reportGroup2 = new InvSaleStockReportGroup();
        reportGroup2.setGroupName("A5 dəftərlər");
        reportGroup2.setCodes("a000024,a000025,a000018,a000111,a000019,a000139,a000021,a000020,a000023,a000022");
        reportGroup2.setReportData(new ArrayList<>());
        groupList.add(reportGroup2);

        InvSaleStockReportGroup reportGroup3 = new InvSaleStockReportGroup();
        reportGroup3.setGroupName("B5 dəftərlər");
        reportGroup3.setCodes("a000003,a000523,a000001,a000027,a000026,a000029,a000002,a000017,a000032,a000016,a000455,a000454,a000444,a000119,a000112,a000110,a000140,a000138,a000012,a000010");
        reportGroup3.setReportData(new ArrayList<>());
        groupList.add(reportGroup3);

        InvSaleStockReportGroup reportGroup4 = new InvSaleStockReportGroup();
        reportGroup4.setGroupName("Dəftərxanalar");
        reportGroup4.setCodes("a000041,a000042,a000037,a000036,a000038,a000039");
        reportGroup4.setReportData(new ArrayList<>());
        groupList.add(reportGroup4);

        for(InvSaleStockReportGroup reportGroup : groupList)
        {
            String invList = reportGroup.getCodes();
            Query query = entityManager.createNativeQuery("""
                          SELECT IM.INV_CODE,
                              IM.INV_NAME,
                              IT.QTY AS SALE,
                              WS.QTY AS WHS_QTY
                          FROM INV_MASTER IM
                          JOIN (SELECT IT.INV_CODE, -SUM(IT.QTY * IT.UOM_FACTOR * IT.DBT_CRD) AS QTY
                                  FROM IVC_TRX AS IT
                                  WHERE IT.TRX_DATE = CAST(GETDATE() AS DATE)
                                      AND IT.TRX_TYPE_ID IN (17, 22, 27)
                                  GROUP BY INV_CODE) AS IT ON IM.INV_CODE = IT.INV_CODE
                          JOIN (SELECT INV_CODE, SUM(WHS_QTY) AS QTY
                                  FROM WHS_SUM
                                  GROUP BY INV_CODE) WS ON IM.INV_CODE = WS.INV_CODE
                          WHERE IM.INV_CODE IN (SELECT Item FROM dbo.Split('""" + invList + """
                          ', ','))
                          ORDER BY IM.INV_NAME, IM.INV_CODE""");

            List<Object[]> resultList = query.getResultList();

            for(Object[] result : resultList)
            {
                InvSaleStockReportData data = new InvSaleStockReportData();
                data.setInvCode(String.valueOf(result[0]));
                data.setInvName(String.valueOf(result[1]));
                data.setSaleQty(Double.parseDouble(String.valueOf(result[2])));
                data.setWhsQty(Double.parseDouble(String.valueOf(result[3])));

                reportGroup.getReportData().add(data);
            }
        }

        return groupList;
    }
}
