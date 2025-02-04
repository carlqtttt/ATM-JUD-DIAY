package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akash
 */
public class Login extends JFrame implements ActionListener {

    // JFrame is class of swing package.
    // Interface ActionListener helps in performing action if a JButton is clicked.
    // ActionListener is present inside java.awt.event
    // We have to override all the methods of interface.
    // We have to define the Buttons, TextFields Globally, so that we can access them in functions.
    JButton login, clear, signup, admin;
    JTextField cardTextField;
    JPasswordField pinTextField;  // So that password is not visible.

    Login() {
        setTitle("Automated Teller Machine");

        // We don't want to use default Layout, But rather we want to make our own custom Layout. So, that we can set location of the image on the Frame.
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.jpg"));  // getSystemResource() is a static method.
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);  // Changing the size of image.
        // awt package has the Image class.
        // We cannot directly place an icon upon a Frame. We need to put it upon JLabel.
        // We cannot put Image class object into JLabel, rather, ImageIcon class's object into JLabel is possible. Therefore we will again convert it into ImageIcon.
        ImageIcon i3 = new ImageIcon(i2);
        JLabel label = new JLabel(i3);
        // Now, we will place the Jlabel upon a Frame. Whenever we want to place a component upon Frame, we need to use add() function.

        // Now, we want to change the location of image on the Frame.
        label.setBounds(70, 10, 100, 100);    // Takes 4 Arguements. First Arguement, distance from left-hand side, 2nd Aruement, Distance from the upside, 3rd & 4th Arguement is width & height of the image respectively.

        add(label);

        // We can also use JLabel to write any content upon JLabel.
        JLabel text = new JLabel("Welcome To ATM");
        text.setFont(new Font("Osward", Font.BOLD, 38));  // Changing the font of the text.
        text.setBounds(200, 40, 400, 40);  // As we have set setLayout(null); Therefore, as long as we don't define the location of where to set the text. 
        add(text);

        JLabel cardno = new JLabel("Card No.:");
        cardno.setFont(new Font("Raleway", Font.BOLD, 28));
        cardno.setBounds(120, 150, 150, 30);
        add(cardno);

        cardTextField = new JTextField();
        // 120 + 150 = 270
        cardTextField.setBounds(300, 150, 230, 30);
        cardTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardTextField);

        JLabel pin = new JLabel("PIN:");
        pin.setFont(new Font("Raleway", Font.BOLD, 28));
        pin.setBounds(120, 220, 250, 30);
        add(pin);

        pinTextField = new JPasswordField();
        // 120 + 150 = 270
        pinTextField.setBounds(300, 220, 230, 30);
        pinTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinTextField);

        login = new JButton("SIGN IN");
        login.setBounds(300, 300, 100, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.white);
        login.addActionListener(this);
        add(login);

        clear = new JButton("CLEAR");
        // 300 + 100 = 400
        clear.setBounds(430, 300, 100, 30);
        clear.setBackground(Color.BLACK);
        clear.setForeground(Color.white);
        clear.addActionListener(this);
        add(clear);

        signup = new JButton("SIGN UP");
        signup.setBounds(300, 350, 230, 30);
        signup.setBackground(Color.BLACK);
        signup.setForeground(Color.white);
        signup.addActionListener(this);
        add(signup);

        admin = new JButton("ADMINS VIEW");
        admin.setBounds(300, 400, 230, 30);
        admin.setBackground(Color.BLACK);
        admin.setForeground(Color.WHITE);
        admin.addActionListener(this);
        add(admin);

        getContentPane().setBackground(Color.white);  // Changing the background colour of Frame. 

        setSize(800, 480);  // setSize(width, height);  sets the size of JFrame.
        setVisible(true);// By default, Frame's visibility is false, is hidden from the user. So, this function is used.
        setLocation(350, 100);  // By default Frame opens in (0,0) axis. We can change it using setLocation(). x,y Axis where Frame will open
    }

    public void actionPerformed(ActionEvent ae) {
        // What action to do, if button is clicked.
        // ActionEvent class helps in finding on which component action was performed. For e.g., Suppose, which button was clicked.
        if (ae.getSource() == clear) {
            cardTextField.setText("");
            pinTextField.setText("");
        } else if (ae.getSource() == login) {
            Conn conn = null;
            try {
                conn = new Conn();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            String cardnumber = cardTextField.getText();
            String pinnnumber = pinTextField.getText();
            // We will compare the card number stored in the database is same or not to the card number entered by the user.
            try {
                // For DDL Command, we use executeQuery() function.
                String hashedPass = passwordHashing.hashPassword(pinTextField.getText());
                ResultSet rs = conn.getData("select * from signup where formno = '" + cardnumber + "' and pin = '" + hashedPass + "'");

                if (rs.next()) {
                    // If data matches.
                    String userType = rs.getString("type");
                    Session sess = Session.getInstance();
                    sess.setSignID(rs.getString("signID"));
                    sess.setDob(rs.getString("dob"));
                    sess.setGender(rs.getString("gender"));
                    sess.setFormno(rs.getString("formno"));

                    if ("admin".equals(userType)) {
                        setVisible(false);
                        new AdminDash().setVisible(true);
                    } else {
                        setVisible(false);
                        new Transactions(pinnnumber).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Card Number or Pin");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (ae.getSource() == signup) {
            setVisible(false);
            new SignupOne().setVisible(true);
        } else if (ae.getSource() == admin) {
            setVisible(false);
            new AdminDash().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new Login();
    }
}
