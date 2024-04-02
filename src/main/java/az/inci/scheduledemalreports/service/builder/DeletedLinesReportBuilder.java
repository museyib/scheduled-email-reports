package az.inci.scheduledemalreports.service.builder;

import az.inci.scheduledemalreports.model.DeletedLinesFromPickingData;
import az.inci.scheduledemalreports.model.DeletedLinesFromPickingData.ExtraDataDetails;
import az.inci.scheduledemalreports.model.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DeletedLinesReportBuilder implements ContentBuilder
{
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    @Override
    public <T extends ReportData> String build(List<T> data)
    {
        Map<String, ExtraDataDetails> extraData = new TreeMap<>();
        Map<String, List<DeletedLinesFromPickingData>> mappedData = new TreeMap<>();

        for(T item: data)
        {
            DeletedLinesFromPickingData pickingData = (DeletedLinesFromPickingData) item;
            String reason = pickingData.getNotPickedReason().isEmpty() ? "{səbəb təyin edilməyib}" : pickingData.getNotPickedReason();
            String whsCode = pickingData.getWhsCode();
            if(extraData.containsKey(reason))
            {
                ExtraDataDetails details = extraData.get(reason);
                Map<String, Integer> whsItemCount = details.getWhsItemCount();
                details.incrementCount();
                details.setTotalQty(details.getTotalQty() + pickingData.getDeletedQty());
                details.setTotalAmount(details.getTotalAmount() + pickingData.getDeletedAmount());

                if (whsItemCount.containsKey(whsCode))
                {
                    whsItemCount.put(whsCode, whsItemCount.get(whsCode) + 1);
                }
                else
                {
                    whsItemCount.put(whsCode, 1);
                }
            }
            else
            {
                ExtraDataDetails details = getExtraDataDetails(data, whsCode, pickingData);
                extraData.put(reason, details);
            }

            if (mappedData.containsKey(whsCode))
            {
                List<DeletedLinesFromPickingData> dataList = mappedData.get(whsCode);
                dataList.add(pickingData);
            }
            else
            {
                List<DeletedLinesFromPickingData> dataList = new ArrayList<>();
                dataList.add(pickingData);
                mappedData.put(whsCode, dataList);
            }
        }

        Context context = new Context();
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("mapped_data", mappedData);
        context.setVariable("extra_data", extraData);
        return templateEngine.process("reports/deleted-lines-from-picking-report", context);
    }

    private static <T extends ReportData> ExtraDataDetails getExtraDataDetails(List<T> data, String whsCode, DeletedLinesFromPickingData pickingData) {
        Map<String, Integer> tmpWhsItemCount = new TreeMap<>();
        for(T tmpItem: data) {
            DeletedLinesFromPickingData tmpPickingData = (DeletedLinesFromPickingData) tmpItem;
            String tmpWhsCode = tmpPickingData.getWhsCode();
            tmpWhsItemCount.put(tmpWhsCode, 0);
        }


        if (tmpWhsItemCount.containsKey(whsCode))
        {
            tmpWhsItemCount.put(whsCode, tmpWhsItemCount.get(whsCode) + 1);
        }
        else
        {
            tmpWhsItemCount.put(whsCode, 1);
        }

        ExtraDataDetails details = new ExtraDataDetails();
        details.setWhsItemCount(tmpWhsItemCount);
        details.setItemCount(1);
        details.setTotalQty(pickingData.getDeletedQty());
        details.setTotalAmount(pickingData.getDeletedAmount());
        return details;
    }
}
