package genetic;

public class Individual {

	public int[] genes;
	private int evaluation;

	public Individual(int size) {
		genes = new int[size];
		evaluation = 0;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}
}
