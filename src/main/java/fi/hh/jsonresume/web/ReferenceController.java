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

import fi.hh.jsonresume.domain.Reference;
import fi.hh.jsonresume.domain.ReferenceRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class ReferenceController {

	@Autowired
	private ReferenceRepository referenceRepository;
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/references", method=RequestMethod.GET)
	public @ResponseBody List<Reference> referenceList() {
		return (List<Reference>) referenceRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/references", method=RequestMethod.POST)
	public ResponseEntity<Object> addReference(@RequestBody String requestBody) {
		
		Reference newReference = new Reference();
		
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
								newReference.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("name")) {
							newReference.setReferenceName(value);
						} else if (key.equals("reference")) {
							newReference.setReferenceSummary(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Reference savedReference = referenceRepository.save(newReference);
		return new ResponseEntity<>(savedReference.getReferenceId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/references/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteReference(@PathVariable("id") Long referenceId) {
		if (referenceRepository.findById(referenceId).isPresent()) {
			referenceRepository.deleteById(referenceId);
			return new ResponseEntity<>("reference successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/references/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateReference(@RequestBody Reference editedReference, @PathVariable("id") Long referenceId) {
		Optional<Reference> referenceOptional = referenceRepository.findById(referenceId);
		if(!referenceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedReference.setReferenceId(referenceId);
		referenceRepository.save(editedReference);
		return ResponseEntity.ok().build();
	}
}
