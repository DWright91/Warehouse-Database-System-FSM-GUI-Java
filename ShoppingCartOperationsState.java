import java.util.Scanner;

public class ShoppingCartOperationsState extends State {
    private static Warehouse warehouse;
    private State previousState; 

    public ShoppingCartOperationsState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
        this.previousState = previousState;
    }

    public void displayOptions() {
        System.out.println("    Shopping Cart Operations");
        System.out.println("1. View Cart Contents");
        System.out.println("2. Add Product to Cart");
        System.out.println("3. Remove Product from Cart");
        System.out.println("4. Change Quantity");
        System.out.println("5. Proceed to Checkout");
        System.out.println("6. Exit");
        
    }

    public State handleInput(String choice) {
        choice = choice.trim();
        switch (choice) {
            case "1":
                viewCartContents();
                return this;
            case "2":
                addProductToCart();
                return this;
            case "3":
                removeProductFromCart();
                return this;
            case "4":
                changeQuantity();
                return this;
            case "5":
                proceedToCheckout();
                return this;
            case "6":
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

    private void viewCartContents() {
        // Implement logic to display cart contents here
        // You can use the warehouse to retrieve the cart contents
        // and display them using text-based graphics.
        System.out.println("Cart Contents: cart empty");
    	
    }

    private void addProductToCart() {
        String productId = context.getString("Enter product ID");
        int quantity = context.getInt("Enter quantity");

        // Implement logic to add a product to the cart here
        // You can use the warehouse to update the cart.
        // Update the cart contents.
        System.out.println("Product added to the cart successfully. [Implement this]");
    }

    private void removeProductFromCart() {
        String productId = context.getString("Enter product ID to remove");

        // Implement logic to remove a product from the cart here
        // You can use the warehouse to update the cart.
        // Update the cart contents.
        System.out.println("Product removed from the cart successfully. [Implement this]");
    }

    private void changeQuantity() {
        String productId = context.getString("Enter product ID to change quantity");
        int newQuantity = context.getInt("Enter new quantity");

        // Implement logic to change the quantity of a product in the cart here
        // You can use the warehouse to update the cart.
        // Update the cart contents.
        System.out.println("Quantity updated successfully. [Implement this]");
    }

    private void proceedToCheckout() {
        int choice = context.getInt("Proceed to checkout and create an invoice? (1: Yes, 2: No)");
        if (choice == 1) {
            // Implement logic to proceed to checkout here
            // You can use the warehouse to create an invoice and complete the checkout.
            // Update cart contents and display the invoice.
            System.out.println("Checkout completed and invoice created. [Implement this]");
        }
    }

}
