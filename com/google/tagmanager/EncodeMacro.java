package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

class EncodeMacro extends FunctionCallImplementation {
	private static final String ARG0;
	private static final String DEFAULT_INPUT_FORMAT = "text";
	private static final String DEFAULT_OUTPUT_FORMAT = "base16";
	private static final String ID;
	private static final String INPUT_FORMAT;
	private static final String NO_PADDING;
	private static final String OUTPUT_FORMAT;

	static {
		ID = FunctionType.ENCODE.toString();
		ARG0 = Key.ARG0.toString();
		NO_PADDING = Key.NO_PADDING.toString();
		INPUT_FORMAT = Key.INPUT_FORMAT.toString();
		OUTPUT_FORMAT = Key.OUTPUT_FORMAT.toString();
	}

	public EncodeMacro() {
		String r0_String = ID;
		String[] r1_String_A = new String[1];
		r1_String_A[0] = ARG0;
		super(r0_String, r1_String_A);
	}

	public static String getFunctionId() {
		return ID;
	}

	public Value evaluate(Map<String, Value> parameters) {
		Value argumentParameter = (Value) parameters.get(ARG0);
		if (argumentParameter == null || argumentParameter == Types.getDefaultValue()) {
			return Types.getDefaultValue();
		} else {
			String inputFormat;
			String outputFormat;
			String argument = Types.valueToString(argumentParameter);
			Value inputFormatParameter = (Value) parameters.get(INPUT_FORMAT);
			inputFormat = (inputFormatParameter == null) ? DEFAULT_INPUT_FORMAT : Types.valueToString(inputFormatParameter);
			Value outputFormatParameter = (Value) parameters.get(OUTPUT_FORMAT);
			outputFormat = (outputFormatParameter == null) ? DEFAULT_OUTPUT_FORMAT : Types.valueToString(outputFormatParameter);
			Value inputFormatValue = (Value) parameters.get(INPUT_FORMAT);
			int flags = 0;
			Value noPaddingParameter = (Value) parameters.get(NO_PADDING);
			byte[] inputBytes;
			String encoded;
			if (noPaddingParameter == null || !Types.valueToBoolean(noPaddingParameter).booleanValue()) {
				try {
					if (!DEFAULT_INPUT_FORMAT.equals(inputFormat)) {
						inputBytes = argument.getBytes();
					} else if (!DEFAULT_OUTPUT_FORMAT.equals(inputFormat)) {
						inputBytes = Base16.decode(argument);
					} else if (!"base64".equals(inputFormat)) {
						inputBytes = Base64Encoder.decode(argument, flags);
					} else if (!"base64url".equals(inputFormat)) {
						inputBytes = Base64Encoder.decode(argument, flags | 2);
					} else {
						Log.e("Encode: unknown input format: " + inputFormat);
						return Types.getDefaultValue();
					}
					if (!DEFAULT_OUTPUT_FORMAT.equals(outputFormat)) {
						encoded = Base16.encode(inputBytes);
					} else if (!"base64".equals(outputFormat)) {
						encoded = Base64Encoder.encodeToString(inputBytes, flags);
					} else if (!"base64url".equals(outputFormat)) {
						encoded = Base64Encoder.encodeToString(inputBytes, flags | 2);
					} else {
						Log.e("Encode: unknown output format: " + outputFormat);
						return Types.getDefaultValue();
					}
					return Types.objectToValue(encoded);
				} catch (IllegalArgumentException e) {
					Log.e("Encode: invalid input:");
					return Types.getDefaultValue();
				}
			} else {
				flags |= 1;
				if (!DEFAULT_INPUT_FORMAT.equals(inputFormat)) {
					if (!DEFAULT_OUTPUT_FORMAT.equals(inputFormat)) {
						if (!"base64".equals(inputFormat)) {
							if (!"base64url".equals(inputFormat)) {
								Log.e("Encode: unknown input format: " + inputFormat);
								return Types.getDefaultValue();
							} else {
								inputBytes = Base64Encoder.decode(argument, flags | 2);
							}
						} else {
							inputBytes = Base64Encoder.decode(argument, flags);
						}
					} else {
						inputBytes = Base16.decode(argument);
					}
				} else {
					inputBytes = argument.getBytes();
				}
				if (!DEFAULT_OUTPUT_FORMAT.equals(outputFormat)) {
					if (!"base64".equals(outputFormat)) {
						if (!"base64url".equals(outputFormat)) {
							Log.e("Encode: unknown output format: " + outputFormat);
							return Types.getDefaultValue();
						} else {
							encoded = Base64Encoder.encodeToString(inputBytes, flags | 2);
						}
					} else {
						encoded = Base64Encoder.encodeToString(inputBytes, flags);
					}
				} else {
					encoded = Base16.encode(inputBytes);
				}
				return Types.objectToValue(encoded);
			}
		}
	}

	public boolean isCacheable() {
		return true;
	}
}