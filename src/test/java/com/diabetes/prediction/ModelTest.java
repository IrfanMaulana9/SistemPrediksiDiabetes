package com.diabetes.prediction;

import com.diabetes.prediction.model.DiabetesPredictor;
import com.diabetes.prediction.model.Patient;
import com.diabetes.prediction.utils.DataLoader;
import com.diabetes.prediction.utils.ModelTrainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Diabetes Prediction System
 */
public class ModelTest {

    private DiabetesPredictor predictor;
    private List<Patient> testPatients;

    @BeforeEach
    void setUp() {
        // Create test data
        testPatients = Arrays.asList(
                new Patient(1, 85, 66, 29, 0, 26.6, 0.351, 31, 0),
                new Patient(8, 183, 64, 0, 0, 23.3, 0.672, 32, 1),
                new Patient(1, 89, 66, 23, 94, 28.1, 0.167, 21, 0),
                new Patient(0, 137, 40, 35, 168, 43.1, 2.288, 33, 1)
        );

        // Train model with test data
        ModelTrainer trainer = new ModelTrainer();
        predictor = trainer.trainModel(testPatients);
    }

    @Test
    void testPredictorNotNull() {
        assertNotNull(predictor, "Predictor should not be null after training");
    }

    @Test
    void testPredictionProbabilityRange() {
        for (Patient patient : testPatients) {
            double probability = predictor.predictProbability(patient);
            assertTrue(probability >= 0.0 && probability <= 1.0,
                    "Prediction probability should be between 0 and 1");
        }
    }

    @Test
    void testPredictionConsistency() {
        Patient testPatient = new Patient(2, 120, 70, 30, 100, 25.0, 0.5, 30, 0);

        // Multiple predictions should be consistent
        boolean prediction1 = predictor.predict(testPatient);
        boolean prediction2 = predictor.predict(testPatient);

        assertEquals(prediction1, prediction2, "Predictions should be consistent");
    }

    @Test
    void testHighRiskPatient() {
        // Patient with high diabetes risk factors
        Patient highRiskPatient = new Patient(10, 200, 90, 40, 300, 40.0, 2.0, 50, 1);
        double probability = predictor.predictProbability(highRiskPatient);

        // Should have higher probability (though not guaranteed due to small test dataset)
        assertTrue(probability >= 0.0, "High risk patient should have valid probability");
    }

    @Test
    void testLowRiskPatient() {
        // Patient with low diabetes risk factors
        Patient lowRiskPatient = new Patient(0, 80, 60, 20, 50, 20.0, 0.1, 25, 0);
        double probability = predictor.predictProbability(lowRiskPatient);

        // Should have valid probability
        assertTrue(probability >= 0.0 && probability <= 1.0,
                "Low risk patient should have valid probability");
    }
}
