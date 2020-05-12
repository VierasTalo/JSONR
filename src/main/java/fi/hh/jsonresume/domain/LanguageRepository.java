package fi.hh.jsonresume.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface LanguageRepository extends CrudRepository<Language, Long>{
	
	Optional<Language> findByLanguage(String language);

}
