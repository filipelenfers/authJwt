package jwt

import scala.util.{Try,Success,Failure}
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.exceptions.{JWTCreationException,JWTVerificationException}
import java.util.Date
import java.time.LocalDateTime
import java.time.ZoneId
import collection.JavaConverters._


object JwtWrapper {
    val algorithm = Algorithm.HMAC256(System.getenv("secret"));
    val issuer = System.getenv("issuer")
    val expirationInMinutes = System.getenv("expirationInMinutes").toInt
    val nbSeconds = System.getenv("nbSeconds").toInt
    val audience = System.getenv("audience").split(",").toSeq

    def createToken(userId:String = null, scopes: Seq[String] = Seq()):Try[String] = {
        try {
            val token = JWT.create()
                .withIssuer(issuer)
                .withSubject(userId)
                .withAudience(audience:_*)
                .withIssuedAt(Date.from((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant)))
                .withExpiresAt(Date.from((LocalDateTime.now().plusMinutes(expirationInMinutes).atZone(ZoneId.systemDefault()).toInstant)) )
                .withNotBefore(Date.from((LocalDateTime.now().minusSeconds(nbSeconds).atZone(ZoneId.systemDefault()).toInstant)) )
                .withArrayClaim("scopes",scopes.toArray)
                .sign(algorithm);
            Success(token)
        } catch {
            case ex: JWTCreationException => Failure(ex)
            case ex:Throwable => Failure(ex)
        }

    }

    def verifyToken(token:String):Try[DecodedJWT] = {
        try {
            val verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                //.withSubject(userId)
                .withAudience(audience:_*)
                .build(); //TODO Reusable verifier instance, move up
            val jwt = verifier.verify(token);
            Success(jwt)
        } catch {
            case ex: JWTVerificationException => Failure(ex)
            case ex:Throwable => Failure(ex)
        }
    }
}