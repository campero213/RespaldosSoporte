package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ListadoArchivos {
	static Logger log = Logger.getLogger(ListadoArchivos.class);
	
	public List<String[]> getListaArchvios(Properties p, String fecha) {
		List<String[]> rutaCarpetas = new ArrayList<String[]>();
		String rutaCompletaResp = "";
		String rutaCompletaDest = "";
		try {
			String[] carpetas = p.get("carpetas").toString().split(","); 
			JSch jsch = new JSch();
			Session session = jsch.getSession(p.getProperty("user"), p.getProperty("host"), Integer.parseInt(p.getProperty("puerto")));
            session.setPassword(p.getProperty("password"));
            
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelExec channel;
            
			for(String c:carpetas) {
				rutaCompletaResp = p.getProperty("rutaRespaldo") + "/" + c;
				rutaCompletaDest = p.getProperty("rutaDestino") + "/" + c;
				String command = "find " + rutaCompletaResp + " -name " + "\"*"+fecha+".txt\"";
				//System.out.println("comand:  "+ command);
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
}
