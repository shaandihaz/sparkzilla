package sparkzilla.sol

/**
  * A text input element of an HTML page
  *
  * @param name - Name of the text input for communicating with the server
  * @param value - Value of the text input as given by the user
  */
case class TextInput(val name: String, var value: Option[String]) extends HTMLElement {  
  /**
   * button - the int corresponding to the text input
   */
  var button: Option[Int] = None

  /**
   * method to print text input
   * 
   * @param count - the int corresponding to the text input
   */
   def render(counter: Int): Unit = {
    if (button == None) {
      button = Some(counter)
      System.out.println("_______[" + counter + "]")
    } else if (value == None) {
      System.out.println("_______[" + button.get + "]")
    } else {
     System.out.println("__" + value.get + "__ [" + button.get + "]") 
    }
  }
  
  /**
   * method to print the input where the "____" went in the text input
   * 
   * @param input - the string to print to the webpage
   */
  def whenClicked(input: String): Unit = {
    value = Some(input)
  }
}
