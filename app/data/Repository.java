package data;

import data.datatype.PersonId;
import domain.Person;
import domain.Query;
import domain.Team;

import java.util.List;

public interface Repository {
    List<Person> listPersons();
    List<Team> listTeams();

    List<Person> queryPersons(Query query);
    List<Team> queryTeams(Query query);

    Person findPersonById(PersonId id);

}
