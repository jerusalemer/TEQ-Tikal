package model.solr.repository;

import java.util.Date;
import java.util.List;

import model.Candidate;
import model.DeliveryStatus;
import model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;


public interface CandidatesRepository extends SolrCrudRepository<Candidate, String> {

    @Query("recruiter_t:?0")
    List<Candidate> findByRecruiter(String recruiter);

    @Query("recruiter_t:?0 AND delivery_t:?1")
    List<Candidate> findByRecruiterAndDelivery(String recruiter, String deliveryStatus);

    @Query("email:?0")
    List<Candidate> findByEmail(String email);

    @Query("groups_txt:*?0*")
    List<Candidate> findByGroup(String group);

    @Query("firstName_t:*?0* OR lastName_t:*?0*")
    List<Candidate> findByFirstNameOrLastName(String firstName, String lastName);

	@Query("firstName_t:*?0* OR lastName_t:*?0*")
	@Facet(fields = { "groups_txt" }, limit = 10)
	FacetPage<Candidate> findByFirstNameOrLastNameFacetOnGroup(String name, Pageable page);

}
