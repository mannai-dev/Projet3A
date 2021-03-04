/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work;

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
import models.Reclamation;

/**
 *
 * @author ASUS
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TableView<Reclamation> reclamationsTable;
    @FXML
    private TableColumn<Reclamation,String> idCol;
    @FXML
    private TableColumn<Reclamation,String> titleCol;
    @FXML
    private TableColumn<Reclamation,String> descriptionCol;
    @FXML
    private TableColumn<Reclamation,String> answerCol;
    @FXML
    private TableColumn<Reclamation,String> editCol;
    
    String query=null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet=null;
    Reclamation reclamation=null;

    
    ObservableList<Reclamation>  ReclamationList = FXCollections.observableArrayList();
    @FXML
    private Button suggestion;
    @FXML
    private Button admin;


            
    
    

    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
    }    

    @FXML
    private void getAddView() {
        
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/work/addReclamation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refreshTable() {
        try {
            ReclamationList.clear();
            
            query="SELECT * FROM `reclamation`";
            
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
            ReclamationList.add(new Reclamation(
                                Integer.parseInt(resultSet.getString("id")),
                                resultSet.getString("title"),
                                resultSet.getString("description"),
                                resultSet.getString("answer"),
                                Integer.parseInt(resultSet.getString("id_user"))));
            reclamationsTable.setItems(ReclamationList);
                    
                    
                    }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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
         Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>> cellFoctory = (TableColumn<Reclamation, String> param) -> {
            // make cell containing buttons
            final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

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
                                reclamation = reclamationsTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `reclamation` WHERE id  ="+reclamation.getId();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            reclamation = reclamationsTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/work/addReclamation.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AddReclamationController addReclamationController = loader.getController();
                            addReclamationController.setUpdate(true);
                            addReclamationController.setTextField(reclamation.getId(), reclamation.getTitle(), 
                                    reclamation.getDescription(),reclamation.getAnswer(), reclamation.getId_user());
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
         reclamationsTable.setItems(ReclamationList);
        
        
        
        
    }

    @FXML
    private void suggestion(ActionEvent event) throws IOException, SQLException  {
    suggestion.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/suggestion/suggestion.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/profil/profil.fxml"));
        Stage mainStage = new Stage();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    private void admin(ActionEvent event) throws IOException , SQLException {
        admin.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/adminReclamation/adminReclamation.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/profil/profil.fxml"));
        Stage mainStage = new Stage();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();
        
    }
    
}
