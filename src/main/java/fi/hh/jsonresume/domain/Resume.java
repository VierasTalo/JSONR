package fi.hh.jsonresume.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@JsonPropertyOrder({
	"resumeId",
	"basics", 
	"work", 
	"volunteer", 
	"education", 
	"awards", 
	"publications", 
	"skills", 
	"languages", 
	"interests", 
	"references"})
public class Resume {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long resumeId;
	
	@ManyToOne
	@JoinColumn(name="resume_user_id")
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Position> positions;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<OnlineProfile> profiles;
	
	
	@JsonIgnore
	private String personName;
	
	@JsonIgnore
	private String personLabel;
	
	@JsonIgnore
	private String email;
	
	@JsonIgnore
	private String phone;
	
	@JsonIgnore
	private String website;
	
	@JsonIgnore
	private String summary;
	
	@JsonIgnore
	private String address;
	
	@JsonIgnore
	private String postalCode;
	
	@JsonIgnore
	private String city;
	
	@JsonIgnore
	private String countryCode;
	
	@JsonIgnore
	private String region;
	
	@JsonIgnore
	private String personPicture;
	
	
	// ------------------------- JSON RESPONSE STARTS HERE (following JSON Resume Schema) -------------------------
	@Transient
	@JsonProperty("basics")
	private Object basics() {
		String objString = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objString = objectMapper.writeValueAsString(new BasicsJSON(this));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		try {
			return objectMapper.readValue(objString, BasicsJSON.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Transient
	@JsonProperty("work")
	private List<WorkPositionJSON> work() {
		List<WorkPositionJSON> list = new ArrayList<>();
		for (Position pos : this.positions) {
			if (pos.getPositionType().getPositionType().equals("work")) {
				list.add(new WorkPositionJSON(pos));
			}
		}
		return list;
	}
	
	@Transient
	@JsonProperty("volunteer")
	private List<VolunteerPositionJSON> volunteer() {
		List<VolunteerPositionJSON> list = new ArrayList<>();
		for (Position pos : this.positions) {
			if (pos.getPositionType().getPositionType().equals("volunteer")) {
				list.add(new VolunteerPositionJSON(pos));
			}
		}
		return list;
	}
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Education> education;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Award> awards;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Publication> publications;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Skill> skills;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<LanguageEntry> languages;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Interest> interests;
	
	@OneToMany(mappedBy="resume", cascade=CascadeType.ALL)
	private List<Reference> references;
	
	// ------------------------- JSON RESPONSE ENDS HERE (following JSON Resume Schema) -------------------------
	
	
	
	public Resume() {						// Default constructor required by ORM/JPA
		this.positions = new ArrayList<>();
		this.personName = "";
		this.personLabel = "";
		this.email = "";
		this.phone = "";
		this.website = "";
		this.summary = "";
		this.address = "";
		this.postalCode = "";
		this.city = "";
		this.countryCode = "";
		this.region = "";
		this.personPicture = "";
	}
	
	
	
	//----------------------------------------------------------------
	// ---------------- getters and setters below
	//----------------------------------------------------------------
	
	
	public long getResumeId() {
		return resumeId;
	}
	
	public void setResumeId(Long resumeId) {
		this.resumeId = resumeId;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonLabel() {
		return personLabel;
	}
	public void setPersonLabel(String personLabel) {
		this.personLabel = personLabel;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public List<OnlineProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<OnlineProfile> profiles) {
		this.profiles = profiles;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public String getPersonPicture() {
		return personPicture;
	}

	public void setPersonPicture(String personPicture) {
		this.personPicture = personPicture;
	}

	public void setResumeId(long resumeId) {
		this.resumeId = resumeId;
	}

	public List<Education> getEducation() {
		return education;
	}

	public void setEducation(List<Education> education) {
		this.education = education;
	}

	public List<LanguageEntry> getLanguages() {
		return languages;
	}

	public void setLanguages(List<LanguageEntry> languages) {
		this.languages = languages;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

}
