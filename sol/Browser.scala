package sparkzilla.sol

import java.io.BufferedReader

import sparkzilla.src.parser.HTMLParser
import sparkzilla.src.HTMLTokenizer
import sparkzilla.src.parser.ParseException
import scala.io.StdIn
import scala.util.matching.Regex
import java.io.IOException
import java.net.Socket
import java.net.ServerSocket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import scala.collection.mutable.ListBuffer

/**
  * A text-based browser class
  */
class Browser() {
  
  /**
   * actions - a string that is the initial actions a user can make
   */
  private val actions = "Actions: [1] Back, [2] Go to URL, [3] Quit"
  
  /**
   * hostname - the current host of the browser
   */
  private var hostname: Option[String] = None
  
  /**
   * cache - the list of past pages
   */
  private var cache = List[List[HTMLElement]]()

  /** Parses the input from the server into a list of HTMLElements.
    * @param inputFromServer- BufferedReader containing HTML from server
    * @returns hierarchical list of the HTMLElements. See the documentation
    * 	and view the sol code for the specific composition of each HTMLElement
    *   within the list.
    */
  private def getHTMLElementList(inputFromServer: BufferedReader): List[HTMLElement] = {
    val parser = new HTMLParser(new HTMLTokenizer(inputFromServer))
    return parser.parse().toHTML
  }
  
  /**
   * a method to print out the elements of a webpage
   * 
   * @param hList - a List[HTMLElement] that contains all of the elements of 
   * the webpage that needs to be printed
   * 
   * @param counter - an int that keeps track of the number beside an active element
   * 
   * @return the int that was the last counter
   */
   def renderPage(hList: List[HTMLElement], counter: Int): Int = hList match {
   case Nil => counter
   case (x: Link)::tail => 
     x.render(counter)
     renderPage(tail, counter+1)
   case (x: SubmitInput)::tail => 
     x.render(counter)
     renderPage(tail, counter+1)
   case (x: TextInput)::tail =>
     x.render(counter)
     renderPage(tail, counter+1)
   case (x: Paragraph)::tail => 
     val count = x.render(counter)
     renderPage(tail, count)
   case (x: PageText)::tail =>
     x.render(counter)
     renderPage(tail, counter)
   case (x: Form)::tail =>
     val count = x.render(counter)
     renderPage(tail, count)
 }
   /**
    * method to figure out how many active elements there are
    * 
    * @param hList - a List[HTMLElement] that contains all of the elements of 
    * the webpage
    * @param counter - where to start counting
    * 
    * @return the number of active elements + the initial counter value
    */
    def lastButton(hList: List[HTMLElement], counter: Int): Int = hList match {
     case Nil => counter
     case (x: Link)::tail => 
     lastButton(tail, counter+1)
     case (x: SubmitInput)::tail => 
     lastButton(tail, counter+1)
     case (x: TextInput)::tail =>
     lastButton(tail, counter+1)
     case (x: Paragraph)::tail => 
     lastButton(tail, x.lastButtonHelper(counter))
     case (x: PageText)::tail =>
       lastButton(tail, counter)
     case (x: Form)::tail =>
       lastButton(tail, x.lastButtonHelper(counter))
   }
   
   /**
    * method to process a GET request to a server and print out the 
    * response webpage
    * 
    * @param host - the host server to put it on, if its none, use the current hostname
    * @param url - the url for the server request
    */
   private def getRequest(host: Option[String], url: String): Unit = {
     var actualHost: Option[String] = None
     if (host == None) {
      actualHost = hostname
     } else {
      actualHost = host 
     }
     try {
       val sock = new Socket(actualHost.get, 8080) 
       val iStream = sock.getInputStream
       val oStream = sock.getOutputStream
       val bRead = new BufferedReader(new InputStreamReader(iStream))
       val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
       bWrite.write("GET " + url + " HTTP/1.0\r\n")
       bWrite.write("Connection: close\r\n")
       bWrite.write("User-Agent: Sparkzilla/1.0\r\n")
       bWrite.write("\r\n")
       bWrite.flush
       // maybe write some thing like connecting...
       var parseStream = bRead.readLine()
       while (parseStream != "") {
         if (parseStream.contains("OK")) {
           System.out.println("OK")
         }
         parseStream = bRead.readLine()
         }
       val page = getHTMLElementList(bRead)
       sock.shutdownInput
       sock.shutdownOutput
       sock.close
       cache = page::cache
       renderPage(cache.head, 4)
       System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
     } catch {
       case e: java.net.UnknownHostException => {
         System.out.println("unknown host")
         if (cache.isEmpty) {
           run()
         } else {
           renderPage(cache.head, 4)
           System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
         }
       }
     }
   }
   
   /**
    * method to process a post request to a server and print out the response
    * 
    * @param host - the host server as an Option[String] to put it on, if its none, use the current hostname
    * @param url - a string representing the url for the server request
    * @param formData - a string of the form data needed to process the request
    * @param contentLength - an Int of the length of the formData string 
    */
   private def postRequest(host: Option[String], url: String, formData: String, contentLength: Int): Unit = {
     var actualHost: Option[String] = None
     if (host == None) {
      actualHost = hostname
     } else {
      actualHost = host 
     }
     try {
       val sock = new Socket(actualHost.get, 8080) 
       val iStream = sock.getInputStream
       val oStream = sock.getOutputStream
       val bRead = new BufferedReader(new InputStreamReader(iStream))
       val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
       bWrite.write("POST " + url + " HTTP/1.0\r\n")
       bWrite.write("Connection: close\r\n")
       bWrite.write("User-Agent: Sparkzilla/1.0\r\n")
       bWrite.write("Content-Type: application/x-www-form-urlencoded\r\n")
       bWrite.write(s"Content-Length: $contentLength\r\n")
       bWrite.write("\r\n")
       bWrite.write(formData)
       bWrite.flush
       // maybe write some thing like connecting...
       var parseStream = bRead.readLine()
       while (parseStream != "") {
         if (parseStream.contains("OK")) {
           System.out.println("OK")
         
         }
         parseStream = bRead.readLine()
         }
       val page = getHTMLElementList(bRead) 
       sock.shutdownInput
       sock.shutdownOutput
       sock.close
       cache = page::cache
       renderPage(cache.head, 4)
       System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
       } catch {
         case e: java.net.UnknownHostException => {
           System.out.println("unknown host")
           if (cache.isEmpty) {
             run()
           } else {
             renderPage(cache.head, 4)
             System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
           }
       }
   }
   }
   
