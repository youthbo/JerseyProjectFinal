package se.plushogskolan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import se.plushogskolan.model.Issue;
import se.plushogskolan.service.IssueService;

@SpringBootApplication
public class JerseyProjectApplication {

	private static final Logger log = LoggerFactory.getLogger(JerseyProjectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JerseyProjectApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(IssueService service) {
        return args -> {
        	Issue issue =service.createIssue(new Issue("new"));
           // Issue issue = service.getIssueById(6L);
           // issue = service.updateIssue(issue, "changed description");
        
        };
    }
	
}
