package emil.dobrev.roadpal.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

//
//@Configuration
//class MongoConfig : AbstractMongoClientConfiguration() {
//    override fun mongoClient(): MongoClient {
//        return MongoClients.create("mongodb://localhost:27017")
//    }
//
//    public override fun getDatabaseName(): String {
//        return "roadpal-db"
//    }
//}
