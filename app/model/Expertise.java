package model;

import java.util.Set;

/**
 * Created by Art on 1/21/14.
 */
public class Expertise {

    private String name;
    private ExpertiseLevel level;
    private Double yearsOfExperience;

    public Expertise(String name, ExpertiseLevel level, Double yearsOfExperience) {
        this.name = name;
        this.level = level;
        this.yearsOfExperience = yearsOfExperience;
    }

    //for database orm
    public Expertise() { }

    public Expertise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpertiseLevel getLevel() {
        return level;
    }

    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setLevel(ExpertiseLevel level) {
        this.level = level;
    }

    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expertise expertise = (Expertise) o;

        if (!name.equals(expertise.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
