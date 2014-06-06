package br.com.util;

import org.neo4j.graphdb.RelationshipType;
/*
 * Tipos de ligações
 */

public enum RelTypes implements RelationshipType {
	
	CONTEM,LIGADO,ESTA_EM;
}
