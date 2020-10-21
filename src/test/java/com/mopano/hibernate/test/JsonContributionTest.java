/*
 * Copyright (c) Mak-Si Management Ltd. Varna, Bulgaria
 * All rights reserved.
 *
 */
package com.mopano.hibernate.test;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonContributionTest {

	private static EntityManagerFactory emf;

	private static final Logger LOGGER = Logger.getLogger(JsonContributionTest.class);

	@BeforeClass
	public static void setupJPA() {
		emf = Persistence.createEntityManagerFactory("com.mopano.hibernate");
	}

	@AfterClass
	public static void closeJPA() {
		emf.close();
	}

	@Test
	public void testWriteRead() {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		try {
			MyEntity entity = new MyEntity();
			entity.id = 1l;
			entity.js = new JSONArray().put("str").put(false).put(0l);
			entity.jo = new JSONObject().put("thearray", new JSONArray().put("str").put(false).put(0l));
			entity.ja = new JSONArray().put(4).put("word");
			LOGGER.info("Persisting entity: " + entity);
			em.persist(entity);
			entity = new MyEntity();
			entity.id = 2l;
			// leave nulls because postgres is being shitty with those
			LOGGER.info("Persisting entity: " + entity);
			em.persist(entity);
			em.getTransaction().commit();
		}
		finally {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}

		em = emf.createEntityManager();
		em.getTransaction().begin();

		try {
			MyEntity entity1 = new MyEntity();
			entity1.id = 1l;
			entity1.js = new JSONArray().put("str").put(false).put(0l);
			entity1.jo = new JSONObject().put("thearray", new JSONArray().put("str").put(false).put(0l));
			entity1.ja = new JSONArray().put(4).put("word");
			MyEntity entity2 = new MyEntity();
			entity2.id = 2l;
			MyEntity me1 = em.find(MyEntity.class, new Long(1));
			LOGGER.info("Extracted entity: " + me1);
			MyEntity me2 = em.find(MyEntity.class, new Long(2));
			LOGGER.info("Extracted entity: " + me2);
			assertEquals(entity1, me1);
			assertEquals(entity2, me2);
		}
		finally {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	@Entity
	@Table(name = "json_entity")
	public static class MyEntity {

		@Id
		public Long id;
		@Column(columnDefinition = "json")
		public JSONArray js;
		@Column(columnDefinition = "json")
		public JSONArray ja;
		@Column(columnDefinition = "json")
		public JSONObject jo;

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof MyEntity)) {
				return false;
			}
			MyEntity it = (MyEntity) other;
			// assuming (possibly incorrectly) that the implementations take care of their equals() properly
			return Objects.equals(id, it.id)
					&& (js == it.js || Objects.equals(String.valueOf(js), String.valueOf(it.js)))
					&& (ja == it.ja || Objects.equals(String.valueOf(ja), String.valueOf(it.ja)))
					&& (jo == it.jo || Objects.equals(String.valueOf(jo), String.valueOf(it.jo)));
		}

		@Override
		public int hashCode() {
			// JSONArray and JSONObject officially don't handle hashCode and equals according to the internal data
			return Objects.hash(id, String.valueOf(js), String.valueOf(ja), String.valueOf(jo));
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append('[')
					.append("id = ")
					.append(id)
					.append(", js = ")
					.append(js)
					.append(", ja = ")
					.append(ja)
					.append(", jo = ")
					.append(jo)
					.append(']');
			return sb.toString();
		}
	}
}
