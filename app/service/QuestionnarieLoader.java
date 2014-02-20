package service;

import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.Candidate;
import model.Group;
import model.Questionnarie;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import play.Logger;
import play.Play;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Created by Art on 2/18/14.
 */
@Component
public class QuestionnarieLoader {

    @Autowired
    private QuestionnarieDao questionnarieDao;

    @Autowired
    private CandidateDao candidateDao;

    public void reloadQuestionnaries() throws IOException {

        for (Group group : Group.values()) {

            InputStream inputStream = Play.application().resourceAsStream("questionnaries/" + group.name().toLowerCase() + ".json");
            if (inputStream != null) {
                Questionnarie questionnarie = loadQuestionnarie(inputStream, group);
                questionnarieDao.save(questionnarie);

                updateExistingCandidates(questionnarie);
                Logger.info("Successfully loaded " + group.name() + " questionnaire");
            }
        }

    }

    /**
     * todo go over existing candidates and update
     * @param questionnarie
     */
    private void updateExistingCandidates(Questionnarie questionnarie) {
        //Collection<Candidate> candidates = candidateDao.find(questionnarie.getGroup());
    }

    private Questionnarie loadQuestionnarie(InputStream inputStream, Group group) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, Questionnarie.class);
    }
}
