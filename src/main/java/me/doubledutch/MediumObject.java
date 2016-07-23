package me.doubledutch;

import java.util.List;

import com.bluelinelabs.logansquare.LoganSquare;

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MediumObject{
	public String id;
	public String type;
	public List<SmallObject> data;
	public int serial;
	public String created;
}