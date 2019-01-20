package partyconverter2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class PartyConverter2 extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage ikkuna) throws Exception {
        BorderPane asettelu = new BorderPane();
        GridPane paneInner = new GridPane();
        GridPane paneOuter = new GridPane();
        Button btnAddNewTable = new Button("Add Table");
        Button btnGo = new Button("Go");
        Button btnShow = new Button("Show HH");
        Label labelInfo = new Label("");
        asettelu.setPrefSize(180, 250);

        ArrayList<TextField> listPlayerTxtF = new ArrayList<>(); //teksikenttien listan luonti
        for (int i = 1; i < 7; i++) {
            PlayerText playerText = new PlayerText();
            listPlayerTxtF.add(playerText.getTextField());
        }
        ArrayList<Label> listLabel = new ArrayList<>(); //labeleitte lsitan luonti
        for (int i = 1; i < 7; i++) {
            PlayerLabel playerLabel = new PlayerLabel("Player", i);
            listLabel.add(playerLabel.getPlayerLabel());
        }
        ArrayList<TextField> listTableTextField = new ArrayList<>(); //pöytälukumäärän kertovan pöytäTekstiKentän listan luonti
        PlayerText tableText = new PlayerText();

        listTableTextField.add(tableText.getTextField());
        paneInner.add(btnAddNewTable, 0, 0);
        paneInner.add(listTableTextField.get(0), 0, 1);
        paneInner.add(new Label("Table Name"), 1, 1);
        xy xy = new xy();
        for (int i = 0; i < listPlayerTxtF.size(); i++) {
            paneInner.add(listPlayerTxtF.get(i), xy.getX(), xy.getY());
            paneInner.add(listLabel.get(i), xy.getX() + 1, xy.getY());
            xy.setY(xy.getY() + 1);
        }
        paneInner.add(btnGo, 0, 8);
        paneInner.add(btnShow, 1, 8);
        
        btnAddNewTable.setOnAction((e) -> {
            GridPane paneNewTalbe = new GridPane();
            paneOuter.setMinWidth(500);
            xy.setX(xy.getX() + 1);
            xy.setY(1);
            TextField tableTextF = new TextField();
            tableTextF.setMaxWidth(100);
            listTableTextField.add(tableTextF);
            Button btnDel = new Button("Delete Table ");
            paneNewTalbe.add(btnDel, xy.getX(), 0);
            paneNewTalbe.add(tableTextF, xy.getX(), xy.getY());
            Label tableLabel = new Label("Table Name");
            paneNewTalbe.add(tableLabel, xy.getX() + 1, xy.getY());
            xy.setY(xy.getY() + 1);
            for (int i = 1; i < 7; i++) {
                PlayerText playerText = new PlayerText();
                listPlayerTxtF.add(playerText.getTextField());
                Label pLabel = new Label("Player " + i);
                paneNewTalbe.add(playerText.getTextField(), xy.getX(), xy.getY());
                paneNewTalbe.add(pLabel, xy.getX() + 1, xy.getY());
                xy.setY(xy.getY() + 1);
            }

            paneOuter.add(paneNewTalbe, xy.getX(), 0);
            btnDel.setOnAction(er -> {
                String tableName = tableTextF.getText();
                int ind = -1;
                if (!(tableName.equalsIgnoreCase(""))) {
                    for (int i = 0; i < listTableTextField.size(); i++) {
                        if (listTableTextField.get(i).getText().equals(tableName)) {
                            ind = i;

                        }
                    }
                    listTableTextField.remove(tableTextF);
                    paneOuter.getChildren().removeAll(paneNewTalbe);
                    if (ind > 0) {
                        for (int i = 0; i < 6; i++) {
                            listPlayerTxtF.remove(ind * 6);
                        }
                    }

                } else {
                    labelInfo.setText("Cannot delete empty table");
                }
            });
        });
        paneOuter.add(paneInner, 0, 0);
        asettelu.setTop(paneOuter);
        asettelu.setBottom(labelInfo);

        Scene scene = new Scene(asettelu);
        
        btnShow.setOnAction(eN -> {
            ArrayList<String> listOriginalHH = new ArrayList<>();
            for (int p = 0; p < listTableTextField.size(); p++) {
                String tableName = listTableTextField.get(p).getText();
                String tiedosto = getFile() + "\\" + tableName + ".txt";
                File directory = new File(tiedosto);
                try (Scanner lukija = new Scanner(new File(tiedosto))) {
                    while (lukija.hasNextLine()) {
                        listOriginalHH.add(lukija.nextLine());
                    }
                } catch (Exception ee) {

                }
            }
            String aa = "";
            for (int i = 0; i < listOriginalHH.size();i++) {
                aa += listOriginalHH.get(i) + "\n";
            }
            BorderPane pane = new BorderPane();
            TextArea txtA = new TextArea(aa);
            Button btnBack = new Button("Back");
            pane.setTop(btnBack);
            pane.setCenter(txtA);
            btnBack.setOnAction(k -> {
                ikkuna.setScene(scene);
            });
            ikkuna.setScene(new Scene(pane));
            
            
        });

        

        ArrayList<String> listHandNumbers = new ArrayList<>();

        RunOrStop runOrStop = new RunOrStop();
        runOrStop.stopRun();
        btnGo.setOnAction(eee -> {
            if (btnGo.getText().equalsIgnoreCase("Go")) {
                runOrStop.startRun();
                btnGo.setText("Stop");
            } else {
                runOrStop.stopRun();
                btnGo.setText("Go");
                ikkuna.setScene(scene);
            }
        });

        ikkuna.setScene(scene);

        new AnimationTimer() {
            private long sleepNanoseconds = 2000 * 1000000;
            private long prevTime = 0;

            @Override
            public void handle(long time) {
                if ((time - prevTime) < sleepNanoseconds) {
                    return;
                }
                //---------
                if (runOrStop.getStatus()) {
                    labelInfo.setText("");
                    for (int p = 0; p < listTableTextField.size(); p++) {
                        ArrayList<String> listOriginalHH = new ArrayList<>();
                        ArrayList<String> listConvertedHH = new ArrayList<>();
                        ArrayList<Integer> listHandStartsAt = new ArrayList<>();

                        String tableName = listTableTextField.get(p).getText();
                        String tiedosto = getFile() + "\\" + tableName + ".txt";
                        File directory = new File(tiedosto);

                        try (Scanner lukija = new Scanner(new File(tiedosto))) {
                            while (lukija.hasNextLine()) {
                                listOriginalHH.add(lukija.nextLine());
                            }
                        } catch (Exception ee) {
                            System.out.println("null");
                            System.out.println(tiedosto);
                            labelInfo.setText("File " + tiedosto + " not found");

                        }
                        for (int i = 0; i < listOriginalHH.size(); i++) {
                            if (listOriginalHH.get(i).contains("Player")) {
                                for (int k = 1; k < 7; k++) {
                                    String playerHhName = "Player" + k;
                                    if (listOriginalHH.get(i).contains(playerHhName)) {
                                        String getPlayerName = listPlayerTxtF.get((p) * 6 + (k - 1)).getText();
                                        if (getPlayerName.equals("")) {
                                            if (!(labelInfo.getText().contains(playerHhName)) && i > listOriginalHH.size() - 80) {
                                                labelInfo.setText(labelInfo.getText() + tableName + ": " + playerHhName + " is empty \n");

                                            }
                                            listConvertedHH.add(listOriginalHH.get(i));
                                        } else {
                                            String changeName = listOriginalHH.get(i).replace(playerHhName, getPlayerName);
                                            listConvertedHH.add(changeName);
                                        }
                                    }
                                }
                            } else {
                                listConvertedHH.add(listOriginalHH.get(i));
                            }
                        }
						String outPutPath = "folder path";
						String outPutFolder = outPutPath + tableName + ".txt";
                        try {
                            PrintWriter kirjoittaja = new PrintWriter(outPutFolder);
                            for (int i = 0; i < listConvertedHH.size(); i++) {
                                kirjoittaja.println(listConvertedHH.get(i));
                                System.out.println(listConvertedHH.get(i));
                            }
                            kirjoittaja.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("File not found");
                        }
                    }
                }
                //---------
                prevTime = time;

            }
        }.start();

        ikkuna.show();
    }

    public static String getFile() {
		String filePath = "file path";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String file = filePath;
        String fileDate = file + "\\" + dtf.format(now);
        return fileDate;
    }

}
