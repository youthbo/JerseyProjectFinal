package se.plushogskolan;

import com.google.gson.Gson;
import org.junit.Test;
import se.plushogskolan.model.Team;

/**
 * Created by daniel on 12/12/16.
 */
public class GsonTEst {


    @Test
    public void testG1son(){
        System.out.println("");
        System.out.println("*************");
        System.out.println("");
        Team team = new Team("Sharks");

        System.out.println("team.toString(): "+team.toString());
        System.out.println("");

        String jsonString = new Gson().toJson(team);
        System.out.println("");

        System.out.println("Team converted to a json string: "+jsonString);
        System.out.println("");

        Team team2 = new Gson().fromJson(jsonString,Team.class);
        System.out.println("");

        System.out.println("team2 bean serialized from json string: "+team2);
        System.out.println("");
        System.out.println("*************");



    }
}
