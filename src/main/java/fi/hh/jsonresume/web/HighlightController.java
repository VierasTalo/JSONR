package fi.hh.jsonresume.web;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import fi.hh.jsonresume.domain.Highlight;
import fi.hh.jsonresume.domain.HighlightRepository;
import fi.hh.jsonresume.domain.Position;
import fi.hh.jsonresume.domain.PositionRepository;

@RestController
public class HighlightController {

	@Autowired 
	private HighlightRepository highlightRepo;
	
	@Autowired
	private PositionRepository positionRepo;
	
	// ----------- POST NEW HIGHLIGHT ----------
	@RequestMapping(value="/highlights", method=RequestMethod.POST)
	public ResponseEntity<Object> addHighlight(@RequestBody String requestBody) {
		Highlight newHighlight = new Highlight();
		
		try {
			JsonParser parser = new JsonFactory().createParser(requestBody);
			while(!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (token != JsonToken.START_OBJECT && token != JsonToken.END_OBJECT) {
					parser.nextToken();
					String key = parser.getCurrentName();
					String value = parser.getText();
					if (parser.getCurrentToken() != null) {
						if (key.equals("positionId")) {
							Optional<Position> positionOptional = positionRepo.findById(Long.parseLong(value));
							if (positionOptional.isPresent()) {
								Position position = positionOptional.get();
								newHighlight.setPosition(position);
							} else {
								return new ResponseEntity<>("Position not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("summary")) {
							newHighlight.setSummary(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Highlight savedHighlight = highlightRepo.save(newHighlight);
		return new ResponseEntity<>(savedHighlight.getHighlightId(), HttpStatus.CREATED);
	}
	
	// ----------- DELETE HIGHLIGHT ---------------
	@RequestMapping(value="/highlights/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteHighlight(@PathVariable("id") Long highlightId) {
		Optional<Highlight> highlightOptional = highlightRepo.findById(highlightId);
		if (highlightOptional.isPresent()) {
			highlightRepo.deleteById(highlightId);
			return new ResponseEntity<>("Highlight successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
}
