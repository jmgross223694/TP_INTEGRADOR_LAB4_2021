package Dominio;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;

import Conexion.Conexion;
import Entidades.Alumno;
import Entidades.Nacionalidad;
import Entidades.Provincia;

public class AlumnosDao {

	public ArrayList<Alumno> obtenerAlumnos(){
		
		ArrayList<Alumno> Lista = new ArrayList<Alumno>();
		
		PreparedStatement st;
		ResultSet rs;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		
		String ListaAlumnos = "SELECT * FROM `vw-alumnos` where Estado = true;";
		
		try 
		{
			st = conexion.prepareStatement(ListaAlumnos);
			rs = st.executeQuery();
			while(rs.next())
			{
				
				Nacionalidad nac = new Nacionalidad();
				Provincia prov = new Provincia();
				Alumno alumno = new Alumno();
				
				alumno.setID(rs.getInt("ID"));
				alumno.setLegajo(rs.getString("Legajo"));
				alumno.setDNI(rs.getString("Dni"));
				alumno.setNombre(rs.getString("Nombre"));
				alumno.setApellido(rs.getString("Apellido"));
				SimpleDateFormat format = new SimpleDateFormat("dd-LL-yyyy");
				alumno.setFechaNac(format.format(rs.getDate("FechaNac")));
				alumno.setDireccion(rs.getString("Direccion"));
				prov.setID(rs.getInt("IdProvincia"));
				prov.setDescripcion(rs.getString("Provincia"));
				alumno.setProvincia(prov);
				nac.setID(rs.getInt("IdNacionalidad"));
				nac.setDescripcion(rs.getString("Nacionalidad"));
				alumno.setNacionalidad(nac);
				alumno.setEmail(rs.getString("Email"));
				alumno.setTelefono(rs.getString("Telefono"));
				
				Lista.add(alumno);
				
			}
			
			//conexion.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return Lista;
	}
	
	public ArrayList<Alumno> filtrarAlumnos(String text) {
		
		ArrayList<Alumno> Lista = new ArrayList<Alumno>();

		PreparedStatement st;
		ResultSet rs;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		
		String ListaFiltrada = "SELECT * FROM `vw-alumnos` where Estado = true and "
				+ "concat_ws(Legajo, Dni, Nombre, Apellido, FechaNac," 
				+ "Direccion, Provincia, Nacionalidad, Email, Telefono) like '%"+text+"%'";
		
		try {
			st = conexion.prepareStatement(ListaFiltrada);
			rs = st.executeQuery();
			if (rs.next()) {
				Nacionalidad nac = new Nacionalidad();
				Provincia prov = new Provincia();
				Alumno alumno = new Alumno();
				alumno.setLegajo(rs.getString("Legajo"));
				alumno.setDNI(rs.getString("Dni"));
				alumno.setNombre(rs.getString("Nombre"));
				alumno.setApellido(rs.getString("Apellido"));
				SimpleDateFormat format = new SimpleDateFormat("dd-LL-yyyy");
				alumno.setFechaNac(format.format(rs.getDate("FechaNac")));
				alumno.setDireccion(rs.getString("Direccion"));
				prov.setDescripcion(rs.getString("Provincia"));
				alumno.setProvincia(prov);
				nac.setDescripcion(rs.getString("Nacionalidad"));
				alumno.setNacionalidad(nac);
				alumno.setEmail(rs.getString("Email"));
				alumno.setTelefono(rs.getString("Telefono"));

				Lista.add(alumno);

			}

			//conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Lista;
	}
	
	public int AgregarAlumno(Alumno a)
	{
		int estado = 0;
		
		CallableStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		try
		{
			statement = conexion.prepareCall("insert into tpintegrador.alumnos (Legajo, Dni, Nombre, "
					+ "Apellido, FechaNac, Direccion, Provincia, Nacionalidad, Email, Telefono)"
					+ "values('"
					+ a.getLegajo() + "', '"
					+ a.getDNI() + "', '"
					+ a.getNombre() + "', '"
					+ a.getApellido() + "', '"
					+ a.getFechaNac() + "', '"
					+ a.getDireccion() + "', '"
					+ a.getProvincia().getID() + "', '"
					+ a.getNacionalidad().getID() + "', '"
					+ a.getEmail() + "', '"
					+ a.getTelefono()
					+ "')");
			
			
			statement.execute();
			
			estado = 1;
			
		}
		catch (SQLException e) 
		{											
			estado = -1;
		}
		return estado;
	}
	
	public Alumno Buscar(String text) {
		
		PreparedStatement st;
		ResultSet rs;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		Nacionalidad nac = new Nacionalidad();
		Provincia prov = new Provincia();
		Alumno alumno = new Alumno();
		
		String Consulta = "select ID, Legajo, Dni, Nombre, Apellido, Day(FechaNac) Dia, "
				+ "Month(FechaNac) Mes, Year(FechaNac) Anio, Direccion, Provincia as IdProvincia, "
				+ "(select Provincias.Descripcion from Provincias where Provincias.ID = alumnos.Provincia) AS Provincia, "
				+ "Nacionalidad as IdNacionalidad, "
				+ "(select Nacionalidades.Descripcion from Nacionalidades where Nacionalidades.ID = alumnos.Nacionalidad) "
				+ "AS Nacionalidad, Email, Telefono, Estado "
				+ "from alumnos where Estado = true and Legajo = '"+text+"' "
				+ "or "
				+ "Estado = true and Dni = '"+text+"'";
		
		try {
			st = conexion.prepareStatement(Consulta);
			rs = st.executeQuery();
			while (rs.next()) {
				alumno.setID(rs.getInt("ID"));
				alumno.setLegajo(rs.getString("Legajo"));
				alumno.setDNI(rs.getString("Dni"));
				alumno.setNombre(rs.getString("Nombre"));
				alumno.setApellido(rs.getString("Apellido"));
				alumno.setDiaNac(rs.getInt("Dia"));
				alumno.setMesNac(rs.getInt("Mes"));
				alumno.setAnioNac(rs.getInt("Anio"));
				alumno.setDireccion(rs.getString("Direccion"));
				prov.setID(rs.getInt("IdProvincia"));
				prov.setDescripcion(rs.getString("Provincia"));
				alumno.setProvincia(prov);
				nac.setID(rs.getInt("IdNacionalidad"));
				nac.setDescripcion(rs.getString("Nacionalidad"));
				alumno.setNacionalidad(nac);
				alumno.setEmail(rs.getString("Email"));
				alumno.setTelefono(rs.getString("Telefono"));
				
			}
			//conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return alumno;
	}
	
	public int Contar(String text) {
		
		PreparedStatement st;
		ResultSet rs;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		int coincidencia = 0;

		String Consulta = "select count(*) as Cantidad "
				+ "from alumnos where Estado = true and Legajo = '" + text + "' "
				+ "or "
				+ "Estado = true and Dni = '" + text + "'";
		
		try {
			st = conexion.prepareStatement(Consulta);
			rs = st.executeQuery();
			while (rs.next()) {
				coincidencia = rs.getInt("Cantidad");
			}
			//conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coincidencia;
	}

	public int ContarModificar(String DniLegajo, int Id) {
		
		PreparedStatement st;
		ResultSet rs;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		int coincidencia = 0;

		String Consulta = "select count(*) as Cantidad "
				+ "from alumnos where Estado = true "
				+ "and "
				+ "Dni = '" + DniLegajo + "' "
				+ "and "
				+ "ID <> " + Id + " "
				+ "or "
				+ "Estado = true "
				+ "and "
				+ "Legajo = '" + DniLegajo + "' "
				+ "and "
				+ "ID <> " + Id + "";
		
		try {
			st = conexion.prepareStatement(Consulta);
			rs = st.executeQuery();
			while (rs.next()) {
				coincidencia = rs.getInt("Cantidad");
			}
			//conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coincidencia;
	}
	
	public int Modificar(Alumno a)
	{
		int estado = 0;
		
		CallableStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		try
		{
			statement = conexion.prepareCall("update alumnos set "
					+ "Legajo = '"+a.getLegajo()+"', "
					+ "Dni= '"+a.getDNI()+"', "
					+ "Nombre= '"+a.getNombre()+"', "
					+ "Apellido= '"+a.getApellido()+"', "
					+ "FechaNac= '"+a.getFechaNac()+"', "
					+ "Direccion= '"+a.getDireccion()+"', "
					+ "Provincia= '"+a.getProvincia().getID()+"', "
					+ "Nacionalidad= '"+a.getNacionalidad().getID()+"', "
					+ "Email= '"+a.getEmail()+"', "
					+ "Telefono= '"+a.getTelefono()+"' "
					+ "where ID = " + a.getID());
			
			statement.execute();
			
			estado = 1;
			
		}
		catch (SQLException e) 
		{											
			estado = -3;
		}
		return estado;
	}
	
	public int Baja(Alumno a)
	{
		int estado = 0;
		
		CallableStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		try
		{
			statement = conexion.prepareCall("update alumnos set "
					+ "Estado = 0 "
					+ "where ID = " + a.getID());
			
			statement.execute();
			
			estado = 1;
			
		}
		catch (SQLException e) 
		{											
			estado = 0;
		}
		return estado;
	}
	
}
