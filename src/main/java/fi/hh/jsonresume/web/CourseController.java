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

import fi.hh.jsonresume.domain.Course;
import fi.hh.jsonresume.domain.CourseRepository;
import fi.hh.jsonresume.domain.Education;
import fi.hh.jsonresume.domain.EducationRepository;
import fi.hh.jsonresume.domain.Highlight;

@RestController
public class CourseController {

	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private EducationRepository educationRepo;
	
	// -------------- POST NEW COURSE ------------
	@RequestMapping(value="/courses", method=RequestMethod.POST)
	public ResponseEntity<Object> addCourse(@RequestBody String requestBody) {
		
		Course newCourse = new Course();
		
		try {
			JsonParser parser = new JsonFactory().createParser(requestBody);
			while(!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (token != JsonToken.START_OBJECT && token != JsonToken.END_OBJECT) {
					parser.nextToken();
					String key = parser.getCurrentName();
					String value = parser.getText();
					if (parser.getCurrentToken() != null) {
						if (key.equals("educationId")) {
							Optional<Education> educationOptional = educationRepo.findById(Long.parseLong(value));
							if (educationOptional.isPresent()) {
								Education education = educationOptional.get();
								newCourse.setEducation(education);
							} else {
								return new ResponseEntity<>("Education not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("course")) {
							newCourse.setCourseName(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Course savedCourse = courseRepo.save(newCourse);
		return new ResponseEntity<>(savedCourse.getCourseId(), HttpStatus.CREATED);	
	}
	
	// ----------- DELETE COURSE ---------------
	@RequestMapping(value="/courses/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteCourse(@PathVariable("id") Long courseId) {
		Optional<Course> courseOptional = courseRepo.findById(courseId);
		if (courseOptional.isPresent()) {
			courseRepo.deleteById(courseId);
			return new ResponseEntity<>("Course successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
