package dao;

import model.Expertise;
import model.ExpertiseLevel;

import java.util.*;

/**
 * Created by Art on 2/13/14.
 */
public class DaoUtils {

    public static void fixMongoDbBug(SortedMap<String, ? extends SortedMap<String, List<Expertise>>> expertiseGroups){
        if (expertiseGroups != null && !expertiseGroups.isEmpty()){
            for (SortedMap<String, List<Expertise>> expertiseSubGroups : expertiseGroups.values()) {
                if(!expertiseSubGroups.isEmpty()){
                    Map<String, List<Expertise>> fixedExpertiseSubGroups = new TreeMap<>();

                    for (Map.Entry<String, List<Expertise>> expertisesSubGroups : expertiseSubGroups.entrySet()) {
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
                            List<Expertise> expertises = expertiseSubGroups.get(fixedSubGroup.getKey());
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
