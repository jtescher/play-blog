package models

import org.squeryl.KeyedEntity

case class Post (id: Long, title: String, body: String) extends KeyedEntity[Long]

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Query
import collection.Iterable

object Post {
  import Database.postsTable

  def allQuery: Query[Post] = from(postsTable) {
    post => select(post)
  }

  def all: Iterable[Post] = inTransaction {
    allQuery.toList
  }

  def firstQuery: Query[Post] = from(postsTable) {
    post => select(post).orderBy(post.id asc)
  }

  def first: Option[Post] = inTransaction {
    try {
      Some(firstQuery.single)
    } catch {
      case _ : RuntimeException => None
    }
  }

  def create(post: Post): Post = inTransaction {
    postsTable.insert(post)
  }

  def find(id: Long): Option[Post] = inTransaction {
    try {
      Some(postsTable.where(post => post.id === id).single)
    } catch {
      case _ : RuntimeException => None
    }
  }

  def update(post: Post) = inTransaction {
    postsTable.update(post)
  }

  def destroy(id: Long) = inTransaction {
     postsTable.deleteWhere(post => post.id === id)
  }

}
