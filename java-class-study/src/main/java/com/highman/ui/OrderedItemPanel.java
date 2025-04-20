package com.highman.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A panel that displays items that can be reordered using up and down buttons.
 */
public class OrderedItemPanel extends JPanel {
    private final List<String> items;
    private final List<JPanel> itemPanels;
    private static final Color BUTTON_ENABLED_COLOR = new Color(70, 130, 180); // Steel blue
    private static final Color BUTTON_DISABLED_COLOR = new Color(200, 200, 200);
    private static final Font ITEM_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final int PANEL_PADDING = 10;
    
    /**
     * Creates a new OrderedItemPanel with the given items.
     * @param items The items to display
     */
    public OrderedItemPanel(List<String> items) {
        this.items = new ArrayList<>(items);
        this.itemPanels = new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        
        // Add instructions at the top
        JLabel instructionsLabel = new JLabel(
            "<html><div style='text-align: center; margin-bottom: 10px;'>" +
            "<b>Arrange the items in the correct order:</b><br>" +
            "<span style='color: #666666; font-size: 0.9em;'>" +
            "Use the ↑ and ↓ buttons to move items up or down</span></div></html>"
        );
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(instructionsLabel);
        add(Box.createVerticalStrut(10));
        
        createItemPanels();
    }
    
    /**
     * Creates the panels for each item.
     */
    private void createItemPanels() {
        // Remove existing panels but keep the instructions
        Component instructions = getComponent(0);
        Component spacing = getComponent(1);
        removeAll();
        add(instructions);
        add(spacing);
        
        itemPanels.clear();
        
        // Create a container panel for all items
        JPanel itemsContainer = new JPanel();
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        itemsContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        for (int i = 0; i < items.size(); i++) {
            JPanel panel = createItemPanel(i);
            itemPanels.add(panel);
            itemsContainer.add(panel);
            if (i < items.size() - 1) {
                itemsContainer.add(Box.createVerticalStrut(5));
            }
        }
        
        add(itemsContainer);
        revalidate();
        repaint();
    }
    
    /**
     * Creates a panel for a single item.
     * @param index The index of the item
     * @return The created panel
     */
    private JPanel createItemPanel(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panel.setBackground(Color.WHITE);
        
        // Create label with number and text
        JLabel label = new JLabel((index + 1) + ". " + items.get(index));
        label.setFont(ITEM_FONT);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        
        // Create styled buttons
        JButton upButton = createStyledButton("↑", index > 0);
        JButton downButton = createStyledButton("↓", index < items.size() - 1);
        
        upButton.addActionListener(e -> moveItem(index, index - 1));
        downButton.addActionListener(e -> moveItem(index, index + 1));
        
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates a styled button with the given text and enabled state.
     */
    private JButton createStyledButton(String text, boolean enabled) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isEnabled()) {
                    g2.setColor(getModel().isPressed() ? BUTTON_ENABLED_COLOR.darker() : BUTTON_ENABLED_COLOR);
                } else {
                    g2.setColor(BUTTON_DISABLED_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(isEnabled() ? Color.WHITE : Color.GRAY);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);
                
                g2.dispose();
            }
        };
        
        button.setEnabled(enabled);
        button.setPreferredSize(new Dimension(36, 36));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        
        return button;
    }
    
    /**
     * Moves an item from one position to another.
     * @param fromIndex The current index of the item
     * @param toIndex The target index
     */
    private void moveItem(int fromIndex, int toIndex) {
        if (toIndex < 0 || toIndex >= items.size()) {
            return;
        }
        
        String movedItem = items.remove(fromIndex);
        items.add(toIndex, movedItem);
        
        createItemPanels();
    }
    
    /**
     * Gets the current order of items.
     * @return The list of items in their current order
     */
    public List<String> getItems() {
        return new ArrayList<>(items);
    }
} 