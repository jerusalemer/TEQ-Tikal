package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Art on 1/27/14.
 */
@Document(collection = "questionnaire-templates")
public class Questionnarie {

    @Id
    private Group group;
    private List<ExpertiseGroup> expertiseGroups;

    public Questionnarie(Group group, List<ExpertiseGroup> expertiseGroups) {
        this.group = group;
        this.expertiseGroups = expertiseGroups;
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

    public List<ExpertiseGroup> getExpertiseGroups() {
        return expertiseGroups;
    }


}
