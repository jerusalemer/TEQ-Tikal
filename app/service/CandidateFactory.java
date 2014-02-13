package service;

import dao.CandidateDao;
import dao.QuestionnarieDao;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Art on 2/9/14.
 */
@Component
public class CandidateFactory {

    @Autowired
    private QuestionnarieDao questionnarieDao;

    @Autowired
    private CandidateDao candidateDao;

    public Candidate createCandidate(String firstName, String lastName, String email, Set<Group> groups) {
        Candidate candidate = new Candidate(lastName, firstName, email, groups);

        Set<Questionnarie> questionnaries = getRelevantQuestionnaries(groups);
        TreeMap<String, TreeMap<String, List<Expertise>>> expertises = mergeQuestionnaries(questionnaries);

        candidate.setExpertises(expertises);

        candidateDao.save(candidate);

        return candidate;

    }

    private Set<Questionnarie> getRelevantQuestionnaries(Collection<Group> groups) {
        Set<Questionnarie> questionnaries = new HashSet<>();
        for (Group group : groups) {
            Questionnarie questionnarie = questionnarieDao.get(group);
            questionnaries.add(questionnarie);
        }
        return questionnaries;
    }


    private TreeMap<String, TreeMap<String, List<Expertise>>> mergeQuestionnaries(Set<Questionnarie> questionnaries) {
        TreeMap<String, TreeMap<String, List<Expertise>>> mergedExpertises = new TreeMap<>();
        for (Questionnarie questionnarie : questionnaries) {

            for (Map.Entry<String, ? extends SortedMap<String, List<Expertise>>> groupEntry : questionnarie.getExpertises().entrySet()) {
                for (Map.Entry<String, List<Expertise>> subGroup : groupEntry.getValue().entrySet()) {
                    for (Expertise expertise : subGroup.getValue() ) {
                        if(!mergedExpertises.containsKey(groupEntry.getKey())){
                            mergedExpertises.put(groupEntry.getKey(), new TreeMap<String, List<Expertise>>());
                        }

                        if(!mergedExpertises.get(groupEntry.getKey()).containsKey(subGroup.getKey())){
                            mergedExpertises.get(groupEntry.getKey()).put(subGroup.getKey(), new ArrayList<Expertise>());
                        }

                        List<Expertise> expertises = mergedExpertises.get(groupEntry.getKey()).get(subGroup.getKey());
                        if(!expertises.contains(expertise)){
                            expertises.add(expertise);
                        }

                    }
                }
            }
        }
        return mergedExpertises;
    }

}
