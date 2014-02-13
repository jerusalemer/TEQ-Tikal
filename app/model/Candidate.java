package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
@Document(collection = "candidates")
public class Candidate {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private Date registrationDate;

    private Set<Group> groups;

    private SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises;

    public Candidate(String lastName, String firstName, String email, Set<Group> groups) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.groups = groups;
        this.registrationDate = new Date();
        expertises = new TreeMap<>();
    }

    public Candidate() {
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public SortedMap<String, ? extends SortedMap<String, List<Expertise>>> getExpertises() {
        return expertises;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setExpertises(SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises) {
        this.expertises = expertises;
    }
}
