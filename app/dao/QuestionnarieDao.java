package dao;

import model.Group;
import model.Questionnarie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Art on 1/27/14.
 */
@Component
public class QuestionnarieDao {

    private Map<Group, Questionnarie> questionnaries = new ConcurrentHashMap<>();
    private static AtomicInteger idSequence = new AtomicInteger(1);

    public Questionnarie save(Questionnarie questionnarie) {
        questionnarie.setId(idSequence.incrementAndGet());
        questionnaries.put(questionnarie.getGroup(), questionnarie);
        return questionnarie;
    }

    public Questionnarie get(Group group){
        return questionnaries.get(group);
    }
}
