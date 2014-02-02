package model;

import java.util.List;

/**
 * Created by Art on 1/27/14.
 */
public class QuestionGroup {

    private String name;
    private List<String> questions;

    public QuestionGroup(String name, List<String> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "QuestionGroup{" +
                "name='" + name + '\'' +
                ", questions=" + questions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionGroup that = (QuestionGroup) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
