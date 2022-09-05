package lk.ijse.dep9.clinic.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep9.clinic.security.SecurityContextHolder;
import lk.ijse.dep9.clinic.security.User;
import lk.ijse.dep9.clinic.security.UserRole;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public JFXTextField txtUsername;
    public JFXTextField txtPassword;
    public JFXButton btnLogin;

    public void initialize(){
        btnLogin.setDefaultButton(true);
    }
    public void btnLoginOnAction(ActionEvent actionEvent) throws ClassNotFoundException, IOException {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if(username.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Username cannot be empty").showAndWait();
            txtUsername.requestFocus();
            txtUsername.selectAll();
            return;
        } else if (password.isBlank()) {
            new Alert(Alert.AlertType.ERROR,"Password cannot be empty").showAndWait();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        } else if (!username.matches("^[a-zA-Z0-9]+$")) {
            new Alert(Alert.AlertType.ERROR,"Invalid Login Credentials").showAndWait();
            txtUsername.requestFocus();
            txtUsername.selectAll();
            return;
        }

        /* Making database connection and executing queries */

        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_clinic","root","3/0tril2NB")){
            System.out.println(connection);
            //String sql = "SELECT role FROM User WHERE username ='%s' AND password ='%s'";
            //sql = String.format(sql, username, password);
            //Statement stm = connection.createStatement();
            //ResultSet rst = stm.executeQuery(sql);

            /* enhancing security for SQL injections with prepareStatement */

            String sql = "SELECT role FROM User WHERE username =? AND password =?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1,username);
            stm.setString(2,password);
            ResultSet rst = stm.executeQuery();

            if(rst.next()){
                String role = rst.getString("role");
                System.out.println(role);
                SecurityContextHolder.setPrincipal(new User(username, UserRole.valueOf(role)));  // UserRole.valueOf(role)
                Scene scene = null;
                switch (role){
                    case "Admin":
                        scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminDashboardFrom.fxml")));
                        break;
                    case "Doctor":
                        scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/DoctorDashboardForm.fxml")));
                        break;
                    default:
                        scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/ReceptionistDashBoardForm.fxml")));
                }
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Open Source Medical Clinic");
                stage.centerOnScreen();
                stage.show();
                btnLogin.getScene().getWindow().hide();
            }else {
                new Alert(Alert.AlertType.ERROR,"Invalid Login Credentials").showAndWait();
                txtUsername.requestFocus();
                txtUsername.selectAll();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to connect with the database. Try again").showAndWait();
            return;
        }
    }
}
