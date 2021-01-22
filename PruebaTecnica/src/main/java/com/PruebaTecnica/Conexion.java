package com.PruebaTecnica;

import java.sql.*;

public class Conexion {
	
	 	private final String host = "localhost";
	    private final String usuario = "root";
	    private final String clave = "";
	    private final String nombre = "sga";
	    private final String url;

	 
	    private Connection link;	 
	    private String mensajeError;

	 
	    public Conexion() {


	        this.mensajeError = "";

	        this.url = "jdbc:mysql://" + this.host + "/" + this.nombre;

	    }


	    public boolean conectar() {

	        try {

	            this.link = DriverManager.getConnection(this.url, this.usuario, this.clave);

	        } catch ( SQLException e) {
	            this.mensajeError = e.getMessage();
	            return false;
	        }

	        return true;
	    }


	    public ResultSet obtener(String consulta) {

	    	ResultSet rs;

	        try {
	        	
	        	PreparedStatement stmt =  this.link.prepareCall(consulta);	        	
	        	rs = stmt.executeQuery(consulta);
	        	
	        } catch (SQLException e) {
	        	System.out.println(e.getMessage());
	            this.mensajeError = e.getMessage();
	            return null;
	        }
	        
	        return rs;
	    }


	    public boolean desconectar() {

	        try {

	            this.link.close();

	        } catch (SQLException e) {
	            this.mensajeError = e.getMessage();
	            return false;
	        }

	        return true;
	    }

	    public String getMensajeError() {
	        return mensajeError;
	    }
	
	
}
