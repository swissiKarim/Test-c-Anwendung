package com.backend.devp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.backend.devp.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class BackendTestCApplication {

//	@Autowired
//	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(BackendTestCApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		User userAdmin = new User();
//		User userStudent1 = new User();
//		User userStudent2 = new User();
//		User userStudent3 = new User();
//		//save Admin User
//		userAdmin.setName("Admin");
//		userAdmin.setUsername("Admin");
//		userAdmin.setPassword("Admin");
//		userAdmin.setRole(Role.ADMIN);	
//		userService.save(userAdmin);
//		//save Student1 User
//		userStudent1.setName("Student1");
//		userStudent1.setUsername("Student1");
//		userStudent1.setPassword("Student1");
//		userStudent1.setRole(Role.USER);	
//		userService.save(userStudent1);
//		//save Student2 User
//		userStudent2.setName("Student2");
//		userStudent2.setUsername("Student2");
//		userStudent2.setPassword("Student2");
//		userStudent2.setRole(Role.USER);	
//		userService.save(userStudent2);
//		//save Student3 User
//		userStudent3.setName("Student3");
//		userStudent3.setUsername("Student3");
//		userStudent3.setPassword("Student3");
//		userStudent3.setRole(Role.USER);	
//		userService.save(userStudent3);
		
		
//	}

	
}
