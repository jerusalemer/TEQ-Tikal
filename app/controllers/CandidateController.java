package controllers;

import dao.CandidateDao;
import model.Candidate;
import model.Group;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.candidates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tema on 2/5/14.
 */
public class CandidateController extends Controller {

    private static CandidateDao candidateDao;

    public static Result getAll(){
        List<Candidate> allCandidates = candidateDao.getAll();
        return ok(candidates.render(allCandidates));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findCandidate(String email) {
        Candidate candidate = candidateDao.get(email);
        if (candidate == null) {
            return notFound();
        }
        ObjectNode result = (ObjectNode) Json.toJson(candidate);
        return ok(result);
    }

    static Candidate save(Candidate candidate) {
        candidateDao.save(candidate);
        return candidate;
    }

    static String validate(Candidate candidate) {
        if (candidate.getGroups().size() == 0) {
            return "No candidate groups found";
        }
        if (candidate.getEmail() == null || candidate.getEmail().isEmpty()) {
            return "No email address specified";
        }
        if (candidate.getFirstName() == null || candidate.getFirstName().isEmpty()) {
            return "No first name specified";
        }
        if (candidate.getLastName() == null || candidate.getLastName().isEmpty()) {
            return "No last name specified";
        }

        return null;
    }

    public static void setUp(CandidateDao candidateDao){
        CandidateController.candidateDao = candidateDao;
    }

    /**
     * Candidate JSON example:
     * {"email":"michael.owen@gmail.com","firstName":"michael","lastName":"owen","phone":"052-123456","groups":["JAVASCRIPT","JAVA"]}
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result registerCandidate() {
        Candidate candidate = createCandidate();
        String validationError = CandidateController.validate(candidate);
        if (validationError == null) {
            candidate = CandidateController.save(candidate);
            ObjectNode result = (ObjectNode) Json.toJson(candidate);
            return ok(result);
        } else {
            return badRequest(validationError);
        }

    }

    private static Candidate createCandidate() {
        JsonNode json = request().body().asJson();

        String firstName = json.findPath("firstName").getTextValue();
        String lastName = json.findPath("lastName").getTextValue();
        String email = json.findPath("email").getTextValue();
        String phone = json.findPath("phone").getTextValue();
        Set<Group> groups = new HashSet<>();
        for (JsonNode group : json.findPath("groups")) {
            groups.add(Group.valueOf(group.getTextValue()));
        }
        return new Candidate(phone, lastName, firstName, email, groups);
    }
}
