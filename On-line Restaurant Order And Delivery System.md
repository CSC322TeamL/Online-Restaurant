# On-line Restaurant Order And Delivery System

nicer formatted document at Notion.so
  
https://www.notion.so/On-line-Restaurant-Order-And-Delivery-System-8a96a292941145c6b4b8884e524f6fa4

### **Fall 2020**

Weiye Kuang - Backend

Leji Li - Frontend, User and Delivery Person

Xian Chen - Frontend, Chef and Manager

Bingjing Dong - Database

In this system, we are about to develop an on-line restaurant order and delivery system so that the restaurant can provide menus of food, customers browse and order the food from the menu, delivery people of the restaurant deliver the food.

In our system, we have developed an on-line restaurant order and delivery system. The system is suitable for customers (regular customers and VIPs ), surfers, chefs, delivery people, and management. The restaurant can provide menus of food by a chef for customers checking and order. Delivery people help the restaurant deliver the food. Management can manage the whole restaurant.

In this system, there are three groups of users:

1. Restaurant:
    1. at least two chefs who independently decide the menus;

        provide 2 different chef user id, the default is set to "chef0001", the chef is not combined the system yet, so there is no log in for chef yet, the user id can now be change by the file getuserid. to and each of them have different personal menu that can be edit by themselves.

    2. at least two delivery people who compete for food delivery

        provide more than 1 delivery people, they have different account.

    3. the manager/superuser who process customer registrations, handles customer compliments and complaints, hire/fire/raise or cut pay for chef(s) and deliver people

        After customer register, the application will show on Manager's page, a manager can choose accept the application: to create a account by the user id provide.  Or deny the application: will not create the account for user.

        Once the customer filed a complaint or compliment, the information of complaint or compliment will send to manager's page on Complaint and Compliment.  Manager can choose to warning user if manager think the complaint or compliment is not reasonable. Manager can choose accept the compliment or complaint, then the files will send to the person who got complaint or compliment,  if manager deny the compliment or compliant, they will not send to the person got compliment or compliant.

        There is a Staff performance, which manager can see all the staff's performance such the complaint and compliment received, and how many times they got demote or promote. the promote and demote automatic count by the compliments and complaints they got. base on the information manager can choose to fire them by de-register their account.

2. Customers:
    1. Registered customers who can browse/search, order and vote (lowest 1 star to highest 5 stars) food delivered (on food and delivery quality/manners individually); can start/participate a discussion topic on cooks/dishes/deliver people.

        Customers/VIPs actions are handle by the SimpleRestaurant App. The first page of this app shows the all the menu that the user can get access to. 

        At the top of the menu, there is a search bar, which provides the ability for user to search the dishes that contains the keyword.  

        At the bottom of the menu, there's an entrance for user to get into the place order page. 

        The user can add a dish to cart directly from the main menu list. 

        By clicking the specific dish on the menu list, there will be a popup window show at the bottom of the page. User can see the full description of this dish, set the quantity and leave a special requirement of this dish.

        In the order cart page, the user can also update the quantity of a dish or delete it from the order cart.

        The second tab of the user application is the discussion. User can create new discussion and view/reply the existing discussion.

        In this page, the user can only see the summary of each discussion and the title. User can read the full discussion and a list of replies of this discussion head.

        User can also submit a new reply to the current discussion by using the input at the bottom of this page.

        The third tab of the user application is the user's order history. Here lists all the orders and user can view the details of a specific order.

    2. VIP customers who spent more than $500 or placed 50 orders as registered customers, whichever comes first, in addition to the actions of registered customers, they will receive 10% discount of their ordinary orders, have access to specially developed dishes, and their complaints/compliments are counted twice as important as ordinary ones.

        When a customer place an order, the systems would make some judgements. If the customer is a VIP, then the system would give him 10% discount. If the balance in the customer's account is less than the money to be charged, the system would freeze the order. After the customer place the order successfully, if the customer is an original customer and the total money he spent before is more than $500 or the number of order he placed is more than 50, then the customer would become a VIP.

3. Surfers: who can browse the menus and ratings only, can apply to be the registered customers with fixed amount of deposit money and checked by the manager.

    At the bottom of the login page, there're surfer's entrance of the application and the sign up entrance.

    When the user get into the application as a surfer, the user can just view the menu list, which includes the rating of each dish, and view the existing discussions.

    Surfer can send a registration request in the sign up page. User is require to provide the contact email and the reason to register. This request will be sent to manager and once the manager accepts the request, the user will receive the information about his/her username and default password, outside of this system. 

    The first time user login, this application will guide the user to reset password page and then let the user fill up the basic information, such as contact information, address and so on. And the user should fund a certain balance to active the account. Here we lower the threshold to $50. Once the user deposit not less than the threshold balance, the user account will be activated. 

System features:

