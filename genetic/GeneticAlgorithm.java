package genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {

	public static int tournament = 3;
	public static Random generator = new Random();
	public static double px = 0.7;
	public static double pm = 0.1;
	public static int popNumber = 500;
	public static int popSize = 1000;
	public static String dataFile = "Uruguay";
	public static ArrayList<City> cities = new ArrayList<City>();

	public static void main(String[] args) throws FileNotFoundException {
		cities.addAll(loadData());
		genetic();

	}

	public static void genetic() {
		System.out.println(popSize);
		ArrayList<Individual> population = initializePopulation();
		for (int generation = 2; generation <= popNumber; generation++) {
			long startTime = System.currentTimeMillis();
			ArrayList<Individual> new_population = new ArrayList<>();
			new_population = crossover(population);
			mutation(new_population);
			population = new_population;
			populationStats(generation, population);
			long finishTime = System.currentTimeMillis();
			// System.out.println("Time: "+(finishTime-startTime));

		}

	}

	private static int popStats(ArrayList<Individual> population) {
		int min = population.get(0).getEvaluation();
		int max = population.get(0).getEvaluation();
		double avg = population.get(0).getEvaluation();
		for (int i = 1; i < popSize; i++) {
			int evaluation = population.get(i).getEvaluation();
			avg += (double) evaluation;
			if (evaluation > max)
				max = evaluation;
			if (evaluation < min)
				min = evaluation;
		}
		return min;
	}

	private static ArrayList<Individual> initializePopulation() {
		ArrayList<Individual> population = new ArrayList<Individual>();
		population.addAll(createPopulation());
		evaluatePopulation(population);
		populationStats(1, population);
		return population;
	}

	private static void mutation(ArrayList<Individual> new_population) {
		inverseMutation(new_population);
	}

	private static void inverseMutation(ArrayList<Individual> new_population) {
		for (int i = 0; i < popSize; i++) {

			if (generator.nextDouble() < pm) {
				int start, end;

				int localization1 = generator.nextInt(cities.size());
				int localization2 = generator.nextInt(cities.size());
				if (localization1 > localization2) {
					end = localization1;
					start = localization2;
				} else {
					end = localization2;
					start = localization1;
				}
				ArrayList<Integer> inverseList = new ArrayList<Integer>();
				for (int m = end; m >= start; m--) {
					inverseList.add(new_population.get(i).genes[m]);
				}
				for (int m = start; m <= end; m++) {
					new_population.get(i).genes[m] = inverseList.get(0);
					inverseList.remove(0);
				}
			}
		}
	}

	private static void multipleMutation(ArrayList<Individual> new_population) {
		for (int i = 0; i < popSize; i++) {

			if (generator.nextDouble() < pm) {
				int value = cities.size() / 100;
				for (int j = 0; j < value; j++) {
					int localization1 = generator.nextInt(cities.size());
					int localization2 = generator.nextInt(cities.size());
					int swap = new_population.get(i).genes[localization1];

					new_population.get(i).genes[localization1] = new_population.get(i).genes[localization2];
					new_population.get(i).genes[localization2] = swap;
				}
			}

		}
	}

	private static void singleMutation(ArrayList<Individual> new_population) {
		for (int i = 0; i < popSize; i++) {

			if (generator.nextDouble() < pm) {
				int localization1 = generator.nextInt(cities.size());
				int localization2 = generator.nextInt(cities.size());
				int swap = new_population.get(i).genes[localization1];

				new_population.get(i).genes[localization1] = new_population.get(i).genes[localization2];
				new_population.get(i).genes[localization2] = swap;
			}

		}
	}

	private static ArrayList<Individual> crossover(ArrayList<Individual> population) {
		ArrayList<Individual> newPopulation = new ArrayList<Individual>();
		for (int i = 0; i < population.size(); i++) {
			Individual o1 = tournament(population);
			if (generator.nextDouble() < px) {

				Individual o2 = tournament(population);
				Individual os1 = CX(o1, o2);
				Individual os2 = CX(o2, o1);
				os1.setEvaluation(evaluateIndividual(os1));
				os2.setEvaluation(evaluateIndividual(os2));

				if (os1.getEvaluation() < os2.getEvaluation())
					newPopulation.add(os1);
				else
					newPopulation.add(os2);

			} else {
				newPopulation.add(o1);
			}
		}
		return newPopulation;
	}

	public static Individual PMX(Individual o1, Individual o2) {
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

	private static int findPosition(int value, Individual o1, Individual o2, int startDivision, int endDivision) {
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

	private static Individual CX(Individual o1, Individual o2) {
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
		while ((relationStart != relationEnd) && (cycleStart != cycleEnd)) {
			relationStart = cycleEnd;
			relationEnd = genes2.get(genes1.indexOf(relationStart));
			cycleEnd = relationEnd;
			positionsToCopy.add(genes1.indexOf(relationStart));
		}
		for (int i = 0; i < cities.size(); i++) {
			genes[i] = positionsToCopy.contains(i) ? genes1.get(i) : genes2.get(i);

		}

		o.genes = genes;
		return o;

	}

	private static Individual tournament(ArrayList<Individual> population) {
		int index = generator.nextInt(population.size());
		int evaluation = population.get(index).getEvaluation();
		for (int i = 0; i < tournament; i++) {
			int idx = generator.nextInt(population.size());
			if (population.get(idx).getEvaluation() < evaluation) {
				evaluation = population.get(idx).getEvaluation();
				index = idx;
			}
		}
		Individual o = new Individual(cities.size()); // tworzymy nowego osobnika
		o.setEvaluation(evaluation); // nadajemy mu cechy
		o.genes = population.get(index).genes;
		return o;
	}

	private static void populationStats(int generation, ArrayList<Individual> population) {
		int min = population.get(0).getEvaluation();
		int max = population.get(0).getEvaluation();
		double avg = population.get(0).getEvaluation();
		for (int i = 1; i < popSize; i++) {
			int evaluation = population.get(i).getEvaluation();
			avg += (double) evaluation;
			if (evaluation > max)
				max = evaluation;
			if (evaluation < min)
				min = evaluation;
		}
		avg = avg / popSize;
		System.out.println(generation + ": " + min + " " + avg + " " + max);
	}

	private static void evaluatePopulation(ArrayList<Individual> population) {
		for (int i = 0; i < popSize; i++) {
			population.get(i).setEvaluation(evaluateIndividual(population.get(i)));
		}
	}

	public static int evaluateIndividual(Individual individual) {
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

	private static boolean isCorrect(Individual individual) {
		for (int i = 1; i <= cities.size(); i++) {
			boolean znal = false;
			for (int j = 0; j < individual.genes.length && !znal; j++) {
				if (individual.genes[j] == i)
					znal = true;
			}
			if (!znal)
				return false;
		}
		return true;
	}

	private static ArrayList<Individual> createPopulation() {
		ArrayList<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < popSize; i++) {
			ArrayList<City> copyOfCities = new ArrayList<City>(cities);
			Individual individual = new Individual(cities.size());
			for (int j = 0; j < cities.size(); j++) {
				int index = generator.nextInt(copyOfCities.size());
				individual.genes[j] = copyOfCities.get(index).getCityNumber();
				copyOfCities.remove(index);
			}
			population.add(individual);
		}
		return population;
	}

	private static ArrayList<City> loadData() throws FileNotFoundException {
		ArrayList<City> cities = new ArrayList<City>();
		String path = "C:/Users/Aleksander/Desktop/testData/" + dataFile + ".txt";
		File file = new File(path);
		Scanner sc = new Scanner(file);
		String numberLines = sc.nextLine();
		int numberOfCities = Integer.parseInt(numberLines);
		System.out.println(numberOfCities);
		for (int j = 1; j <= numberOfCities; j++)
			cities.add(extractCity(sc.nextLine()));
		sc.close();
		return cities;
	}

	private static City extractCity(String line) {
		String[] tab = line.split(" ");
		int index = Integer.parseInt(tab[0]);
		double x = Double.parseDouble(tab[1]);
		double y = Double.parseDouble(tab[2]);
		return new City(index, x, y);
	}

}
