# Welcome to the Learning Outcomes Evaluation

Dear students,

Welcome to this Learning Outcomes Evaluation session. The goal is to assess your understanding and mastery of the learning outcomes for this semester as evidenced by your work that was submitted on your personal git account. Remember to answer each question thoroughly by referencing **Java** code and provide clear explanations where necessary.

Best regards,
Kay Berkling

## Ethics Section regarding generative and other forms of AI

The student acknowledges and agrees that the use of AI is strictly prohibited during this evaluation. By submitting this report, the student affirms that they have completed the form independently and without the assistance of any AI technologies. This agreement serves to ensure the integrity and authenticity of the students work, as well as their understanding of the learning outcomes.


## Checklist before handing in your work

* [ ] Review the assignment requirements to ensure you have completed all the necessary tasks.
* [ ] Double-check your links and make sure that links lead to where you intended. Each answer should have links to work done by you in your own git repository. (Exception is Speed Coding)
* [ ] Make sure you have at least 10 references to your project code (This is important evidence to prove that your project is substantial enough to support the learning outcome of object oriented design and coding within a larger piece of code.)
* [ ] Include comments to explain your referenced code and why it supports the learning outcome.
* [ ] Commit and push this markup file to your personal git repository and hand in the link and a soft-copy via email at the end of the designated time period.

Remember, this checklist is not exhaustive, but it should help you ensure that your work is complete, well-structured, and meets the required standards.

Good luck with your evaluation!

# Project Description (70%)


## Link

https://github.com/bennixm/chatapp

## TECH STACK

Java Spring Boot - Backend
VueJs - Frontend
MySQL - Database
Docker - container for deployment

## What did you achieve? 

I have a full stack Web Chat Application in Real time with : 
- Login ( secured using JWT )
- Register
- Users listing and filtering
- New Chat function
- List of open chats
- Friendship system ( including Friend Request - Request accept - Friends in SideBar)
- Notification System for Requests






## Learning Outcomes

| Exam Question | Total Achievable Points | Points Reached During Grading |
|---------------|------------------------|-------------------------------|
| Q1. Algorithms    |           4            |                               |
| Q2.Data types    |           4            |                               |
| Q3.Complex Data Structures |  4            |                               |
| Q4.Concepts of OOP |          6            |                               |
| Q5.OO Design     |           6            |                               |
| Q6.Testing       |           3            |                               |
| Q7.Operator/Method Overloading | 6 |                               |
| Q8.Templates/Generics |       4            |                               |
| Q9.Class libraries |          4            |                               |


## Evaluation Questions

Please answer the following questions to the best of your ability to show your understanding of the learning outcomes. Please provide examples from your project code to support your answers.


## Evaluation Material


### Q1. Algorithms

Algorithms are manyfold and Java can be used to program these. Examples are sorting or search strategies but also mathematical calculations. Please refer to **two** areas in either your regular coding practice (for example from Semester 1) or within your project, where you have coded an algorithm. Do not make reference to code written for other classes, like theoretical informatics.


I have created Algorithms to practice for myself, as a reference you can check some algorithms of recursive functions : https://github.com/bennixm/javaxprogramming/tree/main/Recursive%20Functions

I do not have basic algorithms in my project because it is not needed to.



| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|           4            |                               |


### Q2. Data types

Please explain the concept of data types and provide examples of different data types in Java.
typical data types in java are int, double, float, char, boolean, long, short, byte, String, and arrays. Please provide one example for each of the **four** following data types in your code.
* arrays
* Strings
* boolean
* your choice


I am using primitive data types but not so often , mostly just on declaring the variables i need in the DTO.

Example of string : in ChatDTO.java (  private String user1Username)
        of boolean : in FriendRequestDTO.java ( private boolean status = false )
        of long : in ChatDTO.java ( private Long chatId )

| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|           4             |                               |



### Q3. Complex Data Structures

Examples of complex data structures in java are ArrayList, HashMap, HashSet, LinkedList, and TreeMap. Please provide an example of how you have used **two** of these complex data structures in your code and explain why you have chosen these data structures.

Example of a Complex Data Structure I used in my project : List for Friends in FriendshipService.java 
Line of code : List<AppUser> friends = new ArrayList<>();
In the method getFriends(AppUser user) , i am creating a new list of Friends where i will put all the friends from the specific user sent by the DTO (Data Transfer Object) User.

