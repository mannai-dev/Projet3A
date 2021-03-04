/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminSuggestion;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Suggestion;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AdminSuggestionController implements Initializable {

    
    @FXML
    private TableView<Suggestion> suggestionsTable;
    @FXML
    private TableColumn<Suggestion,String> idCol;
    @FXML
    private TableColumn<Suggestion,String> titleCol;
    @FXML
    private TableColumn<Suggestion,String> descriptionCol;
    @FXML
    private TableColumn<Suggestion,String> answerCol;
    @FXML
    private TableColumn<Suggestion,String> editCol;
    
    String query=null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet=null;
    Suggestion suggestion=null;
    
    
    ObservableList<Suggestion>  SuggestionList = FXCollections.observableArrayList();




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
    }  

    
     @FXML
    private void refreshTable() {
        try {
            SuggestionList.clear();
            
            query="SELECT * FROM `suggestion`";
            
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
            SuggestionList.add(new Suggestion(
                                Integer.parseInt(resultSet.getString("id")),
                                resultSet.getString("title"),
                                resultSet.getString("description"),
                                resultSet.getString("answer"),
                                Integer.parseInt(resultSet.getString("id_user"))));
            suggestionsTable.setItems(SuggestionList);
                    
                    
                    }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminSuggestionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 private void loadDate() {
        
        connection =DbConnect.getConnect();
        refreshTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
        
        
                //add cell of button edit 
         Callback<TableColumn<Suggestion, String>, TableCell<Suggestion, String>> cellFoctory = (TableColumn<Suggestion, String> param) -> {
            // make cell containing buttons
            final TableCell<Suggestion, String> cell = new TableCell<Suggestion, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                suggestion = suggestionsTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `suggestion` WHERE id  ="+suggestion.getId();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminSuggestionController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            suggestion = suggestionsTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/adminSuggestion/adminAddSuggestion.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(AdminSuggestionController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AdminAddSuggestionController addReclamationController = loader.getController();
                            addReclamationController.setUpdate(true);
                            addReclamationController.setTextField(suggestion.getId(), suggestion.getTitle(), 
                                    suggestion.getDescription(),suggestion.getAnswer(), suggestion.getId_user());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            

                           

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         
         
         editCol.setCellFactory(cellFoctory);
         suggestionsTable.setItems(SuggestionList);
        
        
        
        
    }


    
}
