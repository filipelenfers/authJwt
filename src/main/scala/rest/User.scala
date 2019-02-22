package authJwt

case class User(email: String, password: String, scopes: Seq[String])