package fi.hh.jsonresume.domain;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicsJSON {
	
	private String name;
	private String label;
	private String picture;
	private String email;
	private String phone;
	private String website;
	private String summary;
	private Object location;
	private List<OnlineProfile> profiles;
	
	public BasicsJSON() {}
	
	public BasicsJSON(Resume resume) {
		this.name = resume.getPersonName();
		this.label = resume.getPersonLabel();
		this.picture = resume.getPersonPicture();
		this.email = resume.getEmail();
		this.phone = resume.getPhone();
		this.website = resume.getWebsite();
		this.summary = resume.getSummary();
		String adr = resume.getAddress();
		String pCode = resume.getPostalCode();
		String city = resume.getCity();
		String cCode = resume.getCountryCode();
		String reg = resume.getRegion();
		this.setLocation(adr, pCode, city, cCode, reg);
		this.profiles = resume.getProfiles();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

	public Object getLocation() {
		return location;
	}

	public void setLocation(String adr, String pCode, String city, String cCode, String reg) {
		String objString = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objString = objectMapper.writeValueAsString(new LocationJSON(adr, pCode, city, cCode, reg));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		try {
			this.location = objectMapper.readValue(objString, LocationJSON.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<OnlineProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<OnlineProfile> profiles) {
		this.profiles = profiles;
	}
	
}
