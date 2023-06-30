package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {

	
//	Stato sistema
	
	private Map<Country, Integer> stanziali ;
	
// Parametri Simulazione 
	
	private Graph<Country,DefaultEdge> graph ;
	private int nPersone = 1000 ;
	private Country partenza ;
	
// Output
	
	private int nPassi ; 
	
// Coda degli eventi
	
	private PriorityQueue<Event> queue ;
	public Simulator (Graph<Country,DefaultEdge> graph, Country partenza ) {
		this.graph = graph;
		this.partenza = partenza ;
		
		this.nPassi = 0 ;
		this.stanziali = new HashMap<Country,Integer>() ;
		
		for(Country c: this.graph.vertexSet()) {
			this.stanziali.put(c, 0) ;
		}
		
		this.queue = new PriorityQueue<> () ;
		
	}
	

	public void initialize () {
		this.queue.add(new Event(0, this.partenza, this.nPersone)) ;
		
	}
	
	public void run () {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll() ;
			int time = e.getTime() ;
			Country destinazione = e.getDestinazione() ;
			int dimensione = e.getDimensione(); 
			
			List<Country> vicini = Graphs.neighborListOf(this.graph, destinazione) ;
			
			int migranti = dimensione/2/vicini.size() ; 
			
			this.nPassi = time ;
			
			
//			(Dimensione/2)  si dividono negli stati adiacenti
//			generando eventi INGRESSO con la quota di persone 
			
			
			if(migranti > 0) {
				for(Country c : vicini) {
					this.queue.add(new Event (time +1, c, migranti));
					
				}
			}
//			i rimanenti diventano stanziali nello stato 'destinazione'

			int nuoviStanziali = dimensione - migranti * vicini.size() ;
			this.stanziali.put(destinazione, this.stanziali.get(destinazione)+ nuoviStanziali) ;
		}
	}
	public Map<Country,Integer> getStanziali(){
		return stanziali;
	}

	public int getnPassi() {
		return nPassi;
	}
}
