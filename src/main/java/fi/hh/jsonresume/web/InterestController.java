package fi.hh.jsonresume.web;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
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

import fi.hh.jsonresume.domain.Interest;
import fi.hh.jsonresume.domain.InterestKeyword;
import fi.hh.jsonresume.domain.InterestRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class InterestController {

	@Autowired
	private InterestRepository interestRepository;
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/interests", method=RequestMethod.GET)
	public @ResponseBody List<Interest> interestList() {
		return (List<Interest>) interestRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/interests", method=RequestMethod.POST)
	public ResponseEntity<Object> addInterest(@RequestBody String requestBody) {
		
		Interest newInterest = new Interest();
		
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
								newInterest.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("name")) {
							newInterest.setInterestName(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		List<InterestKeyword> keywords = new ArrayList<>();
		newInterest.setKeywords(keywords);
		
		Interest savedInterest = interestRepository.save(newInterest);
		return new ResponseEntity<>(savedInterest.getInterestId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/interests/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteInterest(@PathVariable("id") Long interestId) {
		if (interestRepository.findById(interestId).isPresent()) {
			interestRepository.deleteById(interestId);
			return new ResponseEntity<>("interest successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/interests/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateInterest(@RequestBody Interest editedInterest, @PathVariable("id") Long interestId) {
		Optional<Interest> interestOptional = interestRepository.findById(interestId);
		if(!interestOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedInterest.setInterestId(interestId);
		interestRepository.save(editedInterest);
		return ResponseEntity.ok().build();
	}
	
}
