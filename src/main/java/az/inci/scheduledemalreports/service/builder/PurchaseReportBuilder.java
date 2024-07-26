package az.inci.scheduledemalreports.service.builder;

import az.inci.scheduledemalreports.model.PurchaseReportData;
import az.inci.scheduledemalreports.model.PurchaseTotalReportData;
import az.inci.scheduledemalreports.model.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseReportBuilder implements ContentBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    @Override
    public <T extends ReportData> String build(List<T> data) {
        Context context = new Context();
        PurchaseTotalReportData generalTotal = new PurchaseTotalReportData();
        PurchaseTotalReportData incrementTotal = new PurchaseTotalReportData();
        PurchaseTotalReportData decrementTotal = new PurchaseTotalReportData();

        List<PurchaseReportData> incrementData = new ArrayList<>();
        List<PurchaseReportData> decrementData = new ArrayList<>();

        for (T item : data)
        {
            PurchaseReportData reportData = (PurchaseReportData) item;
            generalTotal.setAmount(generalTotal.getAmount() + reportData.getAmount());
            generalTotal.setAmountWithOldPrice(generalTotal.getAmountWithOldPrice() + reportData.getAmountWithOldPrice());

            if (reportData.getAmountDifference() > 0)
            {
                incrementTotal.setAmount(incrementTotal.getAmount() + reportData.getAmount());
                incrementTotal.setAmountWithOldPrice(incrementTotal.getAmountWithOldPrice() + reportData.getAmountWithOldPrice());
                incrementData.add(reportData);
            } else if (reportData.getAmountDifference() < 0) {
                decrementTotal.setAmount(decrementTotal.getAmount() + reportData.getAmount());
                decrementTotal.setAmountWithOldPrice(decrementTotal.getAmountWithOldPrice() + reportData.getAmountWithOldPrice());
                decrementData.add(reportData);
            }
        }
        generalTotal.setAmountDifference(generalTotal.getAmount() - generalTotal.getAmountWithOldPrice());
        incrementTotal.setAmountDifference(incrementTotal.getAmount() - incrementTotal.getAmountWithOldPrice());
        decrementTotal.setAmountDifference(decrementTotal.getAmount() - decrementTotal.getAmountWithOldPrice());

        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("data", data);
        context.setVariable("incrementData", incrementData);
        context.setVariable("incrementTotal", incrementTotal);
        context.setVariable("decrementData", decrementData);
        context.setVariable("decrementTotal", decrementTotal);
        context.setVariable("generalTotal", generalTotal);
        return templateEngine.process("reports/purchase-report", context);
    }
}
