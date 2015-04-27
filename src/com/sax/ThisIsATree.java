package com.sax;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ThisIsATree {
	private String[] array = { "root", "A", "B", "C", "D" };
	static List<Node> nodeList = null;// 节点序列

	/*
	 * 节点
	 */
	class Node {
		Node childA;
		Node childB;
		Node childC;
		Node childD;
		String Index;
		List<String> windowNumList = new ArrayList<String>();

		Node(String Index) {
			childA = null;
			childB = null;
			childC = null;
			childD = null;
			this.Index = Index;
		}

		public void setValue(String windowNum) {
			this.windowNumList.add(windowNum);
		}
	}

	/*
	 * 生成树
	 */
	public void createTree() {
		nodeList = new LinkedList<Node>();
		// 生成所有节点
		for (int iA = 0; iA < 5; iA++) {
			if (iA == 0) {
				for (int iB = 0; iB < 5; iB++) {
					nodeList.add(new Node(array[iB]));
				}
			}
			if (iA == 2) {
				for (int iB = 1; iB < 5; iB++) {
					for (int iC = 1; iC < 5; iC++) {
						nodeList.add(new Node(array[iB] + array[iC]));
					}

				}
			}
			if (iA == 3) {
				for (int iB = 1; iB < 5; iB++) {
					for (int iC = 1; iC < 5; iC++) {
						for (int iD = 1; iD < 5; iD++) {
							nodeList.add(new Node(array[iB] + array[iC]
									+ array[iD]));
						}
					}
				}
			}
			if (iA == 4) {
				for (int iB = 1; iB < 5; iB++) {
					for (int iC = 1; iC < 5; iC++) {
						for (int iD = 1; iD < 5; iD++) {
							for (int iE = 1; iE < 5; iE++) {
								nodeList.add(new Node(array[iB] + array[iC]
										+ array[iD] + array[iE]));
							}
						}
					}
				}
			}
		}
		// 建立四叉树
		for (int parentIndex = 0; parentIndex < 85; parentIndex++) {
			nodeList.get(parentIndex).childA = nodeList
					.get(parentIndex * 4 + 1);
			nodeList.get(parentIndex).childB = nodeList
					.get(parentIndex * 4 + 2);
			nodeList.get(parentIndex).childC = nodeList
					.get(parentIndex * 4 + 3);
			nodeList.get(parentIndex).childD = nodeList
					.get(parentIndex * 4 + 4);
		}
	}

	/*
	 * 前序遍历
	 */
	public static void preOrderTraverse(Node node) throws IOException {
		String fileurl = "";
		String content = "";
		if (node == null) {
			return;
		}
		System.out.println(node.Index + " ");
		if (node.windowNumList != null) {
			fileurl = "./tree/" + node.Index + ".txt";
			for (int i = 0; i < node.windowNumList.size(); i++) {
				System.out.println(node.windowNumList.get(i));
				content = content + node.windowNumList.get(i) + "\r\n";
				FileOutputStream fos = new FileOutputStream(fileurl);
				fos.write(content.getBytes());
				fos.close();
			}

		}
		preOrderTraverse(node.childA);
		preOrderTraverse(node.childB);
		preOrderTraverse(node.childC);
		preOrderTraverse(node.childD);
	}
}
