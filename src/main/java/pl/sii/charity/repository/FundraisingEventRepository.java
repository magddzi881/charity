package pl.sii.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sii.charity.entity.FundraisingEvent;

public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {
}
