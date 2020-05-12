package fi.hh.jsonresume.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkPositionJSON {
	
	private Long id;
	private String company;
	private String position;
	private String website;
	private String startDate;
	private String endDate;
	private String summary;
	private List<Highlight> highlights;
	
	public WorkPositionJSON() {}
	
	public WorkPositionJSON(Position pos) {
		this.id = pos.getPositionId();
		this.company = pos.getCompanyName();
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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
