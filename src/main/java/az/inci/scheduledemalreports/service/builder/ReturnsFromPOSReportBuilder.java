package az.inci.scheduledemalreports.service.builder;

import az.inci.scheduledemalreports.model.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReturnsFromPOSReportBuilder implements ContentBuilder
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
        Context context = new Context();
        context.setVariable("date", LocalDate.now().plusDays(-1).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("data", data);
        return templateEngine.process("reports/returns-from-pos", context);
    }
}
