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
class CommentsSpec extends Specification {

  "Comments" should {

    "create new comments" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val body = "Cool comment body"
      val Some(create) = route(FakeRequest(POST, "/posts/1/comments").withFormUrlEncodedBody("body" -> body, "post_id" -> post.id.toString))

      status(create) must equalTo(SEE_OTHER)
      redirectLocation(create) must beSome.which(_ == "/posts/1")
      Comment.first must beSome.which(comment => comment.body == body)
    }

    "show errors creating new comments" in new WithApplication{
      val post = Post.create(new Post(1, "Cool title", "Cool body"))
      val Some(create) = route(FakeRequest(POST, "/posts/1/comments").withFormUrlEncodedBody("body" -> "", "post_id" -> post.id.toString))

      status(create) must equalTo(OK)
      contentType(create) must beSome.which(_ == "text/html")
      contentAsString(create) must contain ("This field is required")
    }

  }

}
