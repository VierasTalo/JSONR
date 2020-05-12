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

import fi.hh.jsonresume.domain.Award;
import fi.hh.jsonresume.domain.AwardRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class AwardController {

	@Autowired
	private AwardRepository awardRepository;
	
	@Autowired
	private ResumeRepository resumeRepo; 
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/awards", method=RequestMethod.GET)
	public @ResponseBody List<Award> awardList() {
		return (List<Award>) awardRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/awards", method=RequestMethod.POST)
	public ResponseEntity<Object> addAward(@RequestBody String requestBody) {
		
		Award newAward = new Award();
		
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
								newAward.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("title")) {
							newAward.setAwardTitle(value);
						} else if (key.equals("date")) {
							newAward.setAwardDate(value);
						} else if (key.equals("awarder")) {
							newAward.setAwarder(value);
						} else if (key.equals("summary")) {
							newAward.setSummary(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Award savedAward = awardRepository.save(newAward);
		return new ResponseEntity<>(savedAward.getAwardId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/awards/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteAward(@PathVariable("id") Long awardId) {
		if (awardRepository.findById(awardId).isPresent()) {
			awardRepository.deleteById(awardId);
			return new ResponseEntity<>("award successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/awards/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateAward(@RequestBody Award editedAward, @PathVariable("id") Long awardId) {
		Optional<Award> awardOptional = awardRepository.findById(awardId);
		if(!awardOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedAward.setAwardId(awardId);
		awardRepository.save(editedAward);
		return ResponseEntity.ok().build();
	}
}
