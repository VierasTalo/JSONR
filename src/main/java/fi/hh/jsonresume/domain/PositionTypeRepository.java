package fi.hh.jsonresume.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface PositionTypeRepository extends CrudRepository<PositionType, Long>{
	
	public Optional<PositionType> findByPositionType(String positionType);

}
