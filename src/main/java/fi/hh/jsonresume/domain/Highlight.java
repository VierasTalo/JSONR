package fi.hh.jsonresume.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Highlight {

	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long highlightId;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="highlight_position_id")
	private Position position;
	
	private String summary;
	
	
	public Highlight() {}


	public void setHighlightId(long highlightId) {
		this.highlightId = highlightId;
	}

	public long getHighlightId() {
		return highlightId;
	}
	
	public void setHighlightId(Long highlightId) {
		this.highlightId = highlightId;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	
	
	@Override
	public String toString() {
		return "\n      \"" + summary + "\"";
	}
}
