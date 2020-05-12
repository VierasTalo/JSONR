package fi.hh.jsonresume.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@RunWith(SpringRunner.class)
@DataJpaTest						// @DataJpaTest annotation used when tests focus only on JPA components
public class ResumeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;	// handles the persist entities for testing
	
	@Autowired
	private ResumeRepository repository;
	
	// Test that ResumeId cannot be null when successfully saved to repository
	@Test
	public void saveResume() {
		Resume resume = new Resume();
		entityManager.persistAndFlush(resume);
		assertThat(resume.getResumeId()).isNotNull();
	}
	
	// Test that entities are correctly deleted from database
	@Test
	public void deleteResumes() {
		for (int i=0; i < 5; i++) {
			entityManager.persistAndFlush(new Resume());
		}
		repository.deleteAll();
		assertThat(repository.findAll()).isEmpty();
	}
	
}
