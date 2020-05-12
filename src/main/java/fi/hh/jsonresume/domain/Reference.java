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
@JsonPropertyOrder({"id", "name", "reference"})
public class Reference {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long referenceId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="reference_resume_id")
	private Resume resume;
	
	@JsonProperty("name")
	private String referenceName;
	
	@JsonProperty("reference")
	private String referenceSummary;
	
	public Reference() {}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceSummary() {
		return referenceSummary;
	}

	public void setReferenceSummary(String referenceSummary) {
		this.referenceSummary = referenceSummary;
	}
	
}
