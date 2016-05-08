package com.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;;

public class AcademicSearch {
	
	public long id1;
	public long id2;
	public boolean [] judge = {true,true,true};
	
	
	public boolean judge_id(long id){
		String expr = "Composite(AA.AuId="+id+")";
		
		try {
			JSONObject judgeID=getAcademic(expr);
			if (judgeID.getJSONArray("entities").length() == 0){
				return true;
			}
			else{
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	

	public JSONObject getAcademic(String expr){
		
		try{
			
			String uriHead = "https://oxfordhk.azure-api.net/academic/v1.0/evaluate";
			String uriExpr = "?expr=";
			String uriAttribute = "&attributes=Id,AA.AuId,AA.AfId,F.FId,J.JId,C.CId,RId";
			//AA.AuId	Author ID
			//AA.AfId	Author affiliation ID
			//F.FId		Field of study ID
			//J.JId		Journal ID
			//C.CId		Conference series ID
			//RId		Reference ID
			String uriKey = "&subscription-key=f7cc29509a8443c5b3a5e56b0e38b5a6";
			
			String uri = uriHead + uriExpr + expr + uriAttribute + uriKey;
			System.out.println(uri);
			
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet request = new HttpGet(uri);
			HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            String strEntity = EntityUtils.toString(entity);
            
            return new JSONObject(strEntity); 
			
		}catch(Exception e){
			System.out.println("http Exception:"+e.getMessage());
		}
		return null;
	}
	
	public JSONArray search1Hop(){
		
		JSONArray results = new JSONArray();
		
		if(judge[1]&&judge[2]){ //case 1:id & id
			
			JSONObject jsonObject1 = getAcademic("Id="+Long.toString(this.id1));
			JSONObject jsonObject2 = getAcademic("Id="+Long.toString(this.id2));
			
			System.out.println(""+jsonObject1.toString()+"\n"+jsonObject2.toString());
			
		}else if((!judge[1])&&judge[2]){ //case 2: auid & id
			
			JSONObject jsonObject1 = getAcademic("Composite(AA.AuId="+this.id1+")");
			JSONObject jsonObject2 = getAcademic("Id="+Long.toString(this.id2));
			
			System.out.println(""+jsonObject1.toString()+"\n"+jsonObject2.toString());
			
		}else if((judge[1])&&(!judge[2])){
			
			try {
			
				JSONObject jsonObject1 = getAcademic("Id="+Long.toString(this.id1));
				JSONObject jsonObject2 = getAcademic("Composite(AA.AuId="+this.id2+")");
				
				System.out.println("case 3");
			
				results = jsonObject1.getJSONArray("entities").getJSONObject(0).getJSONArray("AA");
				
				for (int i = 0 ; i < results.length();++i){
					JSONArray auid =results.getJSONObject(i).getJSONArray("AuId"); 
					if(auid.toString() == Long.toString(id2)){
						System.out.println(auid);
					}
				}
				
				return results;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}else if((!judge[1])&&(!judge[2])){
			JSONObject jsonObject1 = getAcademic("Composite(AA.AuId="+this.id1+")");
			JSONObject jsonObject2 = getAcademic("Composite(AA.AuId="+this.id2+")");

			System.out.println(""+jsonObject1.toString()+"\n"+jsonObject2.toString());
		}
		
		return results;
	}

	public AcademicSearch(long id1,long id2){
		
		this.id1 = id1;
		this.id2 = id2;
		
	}
	
	public JSONArray getHopResults(){
		JSONArray result = new JSONArray();
		judge[1] = judge_id(id1);
		judge[2] = judge_id(id2);
		System.out.println(judge[1]);
		System.out.println(judge[2]);
		
		result = search1Hop();
		
		//JSONObject j = getAcademic("Id="+Long.toString(this.id1));
		return result;
	}
	

}
