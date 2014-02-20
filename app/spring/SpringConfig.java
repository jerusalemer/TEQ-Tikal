package spring;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import play.Play;

/**
 * Created by Art on 2/2/14.
 */
@Configuration
@ComponentScan({"dao", "service"})
public class SpringConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        Mongo mongo;
        String dbName;
        UserCredentials credentials;
        if (Play.isDev()){
            String prodHost = Play.application().configuration().getString("prod.db.mongo.host");
            Integer prodPort = Play.application().configuration().getInt("prod.db.mongo.port");
            mongo = new MongoClient(prodHost, prodPort);
            dbName = Play.application().configuration().getString("prod.db.mongo.name");

            String username = Play.application().configuration().getString("prod.db.mongo.user");
            String password = Play.application().configuration().getString("prod.db.mongo.password");
            credentials = new UserCredentials(username, password);
        }else{
            mongo = new MongoClient();
            dbName = Play.application().configuration().getString("dev.db.mongo");
            credentials = null;
        }

        return new SimpleMongoDbFactory(mongo, dbName, credentials);
    }

    @Bean
    public MongoMappingContext mongoMappingContext(){
        return new MongoMappingContext();
    }

    @Bean(name = "mongoMoxydomainConverter")
    @Autowired
    public MappingMongoConverter mongoMoxydomainConverter(MongoDbFactory mongoDbFactory, MappingContext mappingContext){
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(mongoDbFactory, mappingContext);
        mappingMongoConverter.setMapKeyDotReplacement("\\+");
        return mappingMongoConverter;
    }

    @Bean
    @Autowired
    public MongoTemplate mongoTemplate(MappingMongoConverter mappingMongoConverter) throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), mappingMongoConverter);

        return mongoTemplate;

    }
}
