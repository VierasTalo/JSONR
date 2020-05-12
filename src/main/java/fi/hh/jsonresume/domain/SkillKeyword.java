package fi.hh.jsonresume.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({"id", "keyword"})
public class SkillKeyword {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("id")
	private long keywordId;
	
	@ManyToMany(mappedBy="keywords")
	@JsonIgnore
	private List<Skill> skills;
	
	private String keyword;

	public SkillKeyword() {}


	
	public long getKeywordId() {
		return keywordId;
	}


	public void setKeywordId(long keywordId) {
		this.keywordId = keywordId;
	}


	public List<Skill> getSkills() {
		return skills;
	}


	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
