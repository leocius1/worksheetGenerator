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


    public static SchoolTest createNew(SchoolTest s) {
        String id =(s.id() == null) ? UUID.randomUUID().toString() : s.id();
        return new SchoolTest(id,s.name(),s.classId(),s.date(),s.topics(),s.scores());}
}
