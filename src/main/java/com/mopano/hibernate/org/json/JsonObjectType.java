/*
 * Copyright (c) Mak-Si Management Ltd. Varna, Bulgaria
 * All rights reserved.
 *
 */
package com.mopano.hibernate.org.json;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.json.JSONObject;

public class JsonObjectType extends AbstractSingleColumnStandardBasicType<JSONObject> {

	private static final long serialVersionUID = -7456425337156846489L;

	private final String[] regKeys;
	private final String name;

	private static final String[] DEFAULT_KEYS = new String[]{
		"JSON",
		"json",
		"jsonb",
		JSONObject.class.getName()
	};
	private static final String DEFAULT_NAME = "JSON";

	public static final JsonObjectType INSTANCE = new JsonObjectType();

	public JsonObjectType() {
		// default values
		this(Handling.PGOBJECT, DEFAULT_KEYS, DEFAULT_NAME);
	}

	public JsonObjectType(String[] regKeys, String name) {
		this(Handling.PGOBJECT, regKeys, name);
	}

	@SuppressWarnings("unchecked")
	public JsonObjectType(Handling sqlHandler, String[] regKeys, String name) {
		super(sqlHandler == Handling.STRING ? JsonSqlStringHandler.INSTANCE : JsonSqlPGObjectHandler.INSTANCE,
				JsonObjectJavaTypeDescriptor.INSTANCE);
		this.regKeys = regKeys == null || regKeys.length == 0 ? DEFAULT_KEYS : regKeys;
		this.name = name == null || name.isEmpty() ? DEFAULT_NAME : name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String[] getRegistrationKeys() {
		return (String[]) regKeys.clone();
	}

	@Override
	protected boolean registerUnderJavaType() {
		return true;
	}

}