package models

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._

object Database extends Schema {
  val postsTable = table[Post]("posts")
  val commentsTable = table[Comment]("comments")

  on(postsTable) { p => declare {
    p.id is(autoIncremented)
  }}

  on(commentsTable) { c => declare {
    c.id is(autoIncremented)
  }}

}
