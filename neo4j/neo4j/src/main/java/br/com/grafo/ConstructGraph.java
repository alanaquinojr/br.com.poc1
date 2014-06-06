package br.com.grafo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;






import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.helpers.collection.IteratorUtil;

import br.com.modelo.Bloco;
import br.com.modelo.DG;
import br.com.modelo.Entidade;
import br.com.modelo.Local;
import br.com.modelo.Par;
import br.com.modelo.Sitti;
import br.com.modelo.UnidadeMilitar;
import br.com.modelo.Vertical;
import br.com.processamento.CarregarDados;
import br.com.processamento.Destino;
import br.com.processamento.FileBloco;
import br.com.processamento.Ligacao;
import br.com.util.RelTypes;
import br.com.util.TypeLabel;

/**
 * Esta classe servira para construi a base de dados
 * @author guilherme
 *
 */
public class ConstructGraph {	
	
	

    static Node node;
    static Relationship relationship;
    
  
    static GraphDatabaseService graphDb; 
    
	public static Node inserirUnidadeMilitar(GraphDatabaseService graphDb){		
		
		UnidadeMilitar unidadeMilitar = new UnidadeMilitar("DTCEA-CF", "Unidade Militar", "43 58 06 W", "19 37 59 S");
		Node node = graphDb.createNode(); 	
		node.setProperty("nome", unidadeMilitar.getNome());
		node.setProperty("tipo", unidadeMilitar.getTipo());
		node.setProperty("latitude", unidadeMilitar.getLatitude());
		node.setProperty("longitude", unidadeMilitar.getLongitude());		
		node.addLabel(new TypeLabel("Unidade Militar"));
		return node;
	}
	
	public static Node insertLocal(GraphDatabaseService graphDb, Node unidadeMilitar, Local local){
		return insertNo(graphDb,unidadeMilitar,local,RelTypes.ESTA_EM);
	}
	
	
	public static Node insertDG(GraphDatabaseService graphDb, Node local, DG dg){
		return insertNo(graphDb, local, dg,  RelTypes.ESTA_EM);
	}
	
	
	public static Node insertVertical(GraphDatabaseService graphDb, Node DG, Vertical vertical){
		return insertNo(graphDb, DG, vertical,  RelTypes.ESTA_EM);
	}
	
	public static Node insertBloco(GraphDatabaseService graphDb, Node vertical, Bloco bloco){		
		Node n = insertNo(graphDb, vertical, bloco, RelTypes.ESTA_EM);
		n.setProperty("modelo", bloco.getModelo());
		return  n;
	}
	
	
	public static Node insertPar(GraphDatabaseService graphDb, Node bloco, Par par){
		return insertNo(graphDb, bloco, par, RelTypes.ESTA_EM);
	}
	
	public static Node insertSitti(GraphDatabaseService graphDb, Node local, Sitti sitti){		
		Node n = insertNo(graphDb, local, sitti, RelTypes.ESTA_EM);
		n.setProperty("modelo", sitti.getModelo());
		n.setProperty("pn", sitti.getPn());
		return  n;
	}

	
	private static Node insertNo (GraphDatabaseService graphDb, Node pai, Entidade e, RelTypes relacionamento ){
		Node node = graphDb.createNode();
		node.setProperty("nome", e.getNome());
		node.setProperty("tipo", e.getTipo());
		insertLabel(e,node);
		//relationship = pai.createRelationshipTo(node, relacionamento);
		relationship = node.createRelationshipTo(pai, relacionamento);
		return node;
		
	}
	
	
	private static void insertLabel(Entidade e, Node n){
		
		
		if(e instanceof Local){
			n.addLabel(new TypeLabel("Local"));
		}else if(e instanceof DG){
			n.addLabel(new TypeLabel("DG"));		
		}else if(e instanceof Vertical){
			n.addLabel(new TypeLabel("Vertical"));
		}else if(e instanceof Bloco){
			n.addLabel(new TypeLabel("Bloco"));
		}else if(e instanceof Par){
			n.addLabel(new TypeLabel("Par"));
		}
	}

	
	
	public static void ligaPares(Node parOrigem, Node parDestino){		
		if(parDestino!=null)
			parOrigem.createRelationshipTo(parDestino, RelTypes.LIGADO);
	}
	
	
	/*
	 * Fazer query em cypher que me retorna um nó de um par
	 */
	public static Node getNoOrigem(ExecutionEngine engine, int vertical, int bloco, int par){
	
		   ExecutionResult result;
		   String bl = "B" + bloco;
		   String p = "P"+par;
		   String v = "V"+vertical;
		   result =  engine.execute("MATCH (v:Vertical {nome: '"+v+"'})<-[:ESTA_EM]-(b:Bloco {nome:  '"+  bl + "'})<-[:ESTA_EM]-(p:Par {nome : '"+ p + "'}) RETURN p");
		   Iterator<Node> n_column = result.columnAs("p");
		   for ( Node node : IteratorUtil.asIterable( n_column ) ){
			   return node;
		   }
		   return null;
	
	}
	
	public static Node getNoDestino(ExecutionEngine engine, Destino d){		
	
		return query(engine, d.getVertical(),d.getBloco(),d.getPar());
	}
	
	
	public static Node query(ExecutionEngine engine, String vertical, String bloco, String par){
	   ExecutionResult result;
	   String v = "V"+vertical;
	   String b = "B"+bloco;
	   String p = "P"+par;
	   System.out.println("Vertical: " + v);
	   System.out.println("bloco: " + b);
	   System.out.println("par: " + p);
	   result =  engine.execute("MATCH (v:Vertical {nome: '"+v+"'})<-[:ESTA_EM]-(b:Bloco {nome:  '"+  b+ "'})<-[:ESTA_EM]-(p:Par {nome : '"+ p + "'}) RETURN p");
	   Iterator<Node> n_column = result.columnAs("p");
	   for ( Node node : IteratorUtil.asIterable( n_column ) ){
		   return node;
	   }
	   return null;
	}
	
}
