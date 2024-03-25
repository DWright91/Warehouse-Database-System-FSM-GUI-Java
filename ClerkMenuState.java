import java.util.*;

public class ClerkMenuState extends State {
    private static Warehouse warehouse;
    public ClerkMenuState(Context context,Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        System.out.println("    Clerk Menu State");
        System.out.println("1. Add a Client");
        System.out.println("2. Show Products");
        System.out.println("3. Query System About Clients");
        System.out.println("4. Accept Payment from a Client");
        System.out.println("5. Become a Client");
        System.out.println("6. Display Waitlist for a Product");
        System.out.println("7. Back");
    }
    
    public State handleInput(String choice) {
        choice = choice.trim();
        switch (choice) {
            case "1":
                addClient();
                return this;
            case "2":
            	showProducts();
                return this;
            case "3":
                return new ClientQuerySystemState(context, warehouse);
            case "4":
                acceptPayment();
                return this;
            case "5":
            	// Become a client
                context.setIsClerk(false);
                context.setIsClient(true);
                context.setPreviousState(this);
                String clientID = context.getToken("Client ID: ");
                if (context.isClient(clientID)) {
                    // Switch to ClientMenuState for the specified client
                    return new ClientMenuState(context, clientID,warehouse);
                } else {
                    System.out.println("Invalid client ID. Try again.");
                    return this;
                }
            case "6":
                displayWaitlist();
                return this;
            case "7":
            	if (context.isClerk() && context.getPreviousState() != null
                	&& context.getPreviousState() instanceof ManagerMenuState) 
            	{
            		return context.getPreviousState();
            	} else {
            		return new OpeningState(context,warehouse);
            	}
                
            default:
                System.out.println("Invalid choice. Try again.");
                return this;
        }
    }

    // Method to add a client to the warehouse.
    public void addClient() {
        String clientName = context.getToken("Enter client name");
        String address = context.getToken("Enter address");
        String phone = context.getToken("Enter phone");

        Client client = warehouse.addClient(clientName, address, phone);
        if (client != null) {
            System.out.println("Client added successfully:");
            System.out.println(client);
        } else {
            System.out.println("Client could not be added.");
        }
    }

    // Method to show all products in the warehouse.
    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            System.out.println(product.toString());
        }
    }

    // Method to show all clients in the warehouse.
    public void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        while (allClients.hasNext()) {
            Client client = allClients.next();
            System.out.println(client.toString());
        }
    }
    
    
    // Method to acceptPayment from client.
    private void acceptPayment() {
        String clientID = context.getString("Enter Client ID: ");
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            double balance = client.getBalance();
            String amountStr = context.getString("Enter payment amount: ");
            
            try {
                double amount = Double.parseDouble(amountStr);

                if (amount <= 0) {
                    System.out.println("Invalid payment amount.");
                    return;
                }

                if (amount > balance) {
                    System.out.println("Payment amount exceeds the client's outstanding balance.");
                } else {
                    client.setBalance(balance - amount);
                    System.out.println("Payment accepted. New balance: $" + client.getBalance());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for the payment amount.");
            }
        } else {
            System.out.println("Client not found.");
        }
    }
    
    // Method to display waitlist.
    private void displayWaitlist() {
        String productID = context.getString("Enter Product ID: ");
        Product product = warehouse.getProductById(productID);

        if (product != null) {
            Waitlist waitlist = product.getWaitlist();

            if (waitlist != null && !waitlist.getClients().isEmpty()) {
                System.out.println("Waitlist for Product: " + product.getProductName());

                for (Client client : waitlist.getClients()) {
                    int quantity = waitlist.getClientQuantity(client);
                    System.out.println("Client ID: " + client.getClientId() + ", Quantity: " + quantity);
                }
            } else {
                System.out.println("No clients are currently on the waitlist for this product.");
            }
        } else {
            System.out.println("Product not found.");
        }
    }
}