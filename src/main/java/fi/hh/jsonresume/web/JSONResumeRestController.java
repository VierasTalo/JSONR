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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;
import fi.hh.jsonresume.domain.User;
import fi.hh.jsonresume.domain.UserRepository;

@RestController
public class JSONResumeRestController {

	@Autowired
	private ResumeRepository resumeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	// --------------- GET ALL RESUMES FOR GIVEN USER ---------------
	
	@RequestMapping(value="/users/{userId}/resumes", method=RequestMethod.GET)
	public @ResponseBody List<Resume> getUserResumes(@PathVariable("userId") Long userId) {
		List<Resume> response = new ArrayList<>();
		for (Resume resume : resumeRepo.findAll()) {
			Long requestedId = resume.getUser().getId();
			if (requestedId != null && requestedId == userId) {
				resumeRepo.save(resume);
				response.add(resume);
			}
		}
		return response;
	}
	
	// --------------- GET ALL RESUMES ---------------
	@RequestMapping(value="/resumes", method=RequestMethod.GET)
	public @ResponseBody List<Resume> resumeListRest() {
		return (List<Resume>) resumeRepo.findAll();
	}
	
	// --------------- POST NEW RESUME ---------------
	@RequestMapping(value="/resumes", method=RequestMethod.POST)
	public ResponseEntity<Object> addResumeRest(@RequestBody String requestBody) {
		Resume newResume = new Resume();
		
		try {
			JsonParser parser = new JsonFactory().createParser(requestBody);
			while(!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (token != JsonToken.START_OBJECT && token != JsonToken.END_OBJECT) {
					parser.nextToken();
					String key = parser.getCurrentName();
					String value = parser.getText();
					if (parser.getCurrentToken() != null) {
						if (key.equals("resume_user_id")) {
							Optional<User> userOptional = userRepo.findById(Long.parseLong(value));
							if (userOptional.isPresent()) {
								User user = userOptional.get();
								newResume.setUser(user);
							} else {
								return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
							}
						} else if (key.equals("name")) {
							newResume.setPersonName(value);
						} else if (key.equals("label")) {
							newResume.setPersonLabel(value);
						} else if (key.equals("picture")) {
							newResume.setPersonPicture(value);
						} else if (key.equals("email")) {
							newResume.setEmail(value);
						} else if (key.equals("phone")) {
							newResume.setPhone(value);
						} else if (key.equals("website")) {
							newResume.setWebsite(value);
						} else if (key.equals("summary")) {
							newResume.setSummary(value);
						} else if (key.equals("address")) {
							newResume.setAddress(value);
						} else if (key.equals("postalCode")) {
							newResume.setPostalCode(value);
						} else if (key.equals("city")) {
							newResume.setCity(value);
						} else if (key.equals("countryCode")) {
							newResume.setCountryCode(value);
						} else if (key.equals("region")) {
							newResume.setRegion(value);
						} else {
							System.out.print("Unknown key: " + key + ": ");
							System.out.println(value);
						}	
					}
				}	
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>("Error parsing request!", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Resume savedResume = resumeRepo.save(newResume);
		return new ResponseEntity<>(savedResume.getResumeId(), HttpStatus.CREATED);
	}
	
	// --------------- GET RESUME BY ID ---------------
	@RequestMapping(value="/resumes/{id}", method=RequestMethod.GET)
	public @ResponseBody Optional<Resume> findResumeRest(@PathVariable("id") Long resumeId) {
		return resumeRepo.findById(resumeId);
	}
	
	// --------------- DELETE RESUME ---------------
	@RequestMapping(value="/resumes/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteResumeRest(@PathVariable("id") Long resumeId) {
		if (resumeRepo.findById(resumeId).isPresent()) {
			resumeRepo.deleteById(resumeId);
			return new ResponseEntity<>("Resume successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// --------------- PUT: UPDATE RESUME ---------------
	@RequestMapping(value="/resumes/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateResumeRest(@RequestBody String requestBody, @PathVariable("id") Long resumeId) {
		Optional<Resume> resumeOptional = resumeRepo.findById(resumeId);
		if(!resumeOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Resume editedResume = resumeOptional.get();
		System.out.println(requestBody);
		
		try {
			JsonParser parser = new JsonFactory().createParser(requestBody);
			while(!parser.isClosed()) {
				JsonToken token = parser.nextToken();
				if (token != JsonToken.START_OBJECT && token != JsonToken.END_OBJECT) {
					parser.nextToken();
					String key = parser.getCurrentName();
					String value = parser.getText();
					if (parser.getCurrentToken() != null) {
						if (key.equals("profiles")) {
							break;
						} else if (key.equals("name")) {
							editedResume.setPersonName(value);
						} else if (key.equals("label")) {
							editedResume.setPersonLabel(value);
						} else if (key.equals("picture")) {
							editedResume.setPersonPicture(value);
						} else if (key.equals("email")) {
							editedResume.setEmail(value);
						} else if (key.equals("phone")) {
							editedResume.setPhone(value);
						} else if (key.equals("website")) {
							editedResume.setWebsite(value);
						} else if (key.equals("summary")) {
							editedResume.setSummary(value);
						} else if (key.equals("address")) {
							editedResume.setAddress(value);
						} else if (key.equals("postalCode")) {
							editedResume.setPostalCode(value);
						} else if (key.equals("city")) {
							editedResume.setCity(value);
						} else if (key.equals("countryCode")) {
							editedResume.setCountryCode(value);
						} else if (key.equals("region")) {
							editedResume.setRegion(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		resumeRepo.save(editedResume);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
}
