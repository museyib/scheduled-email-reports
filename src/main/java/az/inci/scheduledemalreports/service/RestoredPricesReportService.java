package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.RestoredPriceData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestoredPricesReportService extends AbstractService {

    public List<RestoredPriceData> getReportData()
    {
        List<RestoredPriceData> reportData = new ArrayList<RestoredPriceData>();

        Query query = entityManager.createNativeQuery("""
                SELECT INV_CODE,
                           INV_NAME,
                           PRICE_CODE,
                           PRICE,
                           BASE_PRICE * FACTOR AS NEW_PRICE
                FROM RESTORED_PRICES
                ORDER BY INV_NAME""");

        List<Object[]> resultList = query.getResultList();

        for (Object[] row : resultList)
        {
            RestoredPriceData data = new RestoredPriceData();
            data.setInvCode((String) row[0]);
            data.setInvName((String) row[1]);
            data.setPriceCode((String) row[2]);
            data.setOldPrice(Double.parseDouble(String.valueOf(row[3])));
            data.setNewPrice(Double.parseDouble(String.valueOf(row[4])));
            reportData.add(data);
        }

        return reportData;
    }
}
