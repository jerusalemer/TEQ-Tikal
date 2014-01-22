package model;

import java.util.Set;

/**
 * Created by Art on 1/21/14.
 */
public class Expertise {

    private String name;
    private ExpertiseLevel level;
    private int yearsOfExperience;
    private Set<Group> relevantGroups;

    public Expertise(String name, ExpertiseLevel level, int yearsOfExperience, Set<Group> relevantGroups) {
        this.name = name;
        this.level = level;
        this.yearsOfExperience = yearsOfExperience;
        this.relevantGroups = relevantGroups;
    }

    public String getName() {
        return name;
    }

    public ExpertiseLevel getLevel() {
        return level;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Set<Group> getRelevantGroups() {
        return relevantGroups;
    }
}
