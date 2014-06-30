package models

import models.Database._
import org.squeryl.KeyedEntity

case class Comment (id: Long, body: String, post_id: Long) extends KeyedEntity[Long]

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Query
import collection.Iterable

object Comment {
  import Database.commentsTable

  def allByIdQuery(postId: Long): Query[Comment] = from(commentsTable) {
    comment => where(comment.post_id === postId).select(comment)
  }

  def allByPostId(postId: Long): Iterable[Comment] = inTransaction {
    allByIdQuery(postId).toList
  }

  def create(comment: Comment): Comment = inTransaction {
    commentsTable.insert(comment)
  }

}
