package model;

import java.util.List;

/**
 * Created by Art on 1/27/14.
 */
public class ExpertiseGroup {

    private String name;
    private List<Expertise> expertise;

    public ExpertiseGroup(String name, List<Expertise> expertise) {
        this.name = name;
        this.expertise = expertise;
    }

    public String getName() {
        return name;
    }

    public List<Expertise> getExpertise() {
        return expertise;
    }

    @Override
    public String toString() {
        return "ExpertiseGroup{" +
                "name='" + name + '\'' +
                ", expertise=" + expertise +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpertiseGroup that = (ExpertiseGroup) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
