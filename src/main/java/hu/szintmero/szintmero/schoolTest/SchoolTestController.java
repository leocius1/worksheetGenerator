package hu.szintmero.szintmero.schoolTest;

import hu.szintmero.szintmero.model.SchoolTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public SchoolTest create(@RequestBody SchoolTest t) {
        SchoolTest test = SchoolTest.createNew(t);
        return repository.save(test);
    }

    @GetMapping("/{id}")
    public SchoolTest get(@PathVariable String id) {
        return repository.findById(id).orElseThrow(); //Im not sure what to throw here
    }

    @GetMapping
    public List<SchoolTest> getByClass(@RequestParam String classId) {
        return repository.findByClassId(classId);
    }

}
