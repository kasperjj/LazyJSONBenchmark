package me.doubledutch;

import com.bluelinelabs.logansquare.LoganSquare;

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class LargeObject{
	public long Created;
	public String MetricType;
	public int SchemaVersion;
	public DeviceObject Device;
	public SessionObject Session;
	public ContextObject Context;
	public UserObject User;
	public ApplicationObject Application;

	public class DeviceObject{
		public String MMMInfo;
		public String DeviceType;
		public String DeviceOSVersion;
		public String DeviceId;
		public String BinaryVersion;
	}

	public class SessionObject{
		public int EventId;
		public String SessionId;
	}

	public class ContextObject{
		public String Identifier;
		public MetaDataObject MetaData;
		public int MetricVersion;
	}

	public class MetaDataObject{
		public int ActivityId;
	}

	public class UserObject{
		public String GlobalUserId;
	}

	public class ApplicationObject{
		public String ApplicationId;
		public String BundleId;
	}
}
/*
{"Device":
	{	"MMMInfo":"iPhone7,2",
		"DeviceType":"ios",
		"DeviceOSVersion":"9.3.2",
		"DeviceId":"cc847acd-2fde-4fb8-98a0-e8f9d001d15c",
		"BinaryVersion":"6.22.0"},
"Created":1469231962607.335,
"Session":{
	"EventId":2,
	"SessionId":"a95f09d3-c817-4695-a0cc-7c8738d5021e"},
"Context":{
	"Identifier":"statusUpdate",
	"MetaData":{"ActivityId":7114147},
	"MetricVersion":10},
"MetricType":"impression",
"User":{"GlobalUserId":"0ce7c5d4-8754-4689-99c4-5f605a96ea35"},
"Application":{
	"ApplicationId":"38d81a80-2fe2-4da0-a2a7-fd152c57b4a3",
	"BundleId":"53f473b2-4783-4a16-8185-11b462557777"},
"SchemaVersion":1},
*/