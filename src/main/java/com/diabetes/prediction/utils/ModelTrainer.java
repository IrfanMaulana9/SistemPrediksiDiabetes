package com.diabetes.prediction.utils;

import com.diabetes.prediction.model.DiabetesPredictor;
import com.diabetes.prediction.model.Patient;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Model training utility using Logistic Regression with Gradient Descent
 */
public class ModelTrainer {
    private static final double LEARNING_RATE = 0.01;
    private static final int MAX_ITERATIONS = 1000;
    private static final double CONVERGENCE_THRESHOLD = 1e-6;

    /**
     * Train logistic regression model on patient data
     */
    public DiabetesPredictor trainModel(List<Patient> patients) {
        // Print dataset statistics
        DataLoader.printDatasetStatistics(patients);

        // Shuffle data
        Collections.shuffle(patients, new Random(42));

        // Calculate feature statistics for normalization
        double[] featureMeans = calculateFeatureMeans(patients);
        double[] featureStds = calculateFeatureStds(patients, featureMeans);

        // Initialize weights and bias
        int numFeatures = 8;
        double[] weights = new double[numFeatures];
        double bias = 0.0;
        Random random = new Random(42);

        // Initialize weights with small random values
        for (int i = 0; i < numFeatures; i++) {
            weights[i] = random.nextGaussian() * 0.01;
        }

        // Gradient descent training
        double prevCost = Double.MAX_VALUE;

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            double[] weightGradients = new double[numFeatures];
            double biasGradient = 0.0;
            double cost = 0.0;

            // Calculate gradients and cost
            for (Patient patient : patients) {
                double[] features = normalizeFeatures(patient.getFeatures(), featureMeans, featureStds);
                double prediction = sigmoid(dotProduct(weights, features) + bias);
                double error = prediction - patient.getOutcome();

                // Update gradients
                for (int i = 0; i < numFeatures; i++) {
                    weightGradients[i] += error * features[i];
                }
                biasGradient += error;

                // Calculate cost (log loss)
                double y = patient.getOutcome();
                cost += -(y * Math.log(Math.max(prediction, 1e-15)) +
                        (1 - y) * Math.log(Math.max(1 - prediction, 1e-15)));
            }

            // Average gradients
            int dataSize = patients.size();
            for (int i = 0; i < numFeatures; i++) {
                weightGradients[i] /= dataSize;
            }
            biasGradient /= dataSize;
            cost /= dataSize;

            // Update weights and bias
            for (int i = 0; i < numFeatures; i++) {
                weights[i] -= LEARNING_RATE * weightGradients[i];
            }
            bias -= LEARNING_RATE * biasGradient;

            // Check for convergence
            if (Math.abs(prevCost - cost) < CONVERGENCE_THRESHOLD) {
                System.out.println("Converged after " + (iteration + 1) + " iterations");
                break;
            }
            prevCost = cost;

            // Print progress every 100 iterations
            if ((iteration + 1) % 100 == 0) {
                System.out.println("Iteration " + (iteration + 1) + ", Cost: " + String.format("%.6f", cost));
            }
        }

        return new DiabetesPredictor(weights, bias, featureMeans, featureStds);
    }

    /**
     * Evaluate model performance
     */
    public void evaluateModel(DiabetesPredictor predictor, List<Patient> patients) {
        int truePositives = 0, trueNegatives = 0, falsePositives = 0, falseNegatives = 0;

        for (Patient patient : patients) {
            boolean actualDiabetes = patient.getOutcome() == 1;
            boolean predictedDiabetes = predictor.predict(patient);

            if (actualDiabetes && predictedDiabetes) truePositives++;
            else if (!actualDiabetes && !predictedDiabetes) trueNegatives++;
            else if (!actualDiabetes && predictedDiabetes) falsePositives++;
            else falseNegatives++;
        }

        double accuracy = (double)(truePositives + trueNegatives) / patients.size();
        double precision = truePositives > 0 ? (double)truePositives / (truePositives + falsePositives) : 0;
        double recall = truePositives > 0 ? (double)truePositives / (truePositives + falseNegatives) : 0;
        double f1Score = (precision + recall) > 0 ? 2 * (precision * recall) / (precision + recall) : 0;

        System.out.println("=== Model Performance ===");
        System.out.println("Accuracy: " + String.format("%.2f%%", accuracy * 100));
        System.out.println("Precision: " + String.format("%.2f%%", precision * 100));
        System.out.println("Recall: " + String.format("%.2f%%", recall * 100));
        System.out.println("F1-Score: " + String.format("%.2f%%", f1Score * 100));
        System.out.println("True Positives: " + truePositives);
        System.out.println("True Negatives: " + trueNegatives);
        System.out.println("False Positives: " + falsePositives);
        System.out.println("False Negatives: " + falseNegatives);
        System.out.println("========================");
    }

    private double[] calculateFeatureMeans(List<Patient> patients) {
        double[] means = new double[8];
        for (Patient patient : patients) {
            double[] features = patient.getFeatures();
            for (int i = 0; i < features.length; i++) {
                means[i] += features[i];
            }
        }
        for (int i = 0; i < means.length; i++) {
            means[i] /= patients.size();
        }
        return means;
    }

    private double[] calculateFeatureStds(List<Patient> patients, double[] means) {
        double[] stds = new double[8];
        for (Patient patient : patients) {
            double[] features = patient.getFeatures();
            for (int i = 0; i < features.length; i++) {
                stds[i] += Math.pow(features[i] - means[i], 2);
            }
        }
        for (int i = 0; i < stds.length; i++) {
            stds[i] = Math.sqrt(stds[i] / patients.size());
        }
        return stds;
    }

    private double[] normalizeFeatures(double[] features, double[] means, double[] stds) {
        double[] normalized = new double[features.length];
        for (int i = 0; i < features.length; i++) {
            if (stds[i] != 0) {
                normalized[i] = (features[i] - means[i]) / stds[i];
            } else {
                normalized[i] = 0;
            }
        }
        return normalized;
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double dotProduct(double[] weights, double[] features) {
        double result = 0.0;
        for (int i = 0; i < weights.length; i++) {
            result += weights[i] * features[i];
        }
        return result;
    }
}
