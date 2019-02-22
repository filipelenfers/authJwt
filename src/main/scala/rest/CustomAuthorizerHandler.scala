package authJwt

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.LambdaLogger //TODO Use log4j 2 in the future: https://docs.aws.amazon.com/pt_br/lambda/latest/dg/java-logging.html
import scala.collection.JavaConverters
import scala.collection.JavaConverters._
import jwt._
import scala.util.{Try,Success,Failure}

class CustomAuthorizerHandler extends  RequestHandler[AuthorizerRequest, java.util.Map[String,Object]] {

  def handleRequest(input: AuthorizerRequest, context: Context): java.util.Map[String,Object] = {
    
    val policy = JwtWrapper.verifyToken(input.authorizationToken) match {
      case Success(j) => definePolicy(input.methodArn,j.getClaim("scopes").asList(classOf[String]).asScala)
      case Failure(e) => /*println("Invalid Token: " + e)*/;generateDenialPolicy(input.methodArn)
    }
    policy
  }

  def definePolicy(resource: String,scopes:Seq[String]) = {
    //println("Scopes: " + scopes)
    val api = resource.split(":")(5).split("/")
    //println("API: " + api)
    val httpMethod = api(2)
    //println("httpMethod: " + httpMethod)
    val path = api.drop(3).mkString("/")
    //println("path: " + path)
    if(validateScope(httpMethod,path,scopes)) {
      //println("Scope válido")
      generateAllowPolicy(resource)//generatePolicy(j.getSubject(),"Allow",input.methodArn) 
    }
    else {
      //println("Scope inválido")
      generateDenialPolicy(resource)
    }
  }

  def validateScope(httpMethod:String,path:String,scopes:Seq[String]) = {
    val neededScope = path + ":" + httpMethodToPermission(httpMethod)
    scopes.contains(neededScope)
  }

  def httpMethodToPermission(httpMethod:String) = {
    httpMethod match {
      case "GET" => "read" //TODO other methods
      case "POST" => "write"
    }
  }

  val generateAllowPolicy = generatePolicy("PolicyFixedPrincipal","Allow", _:String)
  val generateDenialPolicy = generatePolicy("PolicyFixedPrincipal","Deny", _:String)

  def generatePolicy(principalId:String, effect:String, resource:String): java.util.Map[String, Object]  = {
      Map(
        "principalId" -> principalId,
        "policyDocument" -> Map(
          "Version" -> "2012-10-17",
          "Statement" -> Map(
            "Action" -> "execute-api:Invoke",
            "Effect" -> effect,
            "Resource" -> resource
          ).asJava
        ).asJava
      ).asJava     
	}
}