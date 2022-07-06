//STEP 1. Import required packages

import java.io.IOException;
import java.sql.*;
import java.util.*;  


public class JdbcDemo {

//Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//static final String DB_URL = "jdbc:mysql://localhost/companydb";
   static final String DB_URL = "jdbc:mysql://localhost/bank_accounts?allowPublicKeyRetrieval=true&useSSL=false";
//  Database credentials
   static final String USER = "user1";
   static final String PASS = "password";

   public static void main(String[] args)throws IOException {
   Connection conn = null;
   Statement stmt = null;

   Scanner sc= new Scanner(System.in);

// STEP 2. Connecting to the Database
   try{
      //STEP 2a: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      //STEP 2b: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //STEP 2c: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      

      while(true){
         int option;
         System.out.println("\nChoose an option number(1,2,3...) from the following to perform the correponding task:");
         System.out.println("1: To insert an account.");
         System.out.println("2: To modify an account detail.");
         System.out.println("3: To retrieve account details.");
         System.out.println("4: To delete an account.");
         System.out.println("5: To add a branch.");
         System.out.println("6: To update branch city.");
         System.out.println("7: To delete a branch.");
         System.out.println("8: To exit and restart application.\n");
   
         System.out.print("Enter option number: ");
         option = sc.nextInt();

         if(option == 8){
            break;
         }

         process_line(option,stmt,sc);
      }

//STEP 5: Clean-up environment
      stmt.close();
      conn.close();
	}catch(SQLException se){    	 //Handle errors for JDBC
      	se.printStackTrace();
   	}catch(Exception e){        	//Handle errors for Class.forName
      e.printStackTrace();
   }finally{				//finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }					//end finally try
   }					//end try
      }					//end main
   
