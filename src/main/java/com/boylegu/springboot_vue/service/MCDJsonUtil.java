package com.boylegu.springboot_vue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MCDJsonUtil {

	public static String Serialize(Object value) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	public static Object Deserialize(String str, Class<?> T) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(str, T);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static <I> I DeserializeObj(String str, Class<I>T) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(str, T);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> DeserializeArray(String jsonPacket, Class<?> T) {

		List<T> list = new ArrayList<T>();
		try {
			JsonParser parser = new JsonParser();
			JsonElement tradeElement = parser.parse(jsonPacket);
			JsonArray trade = tradeElement.getAsJsonArray();
			for (int i = 0; i < trade.size(); i++) {
				JsonElement item = trade.get(i);
				T d = (T) Deserialize(item.toString(), T);
				list.add(d);
			}
		} catch (Exception e) {
			// Handle the problem
		}
		return list;
	}

}
