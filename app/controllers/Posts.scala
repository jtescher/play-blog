package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import models.Post

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
    Ok(views.html.posts.show(post))
  }

  def destroy(id: Long) = Action {
    Post.destroy(id)
    Redirect(routes.Posts.index()).flashing("success" -> "Post was successfully destroyed.")
  }

}
