package az.inci.scheduledemalreports.service.builder;

import az.inci.scheduledemalreports.model.ReportData;
import az.inci.scheduledemalreports.model.SMSReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class POSAuthSMSReportBuilder implements ContentBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    @Override
    public <T extends ReportData> String build(List<T> data) {

        Map<String, Integer> smsCountData = new TreeMap<>();
        for (T dataItem : data) {
            SMSReportData smsReportData = (SMSReportData) dataItem;
            String key = smsReportData.getSbeCode() + " - " + smsReportData.getSbeName();
            if (smsCountData.containsKey(key)) {
                smsCountData.put(key, smsCountData.get(key) + 1);
            }
            else {
                smsCountData.put(key, 1);
            }
        }

        Context context = new Context();
        context.setVariable("date", LocalDate.now().plusDays(-1).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("data", data);
        context.setVariable("count_data", smsCountData);
        return templateEngine.process("reports/pos-sms-log", context);
    }
}
