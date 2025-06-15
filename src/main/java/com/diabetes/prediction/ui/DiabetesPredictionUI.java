package com.diabetes.prediction.ui;

import com.diabetes.prediction.model.DiabetesPredictor;
import com.diabetes.prediction.model.Patient;
import com.diabetes.prediction.utils.DataLoader;
import com.diabetes.prediction.utils.ModelTrainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiabetesPredictionUI extends Application {

    private static final String DATASET_PATH = "data/diabetes.csv";

    private DiabetesPredictor predictor;
    private List<Patient> patients;

    // UI Components
    private TextField pregnanciesField;
    private TextField glucoseField;
    private TextField bloodPressureField;
    private TextField skinThicknessField;
    private TextField insulinField;
    private TextField bmiField;
    private TextField dpfField;
    private TextField ageField;
    private Button predictButton;
    private Button resetButton;
    private Button viewDataButton;
    private Text resultText;
    private ProgressBar confidenceBar;
    private Label confidenceLabel;

    private StringProperty statusProperty = new SimpleStringProperty("");
    private ExecutorService executorService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Diabetes Prediction System");

        // Root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        VBox headerBox = createHeader();

        // Tab Pane
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Prediction", createPredictionForm()),
                new Tab("About", createAboutSection())
        );

        // Status bar
        HBox statusBar = new HBox();
        statusBar.getStyleClass().add("status-bar");
        Label statusLabel = new Label();
        statusLabel.textProperty().bind(statusProperty);
        statusBar.getChildren().add(statusLabel);

        // Set layout
        root.setTop(headerBox);
        root.setCenter(tabPane);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        // Load data and train model in background
        loadDataAndTrainModel();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getStyleClass().add("header");

        Text title = new Text("Diabetes Prediction System");
        title.getStyleClass().add("header-title");

        Text subtitle = new Text("Pima Indian Women Diabetes Dataset");
        subtitle.getStyleClass().add("header-subtitle");

        headerBox.getChildren().addAll(title, subtitle);
        return headerBox;
    }

    private ScrollPane createAboutSection() {
        VBox aboutBox = new VBox(15);
        aboutBox.setPadding(new Insets(20));
        Text aboutTitle = new Text("About This Application");
        aboutTitle.getStyleClass().add("header-title");

        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(
                new Label("Diabetes Prediction System"),
                new Label("Version 1.0.0"),
                new Label(""),
                new Label("This application uses machine learning to predict diabetes risk based on diagnostic measurements."),
                new Label("The model is trained on the Pima Indian Diabetes Dataset, which contains data from female patients."),
                new Label(""),
                new Label("Dataset Features:"),
                new Label("• Pregnancies: Number of times pregnant"),
                new Label("• Glucose: Plasma glucose concentration (mg/dL)"),
                new Label("• Blood Pressure: Diastolic blood pressure (mm Hg)"),
                new Label("• Skin Thickness: Triceps skin fold thickness (mm)"),
                new Label("• Insulin: 2-Hour serum insulin (mu U/ml)"),
                new Label("• BMI: Body mass index (weight in kg/(height in m)^2)"),
                new Label("• Diabetes Pedigree Function: Genetic score"),
                new Label("• Age: Age in years"),
                new Label(""),
                new Label("The model uses logistic regression with gradient descent optimization."),
                new Label("Features are normalized using z-score standardization for better performance.")
        );

        aboutBox.getChildren().addAll(aboutTitle, new Separator(), contentBox);
        ScrollPane scrollPane = new ScrollPane(aboutBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createPredictionForm() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.getStyleClass().add("prediction-box");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Form fields
        pregnanciesField = new TextField();
        glucoseField = new TextField();
        bloodPressureField = new TextField();
        skinThicknessField = new TextField();
        insulinField = new TextField();
        bmiField = new TextField();
        dpfField = new TextField();
        ageField = new TextField();

        int row = 0;
        formGrid.add(new Label("Pregnancies:"), 0, row);
        formGrid.add(pregnanciesField, 1, row++);

        formGrid.add(new Label("Glucose (mg/dL):"), 0, row);
        formGrid.add(glucoseField, 1, row++);

        formGrid.add(new Label("Blood Pressure (mm Hg):"), 0, row);
        formGrid.add(bloodPressureField, 1, row++);

        formGrid.add(new Label("Skin Thickness (mm):"), 0, row);
        formGrid.add(skinThicknessField, 1, row++);

        formGrid.add(new Label("Insulin (mu U/ml):"), 0, row);
        formGrid.add(insulinField, 1, row++);

        formGrid.add(new Label("BMI:"), 0, row);
        formGrid.add(bmiField, 1, row++);

        formGrid.add(new Label("Diabetes Pedigree Function:"), 0, row);
        formGrid.add(dpfField, 1, row++);

        formGrid.add(new Label("Age (years):"), 0, row);
        formGrid.add(ageField, 1, row++);

        // Buttons
        predictButton = new Button("Predict");
        predictButton.setDisable(true); // Disabled until model is loaded

        resetButton = new Button("Reset");
        viewDataButton = new Button("View Dataset");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(predictButton, resetButton, viewDataButton);

        // Results section
        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(20));
        resultsBox.setAlignment(Pos.CENTER);

        resultText = new Text();
        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        resultText.getStyleClass().add("prediction-negative");

        confidenceBar = new ProgressBar(0);
        confidenceBar.setPrefWidth(300);

        confidenceLabel = new Label("Confidence: 0%");
        confidenceLabel.getStyleClass().add("label");

        resultsBox.getChildren().addAll(
                new Separator(),
                new Label("Prediction Result:"),
                resultText,
                confidenceBar,
                confidenceLabel
        );

        // Combine all elements
        formBox.getChildren().addAll(
                new Label("Enter Patient Information:"),
                formGrid,
                buttonBox,
                resultsBox
        );

        // Event handlers
        predictButton.setOnAction(e -> makePrediction());
        resetButton.setOnAction(e -> resetForm());
        viewDataButton.setOnAction(e -> showDatasetDialog());

        ScrollPane scrollPane = new ScrollPane(formBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void loadDataAndTrainModel() {
        statusProperty.set("Loading dataset and training model...");
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                patients = DataLoader.loadDataset(DATASET_PATH);
                ModelTrainer trainer = new ModelTrainer();
                predictor = trainer.trainModel(patients);

                Platform.runLater(() -> {
                    statusProperty.set("Model trained successfully. Ready for predictions.");
                    predictButton.setDisable(false);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    statusProperty.set("Error: " + e.getMessage());
                    showErrorDialog("Error Loading Data", "Could not load dataset: " + e.getMessage());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusProperty.set("Error: " + e.getMessage());
                    showErrorDialog("Error Training Model", "Could not train model: " + e.getMessage());
                });
            }
        });
    }

    private void makePrediction() {
        try {
            int pregnancies = Integer.parseInt(pregnanciesField.getText());
            double glucose = Double.parseDouble(glucoseField.getText());
            double bloodPressure = Double.parseDouble(bloodPressureField.getText());
            double skinThickness = Double.parseDouble(skinThicknessField.getText());
            double insulin = Double.parseDouble(insulinField.getText());
            double bmi = Double.parseDouble(bmiField.getText());
            double dpf = Double.parseDouble(dpfField.getText());
            int age = Integer.parseInt(ageField.getText());

            // Validasi Input Medis Realistis
            if (glucose <= 50 || glucose >= 250) {
                showErrorDialog("Input Tidak Realistis", "Kadar Glukosa harus antara 50–250 mg/dL");
                return;
            }

            if (bloodPressure < 40 || bloodPressure > 140) {
                showErrorDialog("Input Tidak Realistis", "Tekanan Darah harus antara 40–140 mm Hg");
                return;
            }

            if (skinThickness < 10 || skinThickness > 99) {
                showErrorDialog("Input Tidak Realistis", "Ketebalan Kulit harus antara 10–99 mm");
                return;
            }

            if (bmi < 18.5 || bmi > 67.0) {
                showErrorDialog("Input Tidak Realistis", "BMI harus antara 18.5–67.0 kg/m²");
                return;
            }

            if (age < 18 || age > 120) {
                showErrorDialog("Input Tidak Realistis", "Usia harus antara 18-120 tahun");
                return;
            }

            Patient patient = new Patient(pregnancies, glucose, bloodPressure, skinThickness,
                    insulin, bmi, dpf, age, 0);

            boolean hasDiabetes = predictor.predict(patient);
            double probability = predictor.predictProbability(patient);
            double confidence = hasDiabetes ? probability : (1 - probability);

            resultText.setText(hasDiabetes ? "DIABETES DETECTED" : "NO DIABETES");
            resultText.setFill(hasDiabetes ? Color.RED : Color.GREEN);

            confidenceBar.setProgress(confidence);
            confidenceLabel.setText(String.format("Confidence: %.1f%%", confidence * 100));
            statusProperty.set("Prediction completed.");

        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter valid numeric values for all fields.");
        } catch (Exception e) {
            showErrorDialog("Prediction Error", "Error making prediction: " + e.getMessage());
        }
    }

    private void resetForm() {
        pregnanciesField.clear();
        glucoseField.clear();
        bloodPressureField.clear();
        skinThicknessField.clear();
        insulinField.clear();
        bmiField.clear();
        dpfField.clear();
        ageField.clear();
        resultText.setText("");
        confidenceBar.setProgress(0);
        confidenceLabel.setText("Confidence: 0%");
        statusProperty.set("Form reset.");
    }

    private void showDatasetDialog() {
        if (patients == null || patients.isEmpty()) {
            showErrorDialog("Dataset Not Available", "Dataset is not loaded yet.");
            return;
        }

        Stage dataStage = new Stage();
        dataStage.setTitle("Dataset Viewer");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TableView<Patient> tableView = new TableView<>();
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);

        addTableColumns(tableView);
        tableView.setItems(data);

        VBox statsBox = createDatasetStatistics();

        root.getChildren().addAll(
                new Label("Dataset Records (" + patients.size() + " patients):"),
                tableView,
                new Separator(),
                statsBox
        );

        Scene scene = new Scene(root, 900, 600);
        dataStage.setScene(scene);
        dataStage.show();
    }

    private void addTableColumns(TableView<Patient> tableView) {
        tableView.getColumns().clear();

        String[][] columns = {
                {"Pregnancies", "pregnancies"},
                {"Glucose", "glucose"},
                {"Blood Pressure", "bloodPressure"},
                {"Skin Thickness", "skinThickness"},
                {"Insulin", "insulin"},
                {"BMI", "bmi"},
                {"DPF", "diabetesPedigreeFunction"},
                {"Age", "age"},
                {"Outcome", "outcome"}
        };

        for (String[] col : columns) {
            TableColumn<Patient, ?> tableColumn = new TableColumn<>(col[0]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(col[1]));
            tableView.getColumns().add(tableColumn);
        }
    }

    private VBox createDatasetStatistics() {
        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(10));

        Text statsTitle = new Text("Dataset Statistics");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statsBox.getChildren().add(statsTitle);

        int total = patients.size();
        long diabetesCount = patients.stream().filter(p -> p.getOutcome() == 1).count();
        double diabetesPercentage = (double) diabetesCount / total * 100;

        statsBox.getChildren().addAll(
                new Label("Total Patients: " + total),
                new Label(String.format("Patients with Diabetes: %d (%.1f%%)", diabetesCount, diabetesPercentage)),
                new Label(String.format("Patients without Diabetes: %d (%.1f%%)", total - diabetesCount, 100 - diabetesPercentage))
        );

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Diabetes Distribution");
        xAxis.setLabel("Outcome");
        yAxis.setLabel("Count");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Patient Count");
        series.getData().add(new XYChart.Data<>("No Diabetes", total - diabetesCount));
        series.getData().add(new XYChart.Data<>("Diabetes", diabetesCount));
        barChart.getData().add(series);

        statsBox.getChildren().add(barChart);
        return statsBox;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}