package authJwt

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.LambdaLogger //TODO Use log4j 2 in the future: https://docs.aws.amazon.com/pt_br/lambda/latest/dg/java-logging.html
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table, Item};
import java.security.MessageDigest
import scala.collection.JavaConverters
import scala.collection.JavaConverters._
import jwt._
import scala.util.{Try,Success,Failure}
import io.circe._, io.circe.parser._, io.circe.optics.JsonPath._
import ApiGatewayProxyResponse._



class HelloWorld  extends  ApiGatewayProxyHandler  {

  def handleRequest(input: ApiGatewayProxyRequest, context: Context): ApiGatewayProxyResponse = {
    println(input)
    okResponse("HelloWorld!")
  }
}

class LoginHandler extends ApiGatewayProxyHandler {
  val client = AmazonDynamoDBClientBuilder.standard().build();
  val dynamoDB = new DynamoDB(client);
  val table = dynamoDB.getTable("Users");
  val salt = "8693345"
  

  def handleRequest(input: ApiGatewayProxyRequest, context: Context): ApiGatewayProxyResponse = {
    val logger = context.getLogger();
    parse(input.body) match {
      case Left(failure) => {
        logger.log(input.body)
        errorResponse(400,1,"Invalid json data.")
      } 
      case Right(json) => tryTokenResponse(root.email.string.getOption(json),root.password.string.getOption(json))
    }
  }

  def tryTokenResponse(email: Option[String], password:Option[String]): ApiGatewayProxyResponse = {
    (email,password) match {
      case (Some(e),Some(p)) => getStoredUser(e) match {
                                  case Some(retrivedUser) => validatePassword(e,p,retrivedUser)
                                  case None => errorResponse(403,101,"Invalid login or password.")
                                }
      case (None,Some(_)) => errorResponse(400,2,"The email field is obrigatory.")
      case (Some(_),None) => errorResponse(400,3,"The password field is obrigatory.")
      case (None,None) => errorResponse(400,4,"The email and password fields are obrigatory.")
    }
    
  }

  def getStoredUser(email: String): Option[User] = {
    val item = Option(table.getItem("email", email)) //get item at dynamo
    item match {
      case Some(i) => Some(User(email,i.getString("password").toLowerCase,i.getList[String]("scopes").asScala))
      case None => None
    }
  }

  def validatePassword(email: String, password: String, retrivedUser: User) = {
    val hashedInputPassword = MessageDigest.getInstance("SHA-256").digest((password + salt).getBytes("UTF-8")).map("%02x".format(_)).mkString

    if(retrivedUser.password == hashedInputPassword) {
      val token = JwtWrapper.createToken(email,retrivedUser.scopes).get
      okResponse(s"""{token:"$token"}""""")
    }
    else {
      errorResponse(403,101,"Invalid login or password.")
    }
    
  }

}
