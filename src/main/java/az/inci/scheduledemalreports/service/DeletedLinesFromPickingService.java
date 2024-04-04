package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.DeletedLinesFromPickingData;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeletedLinesFromPickingService extends AbstractService
{

    public List<DeletedLinesFromPickingData> getReportData()
    {
        List<DeletedLinesFromPickingData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                     declare @start datetime=cast(getdate() as date), @end datetime=getdate()
                     exec dbo.SP_DELETED_INVENTORY_BY_DETAILS @start, @end,'*'""");

        return getDeletedLinesFromPickingData(reportData, query);
    }

    public List<DeletedLinesFromPickingData> getReportDataForSbe(String sbeList)
    {
        List<DeletedLinesFromPickingData> reportData = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                     declare @start datetime=cast(getdate() as date), @end datetime=getdate()
                     exec dbo.SP_DELETED_INVENTORY_BY_DETAILS_FOR_STORE @start, @end, :SBE_LIST""");
        query.setParameter("SBE_LIST", sbeList);

        return getDeletedLinesFromPickingData(reportData, query);
    }

    private List<DeletedLinesFromPickingData> getDeletedLinesFromPickingData(List<DeletedLinesFromPickingData> reportData, Query query) {
        List<Object[]> resultList = query.getResultList();

        for (Object[] result : resultList)
        {
            DeletedLinesFromPickingData data = new DeletedLinesFromPickingData();
            data.setTrxNo(String.valueOf(result[0]));
            data.setInvCode(String.valueOf(result[2]));
            data.setInvName(String.valueOf(result[3]));
            data.setInvBrand(String.valueOf(result[4]));
            data.setDeletedQty(Double.parseDouble(String.valueOf(result[5])));
            data.setDeletedAmount(Double.parseDouble(String.valueOf(result[6])));
            data.setWhsCode(String.valueOf(result[8]));
            data.setWhsQty(Double.parseDouble(String.valueOf(result[9])));
            data.setBpCode(String.valueOf(result[11]));
            data.setBpName(String.valueOf(result[12]));
            String sbeCode = String.valueOf(result[13]);
            String sbeName = String.valueOf(result[14]);
            String targetWhs = String.valueOf(result[15]);

            if(sbeCode == null || sbeCode.isEmpty() || sbeCode.equals("null"))
                data.setTarget(targetWhs);
            else
                data.setTarget(sbeCode + " - " + sbeName);

            data.setPickUser(String.valueOf(result[16]));
            data.setPackUser(String.valueOf(result[17]));
            data.setNotPickedReason(String.valueOf(result[18]));

            reportData.add(data);
        }

        return reportData;
    }
}
