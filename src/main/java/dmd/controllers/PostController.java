package dmd.controllers;

import dmd.config.db.DBContextHolder;
import dmd.config.db.DBTypeEnum;
import dmd.dal.PostRepository;
import dmd.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("post")
@SuppressWarnings("unused")
public class PostController {
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Post> getPost(@RequestParam(name = "id") UUID id) {
        setContext(id);
        Post byExtId = postRepository.findByExtId(id.toString());
        if (byExtId == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(byExtId);
    }

    private void setContext(UUID id) {
        int dbId = Math.abs(id.hashCode()) % 3;

        switch (dbId) {
            case 0 -> DBContextHolder.setCurrentDb(DBTypeEnum.FIRST);
            case 1 -> DBContextHolder.setCurrentDb(DBTypeEnum.SECOND);
            case 2 -> DBContextHolder.setCurrentDb(DBTypeEnum.THIRD);
        }
    }

    @PostMapping()
    public ResponseEntity<Post> addPost(@RequestParam(name = "name") String name) {
        UUID uuid = UUID.randomUUID();
        // check for uuid collision

        setContext(uuid);

        Post post = new Post();
        post.setExtId(uuid.toString());
        post.setName(name);

        Post save = postRepository.save(post);

        return ResponseEntity.ok(save);
    }
}
