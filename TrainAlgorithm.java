package com.weka.trainandtest;
/**
This code takes reference from Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
This class basically performs training on the data provided in ArrDelayTrain.arff file
and outputs the ArrDelayModel.model model file which is used by the TestAlgorithm
to predict the result of test instance.
*/
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class TrainAlgorithm {

	Instances trainData;
	// a WEKA filter which converts line of text to vector of strings
	//StringToWordVector filter;							
	
	/* FilteredClassifier is the class for running an arbitrary classifier on data that has been passed through an arbitrary filter. 
	 * Like the classifier, the structure of the filter is based exclusively on the training data and test instances 
	 * will be processed by the filter without changing their structure.
	 */
	FilteredClassifier classifier;						
	
	public void loadDataset(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			System.out.println("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Error reading file: " + fileName);
		}
	}

	/* This function evaluates the untrained algorithm. WEKA documentation suggests that evaluating the trained classifier could
	 * result in misleading results.
	*/
	public void evaluate() {
		try {
			// set the class attribute SPAM, HAM
			trainData.setClassIndex(trainData.numAttributes()-1);						
			classifier = new FilteredClassifier();
			classifier.setClassifier(new NaiveBayes());			
			Evaluation eval = new Evaluation(trainData);

			// evaluate on training data using 4 fold cross validation 
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			System.out.println("===== Evaluating on filtered (training) dataset =====\n");
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println("\n=====================================================");
		}
		catch (Exception e) {
			System.out.println("Error evaluating the algorithm");
		}
	}
	
	// This function trains the classification algorithm - Support Vector Machine
	public void learn() {
		try {
			trainData.setClassIndex(trainData.numAttributes()-1);
			classifier = new FilteredClassifier();
			classifier.setClassifier(new NaiveBayes());
			System.out.println("===== Training on filtered (training) dataset done =====\n");
			classifier.buildClassifier(trainData);
			System.out.println(classifier);
			System.out.println("\n==========================================================");
		}
		catch (Exception e) {
			System.out.println("Error training algorithm");
		}
	}

	// Save the trained model
	public void saveModel(String fileName) {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
 			System.out.println("===== Saved model: " + fileName + " =====");
        } 
		catch (IOException e) {
			System.out.println("Error writing: " + fileName);
		}
	}

	
	public static void main (String[] args) {

		TrainAlgorithm learner;
		learner = new TrainAlgorithm();
		learner.loadDataset("ArrDelayTrain.arff");	// load the dataset file
		learner.evaluate();
		learner.learn();
		learner.saveModel("ArrDelayModel.model");	// save the trained model so that TestAlgorithm can use that
	}
}