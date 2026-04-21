package database;

import java.sql.*; 

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entityClasses.User;
import entityClasses.adminRequests;
import entityClasses.Post;
import entityClasses.Reply;


// TODO: Auto-generated Javadoc
/*******
 * <p> Title: Database Class. </p>
 * 
 * <p> Description: This is an in-memory database built on H2.  Detailed documentation of H2 can
 * be found at https://www.h2database.com/html/main.html (Click on "PDF (2MP) for a PDF of 438 pages
 * on the H2 main page.)  This class leverages H2 and provides numerous special supporting methods.
 * </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.00		2025-04-29 Updated and expanded from the version produce by on a previous
 * 							version by Pravalika Mukkiri and Ishwarya Hidkimath Basavaraj
 * @version 2.01		2025-12-17 Minor updates for Spring 2026
 */

/*
 * The Database class is responsible for establishing and managing the connection to the database,
 * and performing operations such as user registration, login validation, handling invitation 
 * codes, and numerous other database related functions.
 */
public class Database {

	/** The Constant JDBC_DRIVER. */
	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	
	/** The Constant DB_URL. */
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	/** The Constant USER. */
	//  Database credentials 
	static final String USER = "sa"; 
	
	/** The Constant PASS. */
	static final String PASS = ""; 

	/** The connection. */
	//  Shared variables used within this class
	private Connection connection = null;		// Singleton to access the database 
	
	/** The statement. */
	private Statement statement = null;			// The H2 Statement is used to construct queries
	
	/** The next post ID. */
	// Post/reply ID counter
	private int nextPostID = 1;
	
	// These are the easily accessible attributes of the currently logged-in user
	/** The current username. */
	// This is only useful for single user applications
	private String currentUsername;
	
	/** The current password. */
	private String currentPassword;
	
	/** The current first name. */
	private String currentFirstName;
	
	/** The current middle name. */
	private String currentMiddleName;
	
	/** The current last name. */
	private String currentLastName;
	
	/** The current preferred first name. */
	private String currentPreferredFirstName;
	
	/** The current email address. */
	private String currentEmailAddress;
	
	/** The current admin role. */
	private boolean currentAdminRole;
	
	/** The current new role 1. */
	private boolean currentNewRole1;
	
	/** The current new role 2. */
	private boolean currentNewRole2;

	/*******
	 * <p> Method: Database </p>
	 * 
	 * <p> Description: The default constructor used to establish this singleton object.</p>
	 * 
	 */
	
	public Database () {
		
	}
	
	
/*******
 * <p> Method: connectToDatabase </p>
 * 
 * <p> Description: Used to establish the in-memory instance of the H2 database from secondary
 *		storage.</p>
 *
 * @throws SQLException when the DriverManager is unable to establish a connection
 * 
 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
			initializeNextPostID();
			ensureGeneralThreadExists();
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
	/**
	 * *****
	 * <p> Method: createTables </p>
	 * 
	 * <p> Description: Used to create new instances of the two database tables used by this class.</p>
	 *
	 * @throws SQLException the SQL exception
	 */
		private void createTables() throws SQLException {
			// Create the user database
			String userTable = "CREATE TABLE IF NOT EXISTS userDB ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY, "
					+ "userName VARCHAR(255) UNIQUE, "
					+ "password VARCHAR(255), "
					+ "firstName VARCHAR(255), "
					+ "middleName VARCHAR(255), "
					+ "lastName VARCHAR (255), "
					+ "preferredFirstName VARCHAR(255), "
					+ "emailAddress VARCHAR(255), "
					+ "adminRole BOOL DEFAULT FALSE, "
					+ "newRole1 BOOL DEFAULT FALSE, "
					+ "newRole2 BOOL DEFAULT FALSE)";
			statement.execute(userTable);
			
			// Create the invitation codes table with expiration time.
		    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
		            + "code VARCHAR(10) PRIMARY KEY, "
		    		+ "emailAddress VARCHAR(255), "
		            + "role VARCHAR(10),"
		    		+ "expirationTime TIMESTAMP)";
		    statement.execute(invitationCodesTable);
		    
		    String otpTable = "CREATE TABLE IF NOT EXISTS oneTimePasses ("
					+ "otp VARCHAR(255) PRIMARY KEY, "
					+ "userName VARCHAR(255) UNIQUE)";
		    statement.execute(otpTable);
		    
		    // Create discussion post/reply table
		    String postTable = "CREATE TABLE IF NOT EXISTS postDB ("
		    		+ "postID INT PRIMARY KEY, "
		    		+ "parentPostID INT DEFAULT -1, "
		    		+ "userName VARCHAR(255), "
		    		+ "title VARCHAR(255), "
		    		+ "body CLOB, "
		    		+ "threadName VARCHAR(255), "
		    		+ "timeStamp TIMESTAMP, "
		    		+ "isDeleted BOOL DEFAULT FALSE, "
		    		+ "keywords VARCHAR(255), "
		    		+ "feedback CLOB, "
		    		+ "feedbackAuthor VARCHAR(255), "
		    		+ "isFlagged BOOL DEFAULT FALSE, "
		    		+ "reason CLOB)";
		    statement.execute(postTable);
		    
		    // Create thread table
		 	String threadTable = "CREATE TABLE IF NOT EXISTS threadDB ("
		 			+ "threadName VARCHAR(255) PRIMARY KEY, "
		 			+ "createdBy VARCHAR(255), "
		 			+ "createdAt TIMESTAMP)";
		 	statement.execute(threadTable);
		 	
		 	String readStatusTable = "CREATE TABLE IF NOT EXISTS ReadStatus ("
		            + "username VARCHAR(255), "
		            + "postID INT, "                                                      
		            + "PRIMARY KEY (username, postID))";
		    statement.execute(readStatusTable);
		    //Table for requests
		    String requestTable = "CREATE TABLE IF NOT EXISTS requestDB ("
		    		+ "requestID INT AUTO_INCREMENT PRIMARY KEY, "
		    		+ "requestSubmiter VARCHAR(255), "
		    		+ "recievingAdmin VARCHAR(255), "
		    		+ "body CLOB, "
		    		+ "completed BOOL DEFAULT FALSE, "
		    		+ "firstRequestID INT DEFAULT -1, "
		    		+ "timestamp TIMESTAMP)";
		    statement.execute(requestTable);
		}
		


/*******
 * <p> Method: isDatabaseEmpty </p>
 * 
 * <p> Description: If the user database has no rows, true is returned, else false.</p>
 * 
 * @return true if the database is empty, else it returns false
 * 
 */
	public boolean isDatabaseEmpty() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count") == 0;
			}
		}  catch (SQLException e) {
	        return false;
	    }
		return true;
	}
	
	
