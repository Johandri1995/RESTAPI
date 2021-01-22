package com.PruebaTecnica.login;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.sql.SQLException;

import java.sql.ResultSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PruebaTecnica.Conexion;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class Login  {
	

	@RequestMapping("/")
	public String index()  {
		
		return "Api Rest con autentiacion mediant token servicios  POST : /login /registro /consultar /actualizar";
		
	}
	
	@PostMapping("login")
	public Map<String, Object> login(@RequestParam("username") String username, @RequestParam("password") String pwd) throws SQLException  {
		
		Map<String, Object> json = new HashMap<String, Object>();
		
		
		try {
			
			Conexion con = new Conexion();
	        con.conectar();
	        ResultSet rs = con.obtener("{call SpLogin ('Login','"+username+"','"+pwd+"','','','','','','','','')}");
	            
	   
			  if(rs.next()){	
				  json.put("ID",rs.getString("id"));
				  json.put("Token",Token(username,pwd,rs.getString("id")));	
			  }else {
				  json.put("Error","Usuario no Valido");
			  }
			  
			
			
		} catch (SQLException e) {
			
			json.put("Erro",e.getMessage());
		
		} 
			
		
		return json;	
		
	}
	
	
	@PostMapping("registro")
	public Map<String, Object> registro(@RequestParam("username") String username, 
							  @RequestParam("password") String pwd, 
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
	        ResultSet rs = con.obtener("{call SpLogin ('Registrar','"+username+"','"+pwd+"','"+nombre+"','"+apellido+"','"+identificacion+"','"+titular+"','"+numero+"','"+fecha_vc+"','"+cvv+"','')}");
	           
			
		    if(rs.next()){
			  
			   if(rs.getInt("id") == 0) {
				  json.put("Error",rs.getString("respuesta"));
			   }else {
				  json.put("Mensaje","Usuario Registrado");
				  json.put("ID", rs.getString("id") );
			  	  json.put("Token",Token(username,pwd,rs.getString("id")));
			   }	  
		  		
		    } 
			  
		
		} catch (SQLException e) {
			json.put("Error",e.getErrorCode() + " " + e.getMessage());
		}
		
		return json;	
		
	}
	
	
	
	
	public String Token(String username, String pwd,String id) {
		
		String token = getJWTToken(username,pwd,id);				
		return token;
		
	}

	private String getJWTToken(String username,String pwd,String id) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("API")
				.setSubject(username)
				.setSubject(pwd)
				.setSubject(id)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 99999000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
	
	
	
}
