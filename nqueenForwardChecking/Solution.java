package nqueenForwardChecking;

import java.util.ArrayList;

public class Solution {

	private ArrayList<Field> currentDomain;
	private ArrayList<Integer> queens;

	public Solution(ArrayList<Field> currentDomain, ArrayList<Integer> queens) {
		super();
		this.currentDomain = currentDomain;
		this.queens = queens;
	}

	public ArrayList<Field> getCurrentDomain() {
		return currentDomain;
	}

	public void setCurrentDomain(ArrayList<Field> currentDomain) {
		this.currentDomain = currentDomain;
	}

	public ArrayList<Integer> getQueens() {
		return queens;
	}

	public void setQueens(ArrayList<Integer> queens) {
		this.queens = queens;
	}

}
