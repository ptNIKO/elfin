package ru.elfin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.elfin.models.ClientAssessment;

public interface ClientAssessmentRepository extends JpaRepository<ClientAssessment, Long> {
}
