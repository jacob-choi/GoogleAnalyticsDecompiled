package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

class AdwordsClickReferrerMacro extends FunctionCallImplementation {
	private static final String COMPONENT;
	private static final String CONVERSION_ID;
	private static final String ID;
	private final Context context;

	static {
		ID = FunctionType.ADWORDS_CLICK_REFERRER.toString();
		COMPONENT = Key.COMPONENT.toString();
		CONVERSION_ID = Key.CONVERSION_ID.toString();
	}

	public AdwordsClickReferrerMacro(Context context) {
		String r0_String = ID;
		String[] r1_String_A = new String[1];
		r1_String_A[0] = CONVERSION_ID;
		super(r0_String, r1_String_A);
		this.context = context;
	}

	public static String getFunctionId() {
		return ID;
	}

	public Value evaluate(Map<String, Value> parameters) {
		Value conversionIdValue = (Value) parameters.get(CONVERSION_ID);
		if (conversionIdValue == null) {
			return Types.getDefaultValue();
		} else {
			Value componentValue = (Value) parameters.get(COMPONENT);
			String referrer = InstallReferrerUtil.getClickReferrer(this.context, Types.valueToString(conversionIdValue), (componentValue != null) ? Types.valueToString(componentValue) : null);
			return (referrer != null) ? Types.objectToValue(referrer) : Types.getDefaultValue();
		}
	}

	public boolean isCacheable() {
		return true;
	}
}
