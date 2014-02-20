import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "TEQ-Tikal"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13",
    "org.springframework" % "spring-core" % "4.0.0.RELEASE",
   // "org.springframework" % "spring-asm" % "4.0.0.RELEASE",
    "org.springframework" % "spring-context" % "4.0.0.RELEASE",
    "commons-lang" % "commons-lang" % "2.6",
    "org.springframework.data" % "spring-data-mongodb" % "1.3.3.RELEASE",
    "org.mongodb" % "mongo-java-driver" % "2.11.3",
    "cglib" % "cglib" % "3.1",
    "commons-io" % "commons-io" % "2.4",
    "dom4j" % "dom4j" % "1.6.1",
    "jaxen" % "jaxen" % "1.1.6",
    "javax.mail" % "mail" % "1.4.7"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
