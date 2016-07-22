package me.doubledutch;

import com.bluelinelabs.logansquare.LoganSquare;

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SmallObject{
	public String key1;
	public int key2;
	public double key3;
	public boolean key4;
	public String key5;
}