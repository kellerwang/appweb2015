package com.model;

public class ShapeletIndexModel {
	private int row;
	private int column;
	private double[] ftr = null;

	public ShapeletIndexModel(int row, int column, double[] ftr) {
		super();
		this.row = row;
		this.column = column;
		this.ftr = ftr;
	}

	public ShapeletIndexModel(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public double[] getFtr() {
		return ftr;
	}

	public void setFtr(double[] ftr) {
		this.ftr = ftr;
	}

}
