  package accountInfoRecognizer;                                                
                                                                                
  /**
   * The Class AccountInfoRecognizer.
   */
  public class AccountInfoRecognizer {      
	  
	  /** Default constructor. All methods in this class are static; this class is not directly instantiated. */
	  public AccountInfoRecognizer() {}
                                                                              
      /**
       * Check name.
       *
       * @param input the input
       * @param fieldName the field name
       * @return the string
       */
      public static String checkName(String input, String fieldName) {
          if (input.length() == 0) {
              return "There was no " + fieldName + " found";
          }
          if (input.length() > 32) {
              return "A " + fieldName + " must have no more than 32 characters";
          }
          if (!input.matches("[a-zA-Z]+")) {
              return "A " + fieldName + " may only contain characters A-Z and a-z";
          }
          return "";
      }
  }