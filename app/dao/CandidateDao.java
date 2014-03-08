package dao;

import akka.japi.Option;
import model.Candidate;
import model.DeliveryStatus;
import model.Group;
import model.solr.repository.CandidatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import play.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Art on 1/26/14.
 */
@Component
public class CandidateDao {
    @Autowired
    private CandidatesRepository repository;

    @Autowired
    private MongoOperations mongoOperations;


    public void save(Candidate candidate) {
        mongoOperations.save(candidate);
        //solr repository
        repository.save(candidate);
    }

    public Collection<Candidate> getAll() {
        return enrich(repository.findAll());
    }

    private Collection<Candidate> enrich(Iterable<Candidate> candidates) {
        Collection<Candidate> enriched = new ArrayList<>();
        for (Candidate candidate : candidates) {
            enriched.add(enrich(candidate));
        }
        return enriched;
    }

    public Candidate getByEmail(String email) {
        final List<Candidate> candidates = repository.findByEmail(email);
        if (!candidates.isEmpty()) {
            return enrich(candidates.get(0));
        }

        Candidate candidate = mongoOperations.findById(email, Candidate.class);
        //todo return Guava Option here
        if(candidate==null)
            return null;

        DaoUtils.fixMongoDbBug(candidate.getExpertises());
        //add candidate directly to cache
        repository.save(candidate);
        return candidate;
    }

    private Candidate enrich(Candidate candidate) {
        return mongoOperations.findById(candidate.getEmail(), Candidate.class);
    }

    public void delete(Candidate candidate) {
        mongoOperations.remove(candidate);
        //remove from solr repository
        repository.delete(candidate);
    }


    public Collection<Candidate> findByGroup(Group group) {
        return enrich(repository.findByGroup(group.name()));
    }

    private Collection<Candidate> enrich(List<Candidate> list) {
        Collection<Candidate> enriched = new ArrayList<>();
        for (Candidate candidate : list) {
            enriched.add(enrich(candidate));
        }
        return enriched;
    }

    public Collection<Candidate> findByRecruiter(String name) {
        return enrich(repository.findByRecruiter(name));
    }

    public Collection<Candidate> findByRecruiterAndDeliveryStatus(String name, DeliveryStatus deliveryStatus) {
        return enrich(repository.findByRecruiterAndDelivery(name, deliveryStatus.name()));
    }

    public Collection<Candidate> findByNameOrLastName(String name) {
        return enrich(repository.findByFirstNameOrLastName(name, name));
    }

    public void loadAll(boolean cleanSolr) {
        Logger.debug("Candidate get all start ");
        List<Candidate> candidateList = mongoOperations.findAll(Candidate.class);
        Logger.debug("Candidate get mongo find all end ");
        for (Candidate candidate : candidateList) {
            DaoUtils.fixMongoDbBug(candidate.getExpertises());
        }
        Logger.debug("Candidate get all end ");

        if(cleanSolr) {
            repository.deleteAll();
        }

        //add candidate directly to cache
        for (Candidate candidate : candidateList) {
            //solr repository
            repository.save(candidate);
        }
    }
}
