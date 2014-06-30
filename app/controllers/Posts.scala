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
    val post = Post.find(id)
    val comments = Comment.allByPostId(post.id)
    Ok(views.html.posts.show(post, comments, commentForm))
  }

  def destroy(id: Long) = Action {
    Post.destroy(id)
    Redirect(routes.Posts.index()).flashing("success" -> "Post was successfully destroyed.")
  }

  def edit(id: Long) = Action { implicit request =>
    val post = Post.find(id)
    val filledPostForm = postForm.fill(post)
    Ok(views.html.posts.edit(post, filledPostForm))
  }

  def update(id: Long) = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.posts.edit(Post.find(id), formWithErrors)),
      post => {
        val postWithId = post.copy(id = id)
        Post.update(postWithId)
        Redirect(routes.Posts.show(postWithId.id)).flashing("success" -> "Post was successfully update.")
      }
    )

  }

}
