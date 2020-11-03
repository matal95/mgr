package nqueenForwardChecking;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

public class ParallelNQueens {

	public static int n = 15;
	public static int results = 0;
	public static int numThreads = 8;
	public static ArrayList<Solution> resultList = new ArrayList<Solution>();
	public static ArrayList<Solution> listForThreads[] = new ArrayList[numThreads];
	public static List<Thread> listaWatkow = new ArrayList<>();
	public static int iloscProb = 1;

	public void ForwardChecking(ArrayList<Integer> queens, ArrayList<Field> domain) {
		if (queens.size() == n) {
			results++;
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

	private static ArrayList<Field> createDomain() {
		ArrayList<Field> domain = new ArrayList<Field>();

		for (int row = 0; row < n; row++) {
			for (int column = 0; column < n; column++)
				domain.add(new Field(row, column));
		}

		return domain;
	}

	public void prepareForThreads() {

		for (int i = 0; i < n; i++) {
			ArrayList<Field> domain = new ArrayList<Field>(createDomain());
			ArrayList<Integer> queens = new ArrayList<Integer>();
			queens.add(i);
			resultList.add(new Solution(ChangeDomain(queens, domain), queens));
		}
		boolean repeat = true;
		if (resultList.size() % numThreads == 0)
			repeat = false;
		while (repeat) {
			int size = resultList.size();

			for (int i = 0; i < size; i++) {
				Solution solution = new Solution(resultList.get(i).getCurrentDomain(), resultList.get(i).getQueens());
				ArrayList<Field> row = chooseRow(solution.getQueens().size(), solution.getCurrentDomain());

				for (int field = 0; field < row.size(); field++) {
					ArrayList<Integer> queens = new ArrayList<Integer>(solution.getQueens());
					queens.add(row.get(field).y);
					ArrayList<Field> newDomain = ChangeDomain(queens, solution.getCurrentDomain());
					resultList.add(new Solution(newDomain, queens));
				}
			}
			for (int i = 0; i < size; i++)
				resultList.remove(0);
			if ((resultList.size() % numThreads == 0) || (resultList.size() % numThreads > (numThreads / 2 + 1)))
				repeat = false;
		}
	}

	private void sortForThreads() {

		for (int i = 0; i < numThreads; i++) {
			listForThreads[i] = new ArrayList<Solution>();
		}
		for (int i = 0; i < resultList.size(); i++) {
			listForThreads[i % numThreads].add(resultList.get(i));

		}

	}

	private void Wataki() {
		for (int i = 0; i < numThreads; i++) {

			listaWatkow.add(new ThreadNQueens(listForThreads[i], n));
		}
		for (Thread watek : listaWatkow) {
			watek.start();

		}
		while (!isAllThreadsAlreadyFinished(listaWatkow)) {
			try {
				Thread.currentThread().sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean isAllThreadsAlreadyFinished(List<Thread> listaWatkow2) {
		for (Thread thread : listaWatkow) {
			if (thread.getState() != State.TERMINATED) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		ParallelNQueens nqueens = new ParallelNQueens();

		long startTime;
		for (int thread = 1; thread <= 4; thread++) {
			numThreads = thread;
			System.out.println("iloœæ w¹tków: " + thread);
			for (int p = 12; p <= 15; p++) {

				long time = 0;
				n = p;
				for (int i = 0; i < iloscProb; i++) {
					results = 0;
					startTime = System.currentTimeMillis();
					listForThreads = new ArrayList[numThreads];
					nqueens.prepareForThreads();
					nqueens.sortForThreads();
					nqueens.Wataki();
					resultList.clear();

					listaWatkow.clear();
					time += System.currentTimeMillis() - startTime;
				}
				time /= iloscProb;
				System.out.println("Rozmiar planszy: " + n + ", iloœæ mo¿liwych rozwi¹zañ: " + results);
				System.out.println(results + " rozmiar planszy: " + n + " Œredni czas: " + time);
			}

		}
	}
}
