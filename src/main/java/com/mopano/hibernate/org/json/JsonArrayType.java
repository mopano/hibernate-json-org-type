/*
 * Copyright (c) Mak-Si Management Ltd. Varna, Bulgaria
 *
 * License: BSD 3-Clause license.
 * See the LICENSE.md file in the root directory or <https://opensource.org/licenses/BSD-3-Clause>.
 * See also <https://tldrlegal.com/license/bsd-3-clause-license-(revised)>.
 */
package com.mopano.hibernate.org.json;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.json.JSONArray;

public class JsonArrayType extends AbstractSingleColumnStandardBasicType<JSONArray> {

	private static final long serialVersionUID = -7456425337156846489L;

	private final String[] regKeys;
	private final String name;

	private static final String[] DEFAULT_KEYS = new String[]{
		"JSON",
		"json",
		"jsonb",
		JSONArray.class.getName()
	};
	private static final String DEFAULT_NAME = "JSON";

	public static final JsonArrayType INSTANCE = new JsonArrayType();

	public JsonArrayType() {
		// default values
		this(Handling.PGOBJECT, DEFAULT_KEYS, DEFAULT_NAME);
	}

	public JsonArrayType(String[] regKeys, String name) {
		this(Handling.PGOBJECT, regKeys, name);
	}

	@SuppressWarnings("unchecked")
	public JsonArrayType(Handling sqlHandler, String[] regKeys, String name) {
		super(sqlHandler == Handling.STRING ? JsonSqlStringHandler.INSTANCE : JsonSqlPGObjectHandler.INSTANCE,
				JsonArrayJavaTypeDescriptor.INSTANCE);
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
