package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ListadoArchivos {
	static Logger log = Logger.getLogger(ListadoArchivos.class);
	
	public List<String[]> getListaArchvios(String servidor, Properties p, String condicion) {
		List<String[]> rutaCarpetas = new ArrayList<String[]>();
		String rutaCompletaResp = "";
		String rutaCompletaDest = "";
		try {
			String[] carpetas = p.get(servidor+".carpetas").toString().split(","); 
			JSch jsch = new JSch();
			Session session = jsch.getSession(p.getProperty(servidor+".user"), p.getProperty(servidor+".host"), Integer.parseInt(p.getProperty(servidor+".puerto")));
            session.setPassword(p.getProperty(servidor+".password"));
            
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelExec channel;
            
			for(String c:carpetas) {
				rutaCompletaResp = p.getProperty(servidor+".rutaRespaldo") + "/" + c;
				rutaCompletaDest = p.getProperty(servidor+".rutaDestino") + "/" + c;
				String command = "find " + rutaCompletaResp + " -name " + condicion;
				System.out.println("comand:  "+ command);
				channel = (ChannelExec) session.openChannel("exec");
	            channel.setCommand(command);
	            InputStream in = channel.getInputStream();
	            
	            channel.connect();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	//System.out.println("line:  "+ line);
	            	String[] confcarpetas = new String[2];
	                confcarpetas[0] = line;
	                confcarpetas[1] = rutaCompletaDest + line.substring(rutaCompletaResp.length());
	                rutaCarpetas.add(confcarpetas);
	            }
	            channel.disconnect();
	            
			}
			session.disconnect();
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return rutaCarpetas;
	}
	
	public List<Map<String, String>> getListaArchviosSubCarpeta(String servidor, Properties p, Date fecha) {
		List<Map<String, String>> listaArchivos = new ArrayList<Map<String, String>>();
		String stringFecha = (new SimpleDateFormat(p.getProperty(servidor+".formato")).format(fecha));
		String condicion = "\"*"+stringFecha+"\"*" + "| xargs du -k";
		String rutaCompletaResp = "";
		String rutaCompletaDest = "";
		try {
			String[] carpetas = p.get(servidor+".carpetas").toString().split(","); 
			JSch jsch = new JSch();
			Session session = jsch.getSession(p.getProperty(servidor+".user"), p.getProperty(servidor+".host"), Integer.parseInt(p.getProperty(servidor+".puerto")));
            session.setPassword(p.getProperty(servidor+".password"));
            
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelExec channel;
            
			for(String c:carpetas) {
				String[] subCarpeta = c.split("\\|");
				rutaCompletaResp = p.getProperty(servidor+".rutaRespaldo") + "/" + subCarpeta[0];
				rutaCompletaDest = p.getProperty(servidor+".rutaDestino") + "/" + subCarpeta[1];
				String command = "find " + rutaCompletaResp + " -name " + condicion;
				System.out.println("comand:  "+ command);
				channel = (ChannelExec) session.openChannel("exec");
	            channel.setCommand(command);
	            InputStream in = channel.getInputStream();
	            
	            channel.connect();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	//System.out.println("line:  "+ line);
	            	Map<String, String> archivo = new HashMap<String, String>();
	                String[] subline = line.split("\\t");
	                archivo.put("rutaResp", subline[1]);
	                archivo.put("rutaDest", rutaCompletaDest + subline[1].substring(rutaCompletaResp.length()));
	                archivo.put("pathDest", rutaCompletaDest);
	                //archivo.put("pathOrigen", rutaCompletaResp);
	                //archivo.put("nombre", subline[1].substring(rutaCompletaResp.length()));
	                archivo.put("tamaño", subline[0]);
	                listaArchivos.add(archivo);
	            }
	            channel.disconnect();
	            
			}
			session.disconnect();
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return listaArchivos;
	}
}
