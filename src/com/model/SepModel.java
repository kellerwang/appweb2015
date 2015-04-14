package com.model;

public class SepModel implements Comparable<SepModel> {
	private Double gap;
	private Double corr;
	private Double q;// q=length(ind1)/length(ind2)
	private int ql; // ql= qlen
	private int index; // index

	public SepModel(Double gap, Double corr, Double q, int ql, int index) {
		this.gap = gap;
		this.corr = corr;
		this.q = q;
		this.ql = ql;
		this.index = index;
	}

	public Double getGap() {
		return gap;
	}

	public void setGap(Double gap) {
		this.gap = gap;
	}

	public Double getCorr() {
		return corr;
	}

	public void setCorr(Double corr) {
		this.corr = corr;
	}

	public Double getQ() {
		return q;
	}

	public void setQ(Double q) {
		this.q = q;
	}

	public int getQl() {
		return ql;
	}

	public void setQl(int ql) {
		this.ql = ql;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(SepModel o) {
		// TODO Auto-generated method stub
		return o.getGap().compareTo(this.getGap());
	}

}
