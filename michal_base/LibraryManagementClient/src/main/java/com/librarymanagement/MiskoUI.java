package com.librarymanagement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class MiskoUI extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private CheckBox adminCheckBox;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private TableView<User> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        GridPane.setConstraints(usernameField, 0, 0);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        GridPane.setConstraints(passwordField, 0, 1);

        emailField = new TextField();
        emailField.setPromptText("Email");
        GridPane.setConstraints(emailField, 0, 2);

        adminCheckBox = new CheckBox("ADMIN");
        GridPane.setConstraints(adminCheckBox, 0, 3);

        Button clearButton = new Button("Clear");
        GridPane.setConstraints(clearButton, 1, 4);

        Button addButton = new Button("Add");
        GridPane.setConstraints(addButton, 2, 4);

        Button searchButton = new Button("Search");
        GridPane.setConstraints(searchButton, 3, 4);

        Button updateButton = new Button("Update");
        GridPane.setConstraints(updateButton, 4, 4);
        updateButton.setDisable(true);

        Button deleteButton = new Button("Delete");
        GridPane.setConstraints(deleteButton, 5, 4);
        deleteButton.setDisable(true);

        table.setItems(usersData);
        TableColumn<User, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        table.getColumns().addAll(idColumn, usernameColumn, passwordColumn, emailColumn, roleColumn);
        GridPane.setConstraints(table, 0, 5, 6, 1);

        clearButton.setOnAction(event -> clearFields());
        addButton.setOnAction(event -> addUser());
        searchButton.setOnAction(event -> searchUser());
        updateButton.setOnAction(event -> updateUser());
        deleteButton.setOnAction(event -> deleteUser());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                fillFields(newSelection);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        gridPane.getChildren().addAll(usernameField, passwordField, emailField, adminCheckBox, clearButton, addButton, searchButton, updateButton, deleteButton, table);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setTitle("User Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        adminCheckBox.setSelected(false);
    }

    private void fillFields(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        adminCheckBox.setSelected("ADMIN".equals(user.getRole()));
    }

    private void addUser() {
        try {
            Role r=new Role(adminCheckBox.isSelected());
            User user = new User(
                    usernameField.getText(),
                    passwordField.getText(),
                    emailField.getText(),
                    r
            );

            String userJson = new Gson().toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(userJson))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        User addedUser = new Gson().fromJson(response, User.class);
                        usersData.add(addedUser);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchUser() {
        try {
            String url = String.format("http://localhost:8080/api/users/search?username=%s&email=%s", usernameField.getText(), emailField.getText());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        List<User> users = parseUsers(response);
                        usersData.setAll(users);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        User selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                selectedUser.setUsername(usernameField.getText());
                selectedUser.setPassword(passwordField.getText());
                selectedUser.setEmail(emailField.getText());
                selectedUser.setRole(new Role(adminCheckBox.isSelected()));

                String userJson = new Gson().toJson(selectedUser);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/users/" + selectedUser.getUserId()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(userJson))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(response -> {
                            User updatedUser = new Gson().fromJson(response, User.class);
                            usersData.set(usersData.indexOf(selectedUser), updatedUser);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteUser() {
        User selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/users/" + selectedUser.getUserId()))
                        .DELETE()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            usersData.remove(selectedUser);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<User> parseUsers(String responseBody) {
        Gson gson = new Gson();
        User[] usersArray = gson.fromJson(responseBody, User[].class);
        return Arrays.asList(usersArray);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

