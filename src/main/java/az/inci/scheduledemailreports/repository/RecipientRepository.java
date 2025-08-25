package az.inci.scheduledemailreports.repository;

import az.inci.scheduledemailreports.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, String> {
    @Override
    List<Recipient> findAll();
}
