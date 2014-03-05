package spring;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
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
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import play.Play;

import java.net.UnknownHostException;

/**
 * Created by Art on 2/2/14.
 */
@Configuration
@EnableSolrRepositories("model.solr.repository")
@ComponentScan({"dao", "service"})
public class SpringConfig {

    @Bean
    public SolrServer solrServer() {
        return new HttpSolrServer("http://localhost:8983/solr");
    }

    @Bean
    public SolrTemplate solrTemplate(SolrServer server) throws Exception {
        return new SolrTemplate(server);
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        Mongo mongo;
        String dbName;
        UserCredentials credentials;
        try {
            if (Play.isProd()) {
                String prodHost = Play.application().configuration().getString("prod.db.mongo.host");
                Integer prodPort = Play.application().configuration().getInt("prod.db.mongo.port");
                mongo = new MongoClient(prodHost, prodPort);
                dbName = Play.application().configuration().getString("prod.db.mongo.name");

                String username = Play.application().configuration().getString("prod.db.mongo.user");
                String password = Play.application().configuration().getString("prod.db.mongo.password");
                credentials = new UserCredentials(username, password);
                return new SimpleMongoDbFactory(mongo, dbName, credentials);
            } else {
                return getDevMongoDbFactory();
            }
        } catch (Exception e) {
            return getDevMongoDbFactory();
        }
    }

    private MongoDbFactory getDevMongoDbFactory() throws UnknownHostException {
        String dbName;//load configuration manually
        Config config = ConfigFactory.load();
        dbName = config.getString("dev.db.mongo");
        return new SimpleMongoDbFactory(new MongoClient(), dbName, null);
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean(name = "mongoMoxydomainConverter")
    @Autowired
    public MappingMongoConverter mongoMoxydomainConverter(MongoDbFactory mongoDbFactory, MappingContext mappingContext) {
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
