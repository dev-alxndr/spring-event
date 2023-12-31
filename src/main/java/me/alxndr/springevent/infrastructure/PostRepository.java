package me.alxndr.springevent.infrastructure;

import me.alxndr.springevent.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

	Post findByTitle(String title);
}
