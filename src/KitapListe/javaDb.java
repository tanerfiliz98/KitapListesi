package KitapListe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class javaDb {
	
	 String connectionUrl =
             "jdbc:sqlite:sqlLite\\kitaplar.db";
	 Connection conn ;
	 int colNumber;
	 Statement statement=null;
	 public void connectAc() 
	 {
		 try {
				
				conn = DriverManager.getConnection(connectionUrl);
				statement=conn.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
	 }
	 private void connectionKapa() {
		 try
         {
         	conn.close();
             statement.close();
         }
         catch(Exception e)
         {
             e.printStackTrace();
             System.exit(1);
         }
	 }
	 public ArrayList<kitap> selectKitap() {
		 
		 ArrayList<kitap> kitaplar =new ArrayList<kitap>();
		try {
			connectAc();
			ResultSet result;
			result = statement.executeQuery("select * from kitap");
	        while(result.next()) {
	        	kitap ktp=new kitap();
            	ktp.kitapId= (int)result.getObject(1);
            	ktp.kitapAdi=(String)result.getObject(2);
            	ktp.isbnNo=String.valueOf(result.getObject(3));
            	ktp.yazarAdi=(String)result.getObject(4);
            	ktp.yayineviAdi=(String)result.getObject(5);
            	ktp.sayfaSayisi=String.valueOf(result.getObject(6));

                kitaplar.add(ktp);
	        	
                }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			connectionKapa();
		}
		return kitaplar;
         
	 }
	
	 public void insertKitap(kitap ktp) {
		 try {
			 connectAc();
			 String insertSql="Insert into kitap(kitapAdi,isbnNo,yazarAdi,yayineviAdi,sayfaSayisi) values('"+ktp.kitapAdi.replace("'", "''")+"',"+ktp.isbnNo+",'"+ktp.yazarAdi.replace("'", "''")+"','"+ktp.yayineviAdi.replace("'", "''")+"',"+ktp.sayfaSayisi+")";
			 statement.executeUpdate(insertSql);
			 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 finally {
			 connectionKapa();
		}
	 }
	 }
