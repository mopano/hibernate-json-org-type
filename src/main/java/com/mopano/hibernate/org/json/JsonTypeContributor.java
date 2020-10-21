/*
 * Copyright (c) Mak-Si Management Ltd. Varna, Bulgaria
 * All rights reserved.
 *
 */
package com.mopano.hibernate.org.json;

import com.mopano.hibernate.org.json.spi.JsonSettings;
import java.util.Collection;
import java.util.TreeSet;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

import org.jboss.logging.Logger;

public class JsonTypeContributor implements TypeContributor {

	private static final Logger LOGGER = Logger.getLogger(org.hibernate.type.BasicType.class);

	@Override
	public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		AbstractSingleColumnStandardBasicType<?> jtype;
		JavaTypeDescriptor jdesc;
		ClassLoaderService cls = serviceRegistry.getService(ClassLoaderService.class);
		ConfigurationService config = serviceRegistry.getService(ConfigurationService.class);
		Dialect dialect = serviceRegistry.getService( JdbcServices.class ).getDialect();
		String dialectStr = dialect.toString().toLowerCase();
		boolean useMysql = dialectStr.contains("mysql") || dialectStr.contains("mariadb");

		try {

			Collection<JsonSettings> settingsList = cls.loadJavaServices( JsonSettings.class );
			if (settingsList.isEmpty()) {
				if (useMysql) {
					jdesc = JsonArrayJavaTypeDescriptor.INSTANCE;
					jtype = new JsonArrayType(Handling.STRING, null, null);
					JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
					typeContributions.contributeType(jtype, jtype.getRegistrationKeys());

					jdesc = JsonObjectJavaTypeDescriptor.INSTANCE;
					jtype = new JsonObjectType(Handling.STRING, null, null);
					JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
					typeContributions.contributeType(jtype, jtype.getRegistrationKeys());
				}
				else {
					jtype = JsonArrayType.INSTANCE;
					jdesc = JsonArrayJavaTypeDescriptor.INSTANCE;
					JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
					typeContributions.contributeType(jtype, jtype.getRegistrationKeys());

					jtype = JsonObjectType.INSTANCE;
					jdesc = JsonObjectJavaTypeDescriptor.INSTANCE;
					JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
					typeContributions.contributeType(jtype, jtype.getRegistrationKeys());
				}
			}
			else {
				TreeSet<String> regKeys = new TreeSet<>();
				String lastTypeName = null;
				Handling handler = Handling.PGOBJECT;

				for (JsonSettings jset : settingsList) {
					String[] rk = jset.getRegistrationKeys();
					lastTypeName = jset.getTypeName();
					handler = jset.getSqlHandlerType();

					if (rk != null && rk.length > 0) {
						for (String key : rk) {
							regKeys.add(key);
						}
					}
				}

				if (handler == null) {
					throw new NullPointerException("Why did you override the getSqlHandlerType to return null?");
				}

				jdesc = JsonArrayJavaTypeDescriptor.INSTANCE;
				jtype = new JsonArrayType(handler, regKeys.toArray(new String[0]), lastTypeName);
				JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
				typeContributions.contributeType(jtype, jtype.getRegistrationKeys());

				jdesc = JsonObjectJavaTypeDescriptor.INSTANCE;
				jtype = new JsonObjectType(handler, regKeys.toArray(new String[0]), lastTypeName);
				JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(jdesc);
				typeContributions.contributeType(jtype, jtype.getRegistrationKeys());
			}
		}
		catch (Throwable t) {
			LOGGER.error("JSON type contribution failed!", t);
		}

	}

}
