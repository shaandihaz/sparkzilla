package sparkzilla.sol

/**
  * A text element of an HTML page
  *
  * @param text - The text
  */
case class PageText(val text: String) extends HTMLElement {
  
  /**
   * method to print PageText
   * 
   * @param count - an int it passes along
   */
   def render(count:Int): Unit = {
    System.out.println(text)
  }
}
