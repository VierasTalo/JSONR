package fi.hh.jsonresume.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({"id", "language", "fluency"})
public class LanguageEntry {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long languageEntryId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="entry_resume_id")
	private Resume resume;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="entry_language_id")
	private Language language;
	
	@Transient
	@JsonProperty("language")
	private String langString() {
		return this.language.getLanguage();
	}
	
	private String fluency;
	
	public LanguageEntry() {}
	
	public long getLanguageEntryId() {
		return languageEntryId;
	}
	public void setLanguageEntryId(long languageEntryId) {
		this.languageEntryId = languageEntryId;
	}
	public Resume getResume() {
		return resume;
	}
	public void setResume(Resume resume) {
		this.resume = resume;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getFluency() {
		return fluency;
	}
	public void setFluency(String fluency) {
		this.fluency = fluency;
	}
}