/*******
 * <p> Method: getNumberOfUsers </p>
 * 
 * <p> Description: Returns an integer .of the number of users currently in the user database. </p>
 * 
 * @return the number of user records in the database.
 * 
 */
	public int getNumberOfUsers() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch (SQLException e) {
	        return 0;
	    }
		return 0;
	}

/**
 * *****
 * <p> Method: register(User user) </p>
 * 
 * <p> Description: Creates a new row in the database using the user parameter. </p>
 *
 * @param user specifies a user object to be added to the database.
 * @throws SQLException when there is an issue creating the SQL command or executing it.
 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO userDB (userName, password, firstName, middleName, "
				+ "lastName, preferredFirstName, emailAddress, adminRole, newRole1, newRole2) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			currentUsername = user.getUserName();
			pstmt.setString(1, currentUsername);
			
			currentPassword = user.getPassword();
			pstmt.setString(2, currentPassword);
			
			currentFirstName = user.getFirstName();
			pstmt.setString(3, currentFirstName);
			
			currentMiddleName = user.getMiddleName();			
			pstmt.setString(4, currentMiddleName);
			
			currentLastName = user.getLastName();
			pstmt.setString(5, currentLastName);
			
			currentPreferredFirstName = user.getPreferredFirstName();
			pstmt.setString(6, currentPreferredFirstName);
			
			currentEmailAddress = user.getEmailAddress();
			pstmt.setString(7, currentEmailAddress);
			
			currentAdminRole = user.getAdminRole();
			pstmt.setBoolean(8, currentAdminRole);
			
			currentNewRole1 = user.getNewRole1();
			pstmt.setBoolean(9, currentNewRole1);
			
			currentNewRole2 = user.getNewRole2();
			pstmt.setBoolean(10, currentNewRole2);
			
			pstmt.executeUpdate();
		}
		
	}
	
/*******
 *  <p> Method: List getUserList() </p>
 *  
 *  <P> Description: Generate an List of Strings, one for each user in the database,
 *  starting with {@code <Select User>} at the start of the list.
 *  </p>
 *  
 *  @return a list of userNames found in the database.
 */
	public List<String> getUserList () {
		List<String> userList = new ArrayList<String>();
		userList.add("<Select a User>");
		String query = "SELECT userName FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userList.add(rs.getString("userName"));
			}
		} catch (SQLException e) {
	        return null;
	    }
//		System.out.println(userList);
		return userList;
	}