   static void process_line(int option, Statement stmt, Scanner sc) throws IOException, SQLException{ 
      if(option == 1){
         System.out.println("Inserting an account... \n");
         
         String sql;
         sql = String.format("select max(account_id) as acc_id from accounts;");
         ResultSet rs = stmt.executeQuery(sql);

         int id = 0;
         while(rs.next()){
            id = Integer.parseInt(rs.getString("acc_id").substring(2))+1;
         }
         String acc_id = String.format("ac%03d",id);
         
         System.out.print("Enter branch id: ");
         String br_id = sc.next();
         System.out.print("Enter account type: ");
         String acc_type = sc.next();
         System.out.print("Enter phone number: ");
         String phone_no = sc.next();
         System.out.print("Enter balance: ");
         float balance = sc.nextFloat();
         System.out.print("Enter account owner's name: ");
         String name = sc.next();
         System.out.print("Enter account owner's address: ");
         String addr = sc.next();

         String query = String.format("INSERT INTO account_owner VALUES ('%s','%s','%s');",
                                       phone_no,name,addr);

         stmt.executeUpdate(query);

         query = String.format("INSERT INTO accounts VALUES ('%s','%s','%s','%s',%f);",
                                       acc_id,br_id,acc_type,phone_no,balance);
                                 
         stmt.executeUpdate(query);
      }

      else if(option==2){
         System.out.print("Enter account-id: ");
         String acc_id = sc.next();

         System.out.print("Enter new balance: ");
         float balance = sc.nextFloat();

         System.out.print("Enter new account type: ");
         String acc_type = sc.next();

         System.out.print("Enter account owner's new address: ");
         String addr = sc.next();

         String query = String.format("UPDATE accounts SET balance=%f WHERE account_id='%s';",balance,acc_id);
         stmt.executeUpdate(query);

         query = String.format("UPDATE accounts SET account_type='%s' WHERE account_id='%s';",acc_type,acc_id);
         stmt.executeUpdate(query);
         
         String sql;
         sql = String.format("select phone_number from accounts where account_id='%s';",acc_id);
         ResultSet rs = stmt.executeQuery(sql);

         String ph_num = null;
         while(rs.next()){
            ph_num = rs.getString("phone_number");
         }

         query = String.format("UPDATE account_owner SET owner_address='%s' WHERE phone_number='%s';",addr,ph_num);
         stmt.executeUpdate(query);
         rs.close();

      }

      else if(option==3){
         System.out.print("Enter account-id: ");
         String acc_id = sc.next();

         String sql;
         sql = String.format("select branch_id,account_type,phone_number,balance from accounts where account_id='%s';",acc_id);
         ResultSet rs = stmt.executeQuery(sql);

         String br_id = null; String acc_type = null; 
         String ph_num = null; String balance = null;
         while(rs.next()){
            br_id = rs.getString("branch_id");
            acc_type = rs.getString("account_type");
            ph_num = rs.getString("phone_number");
            balance = rs.getString("balance");
         }
         
         sql = String.format("select branch_city from bank_branch where branch_id='%s';",br_id);
         rs = stmt.executeQuery(sql);

         String br_city=null;
         while(rs.next()){
            br_city = rs.getString("branch_city");
         }

         sql = String.format("select owner_name,owner_address from account_owner where phone_number='%s';",ph_num);
         rs = stmt.executeQuery(sql);

         String name=null; String addr=null;
         while(rs.next()){
            name = rs.getString("owner_name");
            addr = rs.getString("owner_address");

         }

         System.out.println("\nAccount owner: " + name);
         System.out.println("Owner's address: " + addr);
         System.out.println("Phone number linked to account: " + ph_num);
         System.out.println("Account type: " + acc_type);
         System.out.println("Balance amount: " + balance.toString());
         System.out.println("Branch-id: " + br_id);
         System.out.println("Branch city: " + br_city + "\n");
         
         rs.close();
      }

      else if(option==4){
         System.out.print("Enter account-id: ");
         String acc_id = sc.next();

         String sql;
         sql = String.format("select phone_number from accounts where account_id='%s';",acc_id);
         ResultSet rs = stmt.executeQuery(sql);

         String ph_num = null; 
         while(rs.next()){
            ph_num = rs.getString("phone_number");
         }

         String query = String.format("DELETE FROM accounts WHERE account_id='%s';",acc_id);
         stmt.executeUpdate(query);

         query = String.format("DELETE FROM account_owner WHERE phone_number='%s';",ph_num);
         stmt.executeUpdate(query);

         rs.close();
      }

      else if(option==5){
         String sql;
         sql = String.format("select max(branch_id) as br_id from bank_branch;");
         ResultSet rs = stmt.executeQuery(sql);

         int id = 0;
         while(rs.next()){
            id = Integer.parseInt(rs.getString("br_id").substring(2))+1;
         }
         String br_id = String.format("br%03d",id);

         System.out.print("Enter city for the new branch: ");
         String br_city = sc.next();

         String query = String.format("INSERT INTO bank_branch VALUES ('%s','%s');",
                                       br_id,br_city);

         stmt.executeUpdate(query);
         rs.close();
      }

      else if(option==6){
         System.out.print("Enter branch id: ");
         String br_id = sc.next();

         System.out.print("Enter new city for the branch: ");
         String br_city = sc.next();

         String query = String.format("UPDATE bank_branch SET branch_city='%s' WHERE branch_id='%s';",br_city,br_id);
         stmt.executeUpdate(query);
      }

      else if(option==7){
         System.out.print("Enter branch id: ");
         String br_id = sc.next();

         System.out.print("Enter branch id of the branch, to which you want to shift the accounts to: ");
         String new_br_id = sc.next();
         
         String query = String.format("UPDATE accounts SET branch_id='%s' WHERE branch_id='%s';",new_br_id,br_id);
         stmt.executeUpdate(query);

         query = String.format("DELETE FROM bank_branch WHERE branch_id='%s';",br_id);
         stmt.executeUpdate(query);
      }
   }

   }					//end class
