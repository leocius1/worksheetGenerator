package hu.szintmero.szintmero.schoolTest;

import hu.szintmero.szintmero.model.SchoolTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tests")
public class SchoolTestController {
    private final SchoolTestRepository repository;

    public SchoolTestController(SchoolTestRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolTest create(@RequestBody SchoolTest st) {
        SchoolTest test = SchoolTest.createNew(st);
        return repository.save(test);
    }

    @GetMapping("/{id}")
    public SchoolTest get(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<SchoolTest> getByClass(@RequestParam String classId) {
        return repository.findByClassId(classId);
    }

}
