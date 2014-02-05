import controllers.CandidateController;
import controllers.QuestionnaireController;
import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.*;
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

        CandidateDao candidateDao = ctx.getBean(CandidateDao.class);
        QuestionnarieDao questionnarieDao = ctx.getBean(QuestionnarieDao.class);

        QuestionnaireController questionnaireController = Play.global(Play.current()).getControllerInstance(QuestionnaireController.class);
        questionnaireController.setUp(candidateDao, questionnarieDao);
        CandidateController candidateController = Play.global(Play.current()).getControllerInstance(CandidateController.class);
        candidateController.setUp(candidateDao);

        //todo for test only, remove when tests are done
        setupMockObjects(candidateDao, questionnarieDao);

        super.onStart(app);
    }

    private void setupMockObjects(CandidateDao candidateDao, QuestionnarieDao questionnarieDao) {
        Candidate candidate = new Candidate("052123453", "Owen", "Michael", "owen@liverpool.com", EnumSet.of(Group.JAVA, Group.ALM));
        candidateDao.save(candidate);

        candidate = new Candidate("052123454", "Gerrard", "Steven", "gerrard@liverpool.com", EnumSet.of(Group.JAVA, Group.ALM));
        candidateDao.save(candidate);


        List<ExpertiseGroup> almExpertiseGroups = new ArrayList<>();
        almExpertiseGroups.add(new ExpertiseGroup("Operating Systems", new ArrayList<>(Arrays.asList(new Expertise("Linux"), new Expertise("Windows"), new Expertise("Solaris")))));
        almExpertiseGroups.add(new ExpertiseGroup("Database", new ArrayList<>(Arrays.asList(new Expertise("SQL"), new Expertise("Designing Database Schema"), new Expertise("Creating ERD"), new Expertise("Writing SQL Statements")))));
        questionnarieDao.save(new Questionnarie(Group.ALM, almExpertiseGroups));

        List<ExpertiseGroup> javaExpertiseGroups = new ArrayList<>();
        javaExpertiseGroups.add(new ExpertiseGroup("Java Language", new ArrayList<>(Arrays.asList(new Expertise("Developing Java Classes"), new Expertise("Using threads")))));
        javaExpertiseGroups.add(new ExpertiseGroup("Database", new ArrayList<>(Arrays.asList(new Expertise("Oracle"),new Expertise("My Sql")))));
        questionnarieDao.save(new Questionnarie(Group.JAVA, javaExpertiseGroups));
    }

    @Override
    public void onStop(Application app) {
        ((ConfigurableApplicationContext)ctx).close();
        super.onStop(app);
    }
}
