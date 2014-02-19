package spring;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Created by Art on 2/2/14.
 */
@Configuration
@ComponentScan({"dao", "service"})
public class SpringConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(), "teq");
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
