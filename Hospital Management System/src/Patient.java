import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient {
    private Connection connection;
    private Scanner sc;

    public Patient(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;   
    }
    public void addPatient(){
        System.out.print("Enter the name of patient: ");
        String name = sc.next();
        System.out.print("Enter the age of patient: ");
        int age = sc.nextInt();
        System.out.print("Enter the gender of patient: ");
        String gender = sc.next();

        try{
            String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int rows = preparedStatement.executeUpdate();
            if(rows>0){
                System.out.println("Patient added successfully");
            }else{
                System.out.println("Failed to add patient");
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                int age = result.getInt("age");
                String gender = result.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}
