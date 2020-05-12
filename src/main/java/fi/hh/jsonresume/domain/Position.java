package fi.hh.jsonresume.domain;

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

@Entity
public class Position {
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long positionId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="position_postype_id")
	private PositionType positionType;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="position_resume_id")
	private Resume resume;
	
	@JsonIgnore
	@OneToMany(mappedBy="position", cascade=CascadeType.ALL)
	private List<Highlight> highlights;
	
	@JsonProperty("company")
	private String companyName;
	
	@JsonProperty("position")
	private String positionName;
	
	@JsonProperty("website")
	private String companyWebsite;
	
	@JsonProperty("startDate")
	private String startDate;
	
	@JsonProperty("endDate")
	private String endDate;
	
	@JsonProperty("summary")
	private String summary;
	
	@Transient
	@JsonProperty("highlights")
	private List<String> highlightsAsString() {
		List<String> list = new ArrayList<>();
		for (Highlight h : this.highlights) {
			list.add(h.getSummary());
		}
		return list;
	}

	
	public Position() {}

	
	// returns stylized form of date, eg. "2013-04-23" converts to "04/2013"
	public String startDateShort() {
		return this.startDate.substring(5, 7) + "/" + this.startDate.substring(0, 4);
	}
	
	// returns stylized form of date, eg. "2013-04-23" converts to "04/2013"
	public String endDateShort() {
		return this.endDate.substring(5, 7) + "/" + this.endDate.substring(0, 4);
	}
	
	

	
	public long getPositionId() {
		return positionId;
	}
	
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
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

	@Override
	public String toString() {
		String organizationType = "";
		if(nullChecked(this.positionType.toString()).equals("volunteer")) {
			organizationType = "organization";
		} else {
			organizationType = "work";
		}
		
		String result = "{\n    \"" + organizationType + "\": \"" + companyName + "\"," +
				"\n    \"position\": \"" + nullChecked(positionName) + "\"," +
				"\n    \"website\": \"" + nullChecked(companyWebsite) + "\"," +
				"\n    \"startDate\": \"" + nullChecked(startDate) + "\"," +
				"\n    \"endDate\": \"" + nullChecked(endDate) + "\"," +
				"\n    \"summary\": \"" + wordWrapped(nullChecked(summary), 6) + "\"," +
				"\n    \"highlights\": [";
		
		for (int i=0; i<this.highlights.size();i++) {
			result += this.highlights.get(i);
			if (i < this.highlights.size()-1) {
				result += ", ";
			}
		}
		result += "\n    ]\n  }";
		return result;
	}
	
	// fix null string to ""
	private String nullChecked(String checkForNull) {
		if (checkForNull == null) {
			return "";
		}
		return checkForNull;
	}
	
	private String wordWrapped(String input, int indent) {	// method used to wrap summary text to several lines, since it might otherwise overflow modal popup view
		String output = "";
		int rowChars = 0;
		boolean wrapAtNextBreak = false;
		for (int i = 0; i < input.length(); i++) {
			output += input.charAt(i);
			rowChars++;
			if (rowChars >= 50) {						// check every 50 chars for next space for line break
				wrapAtNextBreak = true;
			}
			if (wrapAtNextBreak && input.substring(i, i+1).equals(" ")) {
				output += "\n";
				for (int j = 0; j < indent; j++) {
					output += " ";
				}
				rowChars = 0;							// reset count after line break
				wrapAtNextBreak = false;
			}
		}
		return output;
	}
	
	private String jsonOrgType() {
		if (this.positionType.getPositionType().equals("work")) {
			return "company";
		} else {
			return "organization";
		}
	}
	
}
