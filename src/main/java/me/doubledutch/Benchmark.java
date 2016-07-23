package me.doubledutch;

import org.json.*;
import com.google.gson.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.JsonParser;
import com.bluelinelabs.logansquare.LoganSquare;

import org.boon.core.reflection.BeanUtils;
import org.boon.core.reflection.MapObjectConversion;
import static org.boon.Boon.puts;

import me.doubledutch.lazy.*;

public class Benchmark{
	public final int RUNS=100;
	public final boolean HUMAN_OUTPUT=true;
	public int BATCH=1000;

	private JSONObject createSmallObject(){
		JSONObject obj=new JSONObject();
		obj.put("key1","value1");
		obj.put("key2",(int)(Math.random()*1000000));
		obj.put("key3",Math.random());
		obj.put("key4",false);
		obj.put("key5",JSONObject.NULL);
		return obj;
	}

	private JSONObject createMediumObject(int i){
		JSONObject outer=new JSONObject();
		outer.put("id","deadbeef-dead-beef-dead-beef00000001");
		outer.put("type","MediumObject");
		JSONArray a=new JSONArray();
		a.put(createSmallObject());
		a.put(createSmallObject());
		outer.put("data",a);
		outer.put("serial",i);
		outer.put("created",new java.util.Date().toString());
		return outer;
	}

	private JSONObject createLargeObject(){
		JSONObject obj=new JSONObject();
		JSONObject device=new JSONObject();
		device.put("MMMInfo","iPhone7,2");
		device.put("DeviceType","ios");
		device.put("DeviceOSVersion","9.3.2");
		device.put("DeviceId","deadbeef-dead-beef-dead-beef00000001");
		device.put("BinaryVersion","9.99.99");
		obj.put("Device",device);
		obj.put("Created",System.currentTimeMillis());
		JSONObject session=new JSONObject();
		session.put("EventId",(int)(Math.random()*1000));
		session.put("SessionId","deadbeef-dead-beef-dead-beef00000002");
		obj.put("Session",session);
		JSONObject context=new JSONObject();
		context.put("Identifier","statusUpdate");
		JSONObject metadata=new JSONObject();
		metadata.put("ActivityId",(int)(Math.random()*10000000));
		context.put("MetaData",metadata);
		context.put("MetricVersion",99);
		obj.put("Context",context);
		JSONObject user=new JSONObject();
		user.put("GlobalUserId","deadbeef-dead-beef-dead-beef00000003");
		obj.put("User",user);
		JSONObject application=new JSONObject();
		application.put("ApplicationId","deadbeef-dead-beef-dead-beef00000004");
		application.put("BundleId","deadbeef-dead-beef-dead-beef00000005");
		obj.put("Application",application);
		obj.put("SchemaVersion",1);
		return obj;
	}

	public Benchmark(){
		try{
			System.out.println("Generating SmallObject batch data");
			JSONArray array=new JSONArray();
			for(int i=0;i<BATCH;i++){
				array.put(createSmallObject());
			}
			final String raw_batch_1=array.toString();
			array=null;
			System.out.println("Running SmallObject parse test");
			testRawParsing(raw_batch_1);
			testSmallObjectParsing(raw_batch_1);

			System.out.println("Running SmallObject split and serialize test");
			testSplit(raw_batch_1);
			testSmallObjectSplit(raw_batch_1);
			
			System.out.println("Running SmallObject parse and access test");
			testSmallObjectAccess(raw_batch_1);

			System.out.println("Generating MediumObject batch data");
			array=new JSONArray();
			for(int i=0;i<BATCH;i++){
				array.put(createMediumObject(i));
			}
			final String raw_batch_2=array.toString();
			array=null;
			System.out.println("Running MediumObject parse test");
			testRawParsing(raw_batch_2);
			testMediumObjectParsing(raw_batch_2);

			System.out.println("Running MediumObject split and serialize test");
			testSplit(raw_batch_2);
			testMediumObjectSplit(raw_batch_2);

			System.out.println("Generating LargeObject batch data");
			array=new JSONArray();
			for(int i=0;i<BATCH;i++){
				array.put(createLargeObject());
			}
			final String raw_batch_3=array.toString();
			array=null;
			System.out.println("Running LargeObject parse test");
			testRawParsing(raw_batch_3);
			testLargeObjectParsing(raw_batch_3);

			System.out.println("Running LargeObject split and serialize test");
			testSplit(raw_batch_3);
			testLargeObjectSplit(raw_batch_3);
		}catch(Throwable t){
			t.printStackTrace();
		}

	}

