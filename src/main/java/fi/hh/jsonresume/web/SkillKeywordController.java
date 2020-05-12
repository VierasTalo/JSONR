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

import fi.hh.jsonresume.domain.SkillKeyword;
import fi.hh.jsonresume.domain.SkillKeywordRepository;

@RestController
public class SkillKeywordController {

	@Autowired
	private SkillKeywordRepository keywordRepository;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/skillkeywords", method=RequestMethod.GET)
	public @ResponseBody List<SkillKeyword> skillKeywordList() {
		return (List<SkillKeyword>) keywordRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/skillkeywords", method=RequestMethod.POST)
	public ResponseEntity<Object> addSkillKeyword(@RequestBody SkillKeyword newKeyword) {
		SkillKeyword savedKeyword = keywordRepository.save(newKeyword);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedKeyword.getKeywordId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/skillkeyword/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteSkillKeyword(@PathVariable("id") Long keywordId) {
		if (keywordRepository.findById(keywordId).isPresent()) {
			keywordRepository.deleteById(keywordId);
			return new ResponseEntity<>("Keyword successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
