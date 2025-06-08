package com.diabetes.prediction.ui;

import com.diabetes.prediction.model.DiabetesPredictor;
import com.diabetes.prediction.model.Patient;
import com.diabetes.prediction.utils.DataLoader;
import com.diabetes.prediction.utils.ModelTrainer;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiabetesPredictionUI extends Application {

    private static final String DATASET_PATH = "data/diabetes.csv";
    private DiabetesPredictor predictor;
    private List<Patient> patients;

    // Theme management
    private BooleanProperty darkThemeProperty = new SimpleBooleanProperty(false);

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
    private Button themeToggleButton;

    private Text resultText;
    private ProgressBar confidenceBar;
    private Label confidenceLabel;

    private StringProperty statusProperty = new SimpleStringProperty("");
    private ExecutorService executorService;

    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistem Prediksi Diabetes");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);

        // Root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header with theme toggle
        VBox headerBox = createModernHeader();
        root.setTop(headerBox);

        // Main content with tabs
        TabPane tabPane = createModernTabPane();
        root.setCenter(tabPane);

        // Status bar
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        // Create scene
        scene = new Scene(root, 1000, 750);

        // Load CSS
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        // Setup theme binding
        setupThemeBinding(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Load data and train model in background
        loadDataAndTrainModel();

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private VBox createModernHeader() {
        VBox headerBox = new VBox(15);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        headerBox.setAlignment(Pos.CENTER);

        // Header container with theme toggle
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_RIGHT);

        // Theme toggle button
        themeToggleButton = new Button("🌙 Mode Gelap");
        themeToggleButton.getStyleClass().add("theme-toggle");
        themeToggleButton.setOnAction(e -> toggleTheme());

        headerContainer.getChildren().add(themeToggleButton);

        // Title and subtitle
        Text title = new Text("Sistem Prediksi Diabetes");
        title.getStyleClass().add("header-title");

        Text subtitle = new Text("Analisis ML Lanjutan untuk Dataset Wanita Pima Indian");
        subtitle.getStyleClass().add("header-subtitle");

        headerBox.getChildren().addAll(headerContainer, title, subtitle);
        return headerBox;
    }

    private TabPane createModernTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("modern-tab-pane");

        // Prediction tab
        Tab predictionTab = new Tab("🔮 Prediksi");
        predictionTab.setClosable(false);
        predictionTab.setContent(createModernPredictionForm());

        // Analytics tab
        Tab analyticsTab = new Tab("📊 Analitik");
        analyticsTab.setClosable(false);
        analyticsTab.setContent(createAnalyticsSection());

        // About tab
        Tab aboutTab = new Tab("ℹ️ Tentang");
        aboutTab.setClosable(false);
        aboutTab.setContent(createModernAboutSection());

        tabPane.getTabs().addAll(predictionTab, analyticsTab, aboutTab);
        return tabPane;
    }

    private ScrollPane createModernPredictionForm() {
        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30));

        // Form card
        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("card");

        // Section title
        Label sectionTitle = new Label("Informasi Pasien");
        sectionTitle.getStyleClass().add("section-title");

        // Create form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20, 0, 0, 0));

        // Create modern form fields
        pregnanciesField = createModernTextField("0");
        glucoseField = createModernTextField("120");
        bloodPressureField = createModernTextField("70");
        skinThicknessField = createModernTextField("20");
        insulinField = createModernTextField("80");
        bmiField = createModernTextField("25.0");
        dpfField = createModernTextField("0.5");
        ageField = createModernTextField("30");

        // Add labels and fields to grid
        int row = 0;
        addFormField(formGrid, "Jumlah Kehamilan", pregnanciesField, row++);
        addFormField(formGrid, "Tingkat Glukosa (mg/dL)", glucoseField, row++);
        addFormField(formGrid, "Tekanan Darah (mm Hg)", bloodPressureField, row++);
        addFormField(formGrid, "Ketebalan Kulit (mm)", skinThicknessField, row++);
        addFormField(formGrid, "Tingkat Insulin (mu U/ml)", insulinField, row++);
        addFormField(formGrid, "Indeks Massa Tubuh (BMI)", bmiField, row++);
        addFormField(formGrid, "Fungsi Silsilah Diabetes", dpfField, row++);
        addFormField(formGrid, "Usia (tahun)", ageField, row++);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        predictButton = new Button("🔍 Prediksi Diabetes");
        predictButton.getStyleClass().addAll("modern-button");
        predictButton.setDisable(true);
        predictButton.setOnAction(e -> makePredictionWithAnimation());

        resetButton = new Button("🔄 Reset Form");
        resetButton.getStyleClass().addAll("modern-button", "secondary-button");
        resetButton.setOnAction(e -> resetFormWithAnimation());

        viewDataButton = new Button("📋 Lihat Dataset");
        viewDataButton.getStyleClass().addAll("modern-button", "success-button");
        viewDataButton.setOnAction(e -> showModernDatasetDialog());

        buttonBox.getChildren().addAll(predictButton, resetButton, viewDataButton);

        formCard.getChildren().addAll(sectionTitle, formGrid, buttonBox);

        // Results card
        VBox resultsCard = createResultsCard();

        mainContainer.getChildren().addAll(formCard, resultsCard);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private VBox createResultsCard() {
        VBox resultsCard = new VBox(20);
        resultsCard.getStyleClass().add("card");

        Label resultsTitle = new Label("Hasil Prediksi");
        resultsTitle.getStyleClass().add("section-title");

        VBox resultsContent = new VBox(15);
        resultsContent.setAlignment(Pos.CENTER);
        resultsContent.setPadding(new Insets(20, 0, 0, 0));

        resultText = new Text("Masukkan data pasien dan klik prediksi");
        resultText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        resultText.setFill(Color.web("#64748b"));

        // Confidence section
        VBox confidenceSection = new VBox(10);
        confidenceSection.setAlignment(Pos.CENTER);

        confidenceLabel = new Label("Kepercayaan: 0%");
        confidenceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        confidenceBar = new ProgressBar(0);
        confidenceBar.getStyleClass().add("modern-progress-bar");
        confidenceBar.setPrefWidth(300);
        confidenceBar.setPrefHeight(12);

        confidenceSection.getChildren().addAll(confidenceLabel, confidenceBar);

        resultsContent.getChildren().addAll(resultText, confidenceSection);
        resultsCard.getChildren().addAll(resultsTitle, resultsContent);

        return resultsCard;
    }

    private ScrollPane createAnalyticsSection() {
        VBox analyticsBox = new VBox(25);
        analyticsBox.setPadding(new Insets(30));

        Label title = new Label("Analitik Dataset");
        title.getStyleClass().add("section-title");

        // Placeholder for analytics - will be populated when data is loaded
        VBox analyticsContent = new VBox(20);
        analyticsContent.getStyleClass().add("card");
        analyticsContent.setPadding(new Insets(30));

        Label loadingLabel = new Label("Analitik akan tersedia setelah data dimuat...");
        loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748b;");
        analyticsContent.getChildren().add(loadingLabel);

        analyticsBox.getChildren().addAll(title, analyticsContent);

        ScrollPane scrollPane = new ScrollPane(analyticsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private ScrollPane createModernAboutSection() {
        VBox aboutBox = new VBox(25);
        aboutBox.setPadding(new Insets(30));

        VBox aboutCard = new VBox(20);
        aboutCard.getStyleClass().add("card");

        Label aboutTitle = new Label("Tentang Aplikasi Ini");
        aboutTitle.getStyleClass().add("section-title");

        VBox contentBox = new VBox(15);
        contentBox.getChildren().addAll(
                createInfoSection("🎯 Tujuan",
                        "Aplikasi ini menggunakan machine learning canggih untuk memprediksi risiko diabetes berdasarkan pengukuran diagnostik dari dataset Wanita Pima Indian."),
                createInfoSection("🔬 Teknologi",
                        "Dibangun dengan Java dan JavaFX, menampilkan implementasi regresi logistik kustom dengan optimisasi gradient descent."),
                createInfoSection("📊 Fitur Dataset",
                        "• Kehamilan: Jumlah kali hamil\n" +
                                "• Glukosa: Konsentrasi glukosa plasma (mg/dL)\n" +
                                "• Tekanan Darah: Tekanan darah diastolik (mm Hg)\n" +
                                "• Ketebalan Kulit: Ketebalan lipatan kulit trisep (mm)\n" +
                                "• Insulin: Insulin serum 2 jam (mu U/ml)\n" +
                                "• BMI: Indeks massa tubuh\n" +
                                "• Fungsi Silsilah Diabetes: Faktor genetik\n" +
                                "• Usia: Usia dalam tahun"),
                createInfoSection("⚡ Performa",
                        "Model biasanya mencapai akurasi 75-80% dengan fitur yang dinormalisasi menggunakan standardisasi z-score untuk performa optimal."),
                createInfoSection("🎨 Desain",
                        "UI modern dengan dukungan tema terang/gelap, animasi halus, dan desain responsif untuk pengalaman pengguna yang lebih baik.")
        );

        aboutCard.getChildren().addAll(aboutTitle, contentBox);
        aboutBox.getChildren().add(aboutCard);

        ScrollPane scrollPane = new ScrollPane(aboutBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private VBox createInfoSection(String title, String content) {
        VBox section = new VBox(8);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-line-spacing: 2px;");

        section.getChildren().addAll(titleLabel, contentLabel);
        return section;
    }

    private TextField createModernTextField(String promptText) {
        TextField textField = new TextField();
        textField.getStyleClass().add("modern-text-field");
        textField.setPromptText(promptText);
        textField.setPrefHeight(40);
        return textField;
    }

    private void addFormField(GridPane grid, String labelText, TextField field, int row) {
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        grid.add(label, 0, row);
        grid.add(field, 1, row);

        // Make text field expand
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.getStyleClass().add("status-bar");

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("status-text");
        statusLabel.textProperty().bind(statusProperty);

        statusBar.getChildren().add(statusLabel);
        return statusBar;
    }

    private void setupThemeBinding(BorderPane root) {
        darkThemeProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                root.getStyleClass().add("dark-theme");
                themeToggleButton.setText("☀️ Mode Terang");
            } else {
                root.getStyleClass().remove("dark-theme");
                themeToggleButton.setText("🌙 Mode Gelap");
            }
        });
    }

    private void toggleTheme() {
        darkThemeProperty.set(!darkThemeProperty.get());

        // Add a subtle animation to the theme toggle
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), themeToggleButton);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void loadDataAndTrainModel() {
        statusProperty.set("🔄 Memuat dataset dan melatih model...");

        executorService.submit(() -> {
            try {
                // Load dataset
                patients = DataLoader.loadDataset(DATASET_PATH);

                // Train model
                ModelTrainer trainer = new ModelTrainer();
                predictor = trainer.trainModel(patients);

                Platform.runLater(() -> {
                    statusProperty.set("✅ Model berhasil dilatih. Siap untuk prediksi.");
                    predictButton.setDisable(false);
                    updateAnalyticsSection();
                });

            } catch (IOException e) {
                Platform.runLater(() -> {
                    statusProperty.set("❌ Error: " + e.getMessage());
                    showModernErrorDialog("Error Loading Data",
                            "Could not load dataset: " + e.getMessage());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusProperty.set("❌ Error: " + e.getMessage());
                    showModernErrorDialog("Error Training Model",
                            "Could not train model: " + e.getMessage());
                });
            }
        });
    }

    private void makePredictionWithAnimation() {
        try {
            // Get values from form
            int pregnancies = Integer.parseInt(pregnanciesField.getText());
            double glucose = Double.parseDouble(glucoseField.getText());
            double bloodPressure = Double.parseDouble(bloodPressureField.getText());
            double skinThickness = Double.parseDouble(skinThicknessField.getText());
            double insulin = Double.parseDouble(insulinField.getText());
            double bmi = Double.parseDouble(bmiField.getText());
            double dpf = Double.parseDouble(dpfField.getText());
            int age = Integer.parseInt(ageField.getText());

            // Create patient object
            Patient patient = new Patient(pregnancies, glucose, bloodPressure,
                    skinThickness, insulin, bmi, dpf, age, 0);

            // Make prediction
            double probability = predictor.predictProbability(patient);
            boolean hasDiabetes = predictor.predict(patient);

            // Update UI with animation
            double confidence = hasDiabetes ? probability : (1 - probability);

            // Animate result text
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), resultText);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                resultText.setText(hasDiabetes ? "⚠️ DIABETES TERDETEKSI" : "✅ TIDAK ADA DIABETES");
                resultText.getStyleClass().clear();
                resultText.getStyleClass().add(hasDiabetes ? "result-positive" : "result-negative");

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), resultText);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();

            // Animate confidence bar
            confidenceBar.setProgress(0);
            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                    Duration.millis(800),
                    new javafx.animation.KeyValue(confidenceBar.progressProperty(), confidence)
            );
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            confidenceLabel.setText(String.format("Kepercayaan: %.1f%%", confidence * 100));

            statusProperty.set("🎯 Prediksi berhasil diselesaikan.");

        } catch (NumberFormatException e) {
            showModernErrorDialog("Input Tidak Valid", "Harap masukkan nilai numerik yang valid untuk semua field.");
        } catch (Exception e) {
            showModernErrorDialog("Error Prediksi", "Error saat membuat prediksi: " + e.getMessage());
        }
    }

    private void resetFormWithAnimation() {
        // Create fade out animation for all fields
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);
        fadeOut.setOnFinished(e -> {
            pregnanciesField.clear();
            glucoseField.clear();
            bloodPressureField.clear();
            skinThicknessField.clear();
            insulinField.clear();
            bmiField.clear();
            dpfField.clear();
            ageField.clear();

            resultText.setText("Masukkan data pasien dan klik prediksi");
            resultText.getStyleClass().clear();
            resultText.setFill(Color.web("#64748b"));

            confidenceBar.setProgress(0);
            confidenceLabel.setText("Kepercayaan: 0%");

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200));
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();

        statusProperty.set("📝 Form berhasil direset.");
    }

    private void updateAnalyticsSection() {
        // This would update the analytics tab with actual data
        // Implementation would go here
    }

    private void showModernDatasetDialog() {
        if (patients == null || patients.isEmpty()) {
            showModernErrorDialog("Dataset Tidak Tersedia", "Dataset belum dimuat.");
            return;
        }

        Stage dataStage = new Stage();
        dataStage.setTitle("Penampil Dataset");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("card");

        // Apply theme to dialog
        Scene dialogScene = new Scene(root, 1000, 700);
        dialogScene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        if (darkThemeProperty.get()) {
            root.getStyleClass().add("dark-theme");
        }

        // Create modern table
        TableView<Patient> tableView = new TableView<>();
        tableView.getStyleClass().add("modern-table");

        // Define columns with better styling
        TableColumn<Patient, Integer> pregCol = new TableColumn<>("Pregnancies");
        pregCol.setCellValueFactory(new PropertyValueFactory<>("pregnancies"));
        pregCol.setPrefWidth(100);

        TableColumn<Patient, Double> glucoseCol = new TableColumn<>("Glucose");
        glucoseCol.setCellValueFactory(new PropertyValueFactory<>("glucose"));
        glucoseCol.setPrefWidth(80);

        TableColumn<Patient, Double> bpCol = new TableColumn<>("Blood Pressure");
        bpCol.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        bpCol.setPrefWidth(120);

        TableColumn<Patient, Double> skinCol = new TableColumn<>("Skin Thickness");
        skinCol.setCellValueFactory(new PropertyValueFactory<>("skinThickness"));
        skinCol.setPrefWidth(120);

        TableColumn<Patient, Double> insulinCol = new TableColumn<>("Insulin");
        insulinCol.setCellValueFactory(new PropertyValueFactory<>("insulin"));
        insulinCol.setPrefWidth(80);

        TableColumn<Patient, Double> bmiCol = new TableColumn<>("BMI");
        bmiCol.setCellValueFactory(new PropertyValueFactory<>("bmi"));
        bmiCol.setPrefWidth(80);

        TableColumn<Patient, Double> dpfCol = new TableColumn<>("DPF");
        dpfCol.setCellValueFactory(new PropertyValueFactory<>("diabetesPedigreeFunction"));
        dpfCol.setPrefWidth(80);

        TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(60);

        TableColumn<Patient, Integer> outcomeCol = new TableColumn<>("Outcome");
        outcomeCol.setCellValueFactory(new PropertyValueFactory<>("outcome"));
        outcomeCol.setPrefWidth(80);

        tableView.getColumns().addAll(pregCol, glucoseCol, bpCol, skinCol,
                insulinCol, bmiCol, dpfCol, ageCol, outcomeCol);

        // Add data
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);
        tableView.setItems(data);

        // Create modern statistics section
        VBox statsBox = createModernDatasetStatistics();

        // Add components to root
        Label titleLabel = new Label("📊 Ringkasan Dataset (" + patients.size() + " pasien)");
        titleLabel.getStyleClass().add("section-title");

        root.getChildren().addAll(titleLabel, tableView, statsBox);

        dataStage.setScene(dialogScene);
        dataStage.show();
    }

    private VBox createModernDatasetStatistics() {
        VBox statsBox = new VBox(20);
        statsBox.getStyleClass().add("card");
        statsBox.setPadding(new Insets(20));

        Label statsTitle = new Label("📈 Statistik Dataset");
        statsTitle.getStyleClass().add("section-title");

        // Calculate statistics
        int totalPatients = patients.size();
        int diabetesCount = 0;

        for (Patient patient : patients) {
            if (patient.getOutcome() == 1) {
                diabetesCount++;
            }
        }

        int nonDiabetesCount = totalPatients - diabetesCount;
        double diabetesPercentage = (double) diabetesCount / totalPatients * 100;

        // Create modern chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getStyleClass().add("chart");
        barChart.setTitle("Distribusi Diabetes");
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("No Diabetes", nonDiabetesCount));
        series.getData().add(new XYChart.Data<>("Diabetes", diabetesCount));

        barChart.getData().add(series);

        // Statistics grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(30);
        statsGrid.setVgap(10);

        addStatistic(statsGrid, "Total Pasien", String.valueOf(totalPatients), 0);
        addStatistic(statsGrid, "Kasus Diabetes", diabetesCount + String.format(" (%.1f%%)", diabetesPercentage), 1);
        addStatistic(statsGrid, "Kasus Non-Diabetes", nonDiabetesCount + String.format(" (%.1f%%)", 100 - diabetesPercentage), 2);

        statsBox.getChildren().addAll(statsTitle, statsGrid, barChart);
        return statsBox;
    }

    private void addStatistic(GridPane grid, String label, String value, int col) {
        VBox statBox = new VBox(5);
        statBox.setAlignment(Pos.CENTER);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #667eea;");

        Label labelLabel = new Label(label);
        labelLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        statBox.getChildren().addAll(valueLabel, labelLabel);
        grid.add(statBox, col, 0);
    }

    private void showModernErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply theme to dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        if (darkThemeProperty.get()) {
            dialogPane.getStyleClass().add("dark-theme");
        }

        alert.showAndWait();
    }

    @Override
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