1. Provide a GUI, *not necessarily web-based*, with pictures to show the components and descriptions of each dish and price; each registered customer/VIP has a password to login, when they log in, based on the history of their prior choices, different registered customer/VIP will have different top 3 listing dishes. For new customers or surfers, the top 3 most popular (ordered most) dishes and top 3 highest rated dishes are listed on the page.

2. The chef whose dishes received consistently low ratings or 3 complaints, or no order at all for 3 days, will be demoted (less salary), a chef demoted twice is fired. Conversely, a chef whose dishes received high ratings or 3 compliments, will be promoted (higher salary). One compliment can be used to cancel one complaint. The delivery people are handled the same way.

    Once a chef got 3 complaints, he will be demote automatic, and got 3 compliments will automatic promote. If they get 3 complaint and get 1 compliment they will not got demote because the 1 compliment cancel 1 complaint. The delivery people using the same system.

3. A customer can choose to 1) eat the food in the restraint, 2) pick up the dishes by self or 3) by delivery. For 1) s/he need to fix the available time and seating in the restraint; for case 1) and 2) s/he can only complain/compliment the chef.  

    While customer checking their shopping cart, he/she can choose to pick up the dishes or by delivery. Customers can complain/compliment the chef when the order finished. If the customer chooses delivery, he/she also can complain/compliment the delivery people.

4. Customers can file complaints/compliments to chef of the food s/he purchased and deliver person who delivered the dish or other customers who didn’t behave in the discussion forums. Delivery person can complain/compliment customers s/he delivered dishes, all are handled by the manager. The complained person has the right to dispute the complaint, the manager made the final call to dismiss the complaint or convert it to one formal warning and inform the impacted parties. Customers/delivery people whose complaints are decided without merit by the manager will receive one warning.

    Once the order is in `finished` status, the user can file complaint/complement to chef/delivery person. If the order is a `Pick Up` order, the user can only complaint/complement the chef only.

    User can do it on the order page. For each `finished` order, in the order detail page, there are complaint/complement options at the bottom of the page. User can choose his/her own action accordingly.

    When a user/staff file a complaint/complement to a staff/user, the complaint will need to be reviewed by the manager. If the manager accepts the complaint/complement, this record will be showed on the staff's/user's received complaint/complement list.

    For the complaint, saying the user files a complaint to a staff and assuming the staff is a delivery person here, and this complaint is accepted by the manager, the complaint will be displayed on the delivery person's `Received Complaint` page, and the delivery person can make disputation. 

    When someone try to dispute a complaint, he need to find the complaint and click "dispute" button to fill the reason. The disputation will also need to be reviewed by the manager. If the manager decides to accept the disputation, then the complaint would be remove from the account. If the manager decides to deny the disputation, then nothing would be changed. 

5. Registered customers having 3 warnings are de-registered. VIPs having 2 warnings are put back to registered customers (with warnings cleared). The warnings should be displayed in the personalized page when the customers log in.      

    When the customer receives a warning, the system would make a judgement. If the customer is a VIP and the number of warnings he receives is equal to 2 , then the system would clear his warnings and pull him back to original customer.  Our system would not de-register the customers automatically, but display the information about the customer to the manager, like the balance of the customer, number of warnings he received, and how many money the customer spent. Then the manager can decide to de-register the customer or not.                                                                               

6. If the price of the order is more expensive than the deposited money in the account, the order is frozen until the customer put more money in the account.

    If the customer wants to check the order when the price is more than the balance in their account, the order is frozen. User can choose to deposit more money in he/she account, the order will be accepted once have enough balance.

    When the application receives the frozen order response, the application will shows corresponding message to the user, and the dishes on the order will remain on the cart, waiting for user to refill balance and place order again.

7. Customers who are kicked out of the system or choose to quit the system will be handled by the manager: clear the deposit and close the account.

    Once a customer got 3 warnings, the manger can see all customers who have 3 or more warnings, The manager can choose to de-registered to kick out them or just keep them for some reason. Once they got de-registered, the account will be close.

8. The chef is the one who put in the description and keywords for people to search and browse. The average ratings for each food/dish by customers are available for all.

    At each chef's menu page, the menus are different for different chef. They can choose to edit the menu by change the menu name, price, search key word, and the description of the dish. The rating for dish is  show on the menus. 

9. The manager keeps a taboo list of words, any customer who used those taboo words will receive one warning automatically and the words are replaced by ***, a message with more than 3 taboo words are blocked automatically.

    Manager have a taboo list that contain all the taboo words, Manager can edit the taboo words by add more words or delete the words. the word in the taboo list will replace by ***.

    When a customer join in the discussion, if there are 1-3 taboo words in his context, then the taboo words would be replace by ***, and the system would give the customer a warning. If there are more than 3 taboo words in his context, then the system would not only give him a warning, but also block his comment, which means the customer can not post his comment.

10. Each team comes up with a creativity feature of the system to make it more exciting, which is worth 10% of overall score of the final project. Details that are not found in this requirement list are up to your team’s call: you fill in the details to your own liking.

    Our team created Android Apps for the front end.