   /**
    * method to match input number to its respective active element, and then do 
    * the action required for that active element
    * 
    * @param num - an int that is the number to be matched
    * @param hList - the List[HTMLElement] that represents the current webpage
    * @param sysRead - a buffered reader that reads from system.in
    */
   private def activeElementActions(num: Int, hList: List[HTMLElement], sysRead: BufferedReader): Unit = hList match {
     case Nil => Nil
     case (x: Link)::tail =>
       if (x.button.get == num) {
         val info = x.whenClicked
         getRequest(info._1, info._2)
       } else {
         activeElementActions(num, tail, sysRead)
       }
     case (x: TextInput)::tail =>
       if (x.button.get == num) {
         System.out.print("enter value for field " + x.name + ":")
         x.whenClicked(sysRead.readLine())
         renderPage(cache.head, 4)
         System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
       } else {
         activeElementActions(num, tail, sysRead)
       }
     case (x: SubmitInput)::tail => 
       if (x.button.get == num) {
         val info = x.whenClicked()
         postRequest(info._1, info._2, info._3, info._4)
       } else {
         activeElementActions(num, tail, sysRead)
       }
     case (x: PageText)::tail => 
       activeElementActions(num, tail, sysRead)
     case (x: Paragraph)::tail =>
       activeElementActions(num, x.elements:::tail, sysRead)
     case (x: Form)::tail => 
       activeElementActions(num, x.elements:::tail, sysRead)  
   }
   
   /**
    * method to parse user input as an int
    * 
    * @param sysRead - a buffered reader that reads user input, if the user did not 
    * input an int, it prints a message and asks for an int again
    * 
    * @return an int that the user inputtes
    * 
    */
   private def parseInt(sysRead: BufferedReader): Int  = {
     try {
       sysRead.readLine().toInt
     } catch {
       case e: NumberFormatException => {
         System.out.println("not a valid number")
         if (cache.size ==1 | cache.size == 0) {
          run()
          parseInt(sysRead)
        } else {
          cache = cache.drop(1)
          renderPage(cache.head, 4)
          System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
          parseInt(sysRead)
        }
       }    
       }
   }
   /**
    * method that runs the browser
    */
   def run() {
    System.out.println("Rendering Page...")
    System.out.println("-----------------")
    System.out.println("")
    System.out.println("Welcome to Sparkzilla!")
    System.out.println("")
    System.out.println("-----------------")
    System.out.println(actions)
    val regex = new Regex("""//\w+/""")
    val sysRead = new BufferedReader(new InputStreamReader(System.in))
    hostname = None
    cache = List[List[HTMLElement]]()
    var continue = true
    var num = parseInt(sysRead)
    while(continue) {
      if (num == 1) {
        if (cache.size ==1 | cache.size == 0) {
          run()
        } else {
          cache = cache.drop(1) 
          renderPage(cache.head, 4)
          System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
          num = parseInt(sysRead)
        }
      } else if (num == 2) {
        System.out.println("Enter URL:")
        val prompt = sysRead.readLine()
        val host = regex.findFirstMatchIn(prompt)
        if (host == None) {
          if (hostname == None) {
            if (cache.isEmpty) {
              System.out.println("Invalid URL")
              run()
            } else {
              System.out.println("Invalid URL")
              renderPage(cache.head, 4)
              System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
              num = parseInt(sysRead)
            }
          } else if (prompt == "") {
            if (cache.isEmpty) {
              System.out.println("Invalid URL")
              run()
            } else {
              System.out.println("Invalid URL")
              renderPage(cache.head, 4)
              System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
              num = parseInt(sysRead)
            }
          } else if (prompt.charAt(0) != '/') {
            if (cache.isEmpty) {
              System.out.println("Invalid URL")
              run()
            } else {
              System.out.println("Invalid URL")
              renderPage(cache.head, 4)
              System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
              num = parseInt(sysRead)
            }
          } else {
            getRequest(None, prompt)
            num = parseInt(sysRead)
        }
          } else {
            val url = prompt.split("//")
            hostname = Some((host.get.matched.filter(x => x != '/')))
            getRequest(None, url(1).dropWhile(x => x != '/'))
            num = parseInt(sysRead)
          }
      } else if (num == 3) {
        System.out.println("goobye!")
        continue = false
      } else if (
          if (cache.isEmpty) {
            num > 3
          } else {
            num > lastButton(cache.head, 3)
          }) {
        System.out.println("Not a valid input")
        if (cache.isEmpty) {
          run()
        } else {
          renderPage(cache.head, 4)
          System.out.println(actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements")
          num = parseInt(sysRead) 
        }
      } else {
        activeElementActions(num, cache.head, sysRead)
        num = parseInt(sysRead)
      }
    } 
        
  }

}

object Browser extends App {
  val browser = new Browser()
  browser.run
}