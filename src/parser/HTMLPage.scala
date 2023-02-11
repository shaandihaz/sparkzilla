package sparkzilla.src.parser

import sparkzilla.sol.HTMLElement

// An abstract class corresponding to <htmlpage> in the grammar
abstract class HTMLPage {

  /**
    * Converts the parse tree into a list of HTML Element
    *
    * @return A list of HTML Elements
    */
  def toHTML: List[HTMLElement]

}

// The only case for <htmlpage>
class AHTMLPage(stuff: HTMLStuff) extends HTMLPage {

  override def toHTML: List[HTMLElement] = stuff.toHTML

}
