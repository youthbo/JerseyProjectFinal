package se.plushogskolan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JerseyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JerseyProjectApplication.class, args);
	}
	
//	@Bean
//    public CommandLineRunner commandLineRunner(IssueService service) {
//        return args -> {
//        	Issue issue =service.createIssue(new Issue("new"));
//           // Issue issue = service.getIssueById(6L);
//           // issue = service.updateIssue(issue, "changed description");
//        
//        };
//    }
	
}
