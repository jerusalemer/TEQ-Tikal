import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controllers.CandidateController;
import controllers.QuestionnaireController;
import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.Expertise;
import model.Group;
import model.Questionnarie;
import model.solr.repository.CandidatesRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;
import play.api.Play;
import play.api.mvc.EssentialFilter;
import service.CandidateFactory;
import service.CsvExporter;
import service.MailSender;
import service.QuestionnarieLoader;
import spring.SpringConfig;
import web_filters.AccessLogFilter;

import java.util.*;

/**
 * Created by Art on 2/2/14.
 */
public class Global extends GlobalSettings {

    ApplicationContext ctx;

    @Override
    public void onStart(Application app) {

        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        CandidatesRepository repository = ctx.getBean(CandidatesRepository.class);
        CandidateDao candidateDao = ctx.getBean(CandidateDao.class);
        QuestionnarieDao questionnarieDao = ctx.getBean(QuestionnarieDao.class);
        CandidateFactory candidateFactory = ctx.getBean(CandidateFactory.class);
        MailSender mailSender = ctx.getBean(MailSender.class);
        QuestionnarieLoader questionnarieLoader = ctx.getBean(QuestionnarieLoader.class);

        QuestionnaireController questionnaireController = Play.global(Play.current()).getControllerInstance(QuestionnaireController.class);
        questionnaireController.setUp(candidateDao, questionnarieDao, questionnarieLoader);
        CandidateController candidateController = Play.global(Play.current()).getControllerInstance(CandidateController.class);
        CsvExporter setCsvExporter = ctx.getBean(CsvExporter.class);
        candidateController.setUp(candidateDao, setCsvExporter, candidateFactory,mailSender);

        final Config config = ConfigFactory.load();
        final String cleanDataOnStart = config.getString("clean.data.startup");
        if( cleanDataOnStart !=null && Boolean.valueOf(cleanDataOnStart)) {
            //setup mock
             setupMockObjects(questionnarieDao, candidateFactory);
        }

        super.onStart(app);
        //load all from mongo to solr
        candidateDao.loadAll(true);
    }



    //todo for test only, remove when tests are done
    private void setupMockObjects(QuestionnarieDao questionnarieDao, CandidateFactory candidateFactory) {

        TreeMap<String, TreeMap<String, List<Expertise>>> expertises = new TreeMap<>();
        TreeMap<String, List<Expertise>> generalGroup = new TreeMap<>();
        generalGroup.put("Operating Systems", new ArrayList<>(Arrays.asList(new Expertise("Linux"), new Expertise("Windows"), new Expertise("Solaris"))));
        generalGroup.put("Database", new ArrayList<>(Arrays.asList(new Expertise("SQL"), new Expertise("Designing Database Schema"), new Expertise("Creating ERD"), new Expertise("Writing SQL Statements"))));
        expertises.put("General", generalGroup);
        questionnarieDao.save(new Questionnarie(Group.ALM, expertises));

        expertises = new TreeMap<>();
        generalGroup = new TreeMap<>();
        generalGroup.put("Java Language", new ArrayList<>(Arrays.asList(new Expertise("Developing Java Classes"), new Expertise("Using threads"))));
        generalGroup.put("Database", new ArrayList<>(Arrays.asList(new Expertise("Oracle"), new Expertise("My Sql"))));
        expertises.put("General", generalGroup);
        questionnarieDao.save(new Questionnarie(Group.JAVA, expertises));

        candidateFactory.createCandidate("Steven", "Gerrard", "gerrard@liverpool.com", "Tzipi", EnumSet.of(Group.JAVA));
        candidateFactory.createCandidate("Ray", "Charls", "ray_c@liverpool.com", "Tzipi", EnumSet.of(Group.JAVA));
        candidateFactory.createCandidate("Jhone", "Silever", "silver@liverpool.com", "Shirli", EnumSet.of(Group.JAVA, Group.ALM));
        candidateFactory.createCandidate("Michael", "Owen", "owen@liverpool.com", "Shirli", EnumSet.of(Group.JAVA, Group.ALM));
    }

    @Override
    public void onStop(Application app) {
        ((ConfigurableApplicationContext)ctx).close();
        super.onStop(app);
    }

    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return  new Class[] {AccessLogFilter.class};
    }
}
