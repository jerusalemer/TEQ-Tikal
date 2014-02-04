import controllers.MainController;
import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.Candidate;
import model.Group;
import model.QuestionGroup;
import model.Questionnarie;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;
import play.api.Play;
import spring.SpringConfig;

import java.util.*;

/**
 * Created by Art on 2/2/14.
 */
public class Global extends GlobalSettings {

    ApplicationContext ctx;

    @Override
    public void onStart(Application app) {

        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        MainController mainController = Play.global(Play.current()).getControllerInstance(MainController.class);
        CandidateDao candidateDao = ctx.getBean(CandidateDao.class);
        QuestionnarieDao questionnarieDao = ctx.getBean(QuestionnarieDao.class);
        mainController.setUp(candidateDao, questionnarieDao);

        //todo for test only, remove when tests are done
        setupMockObjects(candidateDao, questionnarieDao);

        super.onStart(app);
    }

    private void setupMockObjects(CandidateDao candidateDao, QuestionnarieDao questionnarieDao) {
        Candidate candidate = new Candidate("052123453", "Owen", "Michael", "owen@liverpool.com", EnumSet.of(Group.JAVA, Group.ALM));
        candidateDao.save(candidate);

        List<QuestionGroup> almQuestionGroups = new ArrayList<>();
        almQuestionGroups.add(new QuestionGroup("Operating Systems", new ArrayList<>(Arrays.asList("Linux", "Windows", "Solaris"))));
        almQuestionGroups.add(new QuestionGroup("Database", new ArrayList<>(Arrays.asList("SQL", "Designing Database Schema", "Creating ERD", "Writing SQL Statements"))));
        questionnarieDao.save(new Questionnarie(Group.ALM, almQuestionGroups));

        List<QuestionGroup> javaQuestionGroups = new ArrayList<>();
        javaQuestionGroups.add(new QuestionGroup("Java Language", new ArrayList<>(Arrays.asList("Developing Java Classes", "Using threads"))));
        javaQuestionGroups.add(new QuestionGroup("Database", new ArrayList<>(Arrays.asList("Oracle","My Sql"))));
        questionnarieDao.save(new Questionnarie(Group.JAVA, javaQuestionGroups));
    }

    @Override
    public void onStop(Application app) {
        ((ConfigurableApplicationContext)ctx).close();
        super.onStop(app);
    }
}
