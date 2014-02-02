import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.Candidate;
import model.Group;
import model.QuestionGroup;
import model.Questionnarie;
import play.Application;
import play.GlobalSettings;

import java.util.*;

/**
 * Created by Art on 2/2/14.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        //todo for test only, remove when tests are done
        Candidate candidate = new Candidate("052123456", "Owen", "Michael", "owen@liverpool.com", EnumSet.of(Group.JAVA, Group.ALM));
        CandidateDao.getInstance().save(candidate);

        List<QuestionGroup> almQuestionGroups = new ArrayList<>();
        almQuestionGroups.add(new QuestionGroup("Operating Systems", Arrays.asList("Linux", "Windows", "Solaris")));
        QuestionnarieDao.getInstance().save(new Questionnarie(Group.ALM, almQuestionGroups));

        List<QuestionGroup> javaQuestionGroups = new ArrayList<>();
        javaQuestionGroups.add(new QuestionGroup("Server Side Technologies", Arrays.asList("Spring", "Hibernate")));
        QuestionnarieDao.getInstance().save(new Questionnarie(Group.JAVA, javaQuestionGroups));

        super.onStart(app);
    }

    @Override
    public void onStop(Application app) {
        super.onStop(app);
    }
}
