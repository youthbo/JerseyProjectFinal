package se.plushogskolan.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.service.ServiceException;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQL)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {

	UserService userService;
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
	
//	@Test
//	public void canAddUser(){
//		User user = new User("firstname3", "lastname3", "username03",teamService.findByName("team 1"));
//	    user=userService.createUser(user);
//	    assertNotNull(user);	
//	    
//	}
	
//	@Test 
//	public void usernameShouldHasMoreThan10Char(){
//		thrown.expect(ServiceException.class);
//	    thrown.expectMessage("Username must be at least 10 characters long!");
//		User user = new User("firstname3", "lastname3", "username3",teamService.findByName("team 1"));
//	    user=userService.createUser(user);
//	}
	
	@Test
	public void canFindAllUsers() {	
		assertEquals(userService.findAllUsers(0,10).size(),2);
		
	}
	
	@After
	public void clean(){
		
	}
	

}
