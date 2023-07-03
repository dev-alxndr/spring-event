package me.alxndr.springevent.domain;

import lombok.RequiredArgsConstructor;
import me.alxndr.springevent.infrastructure.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	@Transactional
	public Post create(final Post post) {
		return postRepository.save(post);
	}

}
