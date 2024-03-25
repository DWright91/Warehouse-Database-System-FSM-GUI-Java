# Warehouse Database System FSM modeled MPIS

Purpoose: Design and create a warehouse database system that uses a Multi Panel Interactive System (MPIS) modeled using a finite state machine (FSM).

1. The system has three kinds of users: manager, salesclerk, and client. The first panel will ask
what kind of user is accessing the system - manager, salesperson or client, and also ask for a
username and password. All clients use their IDs as login names and as passwords. The manager
( and the salesclerk, respectively) use the string “manager”(“salesclerk”, respectively)
as both username and password. Password checking will be done by a separate “security
system” object with a method (like, say, bool verifyPasswd(user, password)) that can
be invoked from the UI.


2. The next panel shows what operations are available. There are three different panels depending
on who has logged in:

        • The client panel can do only the client operations: View the account, put in an order,
          check the price of a product, modify the shopping cart, etc.
        • The salesclerk panel operations are: Print a list of all products with quantity on hand, add
          a client, add a product, load database, and any other ops except the ones listed as manager
          ops.
        • The manager operations: Modify the sale price of an item, recieve a shipment, and
          freeze(block all future order processing)/unfreeze a client’s account. All manager operations
          need a password for confirmation.

3. A salesclerk menu also provides an option to “become a client”. If this is chosen, the system
asks for a clientID and if it is valid, switches the menu to the client menu. The salesclerk can
then perform client operations for the specified client. When the logout option is chosen, it
goes back to the salesclerk menu. Likewise the manager menu provides an option to “become
a salesclerk” (the system asks for the manager’s password), and upon logout, goes back to
the manager menu.

4. The system can can go to another “sub-panel” from one of the panels. Say the client menu
panel has options like “edit personal info” , “display account information”, etc.. When display
account information option is chosen, the resulting panel will display the basic client info like
name, id, balance due etc. It could then have options for other itemized things like, “show
invoices”, “show payments” and “quit”. If you click on show invoices, it will then display the
details of the invoices sent to the client. If the client chooses quit again, GUI goes back to
the client-menu panel.

5. Each panel or sub-panel corresponds to a state of the FSM. All operations do not need have
a separate panel; simple operations can be done without a change of state. Each panel will
have choices for operations like back, quit and/or logout as appropriate.

----------------------------------------------------------------------------------------
Finite State Machine (FSM) Model:
1.	Initialization State:

  	•	Purpose: Initial state where the user is prompted to choose the type of user (manager, salesclerk, or client) and provide a username and password.
        •	Transitions: Based on user input, transitions to Manager Panel, Salesclerk Panel, or Client Panel.

3.	Manager Panel:
        •	Purpose: Manager-specific operations like modifying sale prices, receiving shipments, and freezing/unfreezing client accounts.
        •	Transitions: Options to enter a password for confirmation or transition to Logout State.
4.	Salesclerk Panel:
        •	Purpose: Salesclerk-specific operations such as printing product lists, adding clients/products, and loading the database.
        •	Transitions: Options to switch to Client Panel, enter a password to become a manager, or transition to Logout State.
5.	Client Panel:
        •	Purpose: Client-specific operations including viewing account, placing orders, checking product prices, and modifying the shopping cart.
        •	Transitions: Options to go to sub-panels (e.g., Edit Personal Info), switch to Salesclerk Panel, or logout.
6.	Sub-panel (e.g., Edit Personal Info):
        •	Purpose: Sub-panel for detailed operations related to the client's account.
        •	Transitions: Options for specific operations (e.g., show invoices, show payments) and the ability to go back to the Client Panel.
7.	Logout State:
        •	Purpose: State for logging out of the system and transitioning back to the Initialization State.
        •	Transitions: Options to logout and return to the Initialization State or quit the system.

----------------------------------------------------------------------------------------
State Transition Diagram:

![image](https://github.com/DWright91/Warehouse-Database-System-FSM-GUI-Java/assets/94549091/e9e23289-1a57-4fff-bb27-50a69eac3068)

 ----------------------------------------------------------------------------------------
Aspects Not Fitting FSM Definition:
1.	Dynamic User Inputs: FSMs traditionally don't handle dynamic inputs well. To incorporate this, use a stack-based system to manage dynamic input contexts within a state.
2.	Password Verification: FSMs typically don't handle complex input verification. Use callbacks or external systems for password verification, possibly asynchronous, and update the FSM based on the verification result.
3.	Conditional Operations: Some operations depend on conditions (e.g., becoming a client or salesclerk). These conditions can be managed by introducing additional states or by dynamically altering state transition rules.
Plan to Incorporate:
1.	Dynamic Inputs: Implement a context or stack system to manage dynamic inputs. Each state can push or pop contexts, allowing the FSM to adapt to varying input requirements.
2.	External Verification: Utilize callbacks or interfaces to external systems for complex operations like password verification. Ensure the FSM updates accordingly based on the results.
3.	Conditional Operations: Introduce additional states or dynamically alter transition rules based on conditions. For instance, a conditional transition from Salesclerk Panel to Client Panel based on the "become a client" option.




