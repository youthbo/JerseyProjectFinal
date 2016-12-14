package se.plushogskolan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.repository.WorkItemRepository;
import se.plushogskolan.service.WorkItemService;

/**
 * Created by daniel on 12/12/16.
 */

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DanielsTest {

    @Autowired
    WorkItemService workItemService;

    @Test
    public void addWorkItems(){

        WorkItem workItem = new WorkItem("WorkItem1","1 xxxx");
        WorkItem workItem2 = new WorkItem("WorkItem2","2 description");
        WorkItem workItem3 = new WorkItem("WorkItem3","3 description");

        workItemService.create(workItem);
        workItemService.create(workItem2);
        workItemService.create(workItem3);

    }
}
