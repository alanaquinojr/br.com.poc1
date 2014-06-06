package br.com.grafo;


import static br.com.grafo.ConstructGraph.getNoDestino;
import static br.com.grafo.ConstructGraph.getNoOrigem;
import static br.com.grafo.ConstructGraph.inserirUnidadeMilitar;
import static br.com.grafo.ConstructGraph.insertBloco;
import static br.com.grafo.ConstructGraph.insertDG;
import static br.com.grafo.ConstructGraph.insertLocal;
import static br.com.grafo.ConstructGraph.insertPar;
import static br.com.grafo.ConstructGraph.insertVertical;
import static br.com.grafo.ConstructGraph.ligaPares;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

import br.com.modelo.Bloco;
import br.com.modelo.DG;
import br.com.modelo.Local;
import br.com.modelo.Par;
import br.com.modelo.Vertical;
import br.com.processamento.CarregarDados;
import br.com.processamento.Destino;
import br.com.processamento.FileBloco;
import br.com.processamento.Ligacao;
public class GraphBDA
{
	//target/neo4j-hello-db
	
	//Endereço da pasta onde ficara armazenado o grafo
    private static final String DB_PATH = "C:\\Users\\guilherme\\Desktop\\grafos\\Grafo5";
    private static final String PATH_ARQUIVOS = "C:\\Users\\guilherme\\Desktop\\Materias\\BDA\\CSV\\";
  


    GraphDatabaseService graphDb;
    Relationship relationship;
    

    public static void main( final String[] args )
    {
        GraphBDA hello = new GraphBDA();
        hello.createDb();
        hello.insereLigacoes();     
        hello.shutDown();
    }
    

    void createDb()
    {
        clearDb();
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {     	
        	
        	//Inserir UM
        	//inserirUnidadeMilitar(graphDb)
        	Node unidadeMilitar = inserirUnidadeMilitar(graphDb);     
        	
        	//Inserir Local     	
        	Node l1 = insertLocal(graphDb, unidadeMilitar, new Local("Sala Técnica", "Local"));
        	Node l2 = insertLocal(graphDb, unidadeMilitar, new Local("KT -VHF", "Local"));           	
        	
        	//Inserir DG     
        	Node dg1 = insertDG(graphDb, l1, new DG("1", "DG"));
        	Node dg2 = insertDG(graphDb, l2, new DG("2", "DG"));       	
        	
        	List<Node> verticais = new ArrayList<Node>();
        	//Inserir Vertical
        	// insere as verticaiss
        	for (int i = 0;i<40;i++){
        		int id = i+1;
        		String nomeVertical = "V"+id;
        		verticais.add(insertVertical(graphDb, dg2, new Vertical(nomeVertical, "vertical")));
        		
        	}
        	 
        	//Insere os blocos
        	for(Node v : verticais){
        		insereBlocosEPares(v);
        	}
        
            tx.success();
        }
       
    }

    
    public void insereBlocosEPares(Node vertical){
    int numBlocos = 45;
   	 for(int i =0; i<numBlocos; i++ ){     
   		 int idB = i+1;
   		 Node nodeBloco = insertBloco(graphDb, vertical,new Bloco("B"+idB, "Bloco", "Krone"));        		
   		 for(int j =0;j <10; j++){
   			 int idP = j+1;
   			 Par p = new Par("P"+idP, "Par");
   			 insertPar(graphDb, nodeBloco, p);        			 
   		 }
   	 }
    }
    

   private void insereLigacoes(){
	   
	   
	   List<FileBloco> lfb = listFileBloco(30, PATH_ARQUIVOS);
	   
	   registerShutdownHook( graphDb );
	   ExecutionEngine engine = new ExecutionEngine( graphDb );
	   try ( Transaction tx = graphDb.beginTx() ){	   
		
		   
		   for(FileBloco f : lfb){	
			   int numBloco = Integer.parseInt(f.getNumero());
			   int verticalDoBloco = Integer.parseInt(f.getVertical());
			   
			  for(int i=0;i<10;i++){
				  Ligacao ligacao = f.getLigacoes().get(i);
				 
					Destino[] aux = duploDestino(ligacao.getDestino());
					if (aux == null) {
						Destino desti = ligacao.getDestino();
						if (ligacao.getIdentificacao() != null) {
							Node o = getNoOrigem(engine,verticalDoBloco, numBloco, i+1);
							o.setProperty("identificacao",
									ligacao.getIdentificacao());
							Node d = getNoDestino(engine, desti);
							if (d != null) {
								d.setProperty("identificacao",
										ligacao.getIdentificacao());
							}
							ligaPares(o, d);
						}

					}else{						
						Node o = getNoOrigem(engine,verticalDoBloco, numBloco, i+1);
						o.setProperty("identificacao",
								ligacao.getIdentificacao());
						Node d = getNoDestino(engine, aux[0]);
						if (d != null) {
							d.setProperty("identificacao",
									ligacao.getIdentificacao());
						}
						Node x = getNoDestino(engine, aux[1]);
						if (x != null) {
							x.setProperty("identificacao",
									ligacao.getIdentificacao());
						}
						
						ligaPares(o, d);
						ligaPares(o, x);
					}
				}

			}

		   tx.success();
	   }
   }
    

   /*
    * Cria uma lista de FileBlocos. cada FileBloco representa um arquivo txt de um bloco
    * assume-se que todos os arquivos txt estarão em uma pasta nomeados da seguinte forma
    * (1).txt ; (2).txt
    * 
    * Deve-se passar o a quantidade de arquivos presentes em uma pasta, o caminho até pasta
    */
   public List<FileBloco> listFileBloco(int numArquivos, String pastaDosArquivos){
	   List<FileBloco> lfb = new ArrayList<FileBloco>();
	   for(int i =0 ;i<numArquivos;i++){
		   int x = i+1;
		   String nome = " ("+x+").txt";
		  
		   String path = pastaDosArquivos+nome;
		   CarregarDados cd = new CarregarDados(path);
		   lfb.add(cd.getBloco());
	   }
	   return lfb;
   }
    
    private void clearDb()
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }



    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }


    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    /*
     * Esta função retorna um array de 2 posições caso uma ligação tenha dois destinos
     * ou null caso a ligação tenha apenas um destino ou não tem destino
     */
    private  Destino[] duploDestino(Destino d){
		Destino [] a = new Destino[2];
		if(d.getVertical().contains("/")
				||
			d.getBloco().contains("/")
				||
			d.getPar().contains("/")){		
				String []s;
				
				Destino x = new Destino();
				Destino y = new Destino();
				
				s = d.getVertical().split("/");
				x.setVertical(s[0]);
				y.setVertical(s[1]);		
				
				s = d.getBloco().split("/");
				x.setBloco(s[0]);
				y.setBloco(s[1]);
				
				s = d.getPar().split("/");
				x.setPar(s[0]);
				y.setPar(s[1]);
				
				a[0]=x;
				a[1]=y;
			}else{
				return null;
			}
		
		return a;
	}
}