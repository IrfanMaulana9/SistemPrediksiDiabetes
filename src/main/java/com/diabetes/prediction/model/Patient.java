package com.diabetes.prediction.model;

/**
 * Patient data model representing features from Pima Indian Diabetes Dataset
 */
public class Patient {
    private int pregnancies;
    private double glucose;
    private double bloodPressure;
    private double skinThickness;
    private double insulin;
    private double bmi;
    private double diabetesPedigreeFunction;
    private int age;
    private int outcome; // 0 = no diabetes, 1 = diabetes

    public Patient(int pregnancies, double glucose, double bloodPressure,
                   double skinThickness, double insulin, double bmi,
                   double diabetesPedigreeFunction, int age, int outcome) {
        this.pregnancies = pregnancies;
        this.glucose = glucose;
        this.bloodPressure = bloodPressure;
        this.skinThickness = skinThickness;
        this.insulin = insulin;
        this.bmi = bmi;
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
        this.age = age;
        this.outcome = outcome;
    }

    // Convert patient data to feature array for ML model
    public double[] getFeatures() {
        return new double[] {
                pregnancies, glucose, bloodPressure, skinThickness,
                insulin, bmi, diabetesPedigreeFunction, age
        };
    }

    // Getters and Setters
    public int getPregnancies() { return pregnancies; }
    public void setPregnancies(int pregnancies) { this.pregnancies = pregnancies; }

    public double getGlucose() { return glucose; }
    public void setGlucose(double glucose) { this.glucose = glucose; }

    public double getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(double bloodPressure) { this.bloodPressure = bloodPressure; }

    public double getSkinThickness() { return skinThickness; }
    public void setSkinThickness(double skinThickness) { this.skinThickness = skinThickness; }

    public double getInsulin() { return insulin; }
    public void setInsulin(double insulin) { this.insulin = insulin; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public double getDiabetesPedigreeFunction() { return diabetesPedigreeFunction; }
    public void setDiabetesPedigreeFunction(double diabetesPedigreeFunction) {
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
    }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getOutcome() { return outcome; }
    public void setOutcome(int outcome) { this.outcome = outcome; }

    @Override
    public String toString() {
        return String.format("Patient{pregnancies=%d, glucose=%.1f, bloodPressure=%.1f, " +
                        "skinThickness=%.1f, insulin=%.1f, bmi=%.1f, dpf=%.3f, age=%d, outcome=%d}",
                pregnancies, glucose, bloodPressure, skinThickness,
                insulin, bmi, diabetesPedigreeFunction, age, outcome);
    }
}
