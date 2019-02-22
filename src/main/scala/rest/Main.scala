package hello
import jwt._

object App {
    def main(args: Array[String]): Unit = {
        println(JwtWrapper.verifyToken(
        """eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaWxpcGVsZW5mZXJzQGdtYWlsLmNvbSIsImF1ZCI6ImFsbCIsIm5iZiI6MTUzOTQ0MjMwNiwiaXNzIjoibGVuZmVyc2xhYiIsImV4cCI6MTUzOTQ0NTkzNiwiaWF0IjoxNTM5NDQyMzM2fQ.ewYUDlTMyH7AMKR-r6wrz7haQwTdETjS0gHCv0Yrnvk"""
        ).get.getSubject)
    }
}