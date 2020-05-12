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

import fi.hh.jsonresume.domain.Course;
import fi.hh.jsonresume.domain.Education;
import fi.hh.jsonresume.domain.EducationRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class EducationController {

	@Autowired
	private EducationRepository educationRepository;
	
	@Autowired 
	private ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/education", method=RequestMethod.GET)
	public @ResponseBody List<Education> educationList() {
		return (List<Education>) educationRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/education", method=RequestMethod.POST)
	public ResponseEntity<Object> addEducation(@RequestBody String requestBody) {
		
		Education newEducation = new Education();
		
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
								newEducation.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("institution")) {
							newEducation.setInstitution(value);
						} else if (key.equals("area")) {
							newEducation.setArea(value);
						} else if (key.equals("studyType")) {
							newEducation.setStudyType(value);
						} else if (key.equals("startDate")) {
							newEducation.setStartDate(value);
						} else if (key.equals("endDate")) {
							newEducation.setEndDate(value);
						} else if (key.equals("gpa")) {
							newEducation.setGpa(Double.parseDouble(value));
						}
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		List<Course> courses = new ArrayList<>();
		newEducation.setCourses(courses);
		
		Education savedEducation = educationRepository.save(newEducation);
		return new ResponseEntity<>(savedEducation.getEducationId(), HttpStatus.CREATED);
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/education/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteEducation(@PathVariable("id") Long educationId) {
		if (educationRepository.findById(educationId).isPresent()) {
			educationRepository.deleteById(educationId);
			return new ResponseEntity<>("Education successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/education/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateEducation(@RequestBody Education editedEducation, @PathVariable("id") Long educationId) {
		Optional<Education> educationOptional = educationRepository.findById(educationId);
		if(!educationOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedEducation.setEducationId(educationId);
		educationRepository.save(editedEducation);
		return ResponseEntity.ok().build();
	}
}
