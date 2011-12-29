import com.typesafe.sbtscalariform._

scalaVersion := "2.9.1"

seq(webSettings :_*)

seq(ScalariformPlugin.settings: _*)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.0.1",
  "org.eclipse.jetty" % "jetty-webapp" % "7.4.5.v20110725" % "container, compile",
  "org.eclipse.jetty" % "jetty-server" % "7.4.5.v20110725" % "container, compile",
  "javax.servlet" % "servlet-api" % "2.5" % "provided"
)
