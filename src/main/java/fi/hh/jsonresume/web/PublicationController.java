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

import fi.hh.jsonresume.domain.Publication;
import fi.hh.jsonresume.domain.PublicationRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class PublicationController {

	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired 
	private ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/publications", method=RequestMethod.GET)
	public @ResponseBody List<Publication> publicationList() {
		return (List<Publication>) publicationRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/publications", method=RequestMethod.POST)
	public ResponseEntity<Object> addpublication(@RequestBody String requestBody) {
		
		Publication newPublication = new Publication();
		
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
								newPublication.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("name")) {
							newPublication.setPublicationName(value);
						} else if (key.equals("publisher")) {
							newPublication.setPublisher(value);
						} else if (key.equals("releaseDate")) {
							newPublication.setReleaseDate(value);
						} else if (key.equals("website")) {
							newPublication.setPublicationWebsite(value);
						} else if (key.equals("summary")) {
							newPublication.setSummary(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Publication savedPublication = publicationRepository.save(newPublication);
		return new ResponseEntity<>(savedPublication.getPublicationId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/publications/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deletePublication(@PathVariable("id") Long publicationId) {
		if (publicationRepository.findById(publicationId).isPresent()) {
			publicationRepository.deleteById(publicationId);
			return new ResponseEntity<>("publication successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/publications/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updatePublication(@RequestBody Publication editedPublication, @PathVariable("id") Long publicationId) {
		Optional<Publication> publicationOptional = publicationRepository.findById(publicationId);
		if(!publicationOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedPublication.setPublicationId(publicationId);
		publicationRepository.save(editedPublication);
		return ResponseEntity.ok().build();
	}
}
