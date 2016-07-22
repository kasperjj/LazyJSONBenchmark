package me.doubledutch;

import org.json.*;
import com.google.gson.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bluelinelabs.logansquare.LoganSquare;

import org.boon.core.reflection.BeanUtils;
import org.boon.core.reflection.MapObjectConversion;
import static org.boon.Boon.puts;

import me.doubledutch.lazy.*;

public class Benchmark{
	public final int RUNS=100;
	public final boolean HUMAN_OUTPUT=true;
	public int BATCH=1000;

	public Benchmark(){
		try{
			System.out.println("Generating SmallObject batch data");
			JSONArray array=new JSONArray();
			for(int i=0;i<BATCH;i++){
				JSONObject obj=new JSONObject();
				obj.put("key1","value1");
				obj.put("key2",(int)(Math.random()*1000000));
				obj.put("key3",Math.random());
				obj.put("key4",false);
				obj.put("key5",JSONObject.NULL);
				array.put(obj);
			}
			final String raw_batch_1=array.toString();
			System.out.println("Running parse test");
			
			runTest("json.org",new Runnable(){
				public void run(){
					JSONArray arr=new JSONArray(raw_batch_1);
				}
			});

			runTest("gson class based",new Runnable(){
				public void run(){
					Gson gson = new Gson();
					SmallObject[] arr=gson.fromJson(raw_batch_1,SmallObject[].class);
				}
			});

			runTest("gson JsonParser based",new Runnable(){
				public void run(){
					JsonParser parser = new JsonParser();
					JsonElement jsonElement = parser.parse(raw_batch_1);
					JsonArray asJsonArray = jsonElement.getAsJsonArray();
				}
			});

			runTest("boon",new Runnable(){
				public void run(){
					org.boon.json.ObjectMapper mapper = org.boon.json.JsonFactory.create();
					List<SmallObject> beans = mapper.readValue(raw_batch_1, List.class, SmallObject.class );
				}
			});

			runTest("Jackson",new Runnable(){
				public void run(){
					try{
						ObjectMapper mapper = new ObjectMapper();
						JsonNode rootNode = mapper.readTree(raw_batch_1);
						Iterator<JsonNode> ite = rootNode.iterator();				 
						while (ite.hasNext()) {
							JsonNode temp = ite.next(); 
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			runTest("LoganSquare",new Runnable(){
				public void run(){
					try{
						List<SmallObject> list=LoganSquare.parseList(raw_batch_1,SmallObject.class);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

			runTest("LazyJSON",new Runnable(){
				public void run(){
					LazyArray arr=new LazyArray(raw_batch_1);
					for(int i=0;i<arr.length();i++){
						LazyObject obj=arr.getJSONObject(i);
					}
				}
			});
		}catch(Throwable t){
			t.printStackTrace();
		}

	}


	public void runTest(String name,Runnable test){
		try{
			// Run warmup
			test.run();
			Thread.sleep(100);
			for(int i=0;i<10;i++){
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new Benchmark();
	}
}