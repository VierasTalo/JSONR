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
@JsonPropertyOrder({"id", "name", "publisher", "releaseDate", "website", "summary"})
public class Publication {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long publicationId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="publication_resume_id")
	private Resume resume;
	
	@JsonProperty("name")
	private String publicationName;
	
	@JsonProperty("publisher")
	private String publisher;
	
	@JsonProperty("releaseDate")
	private String releaseDate;
	
	@JsonProperty("website")
	private String publicationWebsite;
	
	@JsonProperty("summary")
	private String summary;
	
	public Publication() {}

	
	
	public long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(long publicationId) {
		this.publicationId = publicationId;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getPublicationName() {
		return publicationName;
	}

	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPublicationWebsite() {
		return publicationWebsite;
	}

	public void setPublicationWebsite(String publicationWebsite) {
		this.publicationWebsite = publicationWebsite;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
