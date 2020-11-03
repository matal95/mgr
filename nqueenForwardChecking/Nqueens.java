package nqueenForwardChecking;

import java.util.ArrayList;

public class Nqueens {

	public static int n = 15;
	public static int results = 0;

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
		Field queen = new Field(queens.size()-1, queens.get(queens.size()-1));
		for(Field field: domain) {
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

	public static void main(String[] args) {
		Nqueens nqueens = new Nqueens();
		for(int i=10;i<=16;i++) {
			results=0;
			n = i;
			long startTime = System.currentTimeMillis();
			nqueens.ForwardChecking(new ArrayList<Integer>(), createDomain());
			long finishTime = System.currentTimeMillis();
			System.out.println("forward " + results);
			System.out.println(i+" czas  " + (finishTime - startTime) + " " + results);
		} 
	
	}

}
