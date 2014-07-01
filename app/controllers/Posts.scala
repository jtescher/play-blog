package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import models.{Post, Comment}

object Posts extends Controller {

  def index = Action { implicit request =>
    val posts = Post.all
    Ok(views.html.posts.index(posts))
  }

  val postForm = Form(mapping(
    "id" -> ignored(-1L),
    "title" -> nonEmptyText,
    "body" -> nonEmptyText
  )(Post.apply)(Post.unapply))

  val commentForm = Form(mapping(
    "id" -> ignored(-1L),
    "body" -> nonEmptyText,
    "post_id" -> longNumber
  )(Comment.apply)(Comment.unapply))

  def newPost = Action { implicit request =>
    Ok(views.html.posts.newPost(postForm))
  }

  def create = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.posts.newPost(formWithErrors)),
      post => {
        Post.create(post)
        Redirect(routes.Posts.show(post.id)).flashing("success" -> "Post was successfully created.")
      }
    )
  }

  def show(id: Long) = Action { implicit request =>
    Post.find(id) match {
      case Some(post) => {
        val comments = Comment.allByPostId(post.id)
        Ok(views.html.posts.show(post, comments, commentForm))
      }
      case None => NotFound
    }
  }

  def destroy(id: Long) = Action {
    Post.destroy(id)
    Redirect(routes.Posts.index()).flashing("success" -> "Post was successfully destroyed.")
  }

  def edit(id: Long) = Action { implicit request =>
    Post.find(id) match {
      case Some(post) => {
        val filledPostForm = postForm.fill(post)
        Ok(views.html.posts.edit(post, filledPostForm))
      }
      case None => NotFound
    }
  }

  def update(id: Long) = Action { implicit request =>
    Post.find(id) match {
      case Some(post) => {
        postForm.bindFromRequest.fold(
          formWithErrors => Ok(views.html.posts.edit(post, formWithErrors)),
          validPost => {
            val validPostWithId = validPost.copy(id = id)
            Post.update(validPostWithId)
            Redirect(routes.Posts.show(validPostWithId.id)).flashing("success" -> "Post was successfully update.")
          }
        )
      }
      case None => NotFound
    }
  }

}
