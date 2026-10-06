package hu.szintmero.szintmero.schoolTest;

import hu.szintmero.szintmero.model.SchoolTest;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SchoolTestRepository {
   private final Map<String, SchoolTest> store = new ConcurrentHashMap<>();

   public SchoolTest save(SchoolTest s){
        store.put(s.id(),s);
        return s;
    }

   public Optional<SchoolTest> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }


    public List<SchoolTest> findAll() {
        return List.copyOf(store.values());
    }

    public List<SchoolTest> findByClassId(String classId) {
       return store.values()
               .stream()
               .filter(t -> t.classId().equals(classId))
               .sorted(Comparator.comparing(SchoolTest :: date))
               .toList();
    }

}
