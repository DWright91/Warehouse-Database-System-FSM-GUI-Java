import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.Objects;

public class OpeningState extends State {
    private static Warehouse warehouse;

    public OpeningState(Context context, Warehouse warehouse) {
        super(context);
        this.warehouse = warehouse;
        

        // Create and set up the GUI components
        JFrame frame = new JFrame("Opening State");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Buttons to access different states
        JButton clientButton = new JButton("Client Login");
        JButton clerkButton = new JButton("Clerk Menu");
        JButton managerButton = new JButton("Manager Menu");
        JButton exitButton = new JButton("Exit");

        // Adding buttons to the panel
        panel.add(clientButton);
        panel.add(clerkButton);
        panel.add(managerButton);
        panel.add(exitButton);

        // Action listeners for the buttons
        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String clientID = JOptionPane.showInputDialog(frame, "Enter client ID:");

                if (clientID != null && !clientID.trim().isEmpty()) {
                    context.setCurrentState(new ClientMenuState(context, clientID.trim(), warehouse));
                    frame.dispose();
                } else {
                    // Handle empty or null client ID input
                    JOptionPane.showMessageDialog(frame, "Please enter a valid client ID.");
                }
            }
        });

        clerkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {	
                context.setCurrentState(new ClerkMenuState(context, warehouse));
                frame.dispose();          
            }
        });

        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.setCurrentState(new ManagerMenuState(context, warehouse));
                frame.dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Do you want to save data before closing?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    context.save();
                }
                System.out.println("Exiting the application.");
                System.exit(0);
            }
        });

        // Adding the panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }
    @Override
    public State handleInput(String choice) {
        return this; 
    }
   
    @Override
    public void displayOptions() {
        System.out.println("Welcome to the Warehouse Management System!");
        System.out.println("Please choose an option on panel,");
        System.out.println("If choosing Client press ENTER after panel selection:");
        System.out.println("If choosing Clerk or Manager press ENTER after panel selection:");
        // This is a GUI-based state, so no display is needed in this context
    }
  
}