package sparkzilla.sol


/**
  * A paragraph element of an HTML page
  *
  * @param elements - The HTML elements of the paragraph
  */
case class Paragraph(var elements: List[HTMLElement]) extends HTMLElement {
  
  /**
   * method to print the elements of a paragraph
   * 
   * @param count - the int it passes to its elements
   * 
   * @returns an int representing its place in the element line
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
