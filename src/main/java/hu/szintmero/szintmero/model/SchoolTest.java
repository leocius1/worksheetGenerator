package hu.szintmero.szintmero.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SchoolTest(String id,
                         String name,
                         String classId,
                         LocalDate date,
                         List<MeasuredTopic> topics,
                         List<Score> scores) {

    // TODO (5) Add a static factory method:  public static SchoolTest createNew(name, classId, date, topics, scores)
    //          It generates the id (UUID.randomUUID().toString()) and returns a new SchoolTest.
    //          Do NOT generate the id in the compact constructor: Jackson calls that too when parsing requests,
    //          so every incoming object would get a random id and you couldn't tell "new" from "existing". DONE
    public static SchoolTest createNew(SchoolTest s) {
        String id =(s.id() == null) ? UUID.randomUUID().toString() : s.id();
        return new SchoolTest(id,s.name(),s.classId(),s.date(),s.topics(),s.scores());}
}
