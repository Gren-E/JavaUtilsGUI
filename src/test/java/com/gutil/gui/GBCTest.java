package com.gutil.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBCTest {

    @Test
    public void gbcTest() {
        GBC gbc = new GBC(1, 3, 2, 4);
        GBC gbc1 = new GBC(2,3);
        Assertions.assertEquals(1, gbc.gridx);
        Assertions.assertEquals(3, gbc.gridy);
        Assertions.assertEquals(2, gbc.gridwidth);
        Assertions.assertEquals(4, gbc.gridheight);
        Assertions.assertEquals(2, gbc1.gridx);
        Assertions.assertEquals(3, gbc1.gridy);

        gbc.setAnchor(GridBagConstraints.EAST);
        Assertions.assertEquals(GridBagConstraints.EAST, gbc.anchor);
        Assertions.assertNotEquals(GridBagConstraints.WEST, gbc.anchor);

        gbc.setFill(GridBagConstraints.BOTH);
        Assertions.assertEquals(GridBagConstraints.BOTH, gbc.fill);
        Assertions.assertNotEquals(GridBagConstraints.HORIZONTAL, gbc.fill);

        gbc.setInsets(50);
        gbc1.setInsets(2, 5, 4, 3);
        Assertions.assertEquals(new Insets(50, 50, 50, 50), gbc.insets);
        Assertions.assertEquals(new Insets(2, 5, 4, 3), gbc1.insets);

        gbc.setIpad(30, 20);
        Assertions.assertEquals(30, gbc.ipadx);
        Assertions.assertEquals(20, gbc.ipady);

        gbc.setWeight(2, 1);
        Assertions.assertEquals(2, gbc.weightx);
        Assertions.assertEquals(1, gbc.weighty);
    }
    
}
