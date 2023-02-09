package sparkzilla.sol

import java.net.URLEncoder
import scala.util.matching.Regex

/**
  * A form element of an HTMLPage
  *
  * @param url - The URL to send the form data
  * @param elements - The HTML elements contained in the form
  */
case class Form(val url: String, var elements: List[HTMLElement]) extends HTMLElement {
  /**
   * method that sets elements field
   * 
   * @param elms - the list of html elements to be set
   */
  def setElements(elms: List[HTMLElement]) {
    elements = elms
  }
  
  /**
   * method to render the elements in a form
   * 
   * @param count - an int representing its place in the renderPage method
   */
   def render(count: Int): Int = {
    val browser = new Browser()
    browser.renderPage(elements, count)
  }
   
   /**
    * method to help find the last button in a webpage
    * 
    * @param count - the current int it is on
    */
   def lastButtonHelper(count: Int): Int = {
     val browser = new Browser()
     browser.lastButton(elements, count)
   }

  
  
}
