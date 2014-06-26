package controllers

import play.api._
import play.api.mvc._

import models.Post

object Posts extends Controller {

  def index = Action {
    val posts = Post.all
    Ok(views.html.posts.index(posts))
  }

}
