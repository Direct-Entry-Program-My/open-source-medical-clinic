package lk.ijse.dep9.clinic.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import lk.ijse.dep9.clinic.security.SecurityContextHolder;

public class AdminDashboardFromController {

    public JFXButton btnProfileManage;
    public JFXButton btnViewRecords;
    public JFXButton btnSettings;
    public JFXButton btnLogOut;

    public void initialize(){
        System.out.println(SecurityContextHolder.getPrincipal());
    }

    public void btnProfileManageOnAction(ActionEvent actionEvent) {

    }

    public void btnViewRecordsOnAction(ActionEvent actionEvent) {

    }

    public void btnSettingsOnAction(ActionEvent actionEvent) {

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {

    }
}
