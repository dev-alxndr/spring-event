package me.alxndr.springevent.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.alxndr.springevent.domain.Post;
import me.alxndr.springevent.domain.PostService;
import me.alxndr.springevent.domain.events.PostCreateEvent;
import me.alxndr.springevent.domain.events.PostCreateEventV1;
import me.alxndr.springevent.domain.events.PostCreateEventV2;
import me.alxndr.springevent.domain.events.PostCreateEventV2Exception;
import me.alxndr.springevent.domain.events.PostCreateEventV3;
import me.alxndr.springevent.domain.events.PostCreateEventV4;
import me.alxndr.springevent.domain.events.PostCreateEventV5;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostFacade {

	private final PostService postService;
	private final ApplicationEventPublisher eventPublisher;



	@Transactional // Transaction을 묶어야 하기 떄문에 예제로 Facade에서 Transaction 시작
	public Post create(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEvent(newPost));
		return newPost;
	}


	@Transactional // Transaction을 묶어야 하기 떄문에 예제로 Facade에서 Transaction 시작
	public Post createV1(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV1(newPost));
		return newPost;
	}

	@Transactional
	public Post createV2(Post post) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV2(newPost));

		// 비동기 테스트를 위해 sleep
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		stopWatch.stop();
		log.info("Event Publisher {} : {}s", Thread.currentThread().getName(), stopWatch.getTotalTimeSeconds());

		return newPost;
	}

	@Transactional
	public void createV2Exception(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV2Exception(newPost));
		log.info("Event Publisher {}", Thread.currentThread().getName());
	}

	@Transactional
	public Post createV3(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV3(newPost));
		return newPost;
	}


	@Transactional
	public Post createV4(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV4(newPost));

		return newPost;
	}


	@Transactional
	public Post createV5(Post post) {
		final Post newPost = postService.create(post);

		eventPublisher.publishEvent(new PostCreateEventV5(newPost));
		log.info("V5 Facade Thread Name : {}", Thread.currentThread().getName());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		return newPost;
	}

}
