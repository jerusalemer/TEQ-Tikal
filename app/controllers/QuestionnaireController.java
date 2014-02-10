package controllers;

import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.candidate_questionnarie;

import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
public class QuestionnaireController extends Controller {

    private static CandidateDao candidateDao;
    private static QuestionnarieDao questionnarieDao;

    //Body json example: {"group":"JAVA","questionGroups":[{"id":0,"name":"Java Language","questions":["Developing Java Classes","Using threads"]}]}
    @BodyParser.Of(BodyParser.Json.class)
    public static Result registerQuestionnaire() {
        Questionnarie questionnarie = parseQuestionnaireJson();
        questionnarie = questionnarieDao.save(questionnarie);
        ObjectNode result = (ObjectNode) Json.toJson(questionnarie);

        return ok(result);
    }


    public static Result fillQuestionnarie() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String candidateEmail = formData.get("candidateEmail")[0];
        Candidate candidate = candidateDao.get(candidateEmail);
        if (candidate == null) {
            return notFound();
        }


        List<ExpertiseGroup> expertiseGroups = new ArrayList<>();
        candidate.setExpertises(expertiseGroups);
        for (String fullExpertiseStr : formData.get("expertise")) {
            String expertiseLevelStr = formData.get(fullExpertiseStr + "_level")[0];
            ExpertiseLevel expertiseLevel = expertiseLevelStr == null || "None".equalsIgnoreCase(expertiseLevelStr) ? null : ExpertiseLevel.valueOf(expertiseLevelStr.toUpperCase());
            String expertiseYearsStr = formData.get(fullExpertiseStr + "_years")[0];
            Double expertiseYears = StringUtils.isEmpty(expertiseYearsStr) ? null : Double.valueOf(expertiseYearsStr);

            //question = questionGroup.getName() + "_" + question
            String[] expertiseSplitted = fullExpertiseStr.split("_");
            String expertiseGroupName = expertiseSplitted[0];
            String expertise = expertiseSplitted[1];

            ExpertiseGroup expertiseGroup = new ExpertiseGroup(expertiseGroupName, new ArrayList<Expertise>());
            if (expertiseGroups.contains(expertiseGroup)) {
                expertiseGroup = expertiseGroups.get(expertiseGroups.indexOf(expertiseGroup));
            } else {
                expertiseGroups.add(expertiseGroup);
            }

            expertiseGroup.getExpertise().add(new Expertise(expertise, expertiseLevel, expertiseYears));

        }

        candidateDao.save(candidate);

        return ok("Questionnaire successfully saved");
    }


    private static Questionnarie parseQuestionnaireJson() {
        JsonNode json = request().body().asJson();
        String groupStr = json.findPath("group").getTextValue();
        Group group = Group.valueOf(groupStr);
        List<ExpertiseGroup> expertiseGroups = new ArrayList<>();
        for (JsonNode questionGroup : json.findPath("expertiseGroups")) {
            List<Expertise> questions = new ArrayList<>();
            for (JsonNode question : questionGroup.findPath("questions")) {
                questions.add(new Expertise(question.getTextValue()));
            }
            String groupName = questionGroup.findPath("name").getTextValue();
            expertiseGroups.add(new ExpertiseGroup(groupName, questions));
        }

        return new Questionnarie(group, expertiseGroups);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result getQuestionnaire(String group) {
        List<ExpertiseGroup> expertiseGroups = new ArrayList<>();
        List<Expertise> expertises = new ArrayList<>();
        expertises.add(new Expertise("Developing Java Classes"));
        expertises.add(new Expertise("Using threads"));
        expertiseGroups.add(new ExpertiseGroup("Java Language", expertises));
        Questionnarie questionnarie = new Questionnarie(Group.JAVA, expertiseGroups);
        ObjectNode result = (ObjectNode) Json.toJson(questionnarie);
        return ok(result);
    }

    public static void setUp(CandidateDao candidateDao, QuestionnarieDao questionnarieDao) {
        QuestionnaireController.candidateDao = candidateDao;
        QuestionnaireController.questionnarieDao = questionnarieDao;
    }
}
