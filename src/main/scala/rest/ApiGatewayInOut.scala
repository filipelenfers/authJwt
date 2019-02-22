package authJwt

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import java.util.HashMap

case class ApiGatewayProxyResponse(
  @BeanProperty statusCode: Int,
  @BeanProperty body: String,
  @BeanProperty headers: java.util.Map[String,String] = Map().asJava,
  @BeanProperty isBase64Encoded: Boolean = false
)

object ApiGatewayProxyResponse {
  def errorResponse(httpStatusCode: Int, errorCode: Int, message: String)  = ApiGatewayProxyResponse(httpStatusCode,s"""{errorCode:"$errorCode",message="$message"}""")

  def okResponse(message: String) = ApiGatewayProxyResponse(200,message)
  
}

case class ApiGatewayProxyRequest (
    @BeanProperty var resource : String,
    @BeanProperty var path : String,
    @BeanProperty var httpMethod : String,
    @BeanProperty var headers : java.util.Map[String, String],
    @BeanProperty var queryStringParameters : java.util.Map[String, String],
    @BeanProperty var pathParameters : java.util.Map[String, String],
    @BeanProperty var stageVariables : java.util.Map[String, String],
    @BeanProperty var context : Context,
    @BeanProperty var body : String,
    @BeanProperty var isBase64Encoded : Boolean
) {
    def this() = this (
    "",
    "",
    "",
    new HashMap[String, String](),
    new HashMap[String, String](),
    new HashMap[String, String](),
    new HashMap[String, String](),
    null,
    "",
    false
  )
}

trait ApiGatewayProxyHandler extends  RequestHandler[ApiGatewayProxyRequest, ApiGatewayProxyResponse] 