package com.fanlehai.java.algorithm;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ScrollPaneConstants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fanlehai.java.util.Print;

class Node {
	Node left = null;
	Node right = null;
	String name;

	Node(String name, Node left, Node right) {
		this.left = left;
		this.right = right;
		this.name = name;
	}
}

public class TreeTest {

	// 前序遍历
	public void frontFind(Node node) {
		if (node == null) {
			return;
		}
		Print.printnb(node.name);
		frontFind(node.left);
		frontFind(node.right);
	}

	// 中序遍历
	public void midFind(Node node) {
		if (node == null) {
			return;
		}
		midFind(node.left);
		Print.printnb(node.name);
		midFind(node.right);
	}

	// 后序遍历
	public void backFind(Node node) {
		if (node == null) {
			return;
		}
		backFind(node.left);
		backFind(node.right);
		Print.printnb(node.name);
	}

	Node root;

	@Before
	public void dataInit() {
		Node nodeF = new Node("F", null, null);
		Node nodeE = new Node("E", null, null);
		Node nodeD = new Node("D", null, null);
		Node nodeC = new Node("C", nodeF, null);
		Node nodeB = new Node("B", nodeD, nodeE);
		root = new Node("A", nodeB, nodeC);
	}

	@Test
	public void recursionFindTest() {
		// 递归实现3中遍历
		Print.print("*******用递归方式遍历树*******");

		Print.printnb("先序遍历：");
		frontFind(root);
		Print.print();

		Print.printnb("中序遍历：");
		midFind(root);
		Print.print();

		Print.printnb("后序遍历：");
		backFind(root);
		Print.print();
	}

	// 下面用栈来实现递归
	// 先序
	public void frontStack(Node node) {
		if (node == null) {
			return;
		}
		LinkedList<Node> stackNode = new LinkedList<>();
		stackNode.add(node);
		while (!stackNode.isEmpty()) {
			Node nodeTemp = stackNode.pop();
			Print.printnb(nodeTemp.name);
			if (nodeTemp.right != null) {
				stackNode.push(nodeTemp.right);
			}
			if (nodeTemp.left != null) {
				stackNode.push(nodeTemp.left);
			}
		}
	}

	// 中序
	public void midStack(Node node) {
		if (node == null) {
			return;
		}
		LinkedList<Node> stackNode = new LinkedList<>();
		Node nodeTemp = node;
		while (nodeTemp != null || stackNode.size() > 0) {
			while (nodeTemp != null) {
				stackNode.push(nodeTemp);
				nodeTemp = nodeTemp.left;
			}
			nodeTemp = stackNode.pop();
			Print.printnb(nodeTemp.name);
			nodeTemp = nodeTemp.right;
		}
	}

	// 后序======没有整明白
	public void backStack(Node node) {
		if (node == null) {
			return;
		}
		LinkedList<Node> stackNode = new LinkedList<>();
		LinkedList<Node> stackResult = new LinkedList<>();
		stackNode.add(node);
		while (stackNode.size() > 0) {
			Node tmp = stackNode.pop();
			stackResult.push(tmp);
			if (tmp.left != null) {
				stackNode.push(tmp.left);
			}
			if (tmp.right != null) {
				stackNode.push(tmp.right);
			}
		}
		while (stackResult.size() > 0) {
			Print.printnb(stackResult.pop().name);
		}
	}

	@Test
	public void stackFindTest() {
		Print.print("*******用迭代方式遍历树*******");
		Print.printnb("先序遍历：");
		frontStack(root);
		Print.print();

		Print.printnb("中序遍历：");
		midStack(root);
		Print.print();

		Print.printnb("后序遍历：");
		backStack(root);
		Print.print();
	}

	// 上面都是对树的深度优先搜索，下面实现对树的广度优先搜索
	public void wideFind(Node node) {
		if (node == null) {
			return;
		}
		LinkedList<Node> stack = new LinkedList<>();
		stack.add(node);
		while (!stack.isEmpty()) {
			Node tmp = stack.poll();
			Print.printnb(tmp.name);
			if (tmp.left != null) {
				stack.add(tmp.left);
			}
			if (tmp.right != null) {
				stack.add(tmp.right);
			}
		}
	}

	@Test
	public void wideFindTest() {
		Print.print("*******广度优先搜索测试*******");
		wideFind(root);
	}

}
