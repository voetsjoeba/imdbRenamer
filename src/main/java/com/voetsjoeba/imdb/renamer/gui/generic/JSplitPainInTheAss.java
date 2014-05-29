package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JSplitPane;

public class JSplitPainInTheAss {
	
	public static JSplitPane setDividerLocation(final JSplitPane splitter, final double proportion) {
		if(splitter.isShowing()) {
			if(splitter.getWidth() > 0 && splitter.getHeight() > 0) {
				splitter.setDividerLocation(proportion);
			} else {
				splitter.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent ce) {
						splitter.removeComponentListener(this);
						setDividerLocation(splitter, proportion);
					}
				});
			}
		} else {
			splitter.addHierarchyListener(new HierarchyListener() {
				@Override
				public void hierarchyChanged(HierarchyEvent e) {
					if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && splitter.isShowing()) {
						splitter.removeHierarchyListener(this);
						setDividerLocation(splitter, proportion);
					}
				}
			});
		}
		return splitter;
	}
	
}