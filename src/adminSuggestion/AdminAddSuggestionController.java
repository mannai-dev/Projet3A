/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminSuggestion;

import helpers.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Suggestion;
/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AdminAddSuggestionController implements Initializable {
    
    
    
    @FXML
    private TextArea answerFld;
    
    String query=null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet=null;
    Suggestion suggestion=null;
    private boolean update;
    int suggestionid;
    int id_user;
    String title;
    String description;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void save() {
        
        connection = DbConnect.getConnect();
        
        String title=answerFld.getText();
        int id_user=1;
        
        if(description.isEmpty()){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Please Fill All Data");  
        }else{
        getQuery();
        insert();
        clean();
        
        
        }
        
        
        
    }


    @FXML
    private void clean() {

        answerFld.setText(null);

    }

    private void getQuery() {
        
       if(update==false){
         query="INSERT INTO `suggestion`(`answer`) VALUES (?)";
        }else{
         query="UPDATE `suggestion` SET `answer`=? WHERE id='"+suggestionid+"'";
                 
        }
        
       
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,answerFld.getText());
            
            preparedStatement.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminAddSuggestionController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("insert didnt work");
            
        }
    }
       void setTextField(int id, String title, String description, String answer, int id_user) {
        this.suggestionid = id;
        this.title=title;
        this.description=description;
        answerFld.setText(answer);
        this.id_user=id_user;
        
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

 
    
}
