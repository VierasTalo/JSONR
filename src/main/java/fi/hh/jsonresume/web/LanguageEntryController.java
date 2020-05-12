package fi.hh.jsonresume.web;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import fi.hh.jsonresume.domain.Language;
import fi.hh.jsonresume.domain.LanguageEntry;
import fi.hh.jsonresume.domain.LanguageEntryRepository;
import fi.hh.jsonresume.domain.LanguageRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class LanguageEntryController {

	@Autowired
	private LanguageEntryRepository entryRepository;
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	@Autowired
	private LanguageRepository languageRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/languageentries", method=RequestMethod.GET)
	public @ResponseBody List<LanguageEntry> entryList() {
		return (List<LanguageEntry>) entryRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/languageentries", method=RequestMethod.POST)
	public ResponseEntity<Object> addentry(@RequestBody String requestBody) {
		
		LanguageEntry newEntry = new LanguageEntry();
		
		try {
			JsonParser parser = new JsonFactory().createParser(requestBody);
			while(!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (token != JsonToken.START_OBJECT && token != JsonToken.END_OBJECT) {
					parser.nextToken();
					String key = parser.getCurrentName();
					String value = parser.getText();
					if (parser.getCurrentToken() != null) {
						if (key.equals("resumeId")) {
							Optional<Resume> resumeOptional = resumeRepo.findById(Long.parseLong(value));
							if (resumeOptional.isPresent()) {
								Resume resume = resumeOptional.get();
								newEntry.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("language")) {
							Optional<Language> languageOptional = languageRepo.findByLanguage(value);
							if (languageOptional.isPresent()) {
								newEntry.setLanguage(languageOptional.get());
							} else {
								return new ResponseEntity<>("Language not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("fluency")) {
							newEntry.setFluency(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		LanguageEntry savedEntry = entryRepository.save(newEntry);
		return new ResponseEntity<>(savedEntry.getLanguageEntryId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/languageentries/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteEntry(@PathVariable("id") Long entryId) {
		if (entryRepository.findById(entryId).isPresent()) {
			entryRepository.deleteById(entryId);
			return new ResponseEntity<>("entry successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
