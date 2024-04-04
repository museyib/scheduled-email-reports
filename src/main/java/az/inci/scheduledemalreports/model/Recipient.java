package az.inci.scheduledemalreports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MAIL_REPORT_RECIPIENT")
public class Recipient
{
    @Id
    @Column(name = "EMAIL_ADDRESS")
    private String email;

    @Column(name = "RECIPIENT_NAME")
    private String name;

    @Column(name = "WHS_LIST")
    private String warehouseList;

    @Column(name = "SBE_LIST")
    private String managerCode;
}
