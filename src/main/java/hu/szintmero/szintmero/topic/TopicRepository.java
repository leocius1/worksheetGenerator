package hu.szintmero.szintmero.topic;

// TODO (cleanup) DocumentReference, Strings (Guava) and ConcurrentHashMap are unused now
//      -> Ctrl+Alt+O (Optimize Imports) removes all three. Then delete this TODO and the one on ConcurrentHashMap.

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import hu.szintmero.szintmero.model.Topic;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class TopicRepository {
    private final CollectionReference topics;

    public TopicRepository(Firestore firestore) {
        this.topics = firestore.collection("topics");
    }

    public Topic save(Topic t) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", t.name());
        data.put("subject", t.subject());
        data.put( "description", t.description());
        await(topics.document(t.id()).set(data));
        return t;
    }

    public Optional<Topic> findById(String id) {
        DocumentSnapshot doc = await(topics.document(id).get());
        return doc.exists() ? Optional.of(toTopic(doc)) : Optional.empty();
    }


    public List<Topic> findAll() {
        return   await(topics.get())
                .getDocuments()
                .stream()
                .map(this::toTopic)
                .toList();
    }

    public void delete(String id) { await(topics.document(id).delete()); }

    private static <T> T await(ApiFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while calling Firestore", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Firestore call failed", e.getCause());
        }
    }

    private Topic toTopic(DocumentSnapshot doc) {
        return new Topic(doc.getId(), doc.getString("name"), doc.getString("subject"), doc.getString("description"));
    }

}
