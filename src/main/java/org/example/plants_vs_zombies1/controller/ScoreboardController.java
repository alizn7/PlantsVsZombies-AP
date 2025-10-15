package org.example.plants_vs_zombies1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.model.PlayerRow;
import org.example.plants_vs_zombies1.service.SaveLoadService;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScoreboardController implements Initializable {

    @FXML private TableView<PlayerRow> scoreTable;
    @FXML private TableColumn<PlayerRow, Integer> rankCol;
    @FXML private TableColumn<PlayerRow, String> usernameCol;
    @FXML private TableColumn<PlayerRow, Integer> levelCol;
    @FXML private TableColumn<PlayerRow, Integer> winsCol;
    @FXML private TableColumn<PlayerRow, Integer> lossesCol;
    @FXML private TableColumn<PlayerRow, Integer> scoreCol;
    @FXML private TableColumn<PlayerRow, String> lastPlayedCol;
    @FXML private TableColumn<PlayerRow, String> unlockedPlantsCol;

    private final SaveLoadService saveLoadService = new SaveLoadService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        lastPlayedCol.setCellValueFactory(new PropertyValueFactory<>("lastPlayed"));
        unlockedPlantsCol.setCellValueFactory(new PropertyValueFactory<>("unlockedPlants"));

        List<Player> allPlayers = saveLoadService.loadAllPlayers();

        List<PlayerRow> rankedList = allPlayers.stream()
                .sorted(Comparator.comparingInt(Player::getScore).reversed())
                .map(p -> new PlayerRow(p))
                .collect(Collectors.toList());

        for (int i = 0; i < rankedList.size(); i++) {
            rankedList.get(i).setRank(i + 1);
        }

        ObservableList<PlayerRow> data = FXCollections.observableArrayList(rankedList);
        scoreTable.setItems(data);
    }

    @FXML
    private void onBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Stage stage = (Stage) scoreTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
