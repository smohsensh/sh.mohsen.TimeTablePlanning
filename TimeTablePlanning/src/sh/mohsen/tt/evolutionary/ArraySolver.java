package sh.mohsen.tt.evolutionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import sh.mohsen.tt.evolutionary.operators.ArrayMutation;
import sh.mohsen.tt.evolutionary.operators.MyArrayCrossOver;
import sh.mohsen.tt.general.Loader;
import sh.mohsen.tt.numbergen.IntegerGenerator;

public class ArraySolver {

	int teachersNum ;
	int intervalsNum ;
	int groupsNum ;

	final double mutationProbability = 0.24;
	final private int maxCrossOverPoints = 22;
	final private int terminationMinutes=0;
	final private int populationSize=250;
	
	
	File freetimes ;
	File teacher_num_interval ;
	File course_num_interval;
	File same_resource ;
	File same_course ;
	File init_data;
	
	Loader l;
	
	
	public ArraySolver() {
		super();
		
		freetimes =new File("freetimes.txt");
		teacher_num_interval = new File("teacherNum.txt");
		course_num_interval = new File("courseNum.txt");
		same_resource = new File("sameResource.txt");
		same_course = new File("sameCourse.txt");
		init_data = new File("initData.txt");
		l = new Loader(freetimes, teacher_num_interval, course_num_interval, same_resource, same_course, init_data);
		
		this.teachersNum = l.getTeacherNum();
		this.intervalsNum = l.getIntervalNum();
		this.groupsNum = l.getGroupNum();
		
	}




	
	
	
	public  void run() {
//		CandidateFactory<byte[]> factory = new ObjectArrayPermutationFactory<byte>(table);

		ArrayFactory factory = new ArrayFactory(teachersNum, intervalsNum, groupsNum);
		
		// Create a pipeline that applies cross-over then mutation.
//		ByteArrayCrossover crossover = new ByteArrayCrossover(
//				new IntegerGenerator(crossOverPoints), new ProbabilityGenerator(0.9));
		final FitnessEvaluator<byte[]> fitnessEvaluator = new ArrayEvaluator(teachersNum, intervalsNum, groupsNum, l);
		MyArrayCrossOver crossover = new MyArrayCrossOver(fitnessEvaluator,new IntegerGenerator(maxCrossOverPoints), teachersNum, intervalsNum, groupsNum);
		
		ArrayMutation mutation = new ArrayMutation(teachersNum, intervalsNum, groupsNum , new Probability(mutationProbability));
		
		
		List<EvolutionaryOperator<byte[]>> operators = new LinkedList<EvolutionaryOperator<byte[]>>();
		operators.add(mutation);
		operators.add(crossover);
		
		EvolutionaryOperator<byte[]> pipeline = new EvolutionPipeline<byte[]>(
				operators);

		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		Random rng = new MersenneTwisterRNG();

		EvolutionEngine<byte[]> engine = new GenerationalEvolutionEngine<byte[]>(
				factory, pipeline, fitnessEvaluator, selection, rng);
		
		engine.addEvolutionObserver(new EvolutionObserver<byte[]>()
				{
				    public void populationUpdate(PopulationData<? extends byte[]> data)
				    {
				    	byte[] b = data.getBestCandidate();
				    	int s = 0;
				    	for (int i = 0; i < b.length; i++) {
				    		if(b[i]==1)
				    			s++;
				    	}
				        System.out.println("Generation " + 
				                          data.getGenerationNumber() + " : "+
				                          s + " ones score:" +fitnessEvaluator.getFitness(b, null) );
				    }

					
				});
		
		byte[] result=null;
		
		if(terminationMinutes ==0)
			result = engine.evolve(populationSize, 0, new TargetFitness(0, false));
		else
			result = engine.evolve(populationSize, 0, new TargetFitness(0, false),new ElapsedTime(terminationMinutes*60000));
			
		try {
			printResult(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}



	private void printResult(byte[] result) throws IOException {
		Writer writer = null ;
		
		writer = new FileWriter(new File("gen_" + System.currentTimeMillis() + "_time_" + terminationMinutes +"_max_cr_point:_" + maxCrossOverPoints ));
	
		for (int tea = 0; tea < teachersNum; tea++) {
			for (int inter = 0; inter < intervalsNum; inter++) {
				for (int gr = 0; gr < groupsNum; gr++) {
					if( result[tea*intervalsNum*groupsNum+ inter*groupsNum + gr] ==1){
						String s = " teacher " + tea + " interval " + inter +   ", group " + gr + " =1";
						System.out.println(s );
						writer.write(s + "\n");
					}
				}
			}
		}
		writer.flush();
		writer.close();
	}

}


