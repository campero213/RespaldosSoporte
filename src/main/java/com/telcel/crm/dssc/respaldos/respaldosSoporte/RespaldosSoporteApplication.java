package com.telcel.crm.dssc.respaldos.respaldosSoporte;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.ListadoArchivos;
import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.RespaldoArchivos;

public class RespaldosSoporteApplication {
	static Logger log = Logger.getLogger(RespaldosSoporteApplication.class);

	public static void main(String[] args) {
		try {
			//String urlPropiedades = System.getProperty("user.dir") + "/conf/respaldos.properties";
			//String logConfigFile = System.getProperty("user.dir") + "/conf/RespladosLOG4J.properties";
			//Pruebas local
			System.setProperty("root", System.getProperty("user.dir"));
			String urlPropiedades = System.getProperty("user.dir") + "/src/main/resources/conf/respaldos.properties";
			String logConfigFile = System.getProperty("user.dir") + "/src/main/resources/conf/RespladosLOG4J.properties";
			
			PropertyConfigurator.configure(logConfigFile);
			Properties p = new Properties();
			p.load(new FileReader(urlPropiedades));
			
			ListadoArchivos listadoArchivos = new ListadoArchivos();
			RespaldoArchivos respaldoArchivos = new RespaldoArchivos();
			String fecha = (new SimpleDateFormat("ddMMyy").format(new Date()));
			String fecha2 = (new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			log.info("Se inicia Respaldos para fecha " + fecha2);
			String condicion = "\"*"+fecha+".txt\"";
			List<String[]> listArch = listadoArchivos.getListaArchvios("pagosm2k", p, condicion);
			if(listArch.isEmpty()) {
				log.info("No se encontraron archivos");
			}else {
				log.info("Archvios Encontrados: "+ listArch.size());
				//respaldoArchivos.respaldaArchivos("pagosm2k", listArch, p);
			}
			
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
