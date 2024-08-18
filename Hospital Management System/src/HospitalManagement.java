import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagement {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "sql@1234";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        }catch(ClassNotFoundException e){
            System.out.println("Driver not found");
        }
        Scanner sc = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,sc);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,sc);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }

            }   
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient , Doctor doctor, Connection connection,Scanner sc){
        System.out.println("Enter patient id: ");
        int patientId = sc.nextInt();
        System.out.println("Enter doctor id: ");
        int doctorId = sc.nextInt();
        System.out.println("Enter appointment date: ");
        String appointmentDate = sc.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate,connection)){
                String query = "INSERT INTO appointment (patient_id, doctor_id, appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rows = preparedStatement.executeUpdate();

                    if(rows > 0){
                        System.out.println("Appointment booked successfully");
                    }else{
                        System.out.println("Failed to book appointment");
                    }

                }catch(SQLException e){
                    System.out.println("Error while booking appointment");
                }
            }
        }else{
            System.out.println("Invalid patient id or doctor id");
        }

        
    }

    private static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT count(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0)
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            System.out.println("Error while checking doctor availability");
        }
        return false;
    }
}