We can consider even the DTO User as an structured way to group our data ( it is not a simple data structure). 

| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|           4             |                               |


### Q4. Concepts of OOP
Concepts of OOP are the basic building blocks of object-oriented programming, such as classes, objects, methods, and attributes. 
Explain HOW and WHY your **project** demonstrates the use of OOP by using all of the following concepts:
* Classes/Objects
* Methods
* Attributes 
Link to the code in your project that demonstrates what you have explained above.

I have used oop concepts for almost all of my concepts like : AppUser, Friendship, FriendshipRepository, FrienRequestInfoDTO
I need to use them because the objects are the real data holders, like they represent the users that will use my application. 
Example of class containing : Methods, Object, Attributes : UserController.java - it contains methods like : loginUser, having Object as User and Attributes like : private String username;

| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|             6           |                               |

### Q5. OO Design
Please showcase **two** areas where you have used object orientation and explain the advantage that object oriented code brings to the application or the problem that your code is addressing.
Examples in java of good oo design are encapsulation, inheritance, polymorphism, and abstraction. 

Encapsulation:  in ChatDTO.java for the attributes.
Polymorphism : UserResult - interface used for a better handling of errors .



| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|              6          |                               |



### Q6. Testing
Java code is tested by using JUnit. Please explain how you have used JUnit in your project and provide a link to the code where you have used JUnit. Links do not have to refer to your project and can refer to your practice code. If you tested without JUnit, please explain how you tested your code.
Be detailed about what you are testing and how you argue for your test cases. 

Feel free to refer to the vibe coding session where you explored testing. (pair programming - you may link to your partner git and name him/her.)

Test cases usually cover the following areas:
* boundary cases
* normal cases
* error cases / catching exceptions 

*your text*

| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|         3               |                               |

### Q7. Operator/Method Overloading
An example of operator overloading is the "+" operator that can be used to add two numbers or concatenate two strings. An example of method overloading is having two methods with the same name but different parameters. Please provide an example of how you have used operator or method overloading in your code and explain why you have chosen this method of coding.
The link does not have to be to your project and can be to your practice code.

I did once the UnitTests for class with Prof. Zundel on Java, regarding one of our InventoryManagers but i can not find it at the moment.

| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|          6              |                               |



### Q8. Templates/Generics
Generics in java are used to create classes, interfaces, and methods that operate on objects of specified types. Please provide an example of how you have used generics in your code and explain why you have chosen to use generics. The link does not have to be to your project and can be to your practice code.


I am using generics in FriendshipRepository for ex, JpaRepository which is a Spring Data JPA that provide type-safe repository method. 


| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|           6             |                               |

### Q9. Class Libraries
Examples of class libraries in java are the Java Standard Library, JavaFX, Apache Commons, JUnit, Log4j, Jackson, Guava, Joda-Time, Hibernate, Spring, Maven, and many more. Please provide an example of how you have used a class library in your **project** code and explain why you have chosen to use this class library. 

Example of library in SpringBoot : import org.springframework.security.crypto.password.PasswordEncoder;

This library helps me to encode the password for login , so we do not send the string directly in DB , we encrypt it before sending it .


| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|            6            |                               |


# Creativity (10%)
Which one did you choose: 

* [ * ] Web Interface with Design
* [ * ] Database Connected
* [ ] Multithreading
* [ ] File I/O
* [ * ] API
* [ ] Deployment


I have used a frontend framework which helps me to develope an Web User Interface with design ( CSS + some VueJs libraries).
For the DataBase i have a MySQL database inside of Docker container.
As APIs, i am making requests on backend, so i guess i am using APIs.



| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|            10          |                               |



# Speed Coding (20%)
Please enter **three** Links to your speed coding session GITs and name your partner. 


https://github.com/bennixm/javaxprogramming/tree/main/InventoryManager
https://github.com/bennixm/javaxprogramming/tree/main/week3
https://github.com/bennixm/javaxprogramming/tree/main/week4


Paste your class diagram for your project that you developed during the peer review class here: 

I can not paste it here
but you can see it here : https://github.com/bennixm/javaxprogramming/blob/main/image-2.png

It can be done very simply by just copying any image and pasting it while editing Readme.md.


| Total Achievable Points | Points Reached During Grading |
|------------------------|-------------------------------|
|                        |                               |
|            16            |                               |





