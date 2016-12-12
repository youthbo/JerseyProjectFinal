package se.plushogskolan;

import static org.junit.Assert.assertEquals;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.service.ServiceException;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class JerseyProjectApplicationTests {
	
	private static final Logger log = LoggerFactory.getLogger(JerseyProjectApplication.class);


	@Autowired
	UserService userService;
	@Autowired
	TeamService teamService;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before 
	public void setUp(){
		Team team = new Team("team 1");
		team = teamService.createTeam(team);
		for (int i = 0; i < 2; i++) {
			String firstname = "firstname"+i;
			String lastname = "lastname"+i;
			String username = "username0"+i;
			User user = new User(firstname, lastname, username,team);
			userService.createUser(user);
		}
	}
	
	@Test 
	public void usernameShouldHasMoreThan10Char(){
		thrown.expect(ServiceException.class);
	    thrown.expectMessage("Username must be at least 10 characters long!");
		User user = new User("firstname3", "lastname3", "username3",new Team("team 2"));
	    user=userService.createUser(user);
	}
	
	@Test
	public void canFindAllUsers() {	
		userService.findAllUsers(0, 10).forEach(u->log.info(u.toString()));
		assertEquals(userService.findAllUsers(0,10).size(),2);
		
	}
	
	@After
	public void clean(){
		
	}
	
}
