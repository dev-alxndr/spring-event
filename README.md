### Event를 발행해서 처리하고 싶어요.
GET http://localhost:8080/posts/create

### Event를 받고 싶어요 + Event를 처리하다가 Exception이 나면 발행한 로직도 Rollback하고 싶어요
GET http://localhost:8080/posts/create/v1

### v1 + Event를 비동기로 처리하고 싶어요 w/ @EnableAsync
GET http://localhost:8080/posts/create/v2

### v2 + Exception이 발생한다면 롤백이 될 것인가?
GET http://localhost:8080/posts/create/v2-exception   
이벤트를 처리하다가 에러가 나도 발행한 로직은 정상적으로 수행되길 원해요.

### Transaction을 분리하고 싶어요
GET http://localhost:8080/posts/create/v3   
=> Rollback 안됨

### v3 + 분리했는데 Event를 처리할 때 Transaction을 사용하고 싶어요
GET http://localhost:8080/posts/create/v4

### v4 + Async
GET http://localhost:8080/posts/create/v5   
Main 2초   
Event Listener 2초  
 => Async 이기 때문에 2초 xx ms
