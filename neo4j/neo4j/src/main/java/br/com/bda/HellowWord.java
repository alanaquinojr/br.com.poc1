package br.com.bda;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class HellowWord {

	private static final String DB_PATH = "C:\\Users\\guilherme\\Documents\\Neo4j\\DB";
	public  String greeting; 
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    
	public static void main(String []args){
		HellowWord hw = new HellowWord();
		hw.criarDB();
	}
	
	public void criarDB(){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		// fecha a DB
		 registerShutdownHook( graphDb );
		 
		 
		 // abre uma transação com o banco 
		 try(Transaction tx = graphDb.beginTx()){
			 // criamos os nos 
			 firstNode = graphDb.createNode();
			 secondNode = graphDb.createNode();
			 
			 // setamos as propriedades dos nos
			 firstNode.setProperty("mensagem", "Ola");
			 secondNode.setProperty("mensagem", "mundo");
			 
			 
			 // criamos os relacionamentos entre os nós 
			 relationship = firstNode.createRelationshipTo(secondNode, Relacionamento.KNOWS);
			 relationship.setProperty("Ligado", "Par metalico");
			 
			 // imprimos o resultado
			 greeting =(String)firstNode.getProperty("mensagem") + 
					 	(String)relationship.getProperty("Ligado")+
					 	(String)secondNode.getProperty("mensagem");
			 
			 System.out.println(greeting);
			 tx.success();
		 }
		
	}
	
	public void fecharDB(){
		
	}
	
	public void inserirNo(){
		
	}
	
	public void removerNo(){
		
	}
	
	public void inserirRelacionameno(){
		
	}
	public void removerRelacionamento(){
		
	}
	
	public void retornarCaminho(){
		
	}
	
	public  static enum Relacionamento implements RelationshipType{

		KNOWS
		
	}
	
	
	// START SNIPPET: shutdownHook
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
}
