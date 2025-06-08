package com.diabetes.prediction.utils;

import com.diabetes.prediction.model.Patient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading and processing the diabetes dataset
 */
public class DataLoader {

    /**
     * Load patient data from CSV file
     */
    public static List<Patient> loadDataset(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 9) {
                    try {
                        Patient patient = new Patient(
                                Integer.parseInt(values[0].trim()),      // Pregnancies
                                Double.parseDouble(values[1].trim()),    // Glucose
                                Double.parseDouble(values[2].trim()),    // BloodPressure
                                Double.parseDouble(values[3].trim()),    // SkinThickness
                                Double.parseDouble(values[4].trim()),    // Insulin
                                Double.parseDouble(values[5].trim()),    // BMI
                                Double.parseDouble(values[6].trim()),    // DiabetesPedigreeFunction
                                Integer.parseInt(values[7].trim()),      // Age
                                Integer.parseInt(values[8].trim())       // Outcome
                        );
                        patients.add(patient);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        }

        return patients;
    }

    /**
     * Calculate basic statistics for the dataset
     */
    public static void printDatasetStatistics(List<Patient> patients) {
        if (patients.isEmpty()) {
            System.out.println("No data available");
            return;
        }

        int diabetesCount = 0;
        double avgAge = 0;
        double avgGlucose = 0;
        double avgBMI = 0;

        for (Patient patient : patients) {
            if (patient.getOutcome() == 1) diabetesCount++;
            avgAge += patient.getAge();
            avgGlucose += patient.getGlucose();
            avgBMI += patient.getBmi();
        }

        int totalPatients = patients.size();
        avgAge /= totalPatients;
        avgGlucose /= totalPatients;
        avgBMI /= totalPatients;

        System.out.println("\n=== Dataset Statistics ===");
        System.out.println("Total patients: " + totalPatients);
        System.out.println("Patients with diabetes: " + diabetesCount + " (" +
                String.format("%.1f%%", (diabetesCount * 100.0 / totalPatients)) + ")");
        System.out.println("Patients without diabetes: " + (totalPatients - diabetesCount) + " (" +
                String.format("%.1f%%", ((totalPatients - diabetesCount) * 100.0 / totalPatients)) + ")");
        System.out.println("Average age: " + String.format("%.1f", avgAge));
        System.out.println("Average glucose: " + String.format("%.1f", avgGlucose));
        System.out.println("Average BMI: " + String.format("%.1f", avgBMI));
        System.out.println("===========================\n");
    }
}
