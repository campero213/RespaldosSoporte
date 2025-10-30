package com.telcel.crm.dssc.respaldos.respaldosSoporte;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.CorreoApp;
import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.ListadoArchivos;
import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.RespaldoArchivos;
import com.telcel.crm.dssc.respaldos.respaldosSoporte.utils.Utilerias;

public class RespaldosGeneral {
	static Logger log = Logger.getLogger(RespaldosGeneral.class);

	public static void main(String[] args) {
		try {
			//Pruebas local
			String[] listaServidores = args[0].split(",");
			System.setProperty("root", System.getProperty("user.dir"));
			String urlPropiedades = System.getProperty("user.dir") + "/src/main/resources/conf/respaldos.properties";
			String logConfigFile = System.getProperty("user.dir") + "/src/main/resources/conf/RespladosLOG4J.properties";
			
			//String urlPropiedades = System.getProperty("user.dir") + "/conf/respaldos.properties";
			//String logConfigFile = System.getProperty("user.dir") + "/conf/RespladosLOG4J.properties";
			
			PropertyConfigurator.configure(logConfigFile);
			Properties p = new Properties();
			p.load(new FileReader(urlPropiedades));
			
			ListadoArchivos listadoArchivos = new ListadoArchivos();
			RespaldoArchivos respaldoArchivos = new RespaldoArchivos();
			Utilerias utilerias = new Utilerias();
			
			String stringFecha = (new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			
			CorreoApp correoApp = new CorreoApp();
			
			StringBuilder sbResumen = new StringBuilder();
			Map<String, String> resumen = new HashMap<>();
			
			for(String servidor:listaServidores){
				String[] configServidor = servidor.split("\\|");
				log.info("Se inicia Respaldos de " + configServidor[0] + " para fecha " + stringFecha);
				
				List<Map<String, String>> listArch = listadoArchivos.getListaArchviosSubCarpeta(configServidor[0], p, utilerias.getFecha(-Integer.parseInt(configServidor[1])));
				if(listArch.isEmpty()) {
					log.info("No se encontraron archivos");
					resumen = respaldoArchivos.respaldaArchivos(configServidor[0], p);
					sbResumen.append(utilerias.getResumen(resumen));
				}else {
					log.info("Archvios Encontrados: "+ listArch.size());
					resumen = respaldoArchivos.respaldaArchivos(configServidor[0], listArch, p);
					sbResumen.append(utilerias.getResumen(resumen));
					log.info("Se finaliza el respaldo de para: " + configServidor[0]);
					log.info("_________________________________________________________\n");
				}
			}
			log.info("Se finaliza el Porceso de respaldos");
			log.info("#######################################################################################\n");
			
			String cuerpo = utilerias.leerArchivo(p.getProperty("correo.html"));
			cuerpo = cuerpo + sbResumen.toString() +utilerias.getFinHtml();
			
			correoApp.envia(p.getProperty("correo.asunto"), cuerpo, p.getProperty("correo.destinatarios"), p.getProperty("correo.urlPropiedades"));
			
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
