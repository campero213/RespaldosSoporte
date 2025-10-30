package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utilerias {

	public Date getFecha(int dias) {
		Date date = new Date(); // o pasada como parámetro
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dias);
		return calendar.getTime();
	}

	public List<String> getDestinatarios(String d) {
		String[] dest = d.split(",");
		List<String> listDest = new ArrayList<String>();
		for (int x = 0; x < dest.length; x++) {
			listDest.add(dest[x]);
		}
		return listDest;
	}

	public String leerArchivo(String ruta) {
		String archivo = "";
		try {
			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), "UTF-8"));
			String cadena;
			while ((cadena = b.readLine()) != null)
				archivo = String.valueOf(archivo) + cadena;
			b.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return archivo;
	}

	public String getFechaString(String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		Calendar c = Calendar.getInstance();
		Date ahoraNuevo = c.getTime();
		String fechaComoCadena = sdf.format(ahoraNuevo);
		return fechaComoCadena;
	}

	public String getResumen(Map<String, String> resumenMap) {
		String resumen = "<tbody>" + "<tr><td colspan=\"4\" class=\"subtitulo\"></td></tr>" + "<tr>" + "<td>"
				+ resumenMap.get("servidor") + "</td>" + "<td>" + resumenMap.get("total") + "</td>";
		if (resumenMap.get("error").equals("0"))
			resumen = resumen + "<td class=\"ok\">" + resumenMap.get("exitosos") + "</td>";
		else
			resumen = resumen + "<td class=\"error\">" + resumenMap.get("exitosos") + "</td>";
		resumen = resumen + "<td>" + resumenMap.get("error") + "</td>" + "</tr>" + "<tr>"
				+ "<td class=\"subtitulo\">Ruta Origen:</td>" + "<td colspan=\"3\">" + resumenMap.get("rOrigen")
				+ "</td>" + "</tr>" + "<tr>" + "<td class=\"subtitulo\">Ruta Respaldos:</td>" + "<td colspan=\"3\">"
				+ resumenMap.get("rDestino") + "</td>" + "</tr>";
		/*if (Integer.parseInt(resumenMap.get("exitosos")) > 0)
			resumen = resumen + "<tr>" + "<td colspan=\"4\" class=\"subtitulo\">Arhivos respaldados:</td>" + "</tr>"
					+ "<tr>" + "<td colspan=\"4\">" + resumenMap.get("listOk") + "</td>" + "</tr>";*/
		if (Integer.parseInt(resumenMap.get("error")) > 0)
			resumen = resumen + "<tr>" + "<td colspan=\"4\" class=\"subtitulo\">Errores:</td>" + "</tr>" + "<tr>"
					+ "<td colspan=\"4\">" + resumenMap.get("listError") + "</td>" + "</tr>";
		return resumen;
	}

	public String getFinHtml() {
		String pieHtml = "</tbody>" + "</table>" + "<footer>" + "<br>" + "<p class=\"firma\">"
				+ "Comercio electronico movil eCAC sicatel <br>" + "✆ (55)25813700 Ext: 3977<br>"
				+ "☏ Directo: 25813977<br>" + "✉ soporte.ssc@mail.telcel.com</p>" + "</footer>" + "</body>";
		return pieHtml;
	}
}
