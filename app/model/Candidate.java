package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Art on 1/21/14.
 */
@Document(collection = "candidates")
public class Candidate {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Date registrationDate;

    private Set<Group> groups;
    private List<ExpertiseGroup> expertises;

    public Candidate(String phone, String lastName, String firstName, String email, Set<Group> groups) {
        this.phone = phone;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.groups = groups;
        this.registrationDate = new Date();
        this.expertises = new ArrayList<>();
    }

    public String getPhone() {
        return phone;
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

    public List<ExpertiseGroup> getExpertises() {
        return expertises;
    }

    public void setExpertises(List<ExpertiseGroup> expertises) {
        this.expertises = expertises;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
