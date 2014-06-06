package br.com.backup;

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
    
    static GeradorDeDados geradorDeDados = new GeradorDeDados();
    static GraphDatabaseService graphDb; 
    
	public static Node inserirUnidadeMilitar(GraphDatabaseService graphDb){
		UnidadeMilitar unidadeMilitar = geradorDeDados.gerarUnidade();		
		Node node = graphDb.createNode(); 	
		node.setProperty("nome", unidadeMilitar.getNome());
		node.setProperty("tipo", unidadeMilitar.getTipo());
		node.setProperty("latitude", unidadeMilitar.getLatitude());
		node.setProperty("longitude", unidadeMilitar.getLongitude());		
		node.addLabel(new TypeLabel("Unidade Militar"));
		return node;
	}
	
	public static Node insertLocal(GraphDatabaseService graphDb, Node unidadeMilitar, Local local){
		return insertNo(graphDb,unidadeMilitar,local,RelTypes.CONTEM);
	}
	
	
	public static Node insertDG(GraphDatabaseService graphDb, Node local, DG dg){
		return insertNo(graphDb, local, dg,  RelTypes.CONTEM);
	}
	
	
	public static Node insertVertical(GraphDatabaseService graphDb, Node DG, Vertical vertical){
		return insertNo(graphDb, DG, vertical,  RelTypes.CONTEM);
	}
	
	public static Node insertBloco(GraphDatabaseService graphDb, Node vertical, Bloco bloco){		
		Node n = insertNo(graphDb, vertical, bloco, RelTypes.CONTEM);
		n.setProperty("modelo", bloco.getModelo());
		return  n;
	}
	
	
	public static Node insertPar(GraphDatabaseService graphDb, Node bloco, Par par){
		return insertNo(graphDb, bloco, par, RelTypes.CONTEM);
	}
	
	public static Node insertSitti(GraphDatabaseService graphDb, Node local, Sitti sitti){		
		Node n = insertNo(graphDb, local, sitti, RelTypes.CONTEM);
		n.setProperty("modelo", sitti.getModelo());
		n.setProperty("pn", sitti.getPn());
		return  n;
	}

	
	private static Node insertNo (GraphDatabaseService graphDb, Node pai, Entidade e, RelTypes relacionamento ){
		Node node = graphDb.createNode();
		node.setProperty("nome", e.getNome());
		node.setProperty("tipo", e.getTipo());
		insertLabel(e,node);
		relationship = pai.createRelationshipTo(node, relacionamento);
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
	
	/*
	 * Metodos para inserir lista de elementos
	 */
	
	
	public static List<Node>  insertListLocal(GraphDatabaseService graphDb,  List<Local> filho, Node pai){
		List<Node> list = new ArrayList<Node>();		
		for(Local l : filho){
			list.add(
				insertNo(graphDb,pai,l,RelTypes.CONTEM));
		}
		return list;
		
	}

	public static List<Node>  insertListDG(GraphDatabaseService graphDb,  List<DG> filho, Node pai){
		List<Node> list = new ArrayList<Node>();		
		for(DG l : filho){
			list.add(
				insertNo(graphDb,pai,l,RelTypes.CONTEM));
		}
		return list;
		
	}

	public static List<Node>insertListVertical(GraphDatabaseService graphDb,  List<Vertical> filho, Node pai){
		List<Node> list = new ArrayList<Node>();		
		for(Vertical l : filho){
			list.add(
				insertNo(graphDb,pai,l,RelTypes.CONTEM));
		}
		return list;
		
	}
	
	
	public static List<Node>insertListBloco(GraphDatabaseService graphDb,  List<Bloco> filho, Node pai){
		List<Node> list = new ArrayList<Node>();		
		for(Bloco l : filho){
			list.add(
				insertNo(graphDb,pai,l,RelTypes.CONTEM));
		}
		return list;
		
	}
	
	public static List<Node>insertListPar(GraphDatabaseService graphDb,  List<Par> filho, Node pai){
		List<Node> list = new ArrayList<Node>();		
		for(Par l : filho){
			list.add(
				insertNo(graphDb,pai,l,RelTypes.CONTEM));
		}
		return list;
		
	}
	
	
	public static void ligaPares(Node parOrigem, Node parDestino){
		parOrigem.createRelationshipTo(parDestino, RelTypes.LIGADO);
	}
	
	
	/*
	 * Fazer query em cypher que me retorna um nó de um par
	 */
	public static Node getNoOrigem(ExecutionEngine engine, int bloco, int par){
			bloco = 1;
			par =1;
		   ExecutionResult result;
		   String bl = "B" + bloco;
		   String p = "P"+par;
		   result =  engine.execute("MATCH (v:Vertical {nome: 'V1'})-[:CONTEM]->(b:Bloco {nome:  '"+  bl + "'})-[:CONTEM]->(p:Par {nome : ' "+ par + "'}) RETURN p");
		   Iterator<Node> n_column = result.columnAs("p");
		   for ( Node node : IteratorUtil.asIterable( n_column ) ){
			   return node;
		   }
		   return null;
	
	}
	
	public static Node getNoDestino(ExecutionEngine engine, int bloco, int par){
		
		Destino d = pegaDestionoDopar(bloco, par); 	
		return query( engine, d.getVertical(),d.getBloco(),d.getPar());
	}
	
	public static FileBloco abreArquivoDoBloco(int bloco){		
		CarregarDados cd = new CarregarDados("C:\\Users\\guilherme\\Desktop\\Materias\\BDA\\massaDeDadosTAB\\bloco1.tsv");
		cd.lerArquivo();
		FileBloco b = cd.getBloco();
		return b;
	}
	
	public static Destino pegaDestionoDopar(int bloco , int par){
		Destino d = new Destino();		
		// abreArquivoDoBloco SUBSTITUIR PELO CARREGA DADOS
		FileBloco fb = abreArquivoDoBloco(bloco);
		d = fb.getLigacoes().get(par).getDestino();
		return d;
	}
	
	public static Node query(ExecutionEngine engine, String vertical, String bloco, String par){
	   ExecutionResult result;
	   result =  engine.execute("MATCH (v:Vertical {nome: '"+vertical+"'})-[:CONTEM]->(b:Bloco {nome:  '"+  bloco+ "'})-[:CONTEM]->(p:Par {nome : '"+ par + "'}) RETURN p");
	   Iterator<Node> n_column = result.columnAs("p");
	   for ( Node node : IteratorUtil.asIterable( n_column ) ){
		   return node;
	   }
	   return null;
	}
	
}
