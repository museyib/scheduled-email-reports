package az.inci.scheduledemalreports.service;

import az.inci.scheduledemalreports.model.InvSaleStockReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MailContentBuilder
{
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    public String build(List<InvSaleStockReportData> data)
    {
        Context context = new Context();
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("data", data);
        return templateEngine.process("reports/mail-report", context);
    }
}
