package com.mariocaster.gie;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class CargadorGpx implements Variables_Globales{
	private File archivo;
	private Element raiz;
	private Document gpx;
	private Track track;
	
	/**
	 * Constructor
	 * @param archivo
	 * @throws DataConversionException
	 */
	public CargadorGpx(File archivo) throws DataConversionException{
		this.archivo = archivo;
		
		SAXBuilder builder = new SAXBuilder(false);
		try {
			gpx = builder.build(archivo);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		procesarGPX(gpx);
	}
	
	/**
	 * Constructor para pasarle directamente la informacion a procesar
	 * @param gpxInfo
	 */
	public CargadorGpx(String gpxInfo){
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(new StringReader(gpxInfo));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		procesarGPX(doc);
	}
	
	private int getCadencia(List trackPointExtensionList){
		/*
		 * Varios niveles. Empezamos desde el del parametro
		 * <extensions>	-> trackPointExtensionList
		 * 		<gpxtpx:TrackPointExtension> -> listaExtensiones
		 * 			<gpxtpx:hr>
		 * 			<gpxtpx:cad>
		 * 
		 * Osea, basicamente recogemos la lista del parametro
		 * para buscar otra lista dentro que contiene la lista
		 * de extensiones donde encontrar la cadencia
		 */
		Element posibleCad;
		List listaExtensiones = null;
		Element trackPointExtension;
		for(int i = 0; i<trackPointExtensionList.size(); i++){
			trackPointExtension = ((Element)trackPointExtensionList.get(i));
			if(trackPointExtension.getName().equals("TrackPointExtension")){
				listaExtensiones = trackPointExtension.getChildren();
			}
		}
		if (listaExtensiones!=null) {
			int cad = 0;
			for (int i = 0; i < listaExtensiones.size(); i++) {
				posibleCad = (Element) listaExtensiones.get(i);
				if (posibleCad.getName().equals("cad")) {
					cad = Integer.parseInt(posibleCad.getValue());
					return cad;
				}
			}
			return cad;
		} else {
			return 0;
		}
	}
	
	private String getDesc(List trkNodeList){
		Element nodoTrk;
		for (int j=0; j<trkNodeList.size(); j++){
			nodoTrk = (Element)trkNodeList.get(j);
			if(nodoTrk.getName().equals("desc")){
				return nodoTrk.getValue().toString();
			}
		}
		return null;
	}
	
	private double getElevacion(List trkpt){
		// Extrae el dato, comprueba su validez y,
		// en caso de error, devuelve un 0
		for(int j=0; j<trkpt.size(); j++){
			Element nodo = (Element)trkpt.get(j);
			if(nodo.getName().equals("ele")){
				String eleS = nodo.getValue();
				double ele = Double.parseDouble(eleS);
				return ele;
			}
		}
		return 0;
	}
	
	/**
	 * Devuelve la elevacion acumulada de un track
	 * @return
	 */
	public double getElevacionAcumulada(TrackSegment trackSegment){
		double eleActual = trackSegment.get(0).getEle();
		double eleTotal = 0;

		for(TrackPoint p: trackSegment){
			if(p.getEle()>eleActual){
				eleTotal += p.getEle()-eleActual;
				eleActual = p.getEle();
			} else {
				eleActual = p.getEle();
			}
		}
		return eleTotal;
	}
	
	private Element getExtension(Element trkpt) {
		List hijosTrkpt = trkpt.getChildren();
		Element posibleExtension = null;
		for(int j=0; j<hijosTrkpt.size(); j++){
			posibleExtension = (Element)hijosTrkpt.get(j);
			if(posibleExtension.getName().equals("extensions")){
				return posibleExtension;
			}
		}
		return posibleExtension;
	}
	
	private int getFrecuencia(List trackPointExtensionList){
		/*
		 * Varios niveles. Empezamos desde el del parametro
		 * <extensions>	-> trackPointExtensionList
		 * 		<gpxtpx:TrackPointExtension> -> listaExtensiones
		 * 			<gpxtpx:hr>
		 * 			<gpxtpx:cad>
		 * 
		 * Osea, basicamente recogemos la lista del parametro
		 * para buscar otra lista dentro que contiene la lista
		 * de extensiones donde encontrar la cadencia
		 */
		Element posibleHr;
		List listaExtensiones = null;
		Element trackPointExtension;
		for(int i = 0; i<trackPointExtensionList.size(); i++){
			trackPointExtension = ((Element)trackPointExtensionList.get(i));
			if(trackPointExtension.getName().equals("TrackPointExtension")){
				listaExtensiones = trackPointExtension.getChildren();
			}
		}
		if (listaExtensiones!=null) {
			int hr = 0;
			for (int i = 0; i < listaExtensiones.size(); i++) {
				posibleHr = (Element) listaExtensiones.get(i);
				if (posibleHr.getName().equals("hr")) {
					hr = Integer.parseInt(posibleHr.getValue());
					return hr;
				}
			}
			return hr;
		} else {
			return 0;
		}
	}
	
	private Date getHora(Element trkpt){
		// TODO Extrae el dato, comprueba su validez y,
		// en caso de error, devuelve un 0
		return new Date(0);
	}
	
	private double getLatitud(Element trkpt){
		// Extrae el dato, comprueba su validez y,
		// en caso de error, devuelve un 0
		String latS = trkpt.getAttributeValue("lat");
		double lat = Double.parseDouble(latS);
		return lat;
	}
	
	/**
	 * Parsea un String de tiempo de un fichero GPX
	 * @param s
	 * @return
	 */
	private long getLongDeTiempo(String s){
		try {
			//Extraer fecha
			StringTokenizer stk = new StringTokenizer(s, "T");
			String fecha = stk.nextToken();
			
			//Extraer hora
			String hora = stk.nextToken();
			hora = hora.substring(0, hora.length()-5);
			
			//Formatear fecha
			stk = new StringTokenizer(fecha, "-");
			int anyo = Integer.parseInt(stk.nextToken());
			int mes = Integer.parseInt(stk.nextToken());
			int dia = Integer.parseInt(stk.nextToken());
			
			// Formatear hora
			stk = new StringTokenizer(hora, ":");
			int horas = Integer.parseInt(stk.nextToken());
			int minutos = Integer.parseInt(stk.nextToken());
			int segundos = Integer.parseInt(stk.nextToken());
			
			//Crear fecha
			GregorianCalendar gc = new GregorianCalendar(anyo, mes, dia, horas, minutos, segundos);
			
			return gc.getTimeInMillis();
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}

	private double getLongitud(Element trkpt){
		// Extrae el dato, comprueba su validez y,
		// en caso de error, devuelve un 0
		String lonS = trkpt.getAttributeValue("lon");
		double lon = Double.parseDouble(lonS);
		
		return lon;
	}
	
	private Element getMetadata(List elementosDeRaiz){
		Iterator i = elementosDeRaiz.iterator();
		Element metadataElement = null;
		if (i.hasNext()) {
			while (i.hasNext()) {
				Element hijoRaiz = (Element) i.next();
				if (hijoRaiz.getName().equals(METADATA)) {
					metadataElement = hijoRaiz;
				}
			}
		}
		return metadataElement;
	}

	private String getName(List trkNodeList){
		Element nodoTrk;
		for (int j=0; j<trkNodeList.size(); j++){
			nodoTrk = (Element)trkNodeList.get(j);
			if(nodoTrk.getName().equals("name")){
				return nodoTrk.getValue().toString();
			}
		}
		return null;
	}
	
	public Date getTiempoParaDistancia(int distancia){
		/* Lo primero se necesita iterar por todos los puntos desde el primero hacia delante
		 * cuando se llega a una distancia concreta, se anota el numero inicial y el actual
		 * y se comprueba el tiempo empleado. Una vez comprobado se empieza otra vez
		 */
		
		return null;
	}
	
	/**
	 * Devuelve el tiempo total invertido en el track
	 * @return
	 */
	public Date getTiempoTotal(TrackSegment trackSegment){
		long tiempoActual = trackSegment.get(0).getTiempo();
		
		for(int i = 1; i<trackSegment.size(); i++){
			tiempoActual = trackSegment.get(i).getTiempo()-tiempoActual;
		}
		
		return (new Date(tiempoActual));
	}
	
	
	public Track getTrack(){
		return track;
	}
	
	/**
	 * Devuelve un TrackPoint parseado desde el objeto element
	 */
	private TrackPoint getTrackPointParsed(Element trkptElement){
		// TODO Devuelve un TrackPoint parseado desde el objeto element
		TrackPoint trkpt = new TrackPoint();
		trkpt.setIdTrack(0);	//TODO
		trkpt.setIdTrackSegment(0);	//TODO
		trkpt.setLat(0);	//TODO
		trkpt.setLon(0);	//TODO
		trkpt.setEle(0);	//TODO
		trkpt.setTiempo(0);	//TODO
		trkpt.setVel(0);		//TODO
		trkpt.setFrec(0);		//TODO
		trkpt.setCad(0);		//TODO
		trkpt.setPot(0);		//TODO
		
		return trkpt;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Element getTrk(List elementosDeRaiz){
		Iterator i = elementosDeRaiz.iterator();
		Element trkElement = null;
		if (i.hasNext()) {
			while (i.hasNext()) {
				Element hijoRaiz = (Element) i.next();
				if (hijoRaiz.getName().equals(TRACK)) {
					trkElement = hijoRaiz;
				}
			}
		}
		return trkElement;
	}

	/**
	 * Devuelve objetos TrackSegment partiendo de un Element trkseg
	 * @param trkseg
	 * @return
	 */
	private TrackSegment parseTrackSegment(Element trkseg){
		TrackSegment ts = new TrackSegment();
		List trackPointList = trkseg.getChildren();
		Iterator trkptIter = trackPointList.iterator();
		GpxProcessor gp = new GpxProcessor();
		if (trkptIter.hasNext()) {
			while (trkptIter.hasNext()) {
				Element trkpt = (Element) trkptIter.next();
				TrackPoint pt = new TrackPoint();

				//LATITUD Y LONGITUD
				pt.setLon(getLongitud(trkpt));
				pt.setLat(getLatitud(trkpt));

				//Resto de datos
				List resto = trkpt.getChildren();

				//ELEVACION
				pt.setEle(getElevacion(resto));

				//HORA
				Element hora = (Element) resto.get(1);
				pt.setTiempo(getLongDeTiempo(hora.getValue()));
				
				//EXTENSIONES (HR Y CADENCIA)
				Element extension = getExtension(trkpt);
				int hr = getFrecuencia(extension.getChildren());
				pt.setFrec(hr);

				int cad = getCadencia(extension.getChildren());
				pt.setCad(cad);
				ts.add(pt);

				//ESCRIBIR EN CADA PUNTO LA DISTANCIA QUE SE LLEVA RECORRIDA RESPECTO AL TOTAL
				ts = gp.getDistance(ts);
			}
		}
		
		//Escribir todos los valores en el objeto trackSegment
		ts.alturaMaxima = gp.getMaximumHeight(ts);
		ts.alturaMinima = gp.getMinimumHeight(ts);
		ts.ascensionTotal = gp.getTotalAscent(ts);
		ts.cadenciaMaxima = gp.getMaxCadence(ts);
		ts.cadMedia = gp.getAverageCadence(ts);
		ts.descensoTotal = gp.getTotalDescent(ts);
		ts.distancia = gp.getTotalDistance(ts);
		ts.frecMaxima = gp.getMaxHR(ts);
		ts.frecMedia = gp.getAverageHR(ts);
		ts.porcNegativoMedio = gp.getAverageNegativePercent(ts);
		ts.porcPositivoMedio = gp.getAveragePositivePercent(ts);
		ts.potenciaMaxima = gp.getMaxPower(ts);
		ts.potenciaMedia = gp.getAveragePower(ts);
		ts.rampaNegativaMaxima = gp.getMaxNegativePercent(ts);
		ts.rampaPositivaMaxima = gp.getMaxPositivePercent(ts);
		ts.tiempo = gp.getTime(ts);
		
		return ts;
	}
	
	/**
	 * Parsea un objeto Document en un objeto Track
	 * @param gpx
	 */
	private void procesarGPX(Document gpx){
		if (gpx!=null) {
			this.gpx = gpx;
			track = new Track();

			raiz = gpx.getRootElement(); //gpx
			List elementosDeRaiz = raiz.getChildren();
			
			/* Iteramos por los elementos de mas alto nivel despues de la raiz
			 * Generalmente metadata y trk
			 */
			Element metadata = getMetadata(elementosDeRaiz);
			Element trk = getTrk(elementosDeRaiz);
			
			// Cada trk tiene 2 nodos que contienen el nombre del track y su descripcion, luego, tiene el resto de nodos 
			// tipo tracksegment
			List trkNodeList = trk.getChildren();
			track.name = getName(trkNodeList);
			track.desc = getDesc(trkNodeList);
			
			Element nodoTrk;
			for(int j=0; j<trkNodeList.size(); j++){
				nodoTrk = (Element)trkNodeList.get(j);
				if(nodoTrk.getName().equals("trkseg")){
					track.add(parseTrackSegment(nodoTrk));
				}
			}
		}
	}
}
