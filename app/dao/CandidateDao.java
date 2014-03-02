package dao;

import model.Candidate;
import model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import play.Logger;

import java.util.*;

/**
 * Created by Art on 1/26/14.
 */
@Component
public class CandidateDao {

    @Autowired
    private MongoOperations mongoOperations;

    private Map<String,Candidate> candidates;

    public CandidateDao() {
        candidates = new HashMap<>();
    }

    public void save(Candidate candidate){
        mongoOperations.save(candidate);
        //update candidate directly to cache
        candidates.put(candidate.getEmail(),candidate);

    }

    public Collection<Candidate> getAll(){
        if(!candidates.isEmpty()) {
            return Collections.unmodifiableCollection(candidates.values());
        }

        Logger.debug("Candidate get all start ");
        List<Candidate> candidateList = mongoOperations.findAll(Candidate.class);
        Logger.debug("Candidate get mongo find all end ");
        for (Candidate candidate : candidateList) {
            DaoUtils.fixMongoDbBug(candidate.getExpertises());
        }
        Logger.debug("Candidate get all end ");

        //add candidate directly to cache
        for (Candidate candidate : candidateList) {
            candidates.put(candidate.getEmail(),candidate);
        }
        return candidateList;
    }

    public Candidate get(String email){
        if(candidates.containsKey(email)) {
            return candidates.get(email);
        }

        Candidate candidate = mongoOperations.findById(email, Candidate.class);
        DaoUtils.fixMongoDbBug(candidate.getExpertises());
        //add candidate directly to cache
        candidates.put(candidate.getEmail(),candidate);
        return candidate;
    }

    public void delete(Candidate candidate){
        mongoOperations.remove(candidate);
        //remove candidate directly from cache
        candidates.remove(candidate.getEmail());
    }


    public Collection<Candidate> findByGroup(Group group) {
        //todo find via Solr
        return Collections.emptyList();
    }

    public Collection<Candidate> findByRecruiter(String name) {
        //todo find via Solr
        return Collections.emptyList();
    }

    public Collection<Candidate> findByText(String name) {
        //todo find via Solr
        return Collections.emptyList();
    }
}
