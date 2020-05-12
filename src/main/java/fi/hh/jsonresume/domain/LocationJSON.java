package fi.hh.jsonresume.domain;

public class LocationJSON {
	
	private String address;
	private String postalCode;
	private String city;
	private String countryCode;
	private String region;
	
	public LocationJSON() {}
	
	public LocationJSON(String adr, String pCode, String city, String cCode, String reg) {
		this.address = adr;
		this.postalCode = pCode;
		this.city = city;
		this.countryCode = cCode;
		this.region = reg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
