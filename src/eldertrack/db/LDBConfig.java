package eldertrack.db;

// THIS FILE IS UNDER .GITIGNORE AND WILL NOT CAUSE CONFLICTS.

public class LDBConfig {
	public static final boolean useLocal = false;
	static String hostname = "127.0.0.1";
	static String port = "3306";
	static String schema = "eldertrack";
	static String dbuser = "eldertrackadmin";
	public static final String dbencrypted = "cBZbw9N4H7p6YAPXv4z5/w==";
	static String dbpw = "";
	static String url = "jdbc:mysql://" + hostname + ":" + port + "/" + schema + "?useSSL=false";
	
	public static void setPassword(byte[] newpw) {
		dbpw = new String(newpw);
	}
}