	public void testRawParsing(final String data) throws Exception{
		runTest("json.org",new Runnable(){
			public void run(){
				JSONArray arr=new JSONArray(data);
			}
		});

		runTest("GSON JsonParser",new Runnable(){
			public void run(){
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(data);
				JsonArray asJsonArray = jsonElement.getAsJsonArray();
			}
		});

		runTest("Jackson ObjectMapper",new Runnable(){
			public void run(){
				try{
					ObjectMapper mapper = new ObjectMapper();
					JsonNode rootNode = mapper.readTree(data);
					Iterator<JsonNode> ite = rootNode.iterator();				 
					while (ite.hasNext()) {
						JsonNode temp = ite.next(); 
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		runTest("Jackson JsonParser",new Runnable(){
			public void run(){
				try{
					com.fasterxml.jackson.core.JsonFactory factory = new com.fasterxml.jackson.core.JsonFactory();
					com.fasterxml.jackson.core.JsonParser  parser  = factory.createParser(data);
					while(!parser.isClosed()){
    					com.fasterxml.jackson.core.JsonToken jsonToken = parser.nextToken();
    				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		runTest("LazyJSON",new Runnable(){
			public void run(){
				LazyArray arr=new LazyArray(data);
				for(int i=0;i<arr.length();i++){
					LazyObject obj=arr.getJSONObject(i);
				}
			}
		});
	}

	public void testSplit(final String data) throws Exception{
		final List<String> result=new ArrayList<String>(BATCH);
		runTest("json.org",new Runnable(){
			public void run(){
				result.clear();
				JSONArray arr=new JSONArray(data);
				for(int i=0;i<arr.length();i++){
					result.add(arr.getJSONObject(i).toString());
				}
			}
		});

		runTest("GSON JsonParser based",new Runnable(){
			public void run(){
				result.clear();
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(data);
				JsonArray asJsonArray = jsonElement.getAsJsonArray();
				for(int i=0;i<asJsonArray.size();i++){
					result.add(asJsonArray.get(i).toString());
				}
			}
		});

		runTest("Jackson ObjectMapper",new Runnable(){
			public void run(){
				result.clear();
				try{
					ObjectMapper mapper = new ObjectMapper();
					JsonNode rootNode = mapper.readTree(data);
					Iterator<JsonNode> ite = rootNode.iterator();				 
					while (ite.hasNext()) {
						JsonNode temp = ite.next(); 
						result.add(mapper.writeValueAsString(temp));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		runTest("LazyJSON",new Runnable(){
			public void run(){
				result.clear();
				LazyArray arr=new LazyArray(data);
				for(int i=0;i<arr.length();i++){
					LazyObject obj=arr.getJSONObject(i);
					result.add(obj.toString());
				}
			}
		});
	}

	public void testSmallObjectAccess(final String data) throws Exception{
		List<String> key1=new ArrayList<String>(BATCH);
		List<Integer> key2=new ArrayList<Integer>(BATCH);
		List<Double> key3=new ArrayList<Double>(BATCH);
		List<Boolean> key4=new ArrayList<Boolean>(BATCH);

		runTest("json.org",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				JSONArray arr=new JSONArray(data);
				for(int i=0;i<arr.length();i++){
					JSONObject obj=arr.getJSONObject(i);
					key1.add(obj.getString("key1"));
					key2.add(obj.getInt("key2"));
					key3.add(obj.getDouble("key3"));
					key4.add(obj.getBoolean("key4"));
				}
			}
		});

		runTest("GSON JsonParser",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(data);
				JsonArray asJsonArray = jsonElement.getAsJsonArray();
				for(int i=0;i<asJsonArray.size();i++){
					JsonObject temp=asJsonArray.get(i).getAsJsonObject();
					key1.add(temp.get("key1").getAsString());
					key2.add(temp.get("key2").getAsInt());
					key3.add(temp.get("key3").getAsDouble());
					key4.add(temp.get("key4").getAsBoolean());
				}
			}
		});

		runTest("Jackson ObjectMapper",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				try{
					ObjectMapper mapper = new ObjectMapper();
					JsonNode rootNode = mapper.readTree(data);
					Iterator<JsonNode> ite = rootNode.iterator();				 
					while (ite.hasNext()) {
						JsonNode temp = ite.next(); 
						JsonNode value=temp.findValue("key1");
						key1.add(value.textValue());
						value=temp.findValue("key2");
						key2.add(value.intValue());
						value=temp.findValue("key3");
						key3.add(value.doubleValue());
						value=temp.findValue("key4");
						key4.add(value.booleanValue());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		runTest("Jackson JsonParser",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				try{
					com.fasterxml.jackson.core.JsonFactory factory = new com.fasterxml.jackson.core.JsonFactory();
					com.fasterxml.jackson.core.JsonParser  parser  = factory.createParser(data);
					com.fasterxml.jackson.core.JsonToken token =null;
					while(!parser.isClosed()){
    					 token=parser.nextToken();
    					 if(token==com.fasterxml.jackson.core.JsonToken.START_OBJECT){
    					 	while(token!=com.fasterxml.jackson.core.JsonToken.END_OBJECT){
	    					 	token=parser.nextToken();
	    					 	// should be field name
	    					 	if(token==com.fasterxml.jackson.core.JsonToken.FIELD_NAME){
		    					 	String field=parser.getText();
		    					 	if(field.equals("key1")){
		    					 		parser.nextToken();
		    					 		key1.add(parser.getText());
		    					 	}else if(field.equals("key2")){
		    					 		parser.nextToken();
		    					 		key2.add(parser.getIntValue());
		    					 	}else if(field.equals("key3")){
		    					 		parser.nextToken();
		    					 		key3.add(parser.getDoubleValue());
		    					 	}else if(field.equals("key4")){
		    					 		parser.nextToken();
		    					 		key4.add(parser.getBooleanValue());
		    					 	}
		    					 }
	    					 }
    					 }

    				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		runTest("LazyJSON",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				LazyArray arr=new LazyArray(data);
				for(int i=0;i<arr.length();i++){
					LazyObject obj=arr.getJSONObject(i);
					key1.add(obj.getString("key1"));
					key2.add(obj.getInt("key2"));
					key3.add(obj.getDouble("key3"));
					key4.add(obj.getBoolean("key4"));
				}
			}
		});

		runTest("GSON class based",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				Gson gson = new Gson();
				SmallObject[] arr=gson.fromJson(data,SmallObject[].class);
				for(SmallObject obj:arr){
					key1.add(obj.key1);
					key2.add(obj.key2);
					key3.add(obj.key3);
					key4.add(obj.key4);
				}
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<SmallObject> beans = mapper.readValue(data, List.class, SmallObject.class );
				for(SmallObject obj:beans){
					key1.add(obj.key1);
					key2.add(obj.key2);
					key3.add(obj.key3);
					key4.add(obj.key4);
				}
			}
		});
		
		runTest("LoganSquare",new Runnable(){
			public void run(){
				key1.clear();key2.clear();key3.clear();key4.clear();
				try{
					List<SmallObject> list=LoganSquare.parseList(data,SmallObject.class);
					for(SmallObject obj:list){
						key1.add(obj.key1);
						key2.add(obj.key2);
						key3.add(obj.key3);
						key4.add(obj.key4);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void testSmallObjectParsing(final String data) throws Exception{
		runTest("GSON class based",new Runnable(){
			public void run(){
				Gson gson = new Gson();
				SmallObject[] arr=gson.fromJson(data,SmallObject[].class);
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<SmallObject> beans = mapper.readValue(data, List.class, SmallObject.class );
			}
		});
		
		runTest("LoganSquare",new Runnable(){
			public void run(){
				try{
					List<SmallObject> list=LoganSquare.parseList(data,SmallObject.class);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void testSmallObjectSplit(final String data) throws Exception{
		final List<String> result=new ArrayList<String>(BATCH);
		runTest("GSON class based",new Runnable(){
			public void run(){
				result.clear();
				Gson gson = new Gson();
				SmallObject[] arr=gson.fromJson(data,SmallObject[].class);
				for(SmallObject obj:arr){
					result.add(gson.toJson(obj));
				}
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				result.clear();
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<SmallObject> beans = mapper.readValue(data, List.class, SmallObject.class );
				for(SmallObject obj:beans){
					result.add(mapper.toJson(obj)); 
				}
			}
		});
		
		runTest("LoganSquare",new Runnable(){
			public void run(){
				result.clear();
				try{
					List<SmallObject> list=LoganSquare.parseList(data,SmallObject.class);
					for(SmallObject obj:list){
						result.add(LoganSquare.serialize(obj));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void testMediumObjectParsing(final String data) throws Exception{
		runTest("GSON class based",new Runnable(){
			public void run(){
				Gson gson = new Gson();
				MediumObject[] arr=gson.fromJson(data,MediumObject[].class);
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<MediumObject> beans = mapper.readValue(data, List.class, MediumObject.class );
			}
		});
		
		runTest("LoganSquare",new Runnable(){
			public void run(){
				try{
					List<MediumObject> list=LoganSquare.parseList(data,MediumObject.class);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void testMediumObjectSplit(final String data) throws Exception{
		final List<String> result=new ArrayList<String>(BATCH);
		runTest("GSON class based",new Runnable(){
			public void run(){
				result.clear();
				Gson gson = new Gson();
				MediumObject[] arr=gson.fromJson(data,MediumObject[].class);
				for(MediumObject obj:arr){
					result.add(gson.toJson(obj));
				}
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				result.clear();
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<MediumObject> beans = mapper.readValue(data, List.class, MediumObject.class );
				for(MediumObject obj:beans){
					result.add(mapper.toJson(obj)); 
				}
			}
		});
		
		runTest("LoganSquare",new Runnable(){
			public void run(){
				result.clear();
				try{
					List<MediumObject> list=LoganSquare.parseList(data,MediumObject.class);
					for(MediumObject obj:list){
						result.add(LoganSquare.serialize(obj));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void testLargeObjectParsing(final String data) throws Exception{
		runTest("GSON class based",new Runnable(){
			public void run(){
				Gson gson = new Gson();
				LargeObject[] arr=gson.fromJson(data,LargeObject[].class);
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<LargeObject> beans = mapper.readValue(data, List.class, LargeObject.class );
			}
		});
		/*
		runTest("LoganSquare",new Runnable(){
			public void run(){
				try{
					List<LargeObject> list=LoganSquare.parseList(data,LargeObject.class);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});*/
	}

	public void testLargeObjectSplit(final String data) throws Exception{
		final List<String> result=new ArrayList<String>(BATCH);
		runTest("GSON class based",new Runnable(){
			public void run(){
				result.clear();
				Gson gson = new Gson();
				LargeObject[] arr=gson.fromJson(data,LargeObject[].class);
				for(LargeObject obj:arr){
					result.add(gson.toJson(obj));
				}
			}
		});

		runTest("Boon",new Runnable(){
			public void run(){
				result.clear();
				org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
				List<LargeObject> beans = mapper.readValue(data, List.class, LargeObject.class );
				for(LargeObject obj:beans){
					result.add(mapper.toJson(obj)); 
				}
			}
		});
		/*
		runTest("LoganSquare",new Runnable(){
			public void run(){
				result.clear();
				try{
					List<LargeObject> list=LoganSquare.parseList(data,LargeObject.class);
					for(LargeObject obj:list){
						result.add(LoganSquare.serialize(obj));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});*/
	}


	public void runTest(String name,Runnable test){
		try{
			// Run warmup
			test.run();
			Thread.sleep(100);
			for(int i=0;i<20;i++){
				test.run();
			}
			Thread.sleep(100);
			System.gc();

			long[] times=new long[RUNS];
			long min=-1;
			long max=0;
			
			for(int i=0;i<RUNS;i++){
				long pre=System.nanoTime();
				test.run();
				long post=System.nanoTime();
				times[i]=post-pre;
				System.gc();
				Thread.sleep(100);
			}
			
			if(HUMAN_OUTPUT){
				long total=0;
				for(int i=0;i<RUNS;i++){
					total+=times[i];
					if(min==-1){
						min=times[i];
					}else{
						if(times[i]<min){
							min=times[i];
						}
					}
					if(times[i]>max){
						max=times[i];
					}
				}
				java.util.Arrays.sort(times); 
				System.out.println(" + "+name+" min:"+(min/1000000.0)+" max:"+(max/1000000.0)+" avg:"+(((total/(float)RUNS))/1000000.0)+" med:"+(times[times.length/2]/1000000.0));
			}else{
				StringBuilder buf=new StringBuilder();
				buf.append(name);
				for(long l:times){
					buf.append(", ");
					buf.append(l);
				}
				System.out.println(buf.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new Benchmark();
	}
}