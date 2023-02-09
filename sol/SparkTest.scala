package sparkzilla.sol

import tester.Tester

class SparkTest {

  /**
   * tests the renderPage method, also implicilty tests all of the 
   * render methods in the classes that extend HTMLElement
   */
  def testRenderPage(t:Tester) {
    val browser = new Browser()
    val hList = List(new Paragraph(List(new PageText("Oh Hi Mark"), 
                                        new Link("http://thufir/Index", new PageText( "sparkzilla")))))
    t.checkExpect(browser.renderPage(hList, 4), 5)                                    
  }
  
  /**
   * tests the lastButton methos, also tests the lastbuttonHelper methods 
   * in the classes that extend HTMLElements
   */
  def testLastButton(t: Tester) {
    val browser = new Browser()
    val hList = List(new Paragraph(List(new PageText("Oh Hi Mark"), 
                                        new Link("http://thufir/Index", new PageText( "sparkzilla")))))
    t.checkExpect(browser.lastButton(hList, 3), 4)                                     
  } 
}

 
object SparkTest extends App {
  Tester.run(new SparkTest())
}