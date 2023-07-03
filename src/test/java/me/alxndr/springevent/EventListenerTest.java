package me.alxndr.springevent;

import static org.assertj.core.api.Assertions.*;

import me.alxndr.springevent.application.PostFacade;
import me.alxndr.springevent.domain.Post;
import me.alxndr.springevent.infrastructure.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EventListenerTest {


	@Autowired
	private PostFacade postFacade;

	@Autowired
	private PostRepository postRepository;


	@DisplayName("Normal")
	@Test
	public void normalEventTest() {
		Post post = getPost("Normal");

		final Post newPost = postFacade.create(post);

		assertThat(newPost.getId()).isNotNull();
	}

	@DisplayName("V1 : @EventListener")
	@Test
	public void eventListenerV1() {
		Post post = getPost("v1");

		assertThatThrownBy(() -> postFacade.createV1(post))
				.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("V2 : @EventListener + @Async")
	@Test
	public void eventListenerV2() {
		Post post = getPost("v1");

		final Post newPost = postFacade.createV2(post);

		assertThat(newPost.getId()).isNotNull();
	}


	@DisplayName("V2_Exception : @EventListener + @Async + Exception")
	@Test
	public void eventListenerV2Exception() {
		Post post = getPost("v1");

		assertThatThrownBy(() -> postFacade.createV1(post))
				.isInstanceOf(RuntimeException.class);
	}


	@DisplayName("V3 : @TransactionalEventListener")
	@Test
	public void eventListenerV3() {
		Post post = getPost("v1");

		final Post newPost = postFacade.createV3(post);

		assertThat(newPost.getId()).isNotNull();
	}

	@DisplayName("V4 : @TransactionalEventListener + @Transactional")
	@Test
	public void eventListenerV4() {
		Post post = getPost("v1");

		final Post newPost = postFacade.createV4(post);


		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		assertThat(newPost.getId()).isNotNull();
	}




	private static Post getPost(final String v1) {
		return new Post(null, v1, v1);
	}


}
