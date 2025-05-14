package pl.sii.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sii.charity.entity.CollectionBox;

public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {
}
