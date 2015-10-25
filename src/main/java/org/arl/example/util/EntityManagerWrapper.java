package org.arl.example.util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerWrapper {

	private EntityManagerFactory		emf	= null;
	private static EntityManagerWrapper	emw	= null;

	/**
	 * Construtor.
	 */
	private EntityManagerWrapper() {
		super();
		emf = Persistence.createEntityManagerFactory("jpaexamples");
	}

	public static EntityManager getEntityManager() {
		if (null == emw) {
			emw = new EntityManagerWrapper();
		}
		return emw.emf.createEntityManager();
	}
}
