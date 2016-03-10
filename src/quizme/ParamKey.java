package quizme;

public class ParamKey {
	
	public int questionOrder;
	public int contentOrder;

	public ParamKey( ) {
	}

	
	public boolean parseKey( String key ) {
		try {
			String[] output = key.split("_");
			if (! output[0].equals("response") ) {
				return false;
			}
			questionOrder = Integer.parseInt(output[1]);
			contentOrder = Integer.parseInt(output[2]);
			return true;
		} catch ( Exception e ) {
			return false;
		}
	}
}
