package br.com.processamento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.neo4j.graphdb.Node;

import br.com.util.RelTypes;

public class CarregarDados {	
	
		private static FileBloco bloco;
		private  static String enderecoDoBloco;
		public CarregarDados(String enderecoDoBloco) {
			bloco = new FileBloco();			
			this.enderecoDoBloco = enderecoDoBloco;			
			lerArquivo();
		}
		
		public void ligaPares(Node parOrigem, Node parDestino){
			parOrigem.createRelationshipTo(parDestino, RelTypes.LIGADO);
		}
		
		public static void lerArquivo(){
			
			 try {
			        BufferedReader in = new BufferedReader(new FileReader(enderecoDoBloco));
			            String str;
			            
			            while (in.ready()) {
			                str = in.readLine();			         
			                	process(str);
			            }
			            in.close();
			    } catch (IOException e) {
			    	System.out.println(e.getMessage());
			   }
		}
		
		/*
		 * Seta as propriedades de um FileBloco 
		 */
		static void process(String str){
			
			if(str.contains("DG")){
				bloco.setDg(processLinha(str,0,";"));				
			}else if (str.contains("Vertical")){
				bloco.setVertical(processLinha(str, 1, ";"));
				bloco.setModelo(processLinha(str, 2,";"));	
				bloco.setNumero(processLinha(str, 3, ";"));
			}else if(str.contains("Circuito:")){
				bloco.setCircuito(processLinha(str,1,";"));
			}
			else if(str.contains("Interliga")){
				bloco.setInterligacoExterna(processLinha(str,1,";"));
			}else if(str.contains("#")){	
					bloco.getLigacoes().add(processLigacao(str));
			}
		}
		

		
		static Ligacao processLigacao(String linha){			
	
		
			Ligacao l = new Ligacao(); 
			Destino d = new Destino();
	
			l.setPar(processLinha(linha,1, ";"));
			l.setIdentificacao(processLinha(linha,2, ";"));
			
			d.setVertical(processLinha(linha,3, ";"));
			d.setBloco(processLinha(linha,4, ";"));
			d.setPar(processLinha(linha,5, ";"));
			l.setDestino(d);
			
			return l;
		}
		
		static String  processLinha(String linha, int palavra, String delimitador){
			String []s = linha.split(delimitador);				
			if(s.length <= palavra){
				return "";
			}
			return s[palavra];
		}
		
		public static FileBloco getBloco() {
			return bloco;
		}
		public static void setBloco(FileBloco bloco) {
			CarregarDados.bloco = bloco;
		}

		public String getEnderecoDoBloco() {
			return enderecoDoBloco;
		}

		public void setEnderecoDoBloco(String enderecoDoBloco) {
			this.enderecoDoBloco = enderecoDoBloco;
		}
		
		
		
		

}
