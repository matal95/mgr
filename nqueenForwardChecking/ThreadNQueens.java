package nqueenForwardChecking;

import java.util.ArrayList;

public class ThreadNQueens extends Thread {
	int n;
	int result = 0;
	ArrayList<Solution> solutions;

	public ThreadNQueens(ArrayList<Solution> solutions, int n) {
		super();
		this.solutions = solutions;
		this.n = n;

	}

	@Override
	public void run() {
		for (int i = 0; i < solutions.size(); i += 1) {

			ForwardChecking(solutions.get(i).getQueens(), solutions.get(i).getCurrentDomain());
		}
		ParallelNQueens.results += this.result;

	}

	public void ForwardChecking(ArrayList<Integer> queens, ArrayList<Field> domain) {
		if (queens.size() == n) {
			this.result += 1;
		} else {
			ArrayList<Field> row = chooseRow(queens.size(), domain);
			for (int field = 0; field < row.size(); field++) {

				queens.add(row.get(field).y);
				ArrayList<Field> newDomain = ChangeDomain(queens, domain);
				ForwardChecking(queens, newDomain);
				queens.remove(queens.size() - 1);

			}
		}
	}

	private ArrayList<Field> chooseRow(int row, ArrayList<Field> domain) {
		ArrayList<Field> fieldsInRow = new ArrayList<>();
		for (Field field : domain) {
			if (field.x == row) {
				fieldsInRow.add(field);
			}
		}
		return fieldsInRow;
	}

	private ArrayList<Field> ChangeDomain(ArrayList<Integer> queens, ArrayList<Field> domain) {
		ArrayList<Field> newDomain = new ArrayList<Field>();
		Field queen = new Field(queens.size() - 1, queens.get(queens.size() - 1));
		for (Field field : domain) {
			if (field.x == queen.x) {
				continue;
			}
			if (field.y == queen.y) {
				continue;
			}
			if (Math.abs(field.x - queen.x) == Math.abs(field.y - queen.y)) {
				continue;
			}
			newDomain.add(field);
		}
		return newDomain;
	}
}