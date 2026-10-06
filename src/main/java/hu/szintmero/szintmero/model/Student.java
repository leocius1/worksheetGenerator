package hu.szintmero.szintmero.model;

import java.util.List;

public record Student(String id, String classId, List<String> testIds, String name) {
}
