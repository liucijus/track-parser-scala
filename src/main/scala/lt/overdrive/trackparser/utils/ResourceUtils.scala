package lt.overdrive.trackparser.utils

import java.io.File
import scala.util.{Failure, Success, Try}
import javax.xml.validation.{SchemaFactory, Schema}
import javax.xml.XMLConstants
import java.net.URL

object ResourceUtils {
  def getFile(name: String): Option[File] =
    try2Option(new File(Thread.currentThread.getContextClassLoader.getResource(name).getFile))

  def getResourceUrl(name: String): Option[URL] =
    try2Option(Thread.currentThread.getContextClassLoader.getResource(name))

  def try2Option[A](f: => A): Option[A] = Try(Option(f)) match {
    case Success(o) => o
    case Failure(_) => None
  }

  def loadSchema(name: String): Try[Schema] = {
    val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    Try(sf.newSchema(getResourceUrl(name).get))
  }
}

