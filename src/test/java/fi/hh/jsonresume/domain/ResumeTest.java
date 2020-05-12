package fi.hh.jsonresume.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import fi.hh.jsonresume.domain.Resume;

@RunWith(SpringRunner.class)
public class ResumeTest {

	private Resume resume;
	
	@Before
	public void setUp() {
		resume = new Resume();
	}
	
	@Test
	public void personName() {
		resume.setPersonName("Tero Testaaja");
		assertEquals("Tero Testaaja", resume.getPersonName());
	}
	
	
	@Test
	public void personLabel() {
		resume.setPersonLabel("Testaaja");
		assertEquals("Testaaja", resume.getPersonLabel());
	}
	
	@Test
	public void email() {
		resume.setEmail("Sähköposti@jokudomain.fi");
		assertEquals("Sähköposti@jokudomain.fi", resume.getEmail());
	}
	
	@Test
	public void phone() {
		resume.setPhone("(+358)44-123456");
		assertEquals("(+358)44-123456", resume.getPhone());
	}
	
	@Test public void website() {
		resume.setWebsite("https://www.nettisivu.fi/");
		assertEquals("https://www.nettisivu.fi/", resume.getWebsite());
	}
	
	@Test public void summary() {
		resume.setSummary("Testisummary eli joku lause tänne. Tai ehkä parikin.");
		assertEquals("Testisummary eli joku lause tänne. Tai ehkä parikin.", resume.getSummary());
	}
	
	@Test
	public void address() {
		resume.setAddress("Testitie 1 A1");
		assertEquals("Testitie 1 A1", resume.getAddress());
	}
	
	@Test
	public void postalCode() {
		resume.setPostalCode("00550");
		assertEquals("00550", resume.getPostalCode());
	}
	
	@Test
	public void city() {
		resume.setCity("Helsinki");
		assertEquals("Helsinki", resume.getCity());
	}
	
	@Test 
	public void countryCode() {
		resume.setCountryCode("FI");
		assertEquals("FI", resume.getCountryCode());
	}
	
	@Test 
	public void region() {
		resume.setRegion("Uusimaa");
		assertEquals("Uusimaa", resume.getRegion());
	}
}
