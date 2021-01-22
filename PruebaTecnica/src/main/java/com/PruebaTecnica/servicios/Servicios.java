package com.PruebaTecnica.servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PruebaTecnica.Conexion;
import com.PruebaTecnica.modelos.Datos;


@RestController
public class Servicios {
	
	@PostMapping("consultar")	
	public  Datos consultar( @RequestParam(value="id") String id) {
				
		Datos da = new Datos();		 
		
		try {
	
			
			Conexion con = new Conexion();
	        con.conectar();
	        ResultSet rs = con.obtener("{call SpLogin ('Consultar','','','','','','','','','','"+id+"')}");
	           
			
			if(rs.next()){
				  
				  
			  	da.setId(rs.getString("id"));
				da.setUsername(rs.getString("username"));
				da.setPwd(rs.getString("pwd"));
				da.setNombre(rs.getString("nombre"));
				da.setApellido(rs.getString("apellido"));
				da.setIdentificacion(rs.getString("identificacion"));
									
				da.setTitular(rs.getString("titular"));
				da.setNumero(rs.getString("numero"));
				da.setFecha_vc(rs.getString("fecha_vc"));
				da.setCvv(rs.getString("cvv"));
		  		
		  }else {
			  da.setMensaje("Usuario no encontrado");
		  }
			
			
		} catch (SQLException e) {
			da.setMensaje(e.getErrorCode() + " " + e.getMessage());
		}	
		
		
		return da;
	}
	
	
	
	
	@PostMapping("actualizar")
	public Map<String, Object> actualizar(@RequestParam("id") String id, 
							  @RequestParam("pwd") String pwd, 
							  @RequestParam("nombre") String nombre, 
							  @RequestParam("apellido") String apellido,
							  @RequestParam("identificacion") String identificacion	,
							  @RequestParam("titular") String titular,
							  @RequestParam("numero") String numero,
							  @RequestParam("fecha_vc") String fecha_vc,
							  @RequestParam("cvv") String cvv)  {
		
		
		Map<String, Object> json = new HashMap<String, Object>();
		try {			
			
			Conexion con = new Conexion();
	        con.conectar();
	        ResultSet rs = con.obtener("{call SpLogin ('Actualizar','','"+pwd+"','"+nombre+"','"+apellido+"','"+identificacion+"' ,'"+titular+"','"+numero+"','"+fecha_vc+"','"+cvv+"','"+id+"')}");
	           
			  if(rs.next()){
				  
				  json.put("Mensaje",rs.getString("respuesta")); 
			  		
			  } 
		
		} catch (SQLException e) {
			json.put("Error",e.getErrorCode() + " " + e.getMessage());
		}		
		
		return json;	
		
	}
	
}
