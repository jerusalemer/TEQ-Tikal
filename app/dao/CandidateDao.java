package dao;

import model.Candidate;
import model.Expertise;
import model.ExpertiseLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.*;
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
        List<Candidate> candidateList = mongoOperations.findAll(Candidate.class);
        for (Candidate candidate : candidateList) {
            fixMongoDbBug(candidate);
        }
        return candidateList;
    }

    public Candidate get(String email){
        Candidate candidate = mongoOperations.findById(email, Candidate.class);
        fixMongoDbBug(candidate);
        return candidate;
    }

    private void fixMongoDbBug(Candidate candidate){
        if (candidate.getExpertises() != null && !candidate.getExpertises().isEmpty()){
            for (SortedMap<String, List<Expertise>> expertiseGroups : candidate.getExpertises().values()) {
                if(!expertiseGroups.isEmpty()){
                    Map<String, List<Expertise>> fixedExpertiseSubGroups = new TreeMap<>();

                    for (Map.Entry<String, List<Expertise>> expertisesSubGroups : expertiseGroups.entrySet()) {
                        if(!expertisesSubGroups.getValue().isEmpty()){
                            for (Object expertise : expertisesSubGroups.getValue()) {
                                if(expertise instanceof Map){
                                    if(!fixedExpertiseSubGroups.containsKey(expertisesSubGroups.getKey())){
                                        fixedExpertiseSubGroups.put(expertisesSubGroups.getKey(), new ArrayList<Expertise>());
                                    }
                                    Map expertiseMap = (Map) expertise;
                                    ExpertiseLevel level = expertiseMap.containsKey("level") ? ExpertiseLevel.valueOf((String) expertiseMap.get("level")) : null;
                                    Double yearsOfExperience = expertiseMap.containsKey("yearsOfExperience") ? Double.valueOf((Double) expertiseMap.get("yearsOfExperience")) : null;
                                    Expertise fixedExpertise = new Expertise((String) expertiseMap.get("name"), level, yearsOfExperience);
                                    fixedExpertiseSubGroups.get(expertisesSubGroups.getKey()).add(fixedExpertise);
                                }
                            }
                        }

                    }

                    if(!fixedExpertiseSubGroups.isEmpty()){
                        for (Map.Entry<String, List<Expertise>> fixedSubGroup : fixedExpertiseSubGroups.entrySet()) {
                            List<Expertise> expertises = expertiseGroups.get(fixedSubGroup.getKey());
                            expertises.clear();
                            expertises.addAll(fixedSubGroup.getValue());
                        }
                        fixedExpertiseSubGroups.clear();
                    }
                }
            }
        }
    }
}
