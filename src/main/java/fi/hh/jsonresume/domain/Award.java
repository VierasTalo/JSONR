package fi.hh.jsonresume.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({"id", "title", "date", "awarder", "summary"})
public class Award {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long awardId;
	
	@ManyToOne
	@JoinColumn(name="award_resume_id")
	@JsonIgnore
	private Resume resume;
	
	@JsonProperty("title")
	private String awardTitle;
	
	@JsonProperty("date")
	private String awardDate;
	
	@JsonProperty("awarder")
	private String awarder;
	
	@JsonProperty("summary")
	private String summary;
	
	public Award() {}
	
	
	public long getAwardId() {
		return awardId;
	}
	public void setAwardId(long awardId) {
		this.awardId = awardId;
	}
	public Resume getResume() {
		return resume;
	}
	public void setResume(Resume resume) {
		this.resume = resume;
	}
	public String getAwardTitle() {
		return awardTitle;
	}
	public void setAwardTitle(String awardTitle) {
		this.awardTitle = awardTitle;
	}
	public String getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}
	public String getAwarder() {
		return awarder;
	}
	public void setAwarder(String awarder) {
		this.awarder = awarder;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

}
