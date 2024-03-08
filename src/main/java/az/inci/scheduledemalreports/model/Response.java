package az.inci.scheduledemalreports.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response
{
    public static final String NOT_FOUND_VALID_SHIPMENT = "Bu sənəd üzrə keçərli yükləmə tapılmadı!";
    public static final String NOT_VALID_SHIPMENT_DOC = "Yükləmə üçün keçərli sənəd deyil.";

    private int statusCode;
    private String systemMessage;
    private String developerMessage;
    private Object data;


    public static Response getResultResponse(Object result)
    {
        return Response.builder()
                       .statusCode(0)
                       .data(result)
                       .build();
    }

    public static Response getSuccessResponse()
    {
        return Response.builder()
                       .statusCode(0)
                       .developerMessage("Uğurlu əməliyyat")
                       .build();
    }

    public static Response getServerErrorResponse(String message)
    {
        return Response.builder()
                       .statusCode(1)
                       .systemMessage(message)
                       .developerMessage("Server xətası")
                       .build();
    }

    public static Response getUserErrorResponse(String message)
    {
        return Response.builder()
                       .statusCode(2)
                       .developerMessage(message)
                       .build();
    }
}
