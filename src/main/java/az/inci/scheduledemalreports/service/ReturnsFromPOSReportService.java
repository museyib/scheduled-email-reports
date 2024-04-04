package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.ReturnsFromPOSData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReturnsFromPOSReportService extends AbstractService {

    public List<ReturnsFromPOSData> getReportData(String sbeList)
    {
        List<ReturnsFromPOSData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                    exec dbo.SP_RETURNS_FROM_POS :SBE_LIST""");
        query.setParameter("SBE_LIST", sbeList);
        List<Object[]> resultList = query.getResultList();

        for (Object[] result : resultList)
        {
            ReturnsFromPOSData data = new ReturnsFromPOSData();
            data.setInvCode((String) result[0]);
            data.setInvName((String) result[1]);
            data.setQty(Double.parseDouble(String.valueOf(result[2])));
            data.setTrxDate(String.valueOf(result[3]));
            data.setStore((String) result[4]);
            reportData.add(data);
        }

        return reportData;
    }
}
