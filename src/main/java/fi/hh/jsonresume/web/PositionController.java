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

import fi.hh.jsonresume.domain.Highlight;
import fi.hh.jsonresume.domain.Position;
import fi.hh.jsonresume.domain.PositionRepository;
import fi.hh.jsonresume.domain.PositionType;
import fi.hh.jsonresume.domain.PositionTypeRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class PositionController {

	@Autowired 
	private PositionRepository positionRepository;
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	@Autowired
	private PositionTypeRepository positionTypeRepo;
	
	// --------------- GET ALL POSITIONS ---------------
	@RequestMapping(value="/positions", method=RequestMethod.GET)
	public @ResponseBody List<Position> positionListRest() {
		return (List<Position>) positionRepository.findAll();
	}
	
	// --------------- POST NEW POSITION ---------------
	@RequestMapping(value="/positions", method=RequestMethod.POST)
	public ResponseEntity<Object> addPositionRest(@RequestBody String requestBody) {
		
		Position newPosition = new Position();
		
		System.out.println("REQUEST PAYLOAD: " + requestBody);
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
								newPosition.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("positionType")) {
							Optional<PositionType> posTypeOptional = positionTypeRepo.findByPositionType(value);
							if (posTypeOptional.isPresent()) {
								PositionType posType = posTypeOptional.get();
								newPosition.setPositionType(posType);
							} else {
								return new ResponseEntity<>("Position type not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("company") || key.equals("organization")) {
							newPosition.setCompanyName(value);
						} else if (key.equals("position")) {
							newPosition.setPositionName(value);
						} else if (key.equals("website")) {
							newPosition.setCompanyWebsite(value);
						} else if (key.equals("startDate")) {
							newPosition.setStartDate(value);
						} else if (key.equals("endDate")) {
							newPosition.setEndDate(value);
						} else if (key.equals("summary")) {
							newPosition.setSummary(value);
						}
					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		List<Highlight> highlights = new ArrayList<>();
		newPosition.setHighlights(highlights);
		
		Position savedPosition = positionRepository.save(newPosition);
		return new ResponseEntity<>(savedPosition.getPositionId(), HttpStatus.CREATED);
	}
	
	// --------------- GET POSITION BY ID ---------------
	@RequestMapping(value="/positions/{id}", method=RequestMethod.GET)
	public @ResponseBody Optional<Position> findPositionRest(@PathVariable("id") Long positionId) {
		return positionRepository.findById(positionId);
	}
	
	// --------------- DELETE POSITION ---------------
	@RequestMapping(value="/positions/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deletePositionRest(@PathVariable("id") Long positionId) {
		if (positionRepository.findById(positionId).isPresent()) {
			positionRepository.deleteById(positionId);
			return new ResponseEntity<>("Position successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// --------------- PUT: UPDATE POSITION ---------------
	@RequestMapping(value="/positions/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updatePositionRest(@RequestBody Position editedPosition, @PathVariable("id") Long positionId) {
		Optional<Position> positionOptional = positionRepository.findById(positionId);
		if(!positionOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedPosition.setPositionId(positionId);
		positionRepository.save(editedPosition);
		return ResponseEntity.ok().build();
	}
}
