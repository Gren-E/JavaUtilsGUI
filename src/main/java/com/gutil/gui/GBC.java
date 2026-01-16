package com.gutil.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Class with tools simplifying {@code GridBagConstraints} creation and modification.
 * @author Ewelina Gren
 * @version 1.0
 */
public class GBC extends GridBagConstraints {

	/**
	 * Constructs a {@code GBC} object with grid parameters.
	 * @param gridX where in OX should the cell start.
	 * @param gridY where in OY should the cell start.
	 */
	public GBC(int gridX, int gridY) {
		this.gridx = gridX;
		this.gridy = gridY;
	}
	
	/**
	 * Constructs a {@code GBC} object with grid parameters containing also their width and height.
	 * @param gridX where in OX should the cell start.
	 * @param gridY where in OY should the cell start.
	 * @param gridWidth how many columns the cell contains.
	 * @param gridHeight how many rows the cell contains.
	 */
	public GBC(int gridX, int gridY, int gridWidth, int gridHeight) {
		this.gridx = gridX;
		this.gridy = gridY;
		this.gridwidth = gridWidth;
		this.gridheight = gridHeight;
	}

	/**
	 * Sets anchor for the {@code GridBagLayout} constraints.
	 * @param anchor anchor of the constraints.
	 * @return this {@code GBC} instance.
	 */
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	/**
	 * Sets fill for the {@code GridBagLayout} constraints.
	 * @param fill fill of the constraints.
	 * @return this {@code GBC} instance.
	 */
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}

	/**
	 * Sets weight for the {@code GridBagLayout} constraints.
	 * @param weightX weight of X_AXIS of the constraints.
	 * @param weightY weight of Y_AXIS of the constraints.
	 * @return this {@code GBC} instance.
	 */
	public GBC setWeight(double weightX, double weightY) {
		this.weightx = weightX;
		this.weighty = weightY;
		return this;
	}

	/**
	 * Sets insets for the {@code GridBagLayout} constraints, all with the same value.
	 * @param inset size of insets of the constraints.
	 * @return this {@code GBC} instance.
	 */
	public GBC setInsets(int inset) {
		this.insets = new Insets(inset, inset, inset, inset);
		return this;
	}

	/**
	 * Sets insets for the {@code GridBagLayout} constraints.
	 * @param top size of top inset.
	 * @param left size of left inset.
	 * @param bottom size of bottom inset.
	 * @param right size of right inset.
	 * @return this {@code GBC} instance.
	 */
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	/**
	 * Sets ipads for the {@code GridBagLayout} constraints.
	 * @param ipadX ipad of X_AXIS of the constraints.
	 * @param ipadY ipad of Y_AXIS of the constraints.
	 * @return this {@code GBC} instance.
	 */
	public GBC setIpad(int ipadX, int ipadY) {
		this.ipadx = ipadX;
		this.ipady = ipadY;
		return this;
	}	

}
