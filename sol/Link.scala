package sparkzilla.sol

import scala.util.matching.Regex

/**
  * A link element of an HTML page
  *
  * @param href - The URL of the link
  * @param text - The text to be rendered
  */
case class Link(href: String, text: PageText) extends HTMLElement {
  /**
   * button - the int corresponding to the link
   */
  var button: Option[Int] = None
  
  /**
   * method to print a link
   * 
   * @param count - the int corresponding to the link
   */
   def render(counter: Int): Unit = {
    if (button == None) {
      button = Some(counter)
      System.out.println(text.text + "[" + counter  + "]")
    } else {
      System.out.println(text.text + "[" + button.get + "]")
    }
  }
  
  /**
   * method that submits a GET request when a link is "clicled"
   * 
   * @return a tuple where the firls element as an option[String] 
   * representing the host, and the second is the url
   */
  def whenClicked(): (Option[String], String) = {
    val regex = new Regex("""//\w+/""")
    val host = regex.findFirstMatchIn(href)
    if (host == None) {
      return (None, href)
    } else {
      val path = href.split("//")
      return (Some(host.get.matched.filter(x => x != '/')), "/" + path.dropWhile(x => x != '/'))
    }
  }
  
}
