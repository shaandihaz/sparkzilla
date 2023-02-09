package sparkzilla.sol

import java.net.URLEncoder
import scala.util.matching.Regex

/**
  * A submit button for a form
  *
  * @param form - The form that contains this submit button
  */
case class SubmitInput(form: Form) extends HTMLElement {
  /**
   * button - the int corresponding to the submit input
   */
  var button: Option[Int] = None
  
  /**
   * method to print out a submit button
   * 
   * @param coutner - the int corresponding to the submit input
   */
   def render(counter: Int): Unit = {
    if (button == None) {
      button = Some(counter)
      System.out.println("[Submit][" + counter + "]")
    } else {
      System.out.println("[Submit][" + button.get + "]")
    }
  }
  
  /**
   * method to create the form data from its elements
   * 
   * @param hList - the list of elements its grabbing the form data from
   * 
   * @return a string representing the form data 
   */
  private def createFormData(hList: List[HTMLElement]): String = hList match {
    case Nil => ""
    case (x: TextInput)::tail => x.name + "=" + URLEncoder.encode(x.value.getOrElse(""), "UTF-8") + "&" + createFormData(tail)
    case _::tail => createFormData(tail)
  }
  
  /**
   * method to make send a POST Request to a server 
   * @return tuple4, where:
   * 1 - the host name as an Option[String]
   * 2 - the url as a string
   * 3 - the form data as a string
   * 4 - the length of the form data as an int 
   */
  def whenClicked(): (Option[String], String, String, Int) = {
    val regex = new Regex("""//\w+/""")
    val host = regex.findFirstMatchIn(form.url)
    val data = createFormData(form.elements)
    if (host == None) {
      return (None, form.url, data, data.length())
    } else {
      val path = form.url.split("//")
      return (Some(host.get.matched.filter(x => x != '/')), "/" + path.dropWhile(x => x != '/'), data, data.length())
  }
  }
}