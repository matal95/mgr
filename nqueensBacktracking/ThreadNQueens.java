package nqueensBacktracking;

import java.util.ArrayList;

public class ThreadNQueens extends Thread {
	int n;
	int result = 0;
	ListForThread boards;

	public ThreadNQueens(ListForThread boards, int n) {
		super();
		this.boards = boards;
		this.n = n;

	}

	@Override
	public void run() {
		for (int i = 0; i < boards.resultList.size(); i += 1) {

			Backtracking(boards.resultList.get(i), n);
		}

		Nqueens.results += this.result;

	}

	private void Backtracking(ArrayList<Integer> queens, int n) {
		if (queens.size() == n) {
			this.result += 1;
		} else {
			for (int column = 0; column < n; column++) {
				boolean posible = possibleToPut(queens, column);
				if (posible) {

					queens.add(column);
					Backtracking(queens, n);
					queens.remove(queens.size() - 1);
				}
			}
		}

	}

	private boolean possibleToPut(ArrayList<Integer> queens, int column) {
		int iterator = 0;
		for (Integer queen : queens) {
			if (queen == column)
				return false;
			if (Math.abs(iterator - queens.size()) == Math.abs(column - queen))
				return false;
			iterator++;
		}
		return true;
	}

	public String toString() {
		return "";
	}
}
