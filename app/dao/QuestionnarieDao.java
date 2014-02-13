package dao;

import model.Group;
import model.Questionnarie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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

    @Autowired
    private MongoOperations mongoOperations;

    public Questionnarie save(Questionnarie questionnarie) {
        mongoOperations.save(questionnarie);
        return questionnarie;
    }

    public Questionnarie get(Group group){
        Questionnarie questionnarie = mongoOperations.findById(group, Questionnarie.class);
        DaoUtils.fixMongoDbBug(questionnarie.getExpertises());
        return questionnarie;
    }
}
