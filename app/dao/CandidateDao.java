package dao;

import model.Candidate;
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

    private Map<String, Candidate> candidateMap = new ConcurrentHashMap<>();

    public void save(Candidate candidate){
        candidateMap.put(candidate.getEmail(), candidate);
    }

    public List<Candidate> getAll(){
        return new ArrayList<>(candidateMap.values());
    }

    public Candidate get(String email){
        return candidateMap.get(email);
    }
}
