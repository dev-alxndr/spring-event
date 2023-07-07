package me.alxndr.springevent.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.alxndr.springevent.domain.events.Event;
import me.alxndr.springevent.domain.events.PostCreateEvent;
import me.alxndr.springevent.domain.events.PostCreateEventV1;
import me.alxndr.springevent.domain.events.PostCreateEventV2;
import me.alxndr.springevent.domain.events.PostCreateEventV2Exception;
import me.alxndr.springevent.domain.events.PostCreateEventV3;
import me.alxndr.springevent.domain.events.PostCreateEventV4;
import me.alxndr.springevent.domain.events.PostCreateEventV5;
import me.alxndr.springevent.infrastructure.PostRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener {

	private final PostService postService;

	private final PostRepository postRepository;

	/**
	 * Event를 발행해서 처리하고 싶어요.
	 */
	@EventListener
	public void createPostEventListener(PostCreateEvent event) {
		log.info("Event Listen");
		final Post post = postRepository.findById(event.getPost().getId()).get();
		log.info("Post = {}", post);
	}

	/**
	 * Event를 받고 싶어요 + Event를 처리하다가 Exception이 나면 발행한 로직도 Rollback하고 싶어요
	 */
	@EventListener
	public void createPostEventListenerV1(PostCreateEventV1 eventV1) {
		log.info("V1 Event Listen");
		log.error("ERROR 발생. Event 발행한 곳도 Rollback됩니다.");
		throw new RuntimeException("Exception");
	}

	/**
	 * v1 + Event를 비동기로 처리하고 싶어요
	 * w/ @EnableAsync
	 */
	@Async
	@EventListener
	public void createPostEventListenerV2(PostCreateEventV2 eventV2) {
		log.info("V2 Event Listen");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		log.info("EventListener 2초가 걸렸습니다. : {}", Thread.currentThread().getName());
	}

	/**
	 * v2 + 비동기 처리 중 Exception이 발생하면???
	 * => Rollback 안됨
	 */
	@Async
	@EventListener
	public void createPostEventListenerV2Exception(PostCreateEventV2Exception eventV2) {
		log.info("V2 Exception Event Listen");
		throw new RuntimeException("V2 Exception");
	}

	/**
	 * Transaction을 분리하고 싶어요
	 * 이벤트를 처리하다가 에러가 나도 발행한 로직은 정상적으로 수행되길 원해요.
	 */
	@TransactionalEventListener
	public void createPostEventListenerV3(PostCreateEventV3 event) {
		log.info("V3 Event Listen");
		log.error("ERROR 발생. Event 발행한 곳은 정상적으로 처리됩니다.");
		throw new RuntimeException("Exception");
	}


	/**
	 * v3 + 분리했는데 Event를 처리할 때 Transaction을 사용하고 싶어요
	 */
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createPostEventListenerV4(PostCreateEventV4 event) {
		log.info("V4 Event Listen");
		final Post fromEvent = Post.builder()
				.content("From Event Listener")
				.title("From Event Listener")
				.build();

		postService.create(fromEvent);
	}

	/**
	 * v4 + Async
	 */
	@Async
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createPostEventListenerV5(PostCreateEventV5 event) {
		log.info("V5 Event Listen");
		log.info("V5 Thread Name : {}", Thread.currentThread().getName());
		final Post fromEvent = Post.builder()
				.content("From Event Listener v5")
				.title("From Event Listener v5")
				.build();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		postService.create(fromEvent);
	}

}
