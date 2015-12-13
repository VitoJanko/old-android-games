import java.sql.*;
import java.util.ArrayList;

public class SQL extends Thread {
	Statement s;
	Connection conn = null;
	ArrayList<String[]> seznamIgralcev = new ArrayList<String[]>();
	boolean updating = true;
	MindblastLobby lobbyManager = new MindblastLobby();
	
	public SQL(MindblastLobby lobbyManager){
		this.lobbyManager = lobbyManager;
	}
	
	/**
	 * Pošlje podatke v MindblastLobby
	 */
	public void sendData(){
		lobbyManager.print(seznamIgralcev);
	}
	
	public void insert(String id,String name){
		if(conn == null){
			connect();
		}
		
		try{
		 Statement s = conn.createStatement ();
		 long trenutniCas = System.currentTimeMillis();
		 
		 //Pazljivo ko vstavljaš stringe! Morajo bit navednice oz '!
		 s.executeUpdate ("INSERT INTO mindblastLobby (id,name,score,time) VALUES ("+id+",'"+name+"',0,"+trenutniCas+") ON DUPLICATE KEY UPDATE name='"+name+"', score=0, time="+trenutniCas);
		 s.close ();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Thread se naredi, laufa v neskonènost,
	 * vsakiè se pogleda ali je povezano z sql serverjem èe ni ga poveže.
	 */
	public void run(){
		while(true){
			if(conn == null){
				connect();
				
			}
			querry();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void deletPlayer(String idosebe2,String requestID2){
		if(conn == null){
			connect();
		}
		
		try{
		 Statement s = conn.createStatement ();
		 
		 //Pazljivo ko vstavljaš stringe! Morajo bit navednice oz '!
		 s.executeUpdate("DELETE FROM mindblastLobby WHERE id = "+idosebe2);
		 s.executeUpdate("DELETE FROM mindblastLobby WHERE id = "+requestID2);
		 
		 s.close ();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Pošlje poizvedbo na server
	 * Iz serverja prebere 2 polji ter jih shrani v tabelo igralec
	 * Nato tabelo igralec shrani v seznam seznamIgralec
	 * Ko prebere vse iz SQL serverja pošlje podatke z send()
	 */
	public void querry(){
      try {    	  
    	  s = conn.createStatement ();
    	  long trenutniCas = System.currentTimeMillis()-20000;
    	  
    	  s.executeUpdate("DELETE FROM mindblastLobby WHERE time < "+trenutniCas); //vrže vn starejše od 20-29s
    	  
    	  
    	  s.executeQuery ("SELECT id,name,time FROM mindblastLobby ORDER BY score DESC");
	      
	      ResultSet rs = s.getResultSet ();
	      int count = 0;
	      
	      seznamIgralcev.clear();
	
	      while (rs.next ())
	      {
	          String idVal = rs.getString(1);
	          String nameVal = rs.getString (2);
	          //String catVal = rs.getString (3);
	          
	          String[] igralec = new String[2];
	          igralec[0]=idVal;
	          igralec[1]=nameVal;
	          
	          seznamIgralcev.add(igralec);
	          
//	          System.out.println (
//	                  "id = " + idVal
//	                  + ", name = " + nameVal
//	                  + ", cas = " + catVal);
	          ++count;
	      }
	      rs.close ();
	      s.close ();
	
	      sendData();
	      
//	      System.out.println (count + " rows were retrieved");
	      } catch (SQLException e) {
	  		
	    	conn = null;
	  		e.printStackTrace();
	  	}
	}
	
	/**
	 * Metoda za povezavo z SQL serverjem
	 */
	public void connect(){
		
		
		

        try
        {
            String userName = "zapisovalec";
            String password = "mocnoGeslo1234";
            String url = "jdbc:mysql://zapisovalec.db.6801421.hostedresource.com/zapisovalec";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
            
            /////
//            s = conn.createStatement ();
//            s.executeQuery ("SELECT score,name FROM fifteen ORDER BY score DESC");
//            
//            ResultSet rs = s.getResultSet ();
//            int count = 0;
//            while (rs.next ())
//            {
//                int idVal = rs.getInt (1);
//                String nameVal = rs.getString (2);
////                String catVal = rs.getString ("score");
//                System.out.println (
//                        "id = " + idVal
//                        + ", name = " + nameVal);
////                        + ", score = " + catVal);
//                ++count;
//            }
//            rs.close ();
//            s.close ();
//            System.out.println (count + " rows were retrieved");

        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }
//        finally
//        {
//            if (conn != null)
//            {
//                try
//                {
//                    conn.close ();
//                    System.out.println ("Database connection terminated");
//                }
//                catch (Exception e) { /* ignore close errors */ }
//            }
//        }
		    
	}
}
