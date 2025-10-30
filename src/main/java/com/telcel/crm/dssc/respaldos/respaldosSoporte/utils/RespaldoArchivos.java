package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class RespaldoArchivos {
	static Logger log = Logger.getLogger(RespaldoArchivos.class);
	public Map<String, String>  respaldaArchivos(String servidor, List<Map<String, String>> listaArchvios, Properties p) {
		Map<String, String> resumen = new HashMap<String, String>();
		int c=0, cError=0, cExito=0;
        boolean exito=false;
        String listError = "";
        String listOK = "";
		try {
			SSHJDowloader sshjDowloader = new SSHJDowloader();
			
			sshjDowloader.conectar(p.getProperty(servidor+".host"), p.getProperty(servidor+".puerto"), p.getProperty(servidor+".user"), p.getProperty(servidor+".password"));
			
			resumen.put("servidor", servidor);
            for(Map<String, String> f:listaArchvios) {
            	c++;
            	exito=false;
            	File carpetaLocal = new File(f.get("pathDest"));
            	if(!carpetaLocal.exists())
            		carpetaLocal.mkdir();
            	try {
                    log.info("Se inicia con el respaldo del archivo: ("+c+"/"+listaArchvios.size() +") " + f.get("rutaResp") + " - Tamaño: " + f.get("tamaño") + " KB");
                    sshjDowloader.getSftpClient().get(f.get("rutaResp"), f.get("rutaDest"));
                    exito=true;
                    if(exito) {
                    	cExito++;
	                    File fRespaldo = new File(f.get("rutaDest"));
	                	log.info("Se respalda con exito el archivo: " + f.get("rutaDest") + " - Tamaño: " + (fRespaldo.length() / 1024) + " KB");
	                	listOK = listOK + f.get("rutaDest") + "<br>";
                    }
            	
            	}catch (Exception e) {
            		cError++;
            		log.error("Error durante la transferencia del archivo : "+ f.get("rutaResp") +"\n" + e.getMessage());
            		listError = listError + f.get("rutaResp") + "<br>";
                    e.printStackTrace();
				}
            }
            sshjDowloader.getSftpClient().close();
            sshjDowloader.getSsh().disconnect();
            resumen.put("total", c+"");
            resumen.put("exitosos", cExito+"");
            resumen.put("error", cError+"");
            resumen.put("rOrigen", p.getProperty(servidor+".rutaRespaldo") + "/");
            resumen.put("rDestino", p.getProperty(servidor+".rutaDestino") + "/");
            resumen.put("listOk", listOK);
            resumen.put("listError", listError);
            log.info("Archivo respaldados exitosamente. Cantidad: " + c);
            log.info("Ruta de respaldos " + p.getProperty(servidor+".rutaDestino"));
            
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return resumen;
	}
	
	public Map<String, String> respaldaArchivos(String servidor, Properties p) {
		Map<String, String> resumen = new HashMap<String, String>();
		resumen.put("servidor", servidor);
		resumen.put("total", "0");
        resumen.put("exitosos", "0");
        resumen.put("error", "0");
        resumen.put("rOrigen", p.getProperty(servidor+".rutaRespaldo") + "/");
        resumen.put("rDestino", p.getProperty(servidor+".rutaDestino") + "/");
        
        return resumen;
	}
}
