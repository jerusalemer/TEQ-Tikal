package dao;

import model.Candidate;
import model.Expertise;
import model.ExpertiseLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

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
        List<Candidate> candidateList = mongoOperations.findAll(Candidate.class);
        for (Candidate candidate : candidateList) {
            DaoUtils.fixMongoDbBug(candidate.getExpertises());
        }
        return candidateList;
    }

    public Candidate get(String email){
        Candidate candidate = mongoOperations.findById(email, Candidate.class);
        DaoUtils.fixMongoDbBug(candidate.getExpertises());
        return candidate;
    }


}
