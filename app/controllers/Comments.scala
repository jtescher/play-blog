package controllers

import controllers.Posts._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import models.{Post, Comment}

object Comments extends Controller {

  val commentForm = Form(mapping(
    "id" -> ignored(-1L),
    "body" -> nonEmptyText,
    "post_id" -> longNumber
  )(Comment.apply)(Comment.unapply))

  def create(postId: Long) = Action { implicit request =>
    Post.find(postId) match {
      case Some(post) => {
        val comments = Comment.allByPostId(post.id)
        commentForm.bindFromRequest.fold(
          formWithErrors => Ok(views.html.posts.show(post, comments, formWithErrors)),
          comment => {
            Comment.create(comment)
            Redirect(routes.Posts.show(post.id)).flashing("success" -> "Comment was successfully created.")
          }
        )
      }
      case None => NotFound
    }
  }

}
