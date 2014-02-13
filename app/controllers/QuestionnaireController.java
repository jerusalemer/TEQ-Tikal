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

import java.util.*;

/**
 * Created by Art on 1/21/14.
 */
public class QuestionnaireController extends Controller {

    private static CandidateDao candidateDao;
    private static QuestionnarieDao questionnarieDao;

    public static Result fillQuestionnarie() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String candidateEmail = formData.get("candidateEmail")[0];
        Candidate candidate = candidateDao.get(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        for (String fullExpertiseStr : formData.get("expertise")) {
            String expertiseLevelStr = formData.get(fullExpertiseStr + "_level")[0];
            ExpertiseLevel expertiseLevel = expertiseLevelStr == null || "None".equalsIgnoreCase(expertiseLevelStr) ? null : ExpertiseLevel.valueOf(expertiseLevelStr.toUpperCase());
            String expertiseYearsStr = formData.get(fullExpertiseStr + "_years")[0];
            Double expertiseYears = StringUtils.isEmpty(expertiseYearsStr) ? null : Double.valueOf(expertiseYearsStr);

            //question = questionGroup.getName() + "_" + question
            String[] expertiseSplitted = fullExpertiseStr.split("_");
            String categoryName = expertiseSplitted[0];
            String subCategoryName = expertiseSplitted[1];
            String expertiseName = expertiseSplitted[2];


            List<Expertise> expertises = candidate.getExpertises().get(categoryName).get(subCategoryName);
            Expertise expertise = new Expertise(expertiseName);
            expertise = expertises.get(expertises.indexOf(expertise));
            expertise.setLevel(expertiseLevel);
            expertise.setYearsOfExperience(expertiseYears);
        }

        candidateDao.save(candidate);

        return ok("Questionnaire successfully saved");
    }

    public static void setUp(CandidateDao candidateDao, QuestionnarieDao questionnarieDao) {
        QuestionnaireController.candidateDao = candidateDao;
        QuestionnaireController.questionnarieDao = questionnarieDao;
    }
}
