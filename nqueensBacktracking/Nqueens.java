package nqueensBacktracking;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

public class Nqueens {

	public static int n = 14;
	public static int results = 0;
	public static long startTime;
	public static long finishTime;
	public static int numThreads = 8;
	public static ArrayList<ArrayList<Integer>> resultList;
	public static ArrayList<ListForThread> listForThreads;
	public static ArrayList<Integer> wyniki;
	public static List<Thread> listaWatkow = new ArrayList<>();
	public static int iloscProb = 1;

	public void PrepareBoard() {
		resultList = new ArrayList<ArrayList<Integer>>();
		listForThreads = new ArrayList<ListForThread>();

		results = 0;
	}

	public void Backtracking(ArrayList<Integer> queens) {
		if (queens.size() == n) {
			results++;
		} else {
			for (int column = 0; column < n; column++) {
				boolean possible = possibleToPut(queens, column);
				if (possible) {
					queens.add(column);
					Backtracking(queens);
					queens.remove(queens.size() - 1);
				}
			}
		}
	}

	private static boolean possibleToPut(ArrayList<Integer> queens, int column) {
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

	public static void main(String[] args) {
		Nqueens nqueens = new Nqueens();

		for (int thread = 1; thread <= 8; thread++) {
			numThreads = thread;
			System.out.println("iloœæ w¹tków: " + thread);
			for (int p = 12; p <= 15; p++) {

				long time = 0;
				n = p;
				for (int i = 0; i < iloscProb; i++) {
					results = 0;
					startTime = System.currentTimeMillis();
					nqueens.PrepareBoard();
					nqueens.prepareForThreads();
					nqueens.sortForThreads();
					nqueens.allThreads();
					resultList.clear();
					listForThreads.clear();
					listaWatkow.clear();
					time += System.currentTimeMillis() - startTime;
				}
				time /= iloscProb;
				System.out.println("Rozmiar planszy: " + n + ", iloœæ mo¿liwych rozwi¹zañ: " + results);
				System.out.println(results + " rozmiar planszy: " + n + " Œredni czas: " + time);
			}
		}
	}

	public void prepareForThreads() {

		for (int i = 0; i < n; i++) {
			resultList.add(new ArrayList<Integer>());
			resultList.get(resultList.size() - 1).add(i);
		}
		boolean repeat = true;
		if (resultList.size() % numThreads == 0)
			repeat = false;
		while (repeat) {
			int size = resultList.size();
			for (int i = 0; i < size; i++) {
				ArrayList<Integer> newList = new ArrayList<Integer>(resultList.get(i));

				for (int column = 0; column < n; column++) {
					boolean possible = possibleToPut(newList, column);
					if (possible) {
						newList.add(column);
						ArrayList<Integer> n_l = new ArrayList<Integer>(newList);
						resultList.add(n_l);
						newList.remove(newList.size() - 1);
					}
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
			listForThreads.add(new ListForThread());
		}
		for (int i = 0; i < resultList.size(); i++) {
			listForThreads.get(i % numThreads).resultList.add(resultList.get(i));
		}

	}

	private void allThreads() {
		for (int i = 0; i < numThreads; i++) {

			listaWatkow.add(new ThreadNQueens(listForThreads.get(i), n));
		}
		for (Thread thread : listaWatkow) {
			thread.start();

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
		for (Thread watek : listaWatkow) {
			if (watek.getState() != State.TERMINATED) {
				return false;
			}
		}
		return true;
	}

	private static void countWynik() {
		int wynik = 0;
		for (int watek = 0; watek < numThreads; watek++) {
			wynik += wyniki.get(watek);
		}
		System.out.println(wynik);
	}

}
