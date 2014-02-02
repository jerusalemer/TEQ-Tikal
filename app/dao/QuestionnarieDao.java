package dao;

import model.Group;
import model.Questionnarie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Art on 1/27/14.
 */
public class QuestionnarieDao {

    private Map<Group, Questionnarie> questionnaries = new ConcurrentHashMap<>();
    private static AtomicInteger idSequence = new AtomicInteger(1);

    private static QuestionnarieDao instance = new QuestionnarieDao();
    private QuestionnarieDao(){}

    public static QuestionnarieDao getInstance(){
        return instance;
    }

    public Questionnarie save(Questionnarie questionnarie) {
        questionnarie.setId(idSequence.incrementAndGet());
        questionnaries.put(questionnarie.getGroup(), questionnarie);
        return questionnarie;
    }

    public Questionnarie get(Group group){
        return questionnaries.get(group);
    }
}
