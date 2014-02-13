package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Art on 1/27/14.
 */
@Document(collection = "questionnaire_templates")
public class Questionnarie {

    @Id
    private Group group;
    private SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises;

    public Questionnarie(Group group, SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises) {
        this.group = group;
        this.expertises = expertises;
    }

    public Questionnarie() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Questionnarie that = (Questionnarie) o;

        if (group != that.group) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return group.hashCode();
    }

    public Group getGroup() {
        return group;
    }

    public SortedMap<String, ? extends SortedMap<String, List<Expertise>>> getExpertises() {
        return expertises;
    }
}
