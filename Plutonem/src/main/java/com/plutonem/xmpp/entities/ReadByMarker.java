package com.plutonem.xmpp.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import rocks.xmpp.addr.Jid;

public class ReadByMarker {
    private Jid fullJid;
    private Jid realJid;

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        if (fullJid != null) {
            try {
                jsonObject.put("fullJid", fullJid.toString());
            } catch (JSONException e) {
                //ignore
            }
        }
        if (realJid != null) {
            try {
                jsonObject.put("realJid", realJid.toString());
            } catch (JSONException e) {
                //ignore
            }
        }
        return jsonObject;
    }

    public static Set<ReadByMarker> fromJson(JSONArray jsonArray) {
        HashSet<ReadByMarker> readByMarkers = new HashSet<>();
        for(int i = 0; i < jsonArray.length(); ++i) {
            try {
                readByMarkers.add(fromJson(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                //ignored
            }
        }
        return readByMarkers;
    }

    public static ReadByMarker fromJson(JSONObject jsonObject) {
        ReadByMarker marker = new ReadByMarker();
        try {
            marker.fullJid = Jid.of(jsonObject.getString("fullJid"));
        } catch (JSONException | IllegalArgumentException e) {
            marker.fullJid = null;
        }
        try {
            marker.realJid = Jid.of(jsonObject.getString("realJid"));
        } catch (JSONException | IllegalArgumentException e) {
            marker.realJid = null;
        }
        return marker;
    }

    public static Set<ReadByMarker> fromJsonString(String json) {
        try {
            return fromJson(new JSONArray(json));
        } catch (JSONException | NullPointerException e) {
            return new HashSet<>();
        }
    }

    public static JSONArray toJson(Set<ReadByMarker> readByMarkers) {
        JSONArray jsonArray = new JSONArray();
        for(ReadByMarker marker : readByMarkers) {
            jsonArray.put(marker.toJson());
        }
        return jsonArray;
    }
}
