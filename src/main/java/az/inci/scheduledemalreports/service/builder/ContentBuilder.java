package az.inci.scheduledemalreports.service.builder;

import az.inci.scheduledemalreports.model.ReportData;

import java.util.List;

public interface ContentBuilder
{
    <T extends ReportData> String build(List<T> data);
}
