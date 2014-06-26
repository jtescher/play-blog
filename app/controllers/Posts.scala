package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import models.Post

object Posts extends Controller {

  def index = Action {
    val posts = Post.all
    Ok(views.html.posts.index(posts))
  }

  val postForm = Form(mapping(
    "id" -> ignored(-1L),
    "title" -> nonEmptyText,
    "body" -> nonEmptyText
  )(Post.apply)(Post.unapply))

  def newPost = Action {
    Ok(views.html.posts.newPost(postForm))
  }

  def create = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.posts.newPost(formWithErrors)),
      post => {
        Post.create(post)
        Redirect(routes.Posts.show(post.id))
      }
    )
  }

  def show(id: Long) = Action {
    val post = Post.find(id)
    Ok(views.html.posts.show(post))
  }

}
