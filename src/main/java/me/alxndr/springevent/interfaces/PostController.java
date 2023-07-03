package me.alxndr.springevent.interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import me.alxndr.springevent.application.PostFacade;
import me.alxndr.springevent.domain.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {


	private final PostFacade postFacade;


	@GetMapping({"/create/{version}", "/create"})
	public ResponseEntity createV1(@PathVariable(required = false) String version) {

		final Post newPost = Post.builder()
				.title(version)
				.content(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
				.build();

		if (version == null) {
			postFacade.create(newPost);
		} else if (version.equals("v1")) {
			postFacade.createV1(newPost);
		} else if (version.equals("v2")) {
			postFacade.createV2(newPost);
		} else if (version.equals("v2-exception")) {
			postFacade.createV2Exception(newPost);
		} else if (version.equals("v3")) {
			postFacade.createV3(newPost);
		} else if (version.equals("v4")) {
			postFacade.createV4(newPost);
		}

		return ResponseEntity.ok(null);
	}

}
