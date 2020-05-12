package fi.hh.jsonresume;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.hh.jsonresume.web.JSONResumeController;
import fi.hh.jsonresume.web.JSONResumeRestController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonresumeApplicationTests {
	
	@Autowired
	private JSONResumeController controller;
	
	@Autowired 
	private JSONResumeRestController restController;

	// Test that Spring application context is loaded and controller is created and injected
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void restContextLoads( ) {
		assertThat(restController).isNotNull();
	}

}
