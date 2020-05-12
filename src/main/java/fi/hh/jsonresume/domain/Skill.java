package fi.hh.jsonresume.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({"id", "name", "level", "keywords"})
public class Skill {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long skillId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="skill_resume_id")
	private Resume resume;
	
	@ManyToMany
	@JoinTable(
			name = "SkillKeywordsJoin",
			joinColumns = @JoinColumn(name = "skillId"),
			inverseJoinColumns = @JoinColumn(name = "keywordId"))
	private List<SkillKeyword> keywords;
	
	@JsonProperty("name")
	private String skillName;
	
	@JsonProperty("level")
	private String skillLevel;
	
	@Transient
	@JsonProperty("keywords")
	private List<String> keywordsAsString() {
		List<String> list = new ArrayList<>();
		for (SkillKeyword word : this.keywords) {
			list.add(word.getKeyword());
		}
		return list;
	}
	
	public Skill() {}

	
	public long getSkillId() {
		return skillId;
	}

	public void setSkillId(long skillId) {
		this.skillId = skillId;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}


	public List<SkillKeyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<SkillKeyword> keywords) {
		this.keywords = keywords;
	}
}
