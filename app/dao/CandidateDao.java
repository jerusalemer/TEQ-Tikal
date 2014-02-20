package dao;

import model.Candidate;
import model.Expertise;
import model.ExpertiseLevel;
import model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    public void save(Candidate candidate){
        mongoOperations.save(candidate);
    }

    public List<Candidate> getAll(){
        Logger.debug("Candidate get all start ");
        List<Candidate> candidateList = mongoOperations.findAll(Candidate.class);
        Logger.debug("Candidate get mongo find all end ");
        for (Candidate candidate : candidateList) {
            DaoUtils.fixMongoDbBug(candidate.getExpertises());
        }
        Logger.debug("Candidate get all end ");
        return candidateList;
    }

    public Candidate get(String email){
        Candidate candidate = mongoOperations.findById(email, Candidate.class);
        DaoUtils.fixMongoDbBug(candidate.getExpertises());
        return candidate;
    }

    public void delete(Candidate candidate){
        mongoOperations.remove(candidate);
    }


    public Collection<Candidate> find(Group group) {
        Query query = new Query();
        //query.addCriteria(Criteria.where("groups").);
        return mongoOperations.find(query, Candidate.class);
    }
}
