# sparkzilla

INSTRUCTIONS

- In order to interact with our program, the user uses their keypad in order to select a number (representing a button) that corresponds with an action. From the home screen, the user can enter 1 which corresponds with going back (Note: from the home page, there will be no page for the user to go back to), 2 to enter a url, or 3 to quit. If a user chooses to enter a url, they will be prompted to enter it as a string by typing. The only urls that the user can actually visit using this very basic browser implementation are http://thufir/Index and http://stilgar/Index. Within these url’s there are more actions available to them. In thufir, for example, the user is able to post on a forum, “order” a pizza, play a game of nim, or play a game of coliseum. Within the various activities in thufir, more commands are made available to the user depending on which activity the user has decided to interact with. Additionally, the user is able to go back to the home page of thufir within the various activities in order to select a new one or go back to the main page of our browser so that they may enter a new url, go back, or quit the program completely. 

DESIGN OVERVIEW 

- Our implementation of Sparkzilla includes a browser class, and case classes representing  page text, forms, links, paragraphs, submit inputs, and text inputs. We also have an abstract class representing HTML elements which all of the case classes extend. Our browser class stores the string "Actions: [1] Back, [2] Go to URL, [3] Quit" as a private val actions, sets the host (an Option[String]) to none, and sets the cache (the list of visited pages) to an empty List[List[HTMLElement]]. The browser class also has a method called getHTMLElementList which reads the users input using a Buffered Reader, parses it using the given parser, and returns the list of HTMLElements. The browser class also includes a method called renderPage which takes in a list of HTML element lists and a counter and prints out elements and their buttons (if it is active). It does so by matching on the head of the list (x) recursively and calling the helper render (which each of the case classes has a unique variation of) on x which treats each element type uniquely, and then calling renderPage again on the tail and the counter. The browser class also includes a method called lastButton which takes in an HTMLElementList and a counter (which tells the method where to start counting) and returns the number of active elements in the list + the value of the counter. Additionally, the browser class includes a method called getRequest which takes in a host name (string) and a URL (also a string) and returns nothing. Furthermore, there is a postRequest method which takes in a host, url, and formData (which is a string of the form data that is needed to process the request) and prints out the response and outputs nothing. The browser class also includes a method activeElementActions which matches on the head of a list of HTMLElements and carries out the appropriate action depending on the element’s type. For example, if the head (x) is a text input, the string “enter value for field " + x.name + “:” is printed out, the helper whenClicked is called on x, renderPage is called on the head of the cache, and the string actions + ", [4-" + lastButton(cache.head, 3) + "] Page Elements" is printed. This class also includes a method called parseInt which takes in a buffered reader and parses the user input as an int. If the user fails to input an int in a case in which that is their only option, it throws a NumberFormatException and informs the user that their input is “not a valid number.” Finally, the browser class includes the method run which is a REPL that awaits user input and calls various and methods based on it. The case class representing forms includes (in addition to render) a method createFormData which takes in a list of HTMLElements and outputs a string representing the form data. The link, submit input, and, text input classes all include a method whenClicked which behaves differently depending on class. In case of text input, it prints the input where the “____” appears. If it is a submit input, whenClicked sends a POST request to a server. If it is a link, it  submits a GET request. 

UNIMPLEMENTED FEATURES

-None 

KNOWN BUGS

- When a page has no active elements, our program writes [4-3] Page Elements 

TESTING 

- See SparkSystemTest.txt for system testing 

COLLABORATION

-None
