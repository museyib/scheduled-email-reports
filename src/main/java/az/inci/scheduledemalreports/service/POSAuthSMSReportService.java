package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.SMSReportData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class POSAuthSMSReportService extends AbstractService {
    public List<SMSReportData> getReportData(String sbeList)
    {
        List<SMSReportData> smsReportDataList = new ArrayList<SMSReportData>();

        Query query = entityManager.createNativeQuery("""
                        exec dbo.SP_POS_SMS_REPORT :SBE_LIST""");
        query.setParameter("SBE_LIST", sbeList);
        List<Object[]> resultList = query.getResultList();

        for (Object[] row : resultList)
        {
            SMSReportData data = new SMSReportData();
            data.setRecipient((String) row[0]);
            data.setMessage((String) row[1]);
            data.setSendTime(String.valueOf(row[2]));
            data.setSbeCode((String) row[3]);
            data.setSbeName((String) row[4]);
            smsReportDataList.add(data);
        }

        return smsReportDataList;
    }
}
