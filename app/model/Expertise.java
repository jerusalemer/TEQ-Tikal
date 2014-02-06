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

}
