package model;

import java.util.Date;
import java.util.Set;

/**
 * Created by tema on 1/21/14.
 */
public class Candidate {

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Date registrationDate;

    private Set<Group> groups;
    private Set<Expertise> expertises;
}
