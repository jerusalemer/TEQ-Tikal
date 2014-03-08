package model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
@Document(collection = "candidates")
public class Candidate {

    @Id
    @Field("email")
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
    @Field("delivery_t")
    private DeliveryStatus deliveryStatus;

    private SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertises;

    public Candidate(String lastName, String firstName, String email, String recruiter, Set<Group> groups, DeliveryStatus deliveryStatus) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.recruiter = recruiter;
        this.groups = groups;
        this.deliveryStatus = deliveryStatus;
        this.registrationDate = new Date();
        this.expertises = new TreeMap<>();
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

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }
}
