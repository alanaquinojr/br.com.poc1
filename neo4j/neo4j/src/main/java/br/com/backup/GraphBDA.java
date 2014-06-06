package br.com.backup;


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
    private static final String DB_PATH = "C:\\Users\\guilherme\\Desktop\\grafos\\Grafo2";
   
    public String greeting;

    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Relationship relationship;
    // END SNIPPET: vars

    public static void main( final String[] args )
    {
        GraphBDA hello = new GraphBDA();
        hello.createDb();
        hello.insereLigacoes();
     
        hello.shutDown();
    }
    
    public GraphBDA() {
      
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
        	for (int i = 0;i<11;i++){
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
    int numBlocos = 10;
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
    
    /*
     * esta funcao o esta executando apenas para uma ligação especifica, ainda falta modulariza-la q funcione para os outros casos  
     */
   private void insereLigacoes(){
	   
	   CarregarDados cd = new CarregarDados("C:\\Users\\guilherme\\Desktop\\Materias\\BDA\\massaDeDadosTAB\\bloco1.tsv");
	   FileBloco b = cd.getBloco();
	   Ligacao  l =  b.getLigacoes().get(2);
	   Destino destino = l.getDestino();
	   int idBloco = Integer.parseInt(b.getNumero());
	   
	   registerShutdownHook( graphDb );
	   ExecutionEngine engine = new ExecutionEngine( graphDb );
	   try ( Transaction tx = graphDb.beginTx() ){	   
		   // Tratar o caso em que o destino é da forma 1/7 4/5 1/1
		   // se não tem identificação significa que não existe ligacao
		   if(l.getIdentificacao()!=null){
			 Node o = getNoOrigem(engine, idBloco,1);	
			 o.setProperty("identificao", l.getIdentificacao());
			 Node d = getNoDestino(engine,destino);
			 // se não tem destino significa que o par é um terminal 
			 if(d!=null){
				 d.setProperty("identificacao", l.getIdentificacao());
			 }			
			 ligaPares(o, d);
		   }
		   tx.success();
	   }
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
    // END SNIPPET: shutdownHook
}