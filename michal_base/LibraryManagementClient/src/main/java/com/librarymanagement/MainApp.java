package com.librarymanagement;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class MainApp extends Application {

    private ObservableList<User1> usersData = FXCollections.observableArrayList();
    private TableView<User1> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        GridPane.setConstraints(firstNameField, 0, 0);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        GridPane.setConstraints(lastNameField, 0, 1);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 2);

        Button searchButton = new Button("Search");
        GridPane.setConstraints(searchButton, 2, 2);

        table.setItems(usersData);
        TableColumn<User1, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<User1, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        table.getColumns().addAll(firstNameColumn, lastNameColumn);
        GridPane.setConstraints(table, 0, 3, 3, 1);

        submitButton.setOnAction(event -> submitUser(firstNameField.getText(), lastNameField.getText()));
        searchButton.setOnAction(event -> searchUser(firstNameField.getText(), lastNameField.getText()));

        gridPane.getChildren().addAll(firstNameField, lastNameField, submitButton, searchButton, table);

        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setTitle("User Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void submitUser(String firstName, String lastName) {
        try {
            String json = String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\"}", firstName, lastName);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchUser(String firstName, String lastName) {
        try {
            String url = String.format("http://localhost:8080/api/users/search?firstName=%s&lastName=%s", firstName, lastName);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        // Parse the response and update the table
                        List<User1> users = parseUsers(response);
                        usersData.setAll(users);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<User1> parseUsers(String responseBody) {
        // Assuming responseBody is JSON array of User objects
        Gson gson = new Gson();
        User1[] usersArray = gson.fromJson(responseBody, User1[].class);
        return Arrays.asList(usersArray);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

