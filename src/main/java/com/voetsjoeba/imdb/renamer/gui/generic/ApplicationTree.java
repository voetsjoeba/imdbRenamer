package com.voetsjoeba.imdb.renamer.gui.generic;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Extends JTree to support additionally functionality.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class ApplicationTree extends JTree {
	
	public ApplicationTree() {
		super();
	}
	
	public ApplicationTree(Hashtable<?, ?> value) {
		super(value);
	}
	
	public ApplicationTree(Object[] value) {
		super(value);
	}
	
	public ApplicationTree(TreeModel newModel) {
		super(newModel);
	}
	
	public ApplicationTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}
	
	public ApplicationTree(TreeNode root) {
		super(root);
	}
	
	public ApplicationTree(Vector<?> value) {
		super(value);
	}
	
	public void expandAllNodes(boolean expanded) {
		TreeNode rootNode = (TreeNode) getModel().getRoot();
		expandAllNodes(new TreePath(rootNode), true);
	}
	
	@SuppressWarnings({"rawtypes"})
	protected void expandAllNodes(TreePath parent, boolean expanded) {
		
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		
		if(node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAllNodes(path, expanded);
			}
		}
		
		if(expanded) {
			expandPath(parent);
		} else {
			collapsePath(parent);
		}
		
	}
	
}
