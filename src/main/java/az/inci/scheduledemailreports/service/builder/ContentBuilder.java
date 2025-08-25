package az.inci.scheduledemailreports.service.builder;

import az.inci.scheduledemailreports.model.ReportData;

import java.util.List;

public interface ContentBuilder
{
    <T extends ReportData> String build(List<T> data);
}
