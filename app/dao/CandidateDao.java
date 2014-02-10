package dao;

import model.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        return mongoOperations.findAll(Candidate.class);
    }

    public Candidate get(String email){
        return mongoOperations.findById(email, Candidate.class);
    }
}
