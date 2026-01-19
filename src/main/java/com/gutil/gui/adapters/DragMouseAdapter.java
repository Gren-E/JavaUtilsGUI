package com.gutil.gui.adapters;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse adapter class allowing a component to be dragged on screen.
 * @author Ewelina Gren
 * @version 1.0
 */
public class DragMouseAdapter extends MouseAdapter {

    /**
     * The difference in X value between the component's left top corner position and the cursor position on the component while pressed.
     */
    private int xDifference;

    /**
     * The difference in Y value between the component's left top corner position and the cursor position on the component while pressed.
     */
    private int yDifference;

    /**
     * The component to be moved on screen.
     */
    private final Component component;

    /**
     * The object constructor, assigns the component to be moved.
     * @param component the {@code Component} to be moved
     */
    public DragMouseAdapter(Component component) {
        this.component = component;
    }

    /**
     * Assigns the {@code xDifference} and {@code yDifference} variables based on the initial positions
     * of the component and the cursor when the mouse is first pressed.
     * @param event the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        xDifference = event.getXOnScreen() - component.getX();
        yDifference = event.getYOnScreen() - component.getY();
    }

    /**
     * Calculates and sets the component's new position based on the initial component and cursor positions,
     * and current cursor position.
     * @param event the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
        component.setLocation(event.getXOnScreen() - xDifference, event.getYOnScreen() - yDifference);
    }

}
