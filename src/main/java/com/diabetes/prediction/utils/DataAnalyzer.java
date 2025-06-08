package com.diabetes.prediction.utils;

import com.diabetes.prediction.model.Patient;
import java.util.List;

/**
 * Utility class for analyzing the diabetes dataset
 */
public class DataAnalyzer {

    /**
     * Perform comprehensive data analysis
     */
    public static void analyzeDataset(List<Patient> patients) {
        if (patients.isEmpty()) {
            System.out.println("No data to analyze");
            return;
        }

        System.out.println("\n=== COMPREHENSIVE DATA ANALYSIS ===");

        // Basic statistics
        printBasicStatistics(patients);

        // Feature analysis
        printFeatureAnalysis(patients);

        // Correlation analysis
        printCorrelationAnalysis(patients);

        System.out.println("===================================\n");
    }

    private static void printBasicStatistics(List<Patient> patients) {
        int totalPatients = patients.size();
        int diabetesCount = 0;

        double minAge = Double.MAX_VALUE, maxAge = Double.MIN_VALUE;
        double minGlucose = Double.MAX_VALUE, maxGlucose = Double.MIN_VALUE;
        double minBMI = Double.MAX_VALUE, maxBMI = Double.MIN_VALUE;

        for (Patient patient : patients) {
            if (patient.getOutcome() == 1) diabetesCount++;

            minAge = Math.min(minAge, patient.getAge());
            maxAge = Math.max(maxAge, patient.getAge());
            minGlucose = Math.min(minGlucose, patient.getGlucose());
            maxGlucose = Math.max(maxGlucose, patient.getGlucose());
            minBMI = Math.min(minBMI, patient.getBmi());
            maxBMI = Math.max(maxBMI, patient.getBmi());
        }

        System.out.println("BASIC STATISTICS:");
        System.out.println("Total Patients: " + totalPatients);
        System.out.println("Diabetes Cases: " + diabetesCount + " (" +
                String.format("%.1f%%", (diabetesCount * 100.0 / totalPatients)) + ")");
        System.out.println("Non-Diabetes Cases: " + (totalPatients - diabetesCount) + " (" +
                String.format("%.1f%%", ((totalPatients - diabetesCount) * 100.0 / totalPatients)) + ")");
        System.out.println("Age Range: " + String.format("%.0f - %.0f years", minAge, maxAge));
        System.out.println("Glucose Range: " + String.format("%.0f - %.0f mg/dL", minGlucose, maxGlucose));
        System.out.println("BMI Range: " + String.format("%.1f - %.1f", minBMI, maxBMI));
    }

    private static void printFeatureAnalysis(List<Patient> patients) {
        System.out.println("\nFEATURE ANALYSIS:");

        // Separate diabetes and non-diabetes groups
        double[] diabetesAvg = new double[8];
        double[] nonDiabetesAvg = new double[8];
        int diabetesCount = 0, nonDiabetesCount = 0;

        for (Patient patient : patients) {
            double[] features = patient.getFeatures();
            if (patient.getOutcome() == 1) {
                diabetesCount++;
                for (int i = 0; i < features.length; i++) {
                    diabetesAvg[i] += features[i];
                }
            } else {
                nonDiabetesCount++;
                for (int i = 0; i < features.length; i++) {
                    nonDiabetesAvg[i] += features[i];
                }
            }
        }

        // Calculate averages
        for (int i = 0; i < 8; i++) {
            diabetesAvg[i] /= diabetesCount;
            nonDiabetesAvg[i] /= nonDiabetesCount;
        }

        String[] featureNames = {"Pregnancies", "Glucose", "BloodPressure", "SkinThickness",
                "Insulin", "BMI", "DiabetesPedigreeFunction", "Age"};

        System.out.println("Average values by diabetes status:");
        System.out.printf("%-25s %-15s %-15s %-10s%n", "Feature", "Diabetes", "No Diabetes", "Difference");
        System.out.println("-".repeat(70));

        for (int i = 0; i < featureNames.length; i++) {
            double diff = diabetesAvg[i] - nonDiabetesAvg[i];
            System.out.printf("%-25s %-15.2f %-15.2f %-10.2f%n",
                    featureNames[i], diabetesAvg[i], nonDiabetesAvg[i], diff);
        }
    }

    private static void printCorrelationAnalysis(List<Patient> patients) {
        System.out.println("\nCORRELATION WITH DIABETES:");

        String[] featureNames = {"Pregnancies", "Glucose", "BloodPressure", "SkinThickness",
                "Insulin", "BMI", "DiabetesPedigreeFunction", "Age"};

        for (int i = 0; i < featureNames.length; i++) {
            double correlation = calculateCorrelation(patients, i);
            String strength = getCorrelationStrength(Math.abs(correlation));
            System.out.printf("%-25s: %6.3f (%s)%n", featureNames[i], correlation, strength);
        }
    }

    private static double calculateCorrelation(List<Patient> patients, int featureIndex) {
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        int n = patients.size();

        for (Patient patient : patients) {
            double x = patient.getFeatures()[featureIndex];
            double y = patient.getOutcome();

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return denominator != 0 ? numerator / denominator : 0;
    }

    private static String getCorrelationStrength(double correlation) {
        if (correlation >= 0.7) return "Strong";
        else if (correlation >= 0.5) return "Moderate";
        else if (correlation >= 0.3) return "Weak";
        else return "Very Weak";
    }
}
