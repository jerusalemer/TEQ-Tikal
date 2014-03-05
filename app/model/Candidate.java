package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.apache.solr.client.solrj.beans.Field;

import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
@Document(collection = "candidates")
public class Candidate {

    @Id
    @Field("id")
    private String email;
    @Field("firstName_t")
    private String firstName;
    @Field("recruiter_t")
    private String recruiter;
    @Field("lastName_t")
    private String lastName;
    @Field("registrationDate_dt")
    private Date registrationDate;
    @Field("groups_txt")
    private Set<Group> groups;

    @Field("experties_txt")
    private SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises;

    public Candidate(String lastName, String firstName, String email, String recruiter, Set<Group> groups) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.recruiter = recruiter;
        this.groups = groups;
        this.registrationDate = new Date();
        expertises = new TreeMap<>();
    }

    public Candidate() {}

    public String getRecruiter() {
        return recruiter;
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
