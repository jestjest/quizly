package project;

/**
 * This class is used to uniquely determine one
 * field in a question.
 * @author hadip
 *
 */
public class ParamKey {
	/**
	 * A string containing the name of a field in a form.
	 */
	public String fieldName;

	/**
	 * The question number.
	 */
	public int number;

	/**
	 * Constructor
	 * @param name field name string.
	 * @param num question number.
	 */
	public ParamKey( String name, int num ) {
		fieldName = name;
		number = num;
	}

	/**
	 * Constructor overloaded.
	 * @param both A string containing both field name and
	 * question number in this format: FIELDNAME_NUMBER
	 * For example, responseText_3
	 */
	public ParamKey( String nameNumber ) {
		String[] outputs = nameNumber.split("_", 2);
		fieldName = outputs[0];
		number = Integer.parseInt( outputs[1] );
	}

	/**
	 * Check if the input string has correct format parameter.
	 * @param nameNumber
	 * @return true if valid format, false otherwise.
	 */
	public static Boolean isValid( String nameNumber ) {
		String[] outputs = nameNumber.split("_");
		for ( int i = 0 ; i < outputs.length; i++ ) {
			System.out.println(i+", "+outputs[i]);
		}
		if ( outputs.length != 2 ) {
			return false;
		}
		try { 
			Integer.parseInt( outputs[1] ); 
		} 
		catch(NumberFormatException e) { 
			return false; 
		} 
		catch(NullPointerException e) {
			return false;
		}
		return true;
	}

}
