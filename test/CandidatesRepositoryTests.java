import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.*;
import model.solr.repository.CandidatesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.specs2.internal.scalaz.std.iterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.CandidateFactory;
import spring.SpringConfig;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class CandidatesRepositoryTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private CandidateDao candidateDao;
    @Autowired
    private QuestionnarieDao questionnarieDao;
    @Autowired
    private CandidateFactory candidateFactory;

    @Before
    public void before() {
        setupMockObjects(questionnarieDao, candidateFactory);
        //load all to solr
        candidateDao.loadAll(true);
    }

    @Test
    public void findByName() {
        List<Candidate> candidates = candidatesRepository.findByFirstNameOrLastName("Steven", "");

        // Books with IDs 01, 02, 05, 08 contain "Island" in their name
        assertEquals(candidates.size(), 1);
        assertTrue(containsBooksWithIds(candidates, "gerrard@liverpool.com"));
    }

    @Test
    public void findByGroup() {
        List<Candidate> candidates = candidatesRepository.findByGroup(Group.JAVA.name());
        assertEquals(candidates.size(), 4);
        candidates = candidatesRepository.findByGroup(Group.ALM.name());
        assertEquals(candidates.size(), 2);
    }

    @Test
    public void findByRecruiter() {
        List<Candidate> candidates = candidatesRepository.findByRecruiter("Tzipi");

        // Books with IDs 01, 02, 05, 08 contain "Island" in their name
        assertEquals(candidates.size(), 2);
        assertTrue(containsBooksWithIds(candidates, "gerrard@liverpool.com","ray_c@liverpool.com"));
    }

    @Test
    public void findByRecruiterAndDelivery() {
        List<Candidate> candidates = candidatesRepository.findByRecruiterAndDelivery("Tzipi", DeliveryStatus.NOT_DELIVERED.name());

        // Books with IDs 01, 02, 05, 08 contain "Island" in their name
        assertEquals(candidates.size(), 2);
        assertTrue(containsBooksWithIds(candidates, "gerrard@liverpool.com","ray_c@liverpool.com"));
    }

    @Test
    public void findByRecruiterAndDeliveryAfterDelivery() {
        List<Candidate> candidates = candidatesRepository.findByRecruiterAndDelivery("Tzipi", DeliveryStatus.NOT_DELIVERED.name());

        // Books with IDs 01, 02, 05, 08 contain "Island" in their name
        assertEquals(candidates.size(), 2);
        assertTrue(containsBooksWithIds(candidates, "gerrard@liverpool.com","ray_c@liverpool.com"));
        for (Candidate candidate : candidates) {
            candidate.setDeliveryStatus(DeliveryStatus.DELIVERED);
            candidateDao.save(candidate);
        }
        candidates = candidatesRepository.findByRecruiterAndDelivery("Tzipi", DeliveryStatus.NOT_DELIVERED.name());
        assertEquals(candidates.size(), 0);
    }


    @Test
    public void findByFirstNameOrLastName() {
        List<Candidate> candidates = candidatesRepository.findByFirstNameOrLastName("Owen", "");

        // Books with IDs 01, 02, 05, 08 contain "Island" in their name
        assertEquals(candidates.size(), 1);
        assertTrue(containsBooksWithIds(candidates, "owen@liverpool.com"));
    }


    @Test
    public void findByNameAndFacetOnGroup() {
        FacetPage<Candidate> candidatesFacetPage = candidatesRepository.findByFirstNameOrLastNameFacetOnGroup("", new PageRequest(0, 10));

		// There are 4 books which contain "Island" in their name. However, only the first 2 books are returned because 
		// a page with size 2 is requested. The next two books could be requested using "new PageRequest(1, 2)" 
		
		assertEquals(4, candidatesFacetPage.getNumberOfElements());
		
		// 3 of these 4 books are categorized as Adventure
		// 2 of these 4 books are categorized as Humor
		// 1 of these 4 books is categorized as Romance
		Map<Group, Long> categoryFacetCounts = getCategoryFacetCounts(candidatesFacetPage);
		assertEquals(new Long(2), categoryFacetCounts.get(Group.ALM));
		assertEquals(new Long(4), categoryFacetCounts.get(Group.JAVA));
    }




    private boolean containsSnipplet(HighlightPage<Candidate> booksHighlightPage, String snippletToCheck) {
        for (HighlightEntry<Candidate> he : booksHighlightPage.getHighlighted()) {
            // A HighlightEntry belongs to an Entity (Book) and may have multiple highlighted fields (description)
            for (Highlight highlight : he.getHighlights()) {
                for (String snipplet : highlight.getSnipplets()) {

                    if (snipplet.equals(snippletToCheck)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Map<Group, Long> getCategoryFacetCounts(FacetPage<Candidate> candidates) {
		Map<Group, Long> facetCounts = new HashMap<>();
		for (Page<? extends FacetEntry> p : candidates.getAllFacets()) {
			for (FacetEntry facetEntry : p.getContent()) {
				Group group = Group.valueOf(facetEntry.getValue().toUpperCase());
				facetCounts.put(group, facetEntry.getValueCount());
			}
		}
		return facetCounts;
    }


    private boolean containsBooksWithIds(List<Candidate> books, String... idsToCheck) {
        String[] bookIds = new String[books.size()];
        for (int i = 0; i < books.size(); i++) {
            bookIds[i] = books.get(i).getEmail();
        }
        Arrays.sort(bookIds);
        Arrays.sort(idsToCheck);
        return Arrays.equals(bookIds, idsToCheck);
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
}
