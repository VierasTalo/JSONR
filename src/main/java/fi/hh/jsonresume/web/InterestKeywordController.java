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

import fi.hh.jsonresume.domain.InterestKeyword;
import fi.hh.jsonresume.domain.InterestKeywordRepository;

@RestController
public class InterestKeywordController {

	@Autowired
	private InterestKeywordRepository keywordRepository;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/interestkeywords", method=RequestMethod.GET)
	public @ResponseBody List<InterestKeyword> interestKeywordList() {
		return (List<InterestKeyword>) keywordRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/interestkeywords", method=RequestMethod.POST)
	public ResponseEntity<Object> addInterestKeyword(@RequestBody InterestKeyword newKeyword) {
		InterestKeyword savedKeyword = keywordRepository.save(newKeyword);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedKeyword.getKeywordId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/interestkeyword/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteInterestKeyword(@PathVariable("id") Long keywordId) {
		if (keywordRepository.findById(keywordId).isPresent()) {
			keywordRepository.deleteById(keywordId);
			return new ResponseEntity<>("Keyword successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
