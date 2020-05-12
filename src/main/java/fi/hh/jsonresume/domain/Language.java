package fi.hh.jsonresume.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Language {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long languageId;
	
	@OneToMany(mappedBy="language", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<LanguageEntry> languageEntries;
	
	private String language;
	
	public Language() {}
	
	public long getLanguageId() {
		return languageId;
	}
	public void setLanguageId(long languageId) {
		this.languageId = languageId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
