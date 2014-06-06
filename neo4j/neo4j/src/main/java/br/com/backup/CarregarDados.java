package br.com.backup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.neo4j.graphdb.Node;

import br.com.processamento.FileBloco;
import br.com.util.RelTypes;

public class CarregarDados {
		
		static FileBloco bloco  = new FileBloco();
		public CarregarDados() {
			bloco  = new FileBloco();
		}
		public void lerLinha(String linha){
			/*  
			 *  ler pOrigem
			 *  ler dentificao
			 *  ler vDestino
			 *  ler bDestino
			 *  ler pDestino			  
			 *  
			 */
			
			 
			// Fazer uma pesquisa em cypher que me retorna o pDestino
			// Se nao tiver o destino no arquivo : Nao tem ligacao
			// se não tiver o destino na consulta : Inserir o destino e fazer o relacionamento
		
			
		}
		
		public void ligaPares(Node parOrigem, Node parDestino){
			parOrigem.createRelationshipTo(parDestino, RelTypes.LIGADO);
		}
		
		
		public static void lerArquivo(){
			 try {
				
			        BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\guilherme\\Desktop\\Materias\\BDA\\massaDeDadosTAB\\bloco1.tsv"));
			            String str; int cabecalho =0, pares=0;
			            
			            while (in.ready()) {
			                str = in.readLine();			         
			                	process(str);
			            }
			            in.close();
			    } catch (IOException e) {
			   }
		}
		
		
		//DELETAR
		static void foo(String str){
			
			int numPares =0; 
			if(str.contains("DG")){
				System.out.println("DG "+ processAux(str, "DG"));
			}else if (str.contains("Vertical")){
				System.out.println("Vertical " + processAux(str, "Vertical"));	
				System.out.println("Krone " + processAux(str, "Krone"));
			}else if(str.contains("Interligação Externa")){
				System.out.println("IE " + processInterligacaoExter(str));
			}else if(str.contains("#")){	
				System.out.println(processLigacao(str));
				//bloco.getLigacoes().add(processLigacao(str));
			}
		}
		
		
		static void process(String str){
			int numPares =0; 
			if(str.contains("DG")){
				System.out.println("DG "+ processLinha(str,1," "));
			}else if (str.contains("Vertical")){
				System.out.println("Vertical "+ processLinha(str, 1, " "));
				System.out.println("Modelo " + processLinha(str, 2," "));
				System.out.println("ID " + processLinha(str,3," "));
			}else if(str.contains("Circuito:")){
				System.out.println("Circuito: " + processLinha(str,1," "));
			}
			else if(str.contains("Interligação Externa")){
				System.out.println("IE " + processLinha(str,1,":"));
			}else if(str.contains("#")){	
				processLigacao(str);
				//bloco.getLigacoes().add(processLigacao(str));
			}
		}
		
		static String processAux(String linha, String propriedade){
			StringTokenizer st = new StringTokenizer(linha); 			
			if(linha.contains(propriedade)){
			 while (st.hasMoreElements()) {		
		    		if (st.nextElement().equals(propriedade))
		    			return (String)st.nextElement();
		    					}
			}
			return null;
		}

		static String processInterligacaoExter(String str){			
			String[] aux = str.split(":");			
			return aux[1];
		}
		
		static Ligacao processLigacao(String linha){			
	
			System.out.println("");
			System.out.print(" par "+ processLinha(linha,1, ";"));
			System.out.print(" id " + processLinha(linha,2, ";"));
			System.out.print(" vert " + processLinha(linha,3, ";"));
			System.out.print(" bloc " + processLinha(linha,4, ";"));
			System.out.print(" par d " + processLinha(linha,5, ";"));
			System.out.println("");
			
			return null;
		}
		
		static String  processLinha(String linha, int palavra, String delimitador){
			String []s = linha.split(delimitador);			
			return s[palavra];
		}
		
		
		
		public static void main(String []args){
			lerArquivo();
		}
		
		
		
		

}
