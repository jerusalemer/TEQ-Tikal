package model;

import java.util.List;

/**
 * Created by Art on 1/27/14.
 */
public class Questionnarie {

    private int id;
    private Group group;
    private List<ExpertiseGroup> expertiseGroups;

    public Questionnarie(Group group, List<ExpertiseGroup> expertiseGroups) {
        this.group = group;
        this.expertiseGroups = expertiseGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Questionnarie that = (Questionnarie) o;

        if (id != that.id) return false;
        if (group != that.group) return false;
        if (expertiseGroups != null ? !expertiseGroups.equals(that.expertiseGroups) : that.expertiseGroups != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (expertiseGroups != null ? expertiseGroups.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public List<ExpertiseGroup> getExpertiseGroups() {
        return expertiseGroups;
    }

    @Override
    public String toString() {
        return "Questionnarie{" +
                "id=" + id +
                ", group=" + group +
                ", expertiseGroups=" + expertiseGroups +
                '}';
    }
}
