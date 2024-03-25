import java.util.List;
import java.io.*;
import java.util.*;
public class ClientQuerySystemState extends State {
    private static Warehouse warehouse;

    public ClientQuerySystemState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        System.out.println("    Client Query System State");
        System.out.println("1. Display All Clients");
        System.out.println("2. Display Clients with Outstanding Balance");
        System.out.println("3. Display Clients with No Transactions in the Last Six Months");
        System.out.println("4. Go Back");
    }

    public State handleInput(String choice) {
        choice = choice.trim();
        switch (choice) {
            case "1":
                displayAllClients();
                return this;
            case "2":
                displayClientsWithOutstandingBalance();
                return this;
            case "3":
                displayClientsWithNoTransactionsInLastSixMonths();
                return this;
            case "4":
                return context.getClerkMenuState();
            default:
                System.out.println("Invalid choice. Try again.");
                return this;
        }
    }

    private void displayAllClients() {
        // Implement logic to display all clients
        // You can use the warehouse to retrieve the list of clients.
        List<Client> clients = warehouse.getAllClients();
        if (!clients.isEmpty()) {
            System.out.println("All Clients:");
            for (Client client : clients) {
                System.out.println(client);
            }
        } else {
            System.out.println("No clients found.");
        }
    }

    private void displayClientsWithOutstandingBalance() {
        // Implement logic to display clients with outstanding balance
        // You can use the warehouse to retrieve this information.
        List<Client> clients = warehouse.getClientsWithOutstandingBalance();
        if (!clients.isEmpty()) {
            System.out.println("Clients with Outstanding Balance:");
            for (Client client : clients) {
                System.out.println(client);
            }
        } else {
            System.out.println("No clients with outstanding balance found.");
        }
    }

    private void displayClientsWithNoTransactionsInLastSixMonths() {
        // Implement logic to display clients with no transactions in the last six months
        // You can use the warehouse to retrieve this information.
        List<Client> clients = warehouse.getClientsWithNoTransactionsInLastSixMonths();
        if (!clients.isEmpty()) {
            System.out.println("Clients with No Transactions in Last Six Months:");
            for (Client client : clients) {
                System.out.println(client);
            }
        } else {
            System.out.println("No clients with no transactions in the last six months found.");
        }
    }
}
