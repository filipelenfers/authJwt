package authJwt

import scala.beans.BeanProperty
import com.amazonaws.services.lambda.runtime.Context;
import java.util.Map;
import java.util.HashMap;

class AuthorizerRequest(@BeanProperty var `type`: String, @BeanProperty var authorizationToken: String, @BeanProperty var methodArn: String) {
  def this() = this("", "","")
}
