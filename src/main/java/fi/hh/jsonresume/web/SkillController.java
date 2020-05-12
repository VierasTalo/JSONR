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

import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;
import fi.hh.jsonresume.domain.Skill;
import fi.hh.jsonresume.domain.SkillKeyword;
import fi.hh.jsonresume.domain.SkillRepository;

@RestController
public class SkillController {
	
	@Autowired
	private SkillRepository skillRepository;
	
	@Autowired ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/skills", method=RequestMethod.GET)
	public @ResponseBody List<Skill> skillList() {
		return (List<Skill>) skillRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/skills", method=RequestMethod.POST)
	public ResponseEntity<Object> addskill(@RequestBody String requestBody) {
		
		Skill newSkill = new Skill();
		
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
								newSkill.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("name")) {
							newSkill.setSkillName(value);
						} else if (key.equals("level")) {
							newSkill.setSkillLevel(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		List<SkillKeyword> keywords = new ArrayList<>();
		newSkill.setKeywords(keywords);
		
		Skill savedSkill = skillRepository.save(newSkill);
		return new ResponseEntity<>(savedSkill.getSkillId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/skills/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteSkill(@PathVariable("id") Long skillId) {
		if (skillRepository.findById(skillId).isPresent()) {
			skillRepository.deleteById(skillId);
			return new ResponseEntity<>("skill successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/skills/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateSkill(@RequestBody Skill editedSkill, @PathVariable("id") Long skillId) {
		Optional<Skill> skillOptional = skillRepository.findById(skillId);
		if(!skillOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedSkill.setSkillId(skillId);
		skillRepository.save(editedSkill);
		return ResponseEntity.ok().build();
	}
}
