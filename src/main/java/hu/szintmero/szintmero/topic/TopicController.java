package hu.szintmero.szintmero.topic;

import java.util.List;

import hu.szintmero.szintmero.model.Topic;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    private final TopicRepository repo;
    public TopicController(TopicRepository repo) { this.repo = repo; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topic create(@RequestBody Topic t) { return repo.save(t); }

    @GetMapping
    public List<Topic> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Topic get(@PathVariable String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