/*******
 * <p> Method: boolean loginAdmin(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Admin role.
 * 
 * @return true if the specified user has been logged in as an Admin else false.
 * 
 */
	public boolean loginAdmin(User user){
		// Validates an admin user's login credentials so the user can login in as an Admin.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "adminRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();	// If a row is returned, rs.next() will return true		
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
/*******
 * <p> Method: boolean loginRole1(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Student role.
 * 
 * @return true if the specified user has been logged in as an Student else false.
 * 
 */
	public boolean loginRole1(User user) {
		// Validates a student user's login credentials.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole1 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}

	/*******
	 * <p> Method: boolean loginRole2(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username, password, and role
	 * 		is the same as a row in the table for the username, password, and role. </p>
	 * 
	 * @param user specifies the specific user that should be logged in playing the Reviewer role.
	 * 
	 * @return true if the specified user has been logged in as an Student else false.
	 * 
	 */
	// Validates a reviewer user's login credentials.
	public boolean loginRole2(User user) {
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole2 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}
	
	
	/*******
	 * <p> Method: boolean doesUserExist(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username is  in the table. </p>
	 * 
	 * @param userName specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return true if the specified user is in the table else false.
	 * 
	 */
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM userDB WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

	
	/*******
	 * <p> Method: int getNumberOfRoles(User user) </p>
	 * 
	 * <p> Description: Determine the number of roles a specified user plays. </p>
	 * 
	 * @param user specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return the number of roles this user plays (0 - 5).
	 * 
	 */	
	// Get the number of roles that this user plays
	public int getNumberOfRoles (User user) {
		int numberOfRoles = 0;
		if (user.getAdminRole()) numberOfRoles++;
		if (user.getNewRole1()) numberOfRoles++;
		if (user.getNewRole2()) numberOfRoles++;
		return numberOfRoles;
	}	

	
	/*******
	 * <p> Method: String generateInvitationCode(String emailAddress, String role) </p>
	 * 
	 * <p> Description: Given an email address and a roles, this method establishes and invitation
	 * code and adds a record to the InvitationCodes table.  When the invitation code is used, the
	 * stored email address is used to establish the new user and the record is removed from the
	 * table.</p>
	 * 
	 * @param emailAddress specifies the email address for this new user.
	 * 
	 * @param role specified the role that this new user will play.
	 * 
	 * @return the code of six characters so the new user can use it to securely setup an account.
	 * 
	 */
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String emailAddress, String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 6-character code
	    
	    java.sql.Timestamp expirationTime = new java.sql.Timestamp(
		    	System.currentTimeMillis() + (30*60*1000) 
		);
	    
	    String query = "INSERT INTO InvitationCodes (code, emailaddress, role, expirationTime) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, emailAddress);
	        pstmt.setString(3, role);
	        pstmt.setTimestamp(4, expirationTime);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return code;
	}
	

	

	/*******
	 * <p> Method: int getNumberOfInvitations() </p>
	 * 
	 * <p> Description: Determine the number of outstanding invitations in the table.</p>
	 *  
	 * @return the number of invitations in the table.
	 * 
	 */
	// Number of invitations in the database
	public int getNumberOfInvitations() {
		String query = "SELECT COUNT(*) AS count FROM InvitationCodes";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return 0;
	}
	
	
	/**
	 * ****.
	 *
	 * @param userName the user name
	 * @return the string
	 */
	//generate a one time password and inserts links it to a user
	public String generateOneTimePassword(String userName) {
	    String otp = UUID.randomUUID().toString().substring(0, 10); // Generate a random 10-character code
	    String query = "INSERT INTO oneTimePasses (otp, userName) VALUES (?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, otp);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return otp;
	}
	
	
	/**
	 * **.
	 *
	 * @param userName the user name
	 * @return true, if successful
	 */
	public boolean otpForUserHasBeenGenerated(String userName) {
	    String query = "SELECT COUNT(*) AS count FROM oneTimePasses WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	 //     System.out.println(rs);
	        if (rs.next()) {
	            // Mark the code as used
	        	return rs.getInt("count")>0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}

	
	/*******
	 * <p> Method: boolean emailaddressHasBeenUsed(String emailAddress) </p>
	 * 
	 * <p> Description: Determine if an email address has been user to establish a user.</p>
	 * 
	 * @param emailAddress is a string that identifies a user in the table
	 *  
	 * @return true if the email address is in the table, else return false.
	 * 
	 */
	// Check to see if an email address is already in the database
	public boolean emailaddressHasBeenUsed(String emailAddress) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE emailAddress = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        ResultSet rs = pstmt.executeQuery();
	 //     System.out.println(rs);
	        if (rs.next()) {
	            // Mark the code as used
	        	return rs.getInt("count")>0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
	/*******
	 * <p> Method: String getRoleGivenAnInvitationCode(String code) </p>
	 * 
	 * <p> Description: Get the role associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the role for the code or an empty string.
	 * 
	 */
	// Obtain the roles associated with an invitation code.
	public String getRoleGivenAnInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	
	/**
	 * Gets the user name given otp.
	 *
	 * @param otp the otp
	 * @return the user name given otp
	 */
	public String getUserNameGivenOtp(String otp) {
	    String query = "SELECT * FROM oneTimePasses WHERE otp = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, otp);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("userName");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	
	/*******
	 * <p> Method: String getEmailAddressUsingCode (String code ) </p>
	 * 
	 * <p> Description: Get the email addressed associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the email address for the code or an empty string.
	 * 
	 */
	// For a given invitation code, return the associated email address of an empty string
	public String getEmailAddressUsingCode (String code ) {
	    String query = "SELECT emailAddress FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("emailAddress");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	
	/*******
	 * <p> Method: void removeInvitationAfterUse(String code) </p>
	 * 
	 * <p> Description: Remove an invitation record once it is used.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 */
	// Remove an invitation using an email address once the user account has been setup
	public void removeInvitationAfterUse(String code) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int counter = rs.getInt(1);
	            // Only do the remove if the code is still in the invitation table
	        	if (counter > 0) {
        			query = "DELETE FROM InvitationCodes WHERE code = ?";
	        		try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
	        			pstmt2.setString(1, code);
	        			pstmt2.executeUpdate();
	        		}catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return;
	}
	
	/**
	 * Delete user.
	 *
	 * @param username the username
	 * @return true, if successful
	 */
	public boolean deleteUser(String username) {
		String query = "DELETE FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			int rows = pstmt.executeUpdate();
			
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
	/**
	 * ***.
	 *
	 * @param otp the otp
	 */
	public void removeOTPAfterUse(String otp) {
	    String query = "SELECT COUNT(*) AS count FROM oneTimePasses WHERE otp = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, otp);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int counter = rs.getInt(1);
	            // Only do the remove if the OTP is still in the oneTimePasses table
	        	if (counter > 0) {
        			query = "DELETE FROM oneTimePasses WHERE otp = ?";
	        		try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
	        			pstmt2.setString(1, otp);
	        			pstmt2.executeUpdate();
	        		}catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return;
	}
	
	
	/**
	 * **.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public void updatePassword(String username, String password) {
	    String query = "UPDATE userDB SET password = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPassword = password;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/*******
	 * <p> Method: String getFirstName(String username) </p>
	 * 
	 * <p> Description: Get the first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the first name of a user given that user's username 
	 *  
	 */
	// Get the First Name
	public String getFirstName(String username) {
		String query = "SELECT firstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	

	/*******
	 * <p> Method: void updateFirstName(String username, String firstName) </p>
	 * 
	 * <p> Description: Update the first name of a user given that user's username and the new
	 *		first name.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @param firstName is the new first name for the user
	 *  
	 */
	// update the first name
	public void updateFirstName(String username, String firstName) {
	    String query = "UPDATE userDB SET firstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, firstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentFirstName = firstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/*******
	 * <p> Method: String getMiddleName(String username) </p>
	 * 
	 * <p> Description: Get the middle name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the middle name of a user given that user's username 
	 *  
	 */
	// get the middle name
	public String getMiddleName(String username) {
		String query = "SELECT MiddleName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("middleName"); // Return the middle name if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}

	
	/*******
	 * <p> Method: void updateMiddleName(String username, String middleName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param middleName is the new middle name for the user
	 *  
	 */
	// update the middle name
	public void updateMiddleName(String username, String middleName) {
	    String query = "UPDATE userDB SET middleName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, middleName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentMiddleName = middleName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getLastName(String username) </p>
	 * 
	 * <p> Description: Get the last name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the last name of a user given that user's username 
	 *  
	 */
	// get he last name
	public String getLastName(String username) {
		String query = "SELECT LastName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("lastName"); // Return last name role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateLastName(String username, String lastName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param lastName is the new last name for the user
	 *  
	 */
	// update the last name
	public void updateLastName(String username, String lastName) {
	    String query = "UPDATE userDB SET lastName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, lastName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentLastName = lastName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getPreferredFirstName(String username) </p>
	 * 
	 * <p> Description: Get the preferred first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the preferred first name of a user given that user's username 
	 *  
	 */
	// get the preferred first name
	public String getPreferredFirstName(String username) {
		String query = "SELECT preferredFirstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("preferredFirstName"); // Return the preferred first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updatePreferredFirstName(String username, String preferredFirstName) </p>
	 * 
	 * <p> Description: Update the preferred first name of a user given that user's username and
	 * 		the new preferred first name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param preferredFirstName is the new preferred first name for the user
	 *  
	 */
	// update the preferred first name of the user
	public void updatePreferredFirstName(String username, String preferredFirstName) {
	    String query = "UPDATE userDB SET preferredFirstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, preferredFirstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPreferredFirstName = preferredFirstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getEmailAddress(String username) </p>
	 * 
	 * <p> Description: Get the email address of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the email address of a user given that user's username 
	 *  
	 */
	// get the email address
	public String getEmailAddress(String username) {
		String query = "SELECT emailAddress FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("emailAddress"); // Return the email address if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateEmailAddress(String username, String emailAddress) </p>
	 * 
	 * <p> Description: Update the email address name of a user given that user's username and
	 * 		the new email address.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param emailAddress is the new preferred first name for the user
	 *  
	 */
	// update the email address
	public void updateEmailAddress(String username, String emailAddress) {
	    String query = "UPDATE userDB SET emailAddress = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentEmailAddress = emailAddress;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: boolean getUserAccountDetails(String username) </p>
	 * 
	 * <p> Description: Get all the attributes of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return true of the get is successful, else false
	 *  
	 */
	// get the attributes for a specified user
	public boolean getUserAccountDetails(String username) {
		String query = "SELECT * FROM userDB WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();			
			if (!rs.next()) return false;
	    	currentUsername = rs.getString(2);
	    	currentPassword = rs.getString(3);
	    	currentFirstName = rs.getString(4);
	    	currentMiddleName = rs.getString(5);
	    	currentLastName = rs.getString(6);
	    	currentPreferredFirstName = rs.getString(7);
	    	currentEmailAddress = rs.getString(8);
	    	currentAdminRole = rs.getBoolean(9);
	    	currentNewRole1 = rs.getBoolean(10);
	    	currentNewRole2 = rs.getBoolean(11);
			return true;
	    } catch (SQLException e) {
			return false;
	    }
	}
	
	
	/*******
	 * <p> Method: boolean updateUserRole(String username, String role, String value) </p>
	 * 
	 * <p> Description: Update a specified role for a specified user's and set and update all the
	 * 		current user attributes.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param role is string that specifies the role to update
	 * 
	 * @param value is the string that specified TRUE or FALSE for the role
	 * 
	 * @return true if the update was successful, else false
	 *  
	 */
	// Update a users role
	public boolean updateUserRole(String username, String role, String value) {

			/*******
			 * Admin constraints was initialy placed here but due to debugging issues, it is now in the main controller file
			 * 
			 * No Need for calculation of number of admins via Database since always one Admin is present
			 * User cannot delete their own Admin role
			 * 
			 * Admin Constraints Psuedocode
			 * => IF username == currentLoggedInUsername THEN
			 * =>    RETURN FALSE
			 * => # NOT Needed but in case
			 * => 
			 * => adminCount = COUNT(users WHERE admin_role == true)
			 * => IF adminCount <= 1 THEN
			 * =>    RETURN FALSE
			 * 
			 * 
			 * */
		
		if (role.compareTo("Admin") == 0) {
			String query = "UPDATE userDB SET adminRole = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentAdminRole = true;
				else
					currentAdminRole = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Student") == 0) {
			String query = "UPDATE userDB SET newRole1 = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentNewRole1 = true;
				else
					currentNewRole1 = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Staff") == 0) {
			String query = "UPDATE userDB SET newRole2 = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentNewRole2 = true;
				else
					currentNewRole2 = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}
	
	
	// Attribute getters for the current user
	/*******
	 * <p> Method: String getCurrentUsername() </p>
	 * 
	 * <p> Description: Get the current user's username.</p>
	 * 
	 * @return the username value is returned
	 *  
	 */
	public String getCurrentUsername() { return currentUsername;};

	
	/*******
	 * <p> Method: String getCurrentPassword() </p>
	 * 
	 * <p> Description: Get the current user's password.</p>
	 * 
	 * @return the password value is returned
	 *  
	 */
	public String getCurrentPassword() { return currentPassword;};

	
	/*******
	 * <p> Method: String getCurrentFirstName() </p>
	 * 
	 * <p> Description: Get the current user's first name.</p>
	 * 
	 * @return the first name value is returned
	 *  
	 */
	public String getCurrentFirstName() { return currentFirstName;};

	
	/*******
	 * <p> Method: String getCurrentMiddleName() </p>
	 * 
	 * <p> Description: Get the current user's middle name.</p>
	 * 
	 * @return the middle name value is returned
	 *  
	 */
	public String getCurrentMiddleName() { return currentMiddleName;};

	
	/*******
	 * <p> Method: String getCurrentLastName() </p>
	 * 
	 * <p> Description: Get the current user's last name.</p>
	 * 
	 * @return the last name value is returned
	 *  
	 */
	public String getCurrentLastName() { return currentLastName;};

	
	/*******
	 * <p> Method: String getCurrentPreferredFirstName( </p>
	 * 
	 * <p> Description: Get the current user's preferred first name.</p>
	 * 
	 * @return the preferred first name value is returned
	 *  
	 */
	public String getCurrentPreferredFirstName() { return currentPreferredFirstName;};

	
	/*******
	 * <p> Method: String getCurrentEmailAddress() </p>
	 * 
	 * <p> Description: Get the current user's email address name.</p>
	 * 
	 * @return the email address value is returned
	 *  
	 */
	public String getCurrentEmailAddress() { return currentEmailAddress;};

	
	/*******
	 * <p> Method: boolean getCurrentAdminRole() </p>
	 * 
	 * <p> Description: Get the current user's Admin role attribute.</p>
	 * 
	 * @return true if this user plays an Admin role, else false
	 *  
	 */
	public boolean getCurrentAdminRole() { return currentAdminRole;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole1() </p>
	 * 
	 * <p> Description: Get the current user's Student role attribute.</p>
	 * 
	 * @return true if this user plays a Student role, else false
	 *  
	 */
	public boolean getCurrentNewRole1() { return currentNewRole1;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole2() </p>
	 * 
	 * <p> Description: Get the current user's Reviewer role attribute.</p>
	 * 
	 * @return true if this user plays a Reviewer role, else false
	 *  
	 */
	public boolean getCurrentNewRole2() { return currentNewRole2;};
	
	
	/*******
	 * <p> Debugging method</p>
	 * 
	 * <p> Description: Debugging method that dumps the database of the console.</p>
	 * 
	 * @throws SQLException if there is an issues accessing the database.
	 * 
	 */
	// Dumps the database.
	public void dump() throws SQLException {
		String query = "SELECT * FROM userDB";
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData meta = resultSet.getMetaData();
		while (resultSet.next()) {
		for (int i = 0; i < meta.getColumnCount(); i++) {
		System.out.println(
		meta.getColumnLabel(i + 1) + ": " +
				resultSet.getString(i + 1));
		}
		System.out.println();
		}
		resultSet.close();
	}
	
	/**
	 * *****
	 * <p> Method: getAllUsers() </p>
	 * 
	 * <p> Description: Gets information for all users in the database. </p>
	 *
	 * @return the all users
	 */
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<>();
	    String query = "SELECT * FROM userDB ORDER BY userName";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            String userName = rs.getString("userName");
	            String password = rs.getString("password");
	            String firstName = rs.getString("firstName");
	            String middleName = rs.getString("middleName");
	            String lastName = rs.getString("lastName");
	            String preferredFirstName = rs.getString("preferredFirstName");
	            String emailAddress = rs.getString("emailAddress");
	            boolean adminRole = rs.getBoolean("adminRole");
	            boolean newRole1 = rs.getBoolean("newRole1");
	            boolean newRole2 = rs.getBoolean("newRole2");
	            
	            User user = new User(userName, password, firstName, middleName, lastName, 
	                               preferredFirstName, emailAddress, adminRole, newRole1, newRole2);
	            users.add(user);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return users;
	}
	
	/**
	 * ******
	 * <p> Method: getAllInvitations </p>
	 * 
	 * <p> Description: Returns all invitations sent out by admins </p>.
	 *
	 * @return the all invitations
	 */
	public List<String[]> getAllInvitations() {
		deleteExpiredInvitations();
	    List<String[]> invitations = new ArrayList<>();
	    String query = "SELECT code, emailAddress, role, expirationTime FROM InvitationCodes ORDER BY emailAddress";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            String[] invitation = new String[4];
	            invitation[0] = rs.getString("code");
	            invitation[1] = rs.getString("emailAddress");
	            invitation[2] = rs.getString("role");
	            
	            java.sql.Timestamp expiration = rs.getTimestamp("expirationTime");
	            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
	            invitation[3] = sdf.format(expiration);
	            
	            invitations.add(invitation);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return invitations;
	}
	
	/**
	 * ******
	 * Method: deletedInvitationCode(String code)
	 * 
	 * Description: Deletes invitation from the database.
	 *
	 * @param code the code
	 * @return true, if successful
	 */
	public boolean deleteInvitationCode(String code) {
	    String query = "DELETE FROM InvitationCodes WHERE code = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/*********
	 * <p> Method: deleteExpiredInvitations() </p>
	 * 
	 * <p> Description: Deletes all invitations codes that have expired (more than 24 hrs since creation). </p>
	 * 
	 */
	public void deleteExpiredInvitations() {
		String query = "DELETE FROM InvitationCodes WHERE expirationTime < CURRENT_TIMESTAMP";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        int deleted = pstmt.executeUpdate();
	        if (deleted > 0) {
	            System.out.println("Deleted " + deleted + " expired invitation(s)");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*********
	 * <p> Method: initializeNextPostID() </p>
	 * 
	 * <p> Description: Sets the next post ID based on the current max ID in the table. </p>
	 * 
	 */
	private void initializeNextPostID() {
		String query = "SELECT MAX(postID) AS maxID FROM postDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				nextPostID = rs.getInt("maxID") + 1;
				if (nextPostID <= 0) nextPostID = 1;
			}
		} catch (SQLException e) {
			nextPostID = 1;
		}
	}
	
	
	/*********
	 * <p> Method: generatePostID() </p>
	 * 
	 * <p> Description: Generates the next post ID. </p>
	 * 
	 * @return the next post ID
	 */
	public int generatePostID() {
		return nextPostID++;
	}
	
	
	/*********
	 * <p> Method: ensureGeneralThreadExists() </p>
	 * 
	 * <p> Description: Makes sure the default General thread always exists. </p>
	 * 
	 */
	private void ensureGeneralThreadExists() {
		try {
			String query = "SELECT COUNT(*) AS count FROM threadDB WHERE threadName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, "General");
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() && rs.getInt("count") == 0) {
					String insert = "INSERT INTO threadDB (threadName, createdBy, createdAt) VALUES (?, ?, ?)";
					try (PreparedStatement insertStmt = connection.prepareStatement(insert)) {
						insertStmt.setString(1, "General");
						insertStmt.setString(2, "System");
						insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						insertStmt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/*********
	 * <p> Method: ensureThreadExists(String threadName, String createdBy) </p>
	 * 
	 * <p> Description: Ensures a thread exists before using it. </p>
	 * 
	 * @param threadName the thread name
	 * @param createdBy the creator username
	 */
	private void ensureThreadExists(String threadName, String createdBy) {
		try {
			String query = "SELECT COUNT(*) AS count FROM threadDB WHERE threadName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, threadName);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() && rs.getInt("count") == 0) {
					String insert = "INSERT INTO threadDB (threadName, createdBy, createdAt) VALUES (?, ?, ?)";
					try (PreparedStatement insertStmt = connection.prepareStatement(insert)) {
						insertStmt.setString(1, threadName);
						insertStmt.setString(2, createdBy);
						insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						insertStmt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/*********
	 * <p> Method: getAllThreads() </p>
	 * 
	 * <p> Description: Returns all thread names. </p>
	 * 
	 * @return list of thread names
	 */
	public List<String> getAllThreads() {
	    List<String> threads = new ArrayList<>();
	    String query = "SELECT threadName, createdBy FROM threadDB ORDER BY threadName ASC";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String combined = "|" + rs.getString("threadName") + "| created by " + rs.getString("createdBy");
	            threads.add(combined);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!threads.contains("General")) {
			threads.add(0, "General");
		}

		return threads;
	}
	
	
	/*********
	 * <p> Method: createThread(String threadName, String createdBy) </p>
	 * 
	 * <p> Description: Creates a new thread. </p>
	 * 
	 * @param threadName the thread name
	 * @param createdBy the creator username
	 * @return true if successful else false
	 */
	public boolean createThread(String threadName, String createdBy) {
		if (threadName == null || threadName.trim().isEmpty()) {
			return false;
		}

		String trimmedThreadName = threadName.trim();
		
		// if input is xYYzzZ becomes -> Xyyzzz
		String finalThreadName = trimmedThreadName.substring(0, 1).toUpperCase() + trimmedThreadName.substring(1).toLowerCase();

		try {
			String check = "SELECT COUNT(*) AS count FROM threadDB WHERE threadName = ?";
			try (PreparedStatement checkStmt = connection.prepareStatement(check)) {
				checkStmt.setString(1, finalThreadName);
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt("count") > 0) {
					return false;
				}
			}

			String insert = "INSERT INTO threadDB (threadName, createdBy, createdAt) VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
				pstmt.setString(1, finalThreadName);
				pstmt.setString(2, createdBy);
				pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				pstmt.executeUpdate();
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*********
	 * <p> Method: deleteThread(String threadName) </p>
	 * 
	 * <p> Description: Deletes a thread and all posts/replies in that thread. </p>
	 * 
	 * @param threadName the thread name
	 * @return true if successful else false
	 */
	public boolean deleteThread(String threadName) {
		if (threadName == null || threadName.trim().isEmpty()) {
			return false;
		}

		if (threadName.equals("General")) {
			return false;
		}

		try {
			String deletePosts = "DELETE FROM postDB WHERE threadName = ?";
			try (PreparedStatement pstmtPosts = connection.prepareStatement(deletePosts)) {
				pstmtPosts.setString(1, threadName);
				pstmtPosts.executeUpdate();
			}

			String deleteReadStatus = "DELETE FROM ReadStatus WHERE postID IN (SELECT postID FROM postDB WHERE threadName = ?)";
			try (PreparedStatement pstmtRead = connection.prepareStatement(deleteReadStatus)) {
				pstmtRead.setString(1, threadName);
				pstmtRead.executeUpdate();
			}

			String deleteThread = "DELETE FROM threadDB WHERE threadName = ?";
			try (PreparedStatement pstmtThread = connection.prepareStatement(deleteThread)) {
				pstmtThread.setString(1, threadName);
				int rowsAffected = pstmtThread.executeUpdate();
				return rowsAffected > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*********
	 * <p> Method: getAllPosts() </p>
	 * 
	 * <p> Description: Returns all posts and replies in descending timestamp order. </p>
	 * 
	 * @return list of posts
	 */
	public List<Post> getAllPosts() {
		List<Post> posts = new ArrayList<>();
		String query = "SELECT * FROM postDB ORDER BY timeStamp DESC";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int postID = rs.getInt("postID");
				int parentPostID = rs.getInt("parentPostID");
				String userName = rs.getString("userName");
				String title = rs.getString("title");
				String body = rs.getString("body");
				String threadName = rs.getString("threadName");
				Timestamp timeStamp = rs.getTimestamp("timeStamp");
				boolean isDeleted = rs.getBoolean("isDeleted");
				String keywords = rs.getString("keywords");
				String feedback = rs.getString("feedback");
				String feedbackAuthor = rs.getString("feedbackAuthor");
				boolean isFlagged = rs.getBoolean("isFlagged");
				String reason = rs.getString("reason");

				Post post;
				if (parentPostID == -1) {
					post = new Post(userName, title, body, keywords, threadName);
				} else {
					post = new Reply(parentPostID, userName, body);
					post.setThreadName(threadName);
					post.setKeyWords(keywords);
				}

				post.setPostID(postID);
				if (post instanceof Reply) {
					((Reply) post).setParentPostID(parentPostID);
				}
				if (timeStamp != null) {
					post.setTimestamp(timeStamp.toLocalDateTime());
				}
				if (isDeleted) {
					post.changeDelete();
				}
				post.setFeedback(feedback);
				post.setFeedbackAuthor(feedbackAuthor);
				post.setFlag(isFlagged);
				post.setReason(reason);

				posts.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}
	
	
	/*********
	 * <p> Method: getPostByID() </p>
	 * 
	 * <p> Description: Fetches one post by ID. </p>
	 * 
	 * @param postID the post ID
	 * @return the post or null
	 */
	public Post getPostByID(int postID) {
		String query = "SELECT * FROM postDB WHERE postID = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, postID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int parentPostID = rs.getInt("parentPostID");
				String userName = rs.getString("userName");
				String title = rs.getString("title");
				String body = rs.getString("body");
				String threadName = rs.getString("threadName");
				Timestamp timeStamp = rs.getTimestamp("timeStamp");
				boolean isDeleted = rs.getBoolean("isDeleted");
				String keywords = rs.getString("keywords");
				String feedback = rs.getString("feedback");
				String feedbackAuthor = rs.getString("feedbackAuthor");
				boolean isFlagged = rs.getBoolean("isFlagged");
				String reason = rs.getString("reason");

				Post post;
				if (parentPostID == -1) {
					post = new Post(userName, title, body, keywords, threadName);
				} else {
					post = new Reply(parentPostID, userName, body);
					post.setThreadName(threadName);
					post.setKeyWords(keywords);
				}

				post.setPostID(postID);
				if (post instanceof Reply) {
					((Reply) post).setParentPostID(parentPostID);
				}
				if (timeStamp != null) {
					post.setTimestamp(timeStamp.toLocalDateTime());
				}
				if (isDeleted) {
					post.changeDelete();
				}
				post.setFeedback(feedback);
				post.setFeedbackAuthor(feedbackAuthor);
				post.setFlag(isFlagged);
				post.setReason(reason);

				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/*********
	 * <p> Method: getRepliesForPost() </p>
	 * 
	 * <p> Description: Returns all replies for a given parent post. </p>
	 * 
	 * @param parentPostID the parent post ID
	 * @return list of replies
	 */
	public List<Reply> getRepliesForPost(int parentPostID) {
		List<Reply> replies = new ArrayList<>();
		String query = "SELECT * FROM postDB WHERE parentPostID = ? ORDER BY timeStamp ASC";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, parentPostID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Reply reply = new Reply(
						rs.getInt("parentPostID"),
						rs.getString("userName"),
						rs.getString("body")
				);

				reply.setPostID(rs.getInt("postID"));
				reply.setThreadName(rs.getString("threadName"));
				reply.setKeyWords(rs.getString("keywords"));

				Timestamp replyTimeStamp = rs.getTimestamp("timeStamp");
				if (replyTimeStamp != null) {
					reply.setTimestamp(replyTimeStamp.toLocalDateTime());
				}

				if (rs.getBoolean("isDeleted")) {
					reply.changeDelete();
				}
				reply.setFeedback(rs.getString("feedback"));
				reply.setFeedbackAuthor(rs.getString("feedbackAuthor"));
				reply.setFlag(rs.getBoolean("isFlagged"));
				reply.setReason(rs.getString("reason"));

				replies.add(reply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return replies;
	}
	
	
	/*********
	 * <p> Method: createPost() </p>
	 * 
	 * <p> Description: Creates a post and stores it in the database. </p>
	 * 
	 * @param username the username
	 * @param title the title
	 * @param body the body
	 * @param keywords the keywords
	 * @param threadName the thread name
	 * @return created post
	 * @throws SQLException the SQL exception
	 */
	public Post createPost(String username, String title, String body, String keywords, String threadName) throws SQLException {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Post title cannot be empty.");
		}

		if (body == null || body.trim().isEmpty()) {
			throw new IllegalArgumentException("Post body cannot be empty.");
		}

		if (threadName == null || threadName.trim().isEmpty()) {
			threadName = "General";
		}

		ensureThreadExists(threadName, username);

		Post post = new Post(username, title.trim(), body.trim(), keywords, threadName);
		post.setPostID(generatePostID());

		String insertPost = "INSERT INTO postDB "
				+ "(postID, parentPostID, userName, title, body, threadName, timeStamp, isDeleted, keywords, feedback, feedbackAuthor, isFlagged, reason) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(insertPost)) {
			pstmt.setInt(1, post.getPostID());
			pstmt.setInt(2, -1);
			pstmt.setString(3, post.getUsername());
			pstmt.setString(4, post.getTitle());
			pstmt.setString(5, post.getBody());
			pstmt.setString(6, post.getThreadName());
			pstmt.setTimestamp(7, Timestamp.valueOf(post.getTimestamp()));
			pstmt.setBoolean(8, post.isDeleted());
			pstmt.setString(9, post.getKeyWords());
			pstmt.setString(10, post.getFeedback());
			pstmt.setString(11, post.getFeedbackAuthor());
			pstmt.setBoolean(12, post.isFlag());
			pstmt.setString(13, post.getReason());
			pstmt.executeUpdate();
		}

		return post;
	}
	
	
	/*********
	 * <p> Method: createReply() </p>
	 * 
	 * <p> Description: Creates a reply and stores it in the database. </p>
	 * 
	 * @param username the username
	 * @param body the reply body
	 * @param keywords the keywords
	 * @param threadName the thread name
	 * @param parentPostID the parent post ID
	 * @return created reply
	 * @throws SQLException the SQL exception
	 */
	public Reply createReply(String username, String body, String keywords, String threadName, int parentPostID) throws SQLException {
		if (body == null || body.trim().isEmpty()) {
			throw new IllegalArgumentException("Reply body cannot be empty.");
		}

		Post parentPost = getPostByID(parentPostID);
		if (parentPost == null) {
			throw new IllegalArgumentException("Parent post not found.");
		}

		Reply reply = new Reply(parentPostID, username, body.trim());
		reply.setPostID(generatePostID());
		reply.setThreadName((threadName == null || threadName.isBlank()) ? parentPost.getThreadName() : threadName);
		reply.setKeyWords(keywords);

		ensureThreadExists(reply.getThreadName(), username);

		String insertReply = "INSERT INTO postDB "
				+ "(postID, parentPostID, userName, title, body, threadName, timeStamp, isDeleted, keywords, feedback, feedbackAuthor, isFlagged, reason) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(insertReply)) {
			pstmt.setInt(1, reply.getPostID());
			pstmt.setInt(2, reply.getParentPostID());
			pstmt.setString(3, reply.getUsername());
			pstmt.setString(4, reply.getTitle());
			pstmt.setString(5, reply.getBody());
			pstmt.setString(6, reply.getThreadName());
			pstmt.setTimestamp(7, Timestamp.valueOf(reply.getTimestamp()));
			pstmt.setBoolean(8, reply.isDeleted());
			pstmt.setString(9, reply.getKeyWords());
			pstmt.setString(10, reply.getFeedback());
			pstmt.setString(11, reply.getFeedbackAuthor());
			pstmt.setBoolean(12, reply.isFlag());
			pstmt.setString(13, reply.getReason());
			pstmt.executeUpdate();
		}

		return reply;
	}
	
	/*********
	 * <p> Method: createRequest() </p>
	 * 
	 * <p> Description: Creates a request and stores it in the database. </p>
	 * 
	 * @param requester the user who submited the request
	 * @param admin the admin user who is recieving the request
	 * @param body text that contains the request made by the requester
	 * @return created requests
	 * @throws SQLException the SQL exception
	 */
	
	public adminRequests createRequest(String requester, String admin, String body) {
		adminRequests requests = new adminRequests(requester, body, admin);
		String sql = "INSERT INTO requestDB "
				+ "(requestSubmiter, recievingAdmin, body, adminActions, completed, firstRequestID, timestamp) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, requester);
			pstmt.setString(2, admin);
			pstmt.setString(3, body);
			pstmt.setString(4, "");
			pstmt.setBoolean(5, false);
			pstmt.setInt(6, -1);
			pstmt.setTimestamp(7, Timestamp.valueOf(requests.getTimeStamp()));
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				requests.setRequestID(rs.getInt(1));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return requests;
	}
	/*********
	 * <p> Method: getRequestsForAdmin() </p>
	 * 
	 * <p> Description: gets Requests sent to the current admin user. </p>
	 * 
	 * @param admin current admin
	 * @return created reply
	 * @throws SQLException the SQL exception
	 */
	public List<adminRequests> getRequestsForAdmin(String admin) {

	    List<adminRequests> list = new ArrayList<>();

	    String sql = "SELECT * FROM requestDB WHERE recievingAdmin = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, admin);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            adminRequests request = new adminRequests();

	            request.setRequestID(rs.getInt("requestID"));
	            request.setRequestSubmiter(rs.getString("requestSubmiter"));
	            request.setRecievingAdmin(rs.getString("recievingAdmin"));
	            request.setBody(rs.getString("body"));
	            request.setCompleted(rs.getBoolean("completed"));
	            request.setTimeStamp(rs.getTimestamp("timestamp").toLocalDateTime());

	            list.add(request);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	/*********
	 * <p> Method: requestCompletion() </p>
	 * 
	 * <p> Description: Marks requests made to the admin as completed. </p>
	 * 
	 * @param requestID the ID of the request that the admin would like to mark as completed
	 * @throws SQLException the SQL exception
	 */
	public void requestCompletion(int RequestID) {
		String sql = "UPDATE requestDB SET completed = ? WHERE requestID = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setBoolean(1, true);
			pstmt.setInt(2, RequestID);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*********
	 * <p> Method: getRequests() </p>
	 * 
	 * <p> Description: get all the to put into table. </p>
	 * 
	 * @param user the username
	 * @return list made 
	 * @throws SQLException the SQL exception
	 */
	
	public List<adminRequests> getRequests(String user) {
		List<adminRequests> list = new ArrayList<>();
		String sql = "SELECT * FROM requestDB WHERE requestSubmiter = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, user);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				adminRequests request = new adminRequests();
				
				request.setRequestID(rs.getInt("requestID"));
				request.setFirstRequestID(rs.getInt("firstRequestID"));
				request.setRequestSubmiter(rs.getString("requestSubmiter"));
				request.setRecievingAdmin(rs.getString("recievingAdmin"));
				request.setBody(rs.getString("body"));
				request.setCompleted(rs.getBoolean("completed"));
				request.setTimeStamp(rs.getTimestamp("timestamp").toLocalDateTime());
						
				list.add(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/*********
	 * <p> Method: updateRequest() </p>
	 * 
	 * <p> Description: update Request specified. </p>
	 * 
	 * @param request a request made by user
	 * @throws SQLException the SQL exception
	 */
	
	public void updateRequest(adminRequests request) {
		String sql = "UPDATE requestDB SET body=?, completed=?  WHERE requestID=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, request.getBody());
			pstmt.setBoolean(2, request.getCompleted());
			pstmt.setInt(3, request.getRequestID());
			
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*********
	 * <p> Method: getAllAdmins() </p>
	 * 
	 * <p> Description: makes an array  list . </p>
	 * 
	 * @return admins list of all
	 * @throws SQLException the SQL exception
	 */
	public List<String> getAllAdmins() {
		List<String> admins = new ArrayList<>();
		String sql = "SELECT userName FROM userDB WHERE adminROle = TRUE";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				admins.add(rs.getString("userName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return admins;
	}
	
	
	/**
	 * *******
	 * <p> Method: deletePost() </p>
	 * 
	 * <p> Description: Soft deletes a post in the database. </p>
	 *
	 * @param post the post
	 * @return true if successful else false
	 */
	public boolean deletePost(Post post) {
		if (post == null) {
			return false;
		}

		String query = "UPDATE postDB SET isDeleted = TRUE WHERE postID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, post.getPostID());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*********
	 * <p> Method: deleteReply() </p>
	 * 
	 * <p> Description: Soft deletes a reply in the database. </p>
	 * 
	 * @param reply the reply
	 * @return true if successful else false
	 */
	public boolean deleteReply(Reply reply) {
		if (reply == null) {
			return false;
		}

		String query = "UPDATE postDB SET isDeleted = TRUE WHERE postID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, reply.getPostID());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*********
	 * <p> Method: markPostAsRead() </p>
	 * 
	 * <p> Description: Marks a post as read for a user. </p>
	 * 
	 * @param username the username
	 * @param postID the post ID
	 * @return true if successful else false
	 */
	public boolean markPostAsRead(String username, int postID) {
		String query = "MERGE INTO ReadStatus (username, postID) KEY (username, postID) VALUES (?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setInt(2, postID);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*********
	 * <p> Method: isPostRead() </p>
	 * 
	 * <p> Description: Checks whether a post is marked as read by a user. </p>
	 * 
	 * @param username the username
	 * @param postID the post ID
	 * @return true if read else false
	 */
	public boolean isPostRead(String username, int postID) {
		String query = "SELECT COUNT(*) AS count FROM ReadStatus WHERE username = ? AND postID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setInt(2, postID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("count") > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Search posts.
	 *
	 * @param keyword the keyword
	 * @param thread the thread
	 * @return the list
	 */
	public List<Post> searchPosts(String keyword, String thread) {
		List<Post> posts = new ArrayList<>();
		String query;
 
		if (thread == null || thread.isEmpty()) {
			// Search all threads
			query = "SELECT * FROM postDB WHERE isDeleted = FALSE AND parentPostID = -1 AND "
					+ "(title LIKE ? OR body LIKE ? OR keywords LIKE ?) "
					+ "ORDER BY timeStamp DESC";
		} else {
			// Search specific thread only
			query = "SELECT * FROM postDB WHERE isDeleted = FALSE AND parentPostID = -1 AND threadName = ? AND "
					+ "(title LIKE ? OR body LIKE ? OR keywords LIKE ?) "
					+ "ORDER BY timeStamp DESC";
		}
 
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			String searchTerm = "%" + keyword + "%";
 
			if (thread == null || thread.isEmpty()) {
				pstmt.setString(1, searchTerm);
				pstmt.setString(2, searchTerm);
				pstmt.setString(3, searchTerm);
			} else {
				pstmt.setString(1, thread);
				pstmt.setString(2, searchTerm);
				pstmt.setString(3, searchTerm);
				pstmt.setString(4, searchTerm);
			}
 
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Post post = new Post(
					rs.getString("userName"),
					rs.getString("title"),
					rs.getString("body"),
					rs.getString("keywords"),
					rs.getString("threadName")
				);
				post.setPostID(rs.getInt("postID"));
				Timestamp ts = rs.getTimestamp("timeStamp");
				if (ts != null) {
					post.setTimestamp(ts.toLocalDateTime());
				}
				posts.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Search posts.
	 *
	 * @param thread the thread
	 * @return the list
	 */
	// modified search for thread filtering only
	public List<Post> searchPosts(String thread) {
		List<Post> posts = new ArrayList<>();
		String query;
 

		query = "SELECT * FROM postDB WHERE isDeleted = FALSE AND parentPostID = -1 AND threadName = ? "
					+ "ORDER BY timeStamp DESC";
 
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
 
			pstmt.setString(1, thread);
 
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Post post = new Post(
					rs.getString("userName"),
					rs.getString("title"),
					rs.getString("body"),
					rs.getString("keywords"),
					rs.getString("threadName")
				);
				post.setPostID(rs.getInt("postID"));
				Timestamp ts = rs.getTimestamp("timeStamp");
				if (ts != null) {
					post.setTimestamp(ts.toLocalDateTime());
				}
				posts.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Update an existing post in the database.
	 *
	 * @param post the post
	 * @return true, if successful
	 */
	public boolean updatePost(Post post) {
	    if (post == null) {
	        return false;
	    }
	    
	    try {
	        String sql = "UPDATE postDB SET " 
	    + "title = ?, " 
	    + "body = ?, " 
	    + "threadName = ?, " 
	    + "feedback = ?, " 
	    + "feedbackAuthor = ?, "
	    + "isFlagged = ?, "
	    + "reason = ? "
	    + "WHERE postID = ?";
	        
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, post.getTitle());
	        pstmt.setString(2, post.getBody());
	        pstmt.setString(3, post.getThreadName());
	        pstmt.setString(4, post.getFeedback());
	        pstmt.setString(5, post.getFeedbackAuthor());
	        pstmt.setBoolean(6, post.isFlagged());
	        pstmt.setString(7, post.getReason());
	        pstmt.setInt(8, post.getPostID());
	        
	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * flags a post within the database.
	 *
	 * @param post the post
	 * @return true, if successful
	 */
	public boolean flagPost(Post post) {
	    if (post == null) {
	        return false;
	    }
	    
	    try {
	        String sql = "UPDATE postDB SET " 
	    + "isFlagged = ?, "
	    + "reason = ? "
	    + "WHERE postID = ?";
	        
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setBoolean(1, post.isFlagged());
	        pstmt.setString(2, post.getReason());
	        pstmt.setInt(3, post.getPostID());
	        
	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * flags a post within the database.
	 *
	 * @param post the post
	 * @return true, if successful
	 */
	public boolean unFlagPost(Post post) {
	    if (post == null) {
	        return false;
	    }
	    
	    try {
	        String sql = "UPDATE postDB SET " 
	    + "isFlagged = ?, "
	    + "reason = ? "
	    + "WHERE postID = ?";
	        
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setBoolean(1, post.isFlagged());
	        pstmt.setString(2, post.getReason());
	        pstmt.setInt(3, post.getPostID());
	        
	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	

	/**
	 * <p> Removes the post from the database. </p>
	 *
	 * @param post the post
	 * @return true, if successful
	 */
	public boolean removePost(Post post) {
			if (post == null) {
				return false;
			}

			try {
				String deletePosts = "DELETE FROM postDB WHERE postID = ?";
				try (PreparedStatement pstmtPosts = connection.prepareStatement(deletePosts)) {
					pstmtPosts.setInt(1, post.getPostID());
					return pstmtPosts.executeUpdate() > 0;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	
	/*******
	 * <p> Method: void closeConnection()</p>
	 * 
	 * <p> Description: Closes the database statement and connection.</p>
	 * 
	 */
	// Closes the database statement and connection.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	
}