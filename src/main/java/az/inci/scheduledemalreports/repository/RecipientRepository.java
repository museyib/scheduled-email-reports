package az.inci.scheduledemalreports.repository;

import az.inci.scheduledemalreports.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, String> {
    @Override
    List<Recipient> findAll();
}
