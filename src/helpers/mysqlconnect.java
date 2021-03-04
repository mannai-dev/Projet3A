/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import models.Reclamation;

/**
 *
 * @author ASUS
 */
public class mysqlconnect {
    
    Connection conn=null;
    
    public static Connection ConnectDb(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/work-it");
            JOptionPane.showMessageDialog(null, "connection established");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    
    
    }
    
    public static ObservableList<Reclamation> getDatausers(){
        Connection conn=ConnectDb();
        ObservableList<Reclamation> list=FXCollections.observableArrayList();
        
        try {
            PreparedStatement ps=conn.prepareStatement("select * from reclamation");
            ResultSet rs=ps.executeQuery();
            
            while(rs.next()){
            
            list.add(new Reclamation(Integer.parseInt(rs.getString("id")),rs.getString("title"),rs.getString("description"),rs.getString("answer"),Integer.parseInt(rs.getString("id_user"))));
            }
            
        } catch (Exception e) {
        }
        
        return list;
    };
    
}
