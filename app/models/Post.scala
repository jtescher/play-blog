package models

import org.squeryl.KeyedEntity

case class Post (id: Long, title: String, body: String) extends KeyedEntity[Long]


import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Table
import org.squeryl.Query
import collection.Iterable

object Post {
  import Database.{postsTable}

  def allQuery: Query[Post] = from(postsTable) {
    post => select(post)
  }

  def all: Iterable[Post] = inTransaction {
    allQuery.toList
  }

}
