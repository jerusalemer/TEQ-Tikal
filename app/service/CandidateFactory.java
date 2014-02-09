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

    public Candidate createCandidate(String phone, String firstName, String lastName, String email, Set<Group> groups) {
        Candidate candidate = new Candidate(phone, lastName, firstName, email, groups);

        Set<Questionnarie> questionnaries = getRelevantQuestionnaries(groups);
        List<ExpertiseGroup> expertiseGroups = mergeQuestionnaries(questionnaries);

        candidate.setExpertises(expertiseGroups);

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


    private List<ExpertiseGroup> mergeQuestionnaries(Set<Questionnarie> questionnaries) {
        List<ExpertiseGroup> mergedExpertiseGroups = new ArrayList<>();
        for (Questionnarie questionnarie : questionnaries) {
            for (ExpertiseGroup expertiseGroup : questionnarie.getExpertiseGroups()) {
                if (mergedExpertiseGroups.contains(expertiseGroup)) {
                    mergeExpertiseGroups(expertiseGroup, mergedExpertiseGroups);
                } else {
                    mergedExpertiseGroups.add(expertiseGroup);
                }
            }
        }
        return mergedExpertiseGroups;
    }

    private void mergeExpertiseGroups(ExpertiseGroup newExpertiseGroup, List<ExpertiseGroup> expertiseGroups) {
        int questionGroupIndex = expertiseGroups.indexOf(newExpertiseGroup);
        ExpertiseGroup currentExpertiseGroup = expertiseGroups.get(questionGroupIndex);
        List<Expertise> newExpertises = new ArrayList<>();
        for (Expertise newExpertise : newExpertiseGroup.getExpertise()) {
            if (!currentExpertiseGroup.getExpertise().contains(newExpertise)) {
                newExpertises.add(newExpertise);
            }
        }
        currentExpertiseGroup.getExpertise().addAll(newExpertises);
    }
}
