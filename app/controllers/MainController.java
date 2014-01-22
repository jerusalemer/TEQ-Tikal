package controllers;

import model.Candidate;
import model.Expertise;
import model.ExpertiseLevel;
import model.Group;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Art on 1/21/14.
 */
public class MainController extends Controller {

    public static Result sendQuestionnaire() {
        return ok("send questionnaire");
    }

    /**
     * Candidate JSON example:
     * {"email":"michael.owen@gmail.com","firstName":"michael","lastName":"owen","phone":"052-123456","groups":["JAVASCRIPT","JAVA"]}
     */
    //@BodyParser.Of(BodyParser.Json.class)
    public static Result registerCandidate() {
        Candidate candidate = createCandidate();
        String validationError = validate(candidate);
        if (validationError == null){
            candidate = save(candidate);
            ObjectNode result = (ObjectNode) Json.toJson(candidate);
            return ok(result);
        }else{
            return badRequest(validationError);
        }

    }

    public static Result findCandidate(){
        Set<Group> groups = new HashSet(Arrays.asList(Group.JAVA, Group.JAVASCRIPT));
        Candidate candidate = new Candidate("052-123456", "owen", "michael", "michael.owen@gmail.com", groups);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(System.out, candidate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ok("xxx");
    }

    private static Candidate createCandidate() {
        JsonNode json = request().body().asJson();

        String firstName = json.findPath("firstName").getTextValue();
        String lastName = json.findPath("lastName").getTextValue();
        String email = json.findPath("email").getTextValue();
        String phone = json.findPath("phone").getTextValue();
        Set<Group> groups = new HashSet<>();
        for (JsonNode group : json.findPath("groups")){
            groups.add(Group.valueOf(group.getTextValue()));
        }
        return new Candidate(phone, lastName, firstName, email, groups);
    }

    private static Candidate save(Candidate candidate) {
        return candidate;
    }

    private static String validate(Candidate candidate) {
        if(candidate.getGroups().size() == 0){
            return "No candidate groups found";
        }
        if(candidate.getEmail() == null || candidate.getEmail().isEmpty()){
            return "No email address specified";
        }
        if(candidate.getFirstName() == null || candidate.getFirstName().isEmpty()){
            return "No first name specified";
        }
        if(candidate.getLastName() == null || candidate.getLastName().isEmpty()){
            return "No last name specified";
        }

        return null;
    }

}
