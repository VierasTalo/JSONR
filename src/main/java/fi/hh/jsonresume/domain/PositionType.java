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
public class PositionType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long positionTypeId;					// original database diagram did not have separate id row for PositionType table, but this is easier than using Strings as primary key
	
	@OneToMany(mappedBy="positionType", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Position> positions;
	
	private String positionType;
	
	public PositionType() {}

	
	
	
	
	
	public long getPositionTypeId() {
		return positionTypeId;
	}
	
	public void setPositionTypeId(Long positionTypeId) {
		this.positionTypeId = positionTypeId;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	
	
	@Override
	public String toString() {
		return this.positionType;
	}

}
