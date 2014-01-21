package controllers;

import model.Candidate;
import model.Expertise;
import model.Group;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Map;
import java.util.Set;

/**
 * Created by tema on 1/21/14.
 */
public class MainController extends Controller {

    public static Result sendQuestionnaire() {
        return ok("send questionnaire");
    }

    public static Result registerCandidate() {
        Candidate candidate = createCandidate();
        String validationError = validate(candidate);
        if (validationError == null){
            save(candidate);
            return ok("candidate created successfully");
        }else{
            return badRequest(validationError);
        }

    }

    private static Candidate createCandidate() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String firstName = formData.get("firstName")[0];
        String lastName = formData.get("lastName")[0];
        String email = formData.get("email")[0];
        String phone = formData.get("phone")[0];
        String expertisesJson = formData.get("expertises")[0];
        String groupsJson = formData.get("groups")[0];

        Set<Group> groups = parseGroupsJson(groupsJson);
        Set<Expertise> expertises = parseExpertiseJson(expertisesJson);
        return new Candidate(phone, lastName, firstName, email, groups, expertises);
    }

    private static Set<Expertise> parseExpertiseJson(String expertisesJson) {
        return null;
    }

    private static Set<Group> parseGroupsJson(String groupsJson) {
        return null;
    }

    private static void save(Candidate candidate) {

    }

    private static String validate(Candidate candidate) {
        return null;
    }
}
