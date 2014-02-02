package controllers;

import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.Candidate;
import model.Group;
import model.QuestionGroup;
import model.Questionnarie;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.fill_questionnarie;

import java.io.IOException;
import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
public class MainController extends Controller {

    private static CandidateDao candidateDao = CandidateDao.getInstance();
    private static QuestionnarieDao questionnarieDao = QuestionnarieDao.getInstance();

    public static Result sendQuestionnaire() {
        return ok("send questionnaire");
    }

    /**
     * Candidate JSON example:
     * {"email":"michael.owen@gmail.com","firstName":"michael","lastName":"owen","phone":"052-123456","groups":["JAVASCRIPT","JAVA"]}
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result registerCandidate() {
        Candidate candidate = createCandidate();
        String validationError = validate(candidate);
        if (validationError == null) {
            candidate = save(candidate);
            ObjectNode result = (ObjectNode) Json.toJson(candidate);
            return ok(result);
        } else {
            return badRequest(validationError);
        }

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

    //Body json example: {"group":"JAVA","questionGroups":[{"id":0,"name":"Java Language","questions":["Developing Java Classes","Using threads"]}]}
    @BodyParser.Of(BodyParser.Json.class)
    public static Result registerQuestionnaire() {
        Questionnarie questionnarie = parseQuestionnaireJson();
        questionnarie = questionnarieDao.save(questionnarie);
        ObjectNode result = (ObjectNode) Json.toJson(questionnarie);

        return ok(result);
    }

    // Groups - comma separated questionnarie groups
    public static Result getQuestionnarie(String candidateEmail, String groups) {
        Candidate candidate = candidateDao.get(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        Set<Questionnarie> questionnaries = getRelevantQuestionnaries(groups);
        List<QuestionGroup> questionGroups = mergeQuestionnaries(questionnaries);

        return ok(fill_questionnarie.render(candidate, questionGroups));
    }

    private static List<QuestionGroup> mergeQuestionnaries(Set<Questionnarie> questionnaries) {
        List<QuestionGroup> mergedQuestionGroups = new ArrayList<>();
        for (Questionnarie questionnarie : questionnaries) {
            for (QuestionGroup questionGroup : questionnarie.getQuestionGroups()) {
                if (mergedQuestionGroups.contains(questionGroup)) {
                    mergeQuestionGroups(questionGroup, mergedQuestionGroups);
                } else {
                    mergedQuestionGroups.add(questionGroup);
                }
            }
        }
        return mergedQuestionGroups;
    }

    private static void mergeQuestionGroups(QuestionGroup newQuestionGroup, List<QuestionGroup> questionGroups) {
        int questionGroupIndex = questionGroups.indexOf(newQuestionGroup);
        QuestionGroup currentQuestionGroup = questionGroups.get(questionGroupIndex);
        for (String newQuestion : newQuestionGroup.getQuestions()) {
            if (!currentQuestionGroup.getQuestions().contains(newQuestion)) {
                currentQuestionGroup.getQuestions().add(newQuestion);
            }
        }
    }

    private static Set<Questionnarie> getRelevantQuestionnaries(String groups) {
        String[] groupsArr = groups.split(",");
        Set<Questionnarie> questionnaries = new HashSet<>();
        for (String group : groupsArr) {
            Questionnarie questionnarie = questionnarieDao.get(Group.valueOf(group));
            questionnaries.add(questionnarie);
        }
        return questionnaries;
    }

    private static Questionnarie parseQuestionnaireJson() {
        JsonNode json = request().body().asJson();
        String groupStr = json.findPath("group").getTextValue();
        Group group = Group.valueOf(groupStr);
        List<QuestionGroup> questionGroups = new ArrayList<>();
        for (JsonNode questionGroup : json.findPath("questionGroups")) {
            List<String> questions = new ArrayList<>();
            for (JsonNode question : questionGroup.findPath("questions")) {
                questions.add(question.getTextValue());
            }
            String groupName = questionGroup.findPath("name").getTextValue();
            questionGroups.add(new QuestionGroup(groupName, questions));
        }

        return new Questionnarie(group, questionGroups);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result getQuestionnaire(String group) {
        List<QuestionGroup> questionGroups = new ArrayList<>();
        List<String> questions = new ArrayList<>();
        questions.add("Developing Java Classes");
        questions.add("Using threads");
        questionGroups.add(new QuestionGroup("Java Language", questions));
        Questionnarie questionnarie = new Questionnarie(Group.JAVA, questionGroups);
        ObjectNode result = (ObjectNode) Json.toJson(questionnarie);
        return ok(result);
    }


    public static Result sendQuestioneeryToCandidate(String email, Set<Group> questioneeries) {
        Candidate candidate = candidateDao.get(email);
        if (candidate == null) {
            return notFound();
        }
        return ok("Questioneeries were successfully sent to " + email);
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

    private static Candidate save(Candidate candidate) {
        candidateDao.save(candidate);
        return candidate;
    }

    private static String validate(Candidate candidate) {
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

}
