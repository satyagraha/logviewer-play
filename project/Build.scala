import sbt._
import Keys._
import play.Project._
import scala.xml.XML

object ApplicationBuild extends Build {

  val appName         = "logviewer-play"
  val appVersion      = "0.0.4-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    
    "org.logviewer" % "logviewer-common" % "0.0.4-SNAPSHOT" changing()
  )

  val localMavenRepo: Option[File] = getLocalMavenRepo
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers ++= localMavenRepo.map("Local Maven Repository" at _.toURI.toString).toList
  )

  def getLocalMavenRepo: Option[File] = {
    val defaultMavenPath = Path.userHome.getAbsoluteFile / ".m2"
    val defaultMavenRepo = defaultMavenPath / "repository"
    if (defaultMavenRepo.isDirectory) {
//        println("found std repo")
        Some(defaultMavenRepo)
    }
    else {
//        println("no std repo")
        val defaultMavenSettings = defaultMavenPath / "settings.xml"
        if (defaultMavenSettings.isFile()) {
//            println("found settings")
            val settingsXml = XML.loadFile(defaultMavenSettings)
            val settingsLocalRepoElem = settingsXml \\ "settings" \ "localRepository"
            if (!settingsLocalRepoElem.isEmpty)
                Some(Path(settingsLocalRepoElem.text).asFile.getAbsoluteFile)
            else
                None
        } else {
            None
        }
    }
        
  }
  
}
