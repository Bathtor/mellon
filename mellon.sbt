enablePlugins(WorkbenchPlugin)
enablePlugins(ScalaJSPlugin)
enablePlugins(BuildInfoPlugin)

name := "Mellon"

organization := "com.lkroll.web"

version := "2.0.1"

scalaVersion := "2.12.10"

//resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases")
resolvers += Resolver.mavenLocal
resolvers += Resolver.bintrayRepo("lkrollcom", "maven")

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.+"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.+"
libraryDependencies += "com.outr" %%% "scribe" % "2.7.+"
libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.8.0"
//libraryDependencies += "xyz.ariwaranosai" %%% "scalajs-hashes" % "0.1.0"
//libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.8" % "test"

//jsDependencies += "org.webjars" % "three.js" % "r88" / "three.js" minified "three.min.js"
//jsDependencies += ProvidedJS / "CopyShader.js" dependsOn "three.js"
jsDependencies += "org.webjars.bower" % "jshashes" % "1.0.5" / "hashes.js" minified "hashes.min.js" commonJSName "Hashes"

scalaJSUseMainModuleInitializer := true
skip in packageJSDependencies := false
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "com.lkroll.web.mellon"

//bootSnippet := "com.larskroll.ep.mapviewer.Main().main();"
//refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile)
//localUrl := ("lkroll.sics.se", 12345)
//localUrl := ("192.168.0.102", 12345)
//localUrl := ("10.112.11.78", 12345)
localUrl := ("localhost", 12345)
