package hu.szintmero.szintmero.topic;

import hu.szintmero.szintmero.model.Topic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TopicRepository {
    private final Map<String, Topic> store = new ConcurrentHashMap<>();

    public Topic save(Topic t) {
//        String id = (t.id() == null) ? UUID.randomUUID().toString() : t.id();
//        Topic saved = new Topic(id, t.name(), t.subject(), t.description());
//        store.put(id, saved);
        store.put(t.id(),t);
        return t;
    }
    public Optional<Topic> findById(String id) { return Optional.ofNullable(store.get(id)); }
    public List<Topic> findAll() { return List.copyOf(store.values()); }
    public void delete(String id) { store.remove(id); }
}
