package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class RespaldoArchivos {
	static Logger log = Logger.getLogger(RespaldoArchivos.class);
	public void respaldaArchivos(List<String[]> listaArchvios, Properties p) {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(p.getProperty("user"), p.getProperty("host"), Integer.parseInt(p.getProperty("puerto")));
	        session.setPassword(p.getProperty("password"));
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();
	        
	        Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            int c=0;
            for(String[] f:listaArchvios) {
            	sftpChannel.get(f[0], f[1]);
            	c++;
            }
            sftpChannel.exit();
            session.disconnect();

            log.info("Archivo respaldados exitosamente. Cantidad: " + c);
            log.info("Ruta de respaldos " + p.getProperty("rutaDestino"));
            
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
