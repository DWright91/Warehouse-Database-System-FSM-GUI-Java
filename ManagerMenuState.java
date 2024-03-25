import java.util.*;

public class ManagerMenuState extends State {
    private static Warehouse warehouse;
    public ManagerMenuState(Context context,Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
    }

    public void displayOptions() {
        System.out.println("    Manager Menu State");
        System.out.println("1. Add a Product");
        //System.out.println("2. Show the Waitlist");
        System.out.println("3. Receive a Shipment");
        System.out.println("4. Become a Clerk");
        System.out.println("5. Back");
    }

    public State handleInput(String choice) {
        choice = choice.trim();
        switch (choice) {
            case "1":
                addProduct();
                return this;
            //case "2":
                //showWaitlist();
                //return this;
            case "3":
                supplyProductsInWarehouse();
                return this;
            case "4":
                // Become a Clerk
                context.setIsManager(false);
                context.setIsClerk(true);
                context.setPreviousState(this); // Set the previous state to the current ManagerMenuState
                return new ClerkMenuState(context,warehouse);
            case "5":
                if (context.isManager() && context.getPreviousState() != null
                        && context.getPreviousState() instanceof ClientMenuState) {
                    return context.getPreviousState();
                } else {
                    return new OpeningState(context,warehouse);
                }
            default:
                System.out.println("Invalid choice. Try again.");
                return this;
        }
    }

    // Method to add a product to the warehouse.
    public void addProduct() {
        String productName = context.getToken("Enter product name");
        double price = Double.parseDouble(context.getToken("Enter product price"));
        int quantity = Integer.parseInt(context.getToken("Enter product quantity")); // New line for quantity input

        Product product = warehouse.addProduct(productName, price, quantity); // Update the method call
        if (product != null) {
            System.out.println("Product added successfully:");
            System.out.println(product);
        } else {
            System.out.println("Product could not be added.");
        }
    }

    // Method to show the products in the waitlist.
    //public void showWaitlist() {
        //System.out.println("Waitlist:");

        //Iterator<Product> allProducts = warehouse.getProducts();
        //while (allProducts.hasNext()) {
            //Product product = allProducts.next();
            //Waitlist waitlist = product.getWaitlist();

            //if (!waitlist.getClients().isEmpty()) {
                //System.out.println("Product ID: " + product.getProductId());
                //System.out.println("Product Name: " + product.getProductName());

                //System.out.println("Clients in Waitlist:");

                //for (Client client : waitlist.getClients()) {
                    //System.out.println("  Client ID: " + client.getClientId());
                    //System.out.println("  Client Name: " + client.getClientName());
                    //System.out.println("  Client Address: " + client.getAddress());
                    //System.out.println("  Client Phone: " + client.getPhone());
                    //System.out.println("  Product Quantity: " + waitlist.getClientQuantity(client));
                    //System.out.println();
                //}
            //}
        //}
    //}

    // Method to supply products in the warehouse.
    public void supplyProductsInWarehouse() {
        Iterator<Product> productIterator = warehouse.getProducts();
        List<Product> products = new ArrayList<>();

        // Convert the Iterator to a List
        while (productIterator.hasNext()) {
            products.add(productIterator.next());
        }

        if (products.isEmpty()) {
            System.out.println("No products available in the warehouse.");
            return;
        }

        System.out.println("Available Products:");
        for (Product product : products) {
            System.out.println("Product ID: " + product.getProductId() + " | " + product.getProductName()
                    + " | Quantity: " + product.getQuantity());
        }

        String productId = context.getToken("Enter the Product ID to supply: ");

        Product selectedProduct = null;
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                selectedProduct = product;
                break;
            }
        }

        if (selectedProduct == null) {
            System.out.println("Invalid Product ID. No product found.");
            return;
        }

        int quantityToAdd = context.getNumber("Enter the quantity to add:");

        warehouse.supplyProducts(selectedProduct, quantityToAdd); // Supply the product

    }
}
