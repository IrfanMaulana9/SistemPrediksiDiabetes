package com.diabetes.prediction.model;

/**
 * Diabetes prediction model using Logistic Regression
 */
public class DiabetesPredictor {
    private double[] weights;
    private double bias;
    private double[] featureMeans;
    private double[] featureStds;

    public DiabetesPredictor(double[] weights, double bias, double[] featureMeans, double[] featureStds) {
        this.weights = weights;
        this.bias = bias;
        this.featureMeans = featureMeans;
        this.featureStds = featureStds;
    }

    /**
     * Predict diabetes probability for a patient
     */
    public double predictProbability(Patient patient) {
        double[] features = patient.getFeatures();
        double[] normalizedFeatures = normalizeFeatures(features);

        double logit = bias;
        for (int i = 0; i < weights.length; i++) {
            logit += weights[i] * normalizedFeatures[i];
        }

        // Sigmoid function
        return 1.0 / (1.0 + Math.exp(-logit));
    }

    /**
     * Predict diabetes (true/false) for a patient
     */
    public boolean predict(Patient patient) {
        return predictProbability(patient) >= 0.5;
    }

    /**
     * Normalize features using z-score normalization
     */
    private double[] normalizeFeatures(double[] features) {
        double[] normalized = new double[features.length];
        for (int i = 0; i < features.length; i++) {
            if (featureStds[i] != 0) {
                normalized[i] = (features[i] - featureMeans[i]) / featureStds[i];
            } else {
                normalized[i] = 0;
            }
        }
        return normalized;
    }

    // Getters
    public double[] getWeights() { return weights; }
    public double getBias() { return bias; }
    public double[] getFeatureMeans() { return featureMeans; }
    public double[] getFeatureStds() { return featureStds; }
}
