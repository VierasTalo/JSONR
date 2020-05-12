package fi.hh.jsonresume.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.hh.jsonresume.domain.Resume;
import fi.hh.jsonresume.domain.ResumeRepository;

@Controller
public class JSONResumeController {
	
	@Autowired
	private ResumeRepository resumeRepo;
	
	@RequestMapping("*")  // "*" will map all requests to main page
	public String mainPage(Model model) {
		
		// for demo purposes, resumeRepository will only contain one item which is passed to the model using findById. First item default id is 1.
		long testResumeId = 1;
		model.addAttribute("resume", resumeRepo.findById(testResumeId));
		return "main";
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST)
	public String save(Resume resume) {
		if (resume != null) {
			resumeRepo.save(resume);
		}
		return "redirect:/";
	}
	
}
