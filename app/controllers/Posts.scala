package controllers

import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import models.{Post, Comment}

object Posts extends Controller {

  import play.api.libs.json._
  implicit object PostWrites extends Writes[Post] {
    def writes(p: Post) = Json.obj(
      "id" -> Json.toJson(p.id),
      "title" -> Json.toJson(p.title),
      "body" -> Json.toJson(p.body)
    )
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

  def index = Action { implicit request =>
    val posts = Post.all
    render {
      case Accepts.Html() => Ok(views.html.posts.index(posts))
      case Accepts.Json() => Ok(Json.toJson(posts))
    }
  }

  def newPost = Action { implicit request =>
    Ok(views.html.posts.newPost(postForm))
  }

  def create = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.posts.newPost(formWithErrors)),
      post => {
        Post.create(post)
        render {
          case Accepts.Html() => Redirect(routes.Posts.show(post.id)).flashing("success" -> "Post was successfully created.")
          case Accepts.Json() => Ok(Json.toJson(post))
        }
      }
    )
  }

  def show(id: Long) = Action { implicit request =>
    Post.find(id) match {
      case Some(post) => {
        val comments = Comment.allByPostId(post.id)
        render {
          case Accepts.Html() => Ok(views.html.posts.show(post, comments, commentForm))
          case Accepts.Json() => Ok(Json.toJson(post))
        }
      }
      case None => NotFound
    }
  }

  def destroy(id: Long) = Action { implicit request =>
    Post.destroy(id)
    render {
      case Accepts.Html() => Redirect(routes.Posts.index()).flashing("success" -> "Post was successfully destroyed.")
      case Accepts.Json() => Ok
    }
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
            render {
              case Accepts.Html() => Redirect(routes.Posts.show(validPostWithId.id)).flashing("success" -> "Post was successfully update.")
              case Accepts.Json() => Ok
            }
          }
        )
      }
      case None => NotFound
    }
  }

}
