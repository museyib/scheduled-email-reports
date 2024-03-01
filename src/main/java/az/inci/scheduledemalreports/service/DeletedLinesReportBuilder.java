package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.DeletedLinesFromPickingData;
import az.inci.scheduledemalreports.model.DeletedLinesFromPickingData.ExtraDataDetails;
import az.inci.scheduledemalreports.model.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        data.sort(Comparator.comparing(o -> ((DeletedLinesFromPickingData) o).getNotPickedReason()));
        Map<String, ExtraDataDetails> extraData = new HashMap<>();
        for(T item: data)
        {
            DeletedLinesFromPickingData pickingData = (DeletedLinesFromPickingData) item;
            String reason = pickingData.getNotPickedReason().isEmpty() ? "{səbəb təyin edilməyib}" : pickingData.getNotPickedReason();
            if(extraData.containsKey(reason))
            {
                ExtraDataDetails details = extraData.get(reason);
                details.incrementCount();
                details.setTotalQty(details.getTotalQty() + pickingData.getDeletedQty());
                details.setTotalAmount(details.getTotalAmount() + pickingData.getDeletedAmount());
            }
            else
            {
                ExtraDataDetails details = new ExtraDataDetails();
                details.setItemCount(1);
                details.setTotalQty(pickingData.getDeletedQty());
                details.setTotalAmount(pickingData.getDeletedAmount());
                extraData.put(reason, details);
            }
        }

        Context context = new Context();
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("data", data);
        context.setVariable("extra_data", extraData);
        return templateEngine.process("reports/deleted-lines-from-picking-report", context);
    }
}
