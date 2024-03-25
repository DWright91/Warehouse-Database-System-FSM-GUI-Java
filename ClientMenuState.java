import java.util.*;

public class ClientMenuState extends State {
    private static Warehouse warehouse;
    public ClientMenuState(Context context, String clientID,Warehouse warehouse) {
        super(context);
        context.setCurrentClientID(clientID);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        System.out.println("    Client Menu State");
        System.out.println("1. Show Client Details");
        System.out.println("2. Show Products");
        System.out.println("3. Show Client Invoice");
        System.out.println("4. Add Product to Wishlist");
        System.out.println("5. Remove Product from Wishlist");
        System.out.println("6. Show Client Wishlist");
        System.out.println("7. Show Client Waitlist");
        System.out.println("8. Process an Order");
        System.out.println("9. Shopping Cart");
        System.out.println("10. Back");
    }

    public State handleInput(String choice) {
        choice = choice.trim();
        switch (choice) {
            case "1":
                showClientDetails();
                return this;
            case "2":
                showProducts();
                return this;
            case "3":
                showClientInvoice();
                return this;
            case "4":
                addProductToWishlist();
                return this;
            case "5":
                removeProductFromWishlist();
                return this;
            case "6":
                showWishlist();
                return this;
            case "7":
                showWaitlist();
                return this;
            case "8":
                processOrder();
                return this;
            case "9":
            	// Open the ShoppingCartOperationsState
            	return context.getShoppingCartOperationsState();
            case "10":
                if (context.isClient() && context.getPreviousState() != null
                        && context.getPreviousState() instanceof ClerkMenuState) {
                    return context.getPreviousState();
                } else {
                    return new OpeningState(context,warehouse);
                }

            default:
                //System.out.println("Invalid choice. Try again.");
                return this;
        }
    }

    // Method to open the ShoppingCartOperationsState
    private State openShoppingCartOperations() {
        ShoppingCartOperationsState cartState = new ShoppingCartOperationsState(context, warehouse);
        context.setCurrentState(cartState);
        return cartState;
    }

    private void showClientDetails() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            System.out.println("Client Details:");
            System.out.println(client.toString());
        } else {
            System.out.println("Client not found.");
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

    // Method to show the invoice for a specific client.
    public void showClientInvoice() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            List<Invoice> clientInvoices = warehouse.getInvoicesForClient(client.getClientId());
            if (!clientInvoices.isEmpty()) {
                System.out.println("Invoices for " + client.getClientName() + ":");
                for (Invoice invoice : clientInvoices) {
                    System.out.println(invoice);
                }
            } else {
                System.out.println("No invoices found for " + client.getClientName());
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    // Method to add product for a specific client wish-list.
    public void addProductToWishlist() {
        String clientID = context.getCurrentClientID();
        String productId = context.getString("Enter product ID");
        int quantity = context.getInt("Enter quantity");

        Client client = warehouse.getClientById(clientID);
        Product product = warehouse.getProductById(productId);

        if (client == null || product == null) {
            System.out.println("Client or product not found.");
            return;
        }

        int result = warehouse.addProductToWishlist(client, product, quantity);
        switch (result) {
            case Warehouse.ADD_PRODUCT_TO_WISHLIST_SUCCESS:
                System.out.println("Product added to the wishlist successfully.");
                break;
            case Warehouse.WISHLIST_PRODUCT_ALREADY_EXISTS:
                System.out.println("Product is already in the wishlist.");
                break;
            case Warehouse.CLIENT_ALREADY_IN_WAITLIST:
                System.out.println("Client is already in the waitlist for this product.");
                break;
            default:
                System.out.println("Failed to add the product to the wishlist.");
                break;
        }
    }

    // Method to remove a product from a client's wishlist.
    public void removeProductFromWishlist() {
        String clientID = context.getCurrentClientID();
        String productId = context.getString("Enter product ID");

        Client client = warehouse.getClientById(clientID);
        Product product = warehouse.getProductById(productId);

        if (client == null || product == null) {
            System.out.println("Client or product not found.");
            return;
        }

        int result = warehouse.removeProductFromWishlist(client, product);
        switch (result) {
            case Warehouse.REMOVE_PRODUCT_FROM_WISHLIST_SUCCESS:
                System.out.println("Product removed from the wishlist successfully.");
                break;
            case Warehouse.WISHLIST_PRODUCT_NOT_FOUND:
                System.out.println("Product not found in the wishlist.");
                break;
            default:
                System.out.println("Failed to remove the product from the wishlist.");
                break;
        }
    }

    // Method to show the products in the client wishlist.
    public void showWishlist() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            System.out.println("Wishlist for Client: " + client.getClientName());

            Wishlist wishlist = client.getWishlist();
            if (!wishlist.getProducts().isEmpty()) {
                System.out.println("Products in Wishlist:");

                for (Product product : wishlist.getProducts()) {
                    double totalPrice = wishlist.getProductQuantity(product.getProductId()) * product.getPrice();

                    System.out.println("  Product ID: " + product.getProductId());
                    System.out.println("  Product Name: " + product.getProductName());
                    System.out.println("  Quantity: " + wishlist.getProductQuantity(product.getProductId()));
                    System.out.println("  Product Price: " + product.getPrice());
                    System.out.println("  Total Amount: " + totalPrice);
                    System.out.println();
                }
            } else {
                System.out.println("The wishlist is empty.");
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    // Method to show the products in the wait-list.
    public void showWaitlist() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            System.out.println("Waitlist for Client: " + client.getClientName());

            Iterator<Product> allProducts = warehouse.getProducts();
            while (allProducts.hasNext()) {
                Product product = allProducts.next();
                Waitlist waitlist = product.getWaitlist();
                int clientQuantity = waitlist.getClientQuantity(client);

                if (clientQuantity > 0) {
                    System.out.println("Product ID: " + product.getProductId());
                    System.out.println("Product Name: " + product.getProductName());
                    System.out.println("Client Quantity: " + clientQuantity);
                    System.out.println();
                }
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    // Method to process an order for a client.
    public void processOrder() {
        String clientID = context.getCurrentClientID();
        Client client = warehouse.getClientById(clientID);

        if (client != null) {
            warehouse.processOrder(client);
        } else {
            System.out.println("Client not found.");
        }
    }
}
