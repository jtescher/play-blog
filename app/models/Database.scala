package models

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._

object Database extends Schema {
  val postsTable = table[Post]("posts")

  on(postsTable) { p => declare {
    p.id is(autoIncremented)
  }}

}
