package az.inci.scheduledemalreports.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Data
@Builder
public class AuthRequest {

    private String username;

    private String password;

    private String secretKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequest that = (AuthRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, secretKey);
    }
}
