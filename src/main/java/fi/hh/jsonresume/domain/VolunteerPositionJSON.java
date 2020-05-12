package fi.hh.jsonresume.domain;

import java.util.ArrayList;
import java.util.List;

public class VolunteerPositionJSON {
	
	private Long id;
	private String organization;
	private String position;
	private String website;
	private String startDate;
	private String endDate;
	private String summary;
	private List<Highlight> highlights;
	
	public VolunteerPositionJSON() {}
	
	public VolunteerPositionJSON(Position pos) {
		this.id = pos.getPositionId();
		this.organization = pos.getCompanyName();
		this.position = pos.getPositionName();
		this.website = pos.getCompanyWebsite();
		this.startDate = pos.getStartDate();
		this.endDate = pos.getEndDate();
		this.summary = pos.getSummary();
		this.highlights = pos.getHighlights();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Highlight> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<Highlight> highlights) {
		this.highlights = highlights;
	}

	
}
