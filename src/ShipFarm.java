import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Represents a Starbase where Starships can dock at.
 * @author allison
 * @version 1.0
 */
public class ShipFarm extends Application {
    private static ArrayList<ShipState> queueTypes = new ArrayList<>();
    private static ArrayList<String> queueNames = new ArrayList<>();
    private static Button[] docks = new Button[8];
    /**
     * Method used to create GUI for StarBase.
     * @param primaryStage Stage that the scene is placed on
     */
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        StackPane sp = new StackPane();
        ImageView space = new ImageView(new Image("radish.png"));
        sp.getChildren().add(space);
        sp.getChildren().add(pane);

        HBox header = new HBox();
        Label welcome = new Label("Welcome to Allison's Ship Farm!");
        welcome.setStyle("-fx-font-size: 36pt; -fx-font-family: Vivaldi");
        welcome.setTextFill(Color.WHITE);
        header.getChildren().add(welcome);
        header.setAlignment(Pos.CENTER);
        pane.setTop(header);

        HBox docks1 = new HBox();
        docks1.setSpacing(5);
        docks1.setAlignment(Pos.CENTER);
        HBox docks2 = new HBox();
        docks2.setSpacing(5);
        docks2.setAlignment(Pos.CENTER);

        setUpDocks(docks1, docks2);

        VBox dockGroups = new VBox(new HBox(), docks1, docks2);
        dockGroups.setSpacing(5);
        dockGroups.setAlignment(Pos.TOP_CENTER);
        pane.setCenter(dockGroups);

        setUpInputs(pane);

        Scene scene = new Scene(sp, 700, 400);
        Stage popup = new Stage();
        popup.getIcons().add(new Image("RadishIcon.gif"));
        popup.setTitle("Queue");
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Q) {
                if (!popup.isShowing()) {
                    BorderPane format = new BorderPane();
                    Label names = new Label("Current Ships in Queue: " + queueNames.toString());
                    names.setWrapText(true);
                    format.setCenter(names);
                    Scene popUpScene = new Scene(format, 300, 200);
                    popup.setScene(popUpScene);
                    popup.show();
                }
            }
        });

        primaryStage.setTitle("Starbase Command");
        primaryStage.getIcons().add(new Image("RadishIcon.gif"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Helper method that helps set up the docks.
     * @param docks1    Top row of docks
     * @param docks2    Bottom row of docks
     */
    private static void setUpDocks(HBox docks1, HBox docks2) {
        for (int i = 0; i < docks.length; i++) {
            Button dock = new Button("EMPTY");
            dock.setMinSize(128, 85);
            dock.setContentDisplay(ContentDisplay.TOP);
            dock.setStyle("-fx-background-color: #000000");
            dock.setOnMouseClicked(e -> {
                if (queueNames.size() < 1) {
                    dock.setText("EMPTY");
                    dock.setStyle("-fx-background-color: #000000");
                    dock.setGraphic(null);
                } else {
                    dock.setText(queueNames.get(0) + "\n" + queueTypes.get(0));
                    switch (queueTypes.get(0)) {
                    case SINGLE:
                        dock.setGraphic(new ImageView(new Image("single.gif")));
                        break;
                    case CRUSH:
                        dock.setGraphic(new ImageView(new Image("crush.gif")));
                        break;
                    case UWU:
                        dock.setGraphic(new ImageView(new Image("uwu.gif")));
                        break;
                    case BROKEN:
                        dock.setGraphic(new ImageView(new Image("broken.gif")));
                        break;
                    default:
                        break;
                    }
                    queueNames.remove(0);
                    queueTypes.remove(0);
                }
            });
            dock.setTextFill(Color.WHITE);
            if (docks1.getChildren().size() < 4) {
                docks1.getChildren().add(dock);
            } else {
                docks2.getChildren().add(dock);
            }
            docks[i] = dock;
        }
    }

    /**
     * Helper method that helps set up the inputs from user.
     * @param pane  the BorderPane that the inputs will be added to
     */
    private static void setUpInputs(BorderPane pane) {
        TextField name = new TextField();
        name.setPromptText("Ship name");
        ComboBox type = new ComboBox();
        type.getItems().add("Single");
        type.getItems().add("Crush");
        type.getItems().add("uwu");
        type.getItems().add("Broken");
        Button docking = new Button("Plant ship");
        docking.setOnAction(e -> {
            if (type.getValue() == null) {
                Alert noType = new Alert(Alert.AlertType.ERROR);
                noType.setHeaderText("Try Again");
                noType.setTitle("No Type");
                noType.setContentText("No ship state specified.");
                noType.showAndWait();
            } else if (name.getText().equals("")) {
                Alert noType = new Alert(Alert.AlertType.ERROR);
                noType.setHeaderText("Try Again");
                noType.setTitle("No Name");
                noType.setContentText("No ship name specified.");
                noType.showAndWait();
            } else {
                boolean docked = false;
                ShipState ship = null;
                Image deboarding = null;
                switch ((String) type.getValue()) {
                case "Single":
                    ship = ShipState.SINGLE;
                    deboarding = new Image("single.gif");
                    break;
                case "Crush":
                    ship = ShipState.CRUSH;
                    deboarding = new Image("crush.gif");
                    break;
                case "uwu":
                    ship = ShipState.UWU;
                    deboarding = new Image("uwu.gif");
                    break;
                case "Broken":
                    ship = ShipState.BROKEN;
                    deboarding = new Image("broken.gif");
                    break;
                default:
                    break;
                }
                for (int i = 0; i < docks.length; i++) {
                    if (docks[i].getText().equals("EMPTY")) {
                        docked = true;
                        String shipName = name.getText();
                        docks[i].setText(shipName + "\n" + ship);
                        docks[i].setGraphic(new ImageView(deboarding));
                        docks[i].setStyle("-fx-background-color: #bf9fd6");
                        break;
                    }
                }
                if (!docked) {
                    queueTypes.add(ship);
                    queueNames.add(name.getText());
                    Alert noSpace = new Alert(Alert.AlertType.WARNING);
                    noSpace.setHeaderText("Please wait");
                    noSpace.setTitle("No Docks Available");
                    noSpace.setContentText(name.getText() + " has not been granted docking clearence! \n"
                            + name.getText()
                            + " will be let in as docks open.");
                    noSpace.showAndWait();
                }
            }
            name.clear();
            type.getSelectionModel().clearSelection();
        });
        Button evacuate = new Button("EMPTY");
        evacuate.setStyle("-fx-background-color: #ba0000");
        evacuate.setTextFill(Color.WHITE);
        evacuate.setOnAction(e -> {
            for (int i = 0; i < docks.length; i++) {
                docks[i].setText("EMPTY");
                docks[i].setStyle("-fx-background-color: #000000");
                docks[i].setGraphic(null);
            }
            queueNames.clear();
            queueTypes.clear();
        });
        HBox inputs = new HBox(name, type, docking, evacuate);
        inputs.setSpacing(5);
        inputs.setMinHeight(50);
        inputs.setAlignment(Pos.TOP_CENTER);
        pane.setBottom(inputs);
    }

    /**
     * Main method for Starbase.
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
