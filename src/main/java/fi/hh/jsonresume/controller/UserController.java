package fi.hh.jsonresume.controller;

import fi.hh.jsonresume.exception.ResourceNotFoundException;
import fi.hh.jsonresume.domain.User;
import fi.hh.jsonresume.domain.UserRepository;
import fi.hh.jsonresume.security.CurrentUser;
import fi.hh.jsonresume.security.UserPrincipal;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    //Used to retrieve CurrentUser's repo-details
    @GetMapping("/user/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
    
 // ---------- GET ALL ----------
 	@RequestMapping(value="/users", method=RequestMethod.GET)
 	public @ResponseBody List<User> userList() {
 		return (List<User>) userRepository.findAll();
 	}
 	
 	// ---------- POST NEW ----------
 	@RequestMapping(value="/users", method=RequestMethod.POST)
 	public ResponseEntity<Object> addUser(@RequestBody User newUser) {
 		User savedUser = userRepository.save(newUser);
 		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
 				.buildAndExpand(savedUser.getId()).toUri();
 		return ResponseEntity.created(location).build();
 	}
 	
 	// ---------- DELETE ----------
 	@RequestMapping(value="/user/{id}", method=RequestMethod.DELETE)
 	public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
 		if (userRepository.findById(userId).isPresent()) {
 			userRepository.deleteById(userId);
 			return new ResponseEntity<>("user successfully deleted", HttpStatus.NO_CONTENT);
 		} else {
 			return ResponseEntity.notFound().build();
 		}
 	}
 	
 	// ---------- PUT UPDATE ----------
 	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
 	public ResponseEntity<Object> updateUser(@RequestBody User editedUser, @PathVariable("id") Long userId) {
 		Optional<User> userOptional = userRepository.findById(userId);
 		if(!userOptional.isPresent()) {
 			return ResponseEntity.notFound().build();
 		}
 		editedUser.setId(userId);
 		userRepository.save(editedUser);
 		return ResponseEntity.ok().build();
 	}
}