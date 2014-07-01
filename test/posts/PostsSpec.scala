import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.{Post, Comment}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PostsSpec extends Specification {

  "Posts" should {

    "render the index page" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val index = route(FakeRequest(GET, "/posts")).get

      status(index) must equalTo(OK)
      contentType(index) must beSome.which(_ == "text/html")
      contentAsString(index) must contain (post.body)
    }

    "render the new page" in new WithApplication{
      val newPost = route(FakeRequest(GET, "/posts/new")).get

      status(newPost) must equalTo(OK)
      contentType(newPost) must beSome.which(_ == "text/html")
      contentAsString(newPost) must contain ("New Post")
    }

    "create new posts" in new WithApplication{
      val title = "Cool Title"
      val body = "Cool Body"
      val Some(create) = route(FakeRequest(POST, "/posts").withFormUrlEncodedBody("title" -> title, "body" -> body))

      status(create) must equalTo(SEE_OTHER)
      redirectLocation(create) must beSome.which(_ == "/posts/1")
      Post.first must beSome.which(post => post.body == body)
    }

    "show errors creating invalid posts" in new WithApplication{
      val Some(create) = route(FakeRequest(POST, "/posts").withFormUrlEncodedBody("title" -> "Valid Title", "body" -> ""))

      status(create) must equalTo(OK)
      contentType(create) must beSome.which(_ == "text/html")
      contentAsString(create) must contain ("This field is required")
    }

    "render the show page" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val comment = Comment.create(new Comment(1, "Cool comment body", 1))
      val index = route(FakeRequest(GET, "/posts/1")).get

      status(index) must equalTo(OK)
      contentType(index) must beSome.which(_ == "text/html")
      contentAsString(index) must contain (post.body)
      contentAsString(index) must contain (comment.body)
    }

    "render the edit page" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val edit = route(FakeRequest(GET, "/posts/1/edit")).get

      status(edit) must equalTo(OK)
      contentType(edit) must beSome.which(_ == "text/html")
      contentAsString(edit) must contain (post.body)
    }

    "update existing posts" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val changedBody = "Changed body text"
      val Some(edit) = route(FakeRequest(PUT, "/posts/1").withFormUrlEncodedBody("title" -> post.title, "body" -> changedBody))

      status(edit) must equalTo(SEE_OTHER)
      redirectLocation(edit) must beSome.which(_ == "/posts/1")
      Post.first must beSome.which(post => post.body == changedBody)
    }

    "show errors updating invalid posts" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val changedBody = ""
      val Some(edit) = route(FakeRequest(PUT, "/posts/1").withFormUrlEncodedBody("title" -> post.title, "body" -> changedBody))

      status(edit) must equalTo(OK)
      contentType(edit) must beSome.which(_ == "text/html")
      contentAsString(edit) must contain ("This field is required")
    }

    "delete existing posts" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val Some(delete) = route(FakeRequest(DELETE, "/posts/1"))

      status(delete) must equalTo(SEE_OTHER)
      redirectLocation(delete) must beSome.which(_ == "/posts")
      Post.first must beNone
    }

  }

}
