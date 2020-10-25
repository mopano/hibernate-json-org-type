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
import org.json.JSONArray;

public class JsonArrayJavaTypeDescriptor extends AbstractTypeDescriptor<JSONArray> {

	public static final JsonArrayJavaTypeDescriptor INSTANCE = new JsonArrayJavaTypeDescriptor();
	private static final long serialVersionUID = 4350209361021258277L;

	public JsonArrayJavaTypeDescriptor() {
		super(JSONArray.class);
	}

	@Override
	public String toString(JSONArray value) {
		return value.toString();
	}

	@Override
	public JSONArray fromString(String string) {
		return new JSONArray(string);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X unwrap(JSONArray value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}

		if (String.class.isAssignableFrom(type)) {
			return (X) this.toString(value);
		}

		throw unknownUnwrap(type);
	}

	@Override
	public <X> JSONArray wrap(X value, WrapperOptions options) {
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
