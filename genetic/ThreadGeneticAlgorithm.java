package genetic;

import java.util.ArrayList;
import java.util.Random;

public class ThreadGeneticAlgorithm extends Thread {

	public Random generator = new Random();
	ArrayList<City> cities;
	ArrayList<Individual> populationToCrossover;

	public ThreadGeneticAlgorithm(ArrayList<Individual> populationToCrossover, ArrayList<City> cities) {
		super();
		this.populationToCrossover = populationToCrossover;
		this.cities = cities;
	}

	@Override
	public void run() {
		parallelGenetic(populationToCrossover, cities);
	}

	private void parallelGenetic(ArrayList<Individual> populationToCrossover, ArrayList<City> cities) {
		ArrayList<Individual> newPopulation = new ArrayList<Individual>();
		while (populationToCrossover.size() > 0) {
			Individual o1 = populationToCrossover.get(0);
			Individual o2 = populationToCrossover.get(1);
			Individual os1 = CX(o1, o2);
			Individual os2 = CX(o2, o1);
			os1.setEvaluation(evaluateIndividual(os1, cities));
			os2.setEvaluation(evaluateIndividual(os2, cities));
			if (os1.getEvaluation() < os2.getEvaluation())
				newPopulation.add(os1);
			else
				newPopulation.add(os2);
			populationToCrossover.remove(0);

			populationToCrossover.remove(0);
		}
		synchronized (ParallelGeneticAlgorithm.newPopulation) {
			ParallelGeneticAlgorithm.newPopulation.addAll(newPopulation);
		}

	}

	private Individual PMX(Individual o1, Individual o2) {
		Individual newIndividual = new Individual(cities.size());
		int[] genes = new int[cities.size()];
		int startDivision = cities.size() / 4;
		int endDivision = (cities.size() * 3) / 4;
		for (int i = startDivision; i <= endDivision; i++)
			genes[i] = o1.genes[i];
		for (int i = startDivision; i <= endDivision; i++) {
			boolean found = false;
			for (int j = startDivision; j <= endDivision && !found; j++) {
				if (o2.genes[i] == genes[j])
					found = true;

			}
			if (!found) {
				int newPosition = findPosition(i, o1, o2, startDivision, endDivision);
				genes[newPosition] = o2.genes[i];
			}
		}
		for (int i = 0; i < cities.size(); i++) {
			if (genes[i] == 0)
				genes[i] = o2.genes[i];
		}
		newIndividual.genes = genes;

		return newIndividual;
	}

	private int findPosition(int value, Individual o1, Individual o2, int startDivision, int endDivision) {
		boolean found = false;
		while (!found) {
			boolean foundValue = false;

			for (int i = 0; i < cities.size() && !foundValue; i++) {
				if (o1.genes[value] == o2.genes[i]) {
					foundValue = true;
					value = i;

					if (value < startDivision || value > endDivision) {
						found = true;
					}
				}
			}
		}
		return value;
	}

	private int evaluateIndividual(Individual individual, ArrayList<City> cities) {
		int evaluation = 0;
		for (int j = 0; j < (cities.size() - 1); j++) {

			City city1 = cities.get(individual.genes[j] - 1);
			City city2 = cities.get(individual.genes[j + 1] - 1);
			double sqr = (city1.getX() - city2.getX()) * (city1.getX() - city2.getX())
					+ (city1.getY() - city2.getY()) * (city1.getY() - city2.getY());
			int distance = (int) Math.round(Math.sqrt(sqr));
			evaluation += distance;
		}

		return evaluation;

	}

	private Individual CX(Individual o1, Individual o2) {
		int[] genes = new int[cities.size()];
		Individual o = new Individual(cities.size());
		ArrayList<Integer> genes1 = new ArrayList<>();
		ArrayList<Integer> genes2 = new ArrayList<>();

		for (int i = 0; i < cities.size(); i++) {
			genes1.add(o1.genes[i]);
			genes2.add(o2.genes[i]);
		}

		ArrayList<Integer> positionsToCopy = new ArrayList<>();
		int index = generator.nextInt(cities.size());
		int cycleStart = genes1.get(index);
		int relationStart = cycleStart;
		int cycleEnd = genes2.get(index);
		int relationEnd = cycleEnd;
		positionsToCopy.add(genes1.indexOf(relationStart));
		// int iterator = 0;
//		if(!isCorrect(o1)) {
//			
//			System.out.println("blad przed zapetleniem");
//		}
//
//		if(!isCorrect(o2)) {
//
//		
//			System.out.println("blad przed zapetleniem");
//		}

		while ((relationStart != relationEnd) && (cycleStart != cycleEnd)) {
			relationStart = cycleEnd;
//			if (relationStart < 0)
//				System.out.println("nie dzia³a -1");
//			if (genes1.indexOf(relationStart) < 0)
//				System.out.println("nie dzia³a -1 genes1.index");
//
//			if (genes1.indexOf(relationStart) < 0)
//				return o1;
//			iterator++;
//			if (iterator >= cities.size()) {
//				System.out.println("zapetlenie");
//				return o1;
//			}
			// System.out.println(relationStart+" "+relationEnd+" "+cycleStart+"
			// "+cycleEnd);
			relationEnd = genes2.get(genes1.indexOf(relationStart));
			cycleEnd = relationEnd;
			positionsToCopy.add(genes1.indexOf(relationStart));
		}

		for (int i = 0; i < cities.size(); i++) {
			genes[i] = positionsToCopy.contains(i) ? genes1.get(i) : genes2.get(i);

		}

		o.genes = genes;
//		if(!isCorrect(o)) {
//			System.out.println("zle krzy¿owanie");
//			return o1;
//		} 
		return o;

	}

	private boolean isCorrect(Individual individual) {
		for (int i = 1; i <= cities.size(); i++) {
			boolean znal = false;
			for (int j = 0; j < individual.genes.length && !znal; j++) {
				if (individual.genes[j] == i)
					znal = true;
			}
			if (!znal) {
				System.out.println("double " + i);
				return false;
			}
		}
		return true;
	}

}
