package hu.szintmero.szintmero.model;

import java.util.UUID;

public record Topic(String id, String name, String subject, String description) {


    public static Topic createNew(Topic t) {
        String id =(t.id() == null) ? UUID.randomUUID().toString() : t.id();
        return new Topic(id,t.name,t.subject,t.description);}
}

