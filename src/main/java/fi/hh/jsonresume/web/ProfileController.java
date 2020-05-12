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

import fi.hh.jsonresume.domain.OnlineProfile;
import fi.hh.jsonresume.domain.OnlineProfileRepository;
import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RestController
public class ProfileController {

	@Autowired
	private OnlineProfileRepository profileRepository;
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	// ---------- GET ALL ----------
	@RequestMapping(value="/profiles", method=RequestMethod.GET)
	public @ResponseBody List<OnlineProfile> profilesList() {
		return (List<OnlineProfile>) profileRepository.findAll();
	}
	
	// ---------- POST NEW ----------
	@RequestMapping(value="/profiles", method=RequestMethod.POST)
	public ResponseEntity<Object> addProfile(@RequestBody String requestBody) {
		OnlineProfile newProfile = new OnlineProfile();
		
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
								newProfile.setResume(resume);
							} else {
								return new ResponseEntity<>("Resume not found!", HttpStatus.BAD_REQUEST);
							}
						} else if (key.equals("networkName")) {
							newProfile.setNetworkName(value);
						} else if (key.equals("username")) {
							newProfile.setUsername(value);
						} else if (key.equals("url")) {
							newProfile.setUrl(value);
						} else {
							System.out.print("Unknown key: " + key + ": ");
							System.out.print(value);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		OnlineProfile savedProfile = profileRepository.save(newProfile);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedProfile.getProfileId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	// ---------- DELETE ----------
	@RequestMapping(value="/profiles/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteProfile(@PathVariable("id") Long profileId) {
		if (profileRepository.findById(profileId).isPresent()) {
			profileRepository.deleteById(profileId);
			return new ResponseEntity<>("Profile successfully deleted", HttpStatus.NO_CONTENT);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	// ---------- PUT UPDATE ----------
	@RequestMapping(value="/profiles/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateProfile(@RequestBody OnlineProfile editedProfile, @PathVariable("id") Long profileId) {
		Optional<OnlineProfile> profileOptional = profileRepository.findById(profileId);
		if(!profileOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		editedProfile.setProfileId(profileId);
		profileRepository.save(editedProfile);
		return ResponseEntity.ok().build();
	}
}
