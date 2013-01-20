package org.skarb.log.pile.client.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: skarb
 * Date: 17/01/13
 */
public class Window extends JFrame {

    final TypeLogger logger;
    private JTextField message;
    private JComboBox exception;
    private JTextField messageException;

    public Window(final TypeLogger clazz) {
        super();
        this.logger = clazz;

        build();

    }

    private void build() {
        setTitle(" Logger :" + logger.create().title());
        setSize(450, 250);
        setLocationRelativeTo(null);
        // setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel contentPane = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(3, 3, 3, 3);
        contentPane.setLayout(new GridBagLayout());
        JPanel jPanel = new JPanel(new FlowLayout());
        jPanel.add(new JLabel("Message : "));
        message = new JTextField(25);
        jPanel.add(message);
        contentPane.add(jPanel, gbc);
        JPanel jPanel1 = new JPanel();
        jPanel1.add(new JLabel("Exception : "));

        exception = new JComboBox<String>(new String[]{"", NullPointerException.class.getName(),
                NumberFormatException.class.getName()});
        jPanel1.add(exception);
        contentPane.add(jPanel1, gbc);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(new JLabel("Message of Exception : "));


        messageException = new JTextField(25);
        jPanel2.add(messageException);
        contentPane.add(jPanel2, gbc);

        JPanel bPanel = new JPanel(new FlowLayout());
        JButton jButton = new JButton("Call logger");

        bPanel.add(jButton, gbc);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Error error = logger.create();

                    error.setException(exception.getSelectedItem().toString());
                    error.setMessage(message.getText());
                    error.setMessageException(messageException.getText());

                    error.dolog();
                } catch (Exception ex) {
                    throw new IllegalStateException("error", ex);
                }
            }
        });
        contentPane.add(bPanel, gbc);
        setContentPane(contentPane);
    }
}
