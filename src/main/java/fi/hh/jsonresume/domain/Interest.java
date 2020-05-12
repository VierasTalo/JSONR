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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({"id", "name", "keywords"})
public class Interest {
	
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long interestId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="interest_resume_id")
	private Resume resume;
	
	@ManyToMany
	@JoinTable(
			name = "InterestKeywordsJoin",
			joinColumns = @JoinColumn(name = "interest_join"),
			inverseJoinColumns = @JoinColumn(name = "keyword_join"))
	private List<InterestKeyword> keywords;
	
	@JsonProperty("name")
	private String interestName;
	
	@JsonProperty("keywords")
	private List<String> keywordsAsString() {
		List<String> list = new ArrayList<>();
		for (InterestKeyword word : this.keywords) {
			list.add(word.getKeyword());
		}
		return list;
	}
	
	public Interest() {}

	
	public long getInterestId() {
		return interestId;
	}

	public void setInterestId(long interestId) {
		this.interestId = interestId;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public List<InterestKeyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<InterestKeyword> keywords) {
		this.keywords = keywords;
	}

	public String getInterestName() {
		return interestName;
	}

	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}

}
