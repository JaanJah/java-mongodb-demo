import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class Main {
    public static void main(String[] args) {

        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("bruh");
        MongoCollection<Document> collection = database.getCollection("test");


        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 100; i++) {
            Document doc = new Document("user_id", i)
                    .append("server_id", i * 123)
                    .append("counter", Math.random() * 100);
            documents.add(doc);
        }
        collection.insertMany(documents);

        Consumer<Document> printBlock = new Consumer<Document>() {
            @Override
            public void accept(final Document document) {
                System.out.println(document.toJson());
            }
        };

        System.out.println("Before update:");
        collection.find(eq("user_id", 15)).forEach(printBlock);

        collection.updateOne(eq("user_id", 15),
                combine(set("counter", 10000)));

        System.out.println("After update:");
        collection.find(eq("user_id", 15)).forEach(printBlock);
    }
}
