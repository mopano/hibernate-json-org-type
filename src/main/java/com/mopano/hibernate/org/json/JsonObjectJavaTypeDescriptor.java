/*
 * Copyright (c) Mak-Si Management Ltd. Varna, Bulgaria
 *
 * License: BSD 3-Clause license.
 * See the LICENSE.md file in the root directory or <https://opensource.org/licenses/BSD-3-Clause>.
 * See also <https://tldrlegal.com/license/bsd-3-clause-license-(revised)>.
 */
package com.mopano.hibernate.org.json;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.json.JSONObject;

public class JsonObjectJavaTypeDescriptor extends AbstractTypeDescriptor<JSONObject> {

	public static final JsonObjectJavaTypeDescriptor INSTANCE = new JsonObjectJavaTypeDescriptor();
	private static final long serialVersionUID = 4350209361021258277L;

	public JsonObjectJavaTypeDescriptor() {
		super(JSONObject.class);
	}

	@Override
	public String toString(JSONObject value) {
		return value.toString();
	}

	@Override
	public JSONObject fromString(String string) {
		return new JSONObject(string);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X unwrap(JSONObject value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}

		if (String.class.isAssignableFrom(type)) {
			return (X) this.toString(value);
		}

		throw unknownUnwrap(type);
	}

	@Override
	public <X> JSONObject wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}

		Class type = value.getClass();

		if (String.class.isAssignableFrom(type)) {
			String s = (String) value;
			return this.fromString(s);
		}

		throw unknownWrap(type);
	}

}
