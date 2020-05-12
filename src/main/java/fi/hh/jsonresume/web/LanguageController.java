package fi.hh.jsonresume.web;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fi.hh.jsonresume.domain.Language;
import fi.hh.jsonresume.domain.LanguageRepository;

@RestController
public class LanguageController {

	@Autowired
	private LanguageRepository languageRepository;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/languages", method=RequestMethod.GET)
	public @ResponseBody List<Language> languageList() {
		return (List<Language>) languageRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/languages", method=RequestMethod.POST)
	public ResponseEntity<Object> addLanguage(@RequestBody Language newLanguage) {
		Language savedLanguage = languageRepository.save(newLanguage);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedLanguage.getLanguageId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/languages/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteLanguage(@PathVariable("id") Long languageId) {
		if (languageRepository.findById(languageId).isPresent()) {
			languageRepository.deleteById(languageId);
			return new ResponseEntity<>("language successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}