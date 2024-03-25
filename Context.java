import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;

// A class representing the user interface for managing the warehouse.
public class Context {
    private static Context userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private State currentState;
    private Context context;
    private Scanner scanner;
    private State[][] fsmMatrix = new State[6][9];
    private String currentClientID;
    private boolean isClient;
    private boolean isClerk;
    private boolean isManager;
    private State previousState;
    
    public void setCurrentState(State newState) {
        currentState = newState;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(State state) {
        previousState = state;
    }

    public String getCurrentClientID() {
        return currentClientID;
    }

    public void setCurrentClientID(String clientID) {
        currentClientID = clientID;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setIsClient(boolean client) {
        isClient = client;
    }

    public boolean isClerk() {
        return isClerk;
    }

    public void setIsClerk(boolean clerk) {
        isClerk = clerk;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public boolean isClient(String clientID) {
        return clientID != null && !clientID.isEmpty();
    }

    public static Context instance() {
        if (userInterface == null) {
            return userInterface = new Context();
        } else {
            return userInterface;
        }
    }

    public Context() {
        context = this;
        scanner = new Scanner(System.in);
        String clientId = "";
        
       if (yesOrNo("Look for saved data and use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }

        // Initialize the FSM matrix
        fsmMatrix[0][0] = new OpeningState(context,warehouse);
        fsmMatrix[0][1] = new ClientMenuState(context, clientId,warehouse);
        fsmMatrix[0][2] = new ClerkMenuState(context,warehouse);
        fsmMatrix[0][3] = new ManagerMenuState(context,warehouse);
        fsmMatrix[0][4] = new ShoppingCartOperationsState(context, warehouse);
        fsmMatrix[0][5] = new ClientQuerySystemState(context, warehouse);
        fsmMatrix[1][0] = new ExitState(context);

        // Initialize the Clerk Menu and Manager Menu states
        fsmMatrix[1][2] = new ClerkMenuState(context,warehouse);
        fsmMatrix[1][3] = new ManagerMenuState(context,warehouse);
        
        // Add the new states.													
        fsmMatrix[1][4] = new ShoppingCartOperationsState(context, warehouse);	
        fsmMatrix[1][5] = new ClientQuerySystemState(context, warehouse);
        
        // Set the initial state
        currentState = fsmMatrix[0][0];
        
        
     
        
    }
    public ClerkMenuState getClerkMenuState() {
        return (ClerkMenuState)fsmMatrix[0][2]; // Assuming ShoppingCartOperationsState is at position [0][4].
    }
    
    public ShoppingCartOperationsState getShoppingCartOperationsState() {
        return (ShoppingCartOperationsState)fsmMatrix[0][4]; // Assuming ShoppingCartOperationsState is at position [0][4].
    }
    
    public ClientQuerySystemState getClientQuerySystemState() {
        return (ClientQuerySystemState)fsmMatrix[0][5]; // Assuming ClientQuerySystemState is at position [0][5].
    }

    public void process() {
        while (!(currentState instanceof ExitState)) {
            currentState.displayOptions();
            String choice = getToken("Pick a choice: ");
            currentState = currentState.handleInput(choice);

            try {
                int choiceInt = Integer.parseInt(choice);
                
                // Determine the number of states for the current user type
                int numStates = context.isClerk() ? 2 : context.isManager() ? 4 : 1;

                if (choiceInt >= 0 && choiceInt < numStates) {
                    //State nextState = fsmMatrix[numStates][choiceInt];
                	currentState = fsmMatrix[numStates][choiceInt];
                } else { 
                	//System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                //System.out.println("Invalid choice. Try again.");
            }
        }
    }

    
    // Method to get a token (user input) with a prompt.
    public String getToken(String prompt) {
        String line = null;
        do {
            try {
                System.out.println(prompt);
                line = reader.readLine();
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (line == null);

        return line.trim(); // Trim whitespace
    }

    // Method to get a yes or no answer from the user.
    public boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        return (more.charAt(0) == 'y' || more.charAt(0) == 'Y');
    }

    // Method to get a string from the user.
    public String getString(String prompt) {
        do {
            try {
                System.out.println(prompt);
                return reader.readLine().trim(); // Read and trim whitespace
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    // Method to set the client's balance.
    public void setClientBalance() {
        String clientId = getString("Enter client ID");
        double newBalance = Double.parseDouble(getToken("Enter new balance"));

        Client client = warehouse.getClientById(clientId);

        if (client != null) {
            client.setBalance(newBalance);
            System.out.println("Balance updated successfully.");
            System.out.println("New Balance: $" + newBalance);
        } else {
            System.out.println("Client not found.");
        }
    }

    // Method to get the client's balance.
    public void getClientBalance() {
        String clientId = getString("Enter client ID");

        Client client = warehouse.getClientById(clientId);

        if (client != null) {
            double balance = client.getBalance();
            System.out.println("Client Balance: $" + balance);
        } else {
            System.out.println("Client not found.");
        }
    }

    // Method to get a number from the user.
    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num;
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number");
            }
        } while (true);
    }

    // Method to save warehouse data to a file.
    public void save() {
        if (Warehouse.save()) {
            System.out.println("The warehouse data has been successfully saved in the file WarehouseData");
        } else {
            System.out.println("There has been an error in saving the warehouse data");
        }
    }

    // Method to retrieve warehouse data from a file.
    public void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println("The warehouse data has been successfully retrieved from the file WarehouseData");
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesn't exist; creating a new warehouse");
                warehouse = Warehouse.instance();
            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public int getInt(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    // Method to add a client to a product's waitlist.
    public void addClientToWaitlist() {
        String clientId = getString("Enter client ID");
        String productId = getString("Enter product ID");
        int quantity = getInt("Enter quantity");

        Client client = warehouse.getClientById(clientId);
        Product product = warehouse.getProductById(productId);

        if (client == null || product == null) {
            System.out.println("Client or product not found.");
            return;
        }

        int result = warehouse.addClientToWaitlist(client, product, quantity);
        switch (result) {
            case Warehouse.ADD_CLIENT_TO_WAITLIST_SUCCESS:
                System.out.println("Client added to the waitlist for the product.");
                break;
            case Warehouse.CLIENT_ALREADY_IN_WAITLIST:
                System.out.println("Client is already in the waitlist for the product.");
                break;
            default:
                System.out.println("Failed to add the client to the waitlist.");
                break;
        }
    }

    // Method to remove a client from a product's waitlist.
    public void removeClientFromWaitlist() {
        String clientId = getString("Enter client ID");
        String productId = getString("Enter product ID");

        Client client = warehouse.getClientById(clientId);
        Product product = warehouse.getProductById(productId);

        if (client == null || product == null) {
            System.out.println("Client or product not found.");
            return;
        }

        int result = warehouse.removeClientFromWaitlist(client, product);
        switch (result) {
            case Warehouse.REMOVE_CLIENT_FROM_WAITLIST_SUCCESS:
                System.out.println("Client removed from the waitlist for the product.");
                break;
            case Warehouse.CLIENT_NOT_FOUND_IN_WAITLIST:
                System.out.println("Client not found in the waitlist for the product.");
                break;
            default:
                System.out.println("Failed to remove the client from the waitlist.");
                break;
        }
    }

    // Method to show all invoices.
    public void showInvoices() {
        Iterator<Invoice> allInvoices = warehouse.getInvoices();
        while (allInvoices.hasNext()) {
            Invoice invoice = allInvoices.next();
            System.out.println(invoice);
        }
    }
    
    // Main method to start the user interface.
    public static void main(String[] args) {
        Context.instance().process();
    }
}

//$ javac Context.java
//$ java Context
