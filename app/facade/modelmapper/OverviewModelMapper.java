package facade.modelmapper;

import domain.Person;
import model.OverviewEntryModel;

public class OverviewModelMapper {
    public OverviewEntryModel map(Person person) {
        String link = person.getId().value;
        return new OverviewEntryModel(person.team().name(), person.name(), person.picture(), person.team().colour(), link);
    }
}
